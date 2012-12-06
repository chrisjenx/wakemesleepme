//
//  AppDelegate.m
//  SavingEnergy
//
//  Created by Valentin Kalchev on 01/12/2012.
//  Copyright (c) 2012 Valentin Kalchev. All rights reserved.
//

#import "AppDelegate.h"
#import "GCDAsyncUdpSocket.h"
#include <netinet/in.h>
#include <stdio.h>
#include <CoreServices/CoreServices.h>
#include <Carbon/Carbon.h>


#include <sys/socket.h>
#include <sys/sysctl.h>
#include <net/if.h>

#include <net/if_dl.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/sysctl.h>


@implementation AppDelegate
@synthesize infoTextField;
@synthesize countdownLabel;
@synthesize selectButton;

bool changeTitle = TRUE;
NSTimer *sendPackagesTimer;
NSTimer *receivePackagesTimer;
int ticks;
NSTimer *sleepTimer;
GCDAsyncUdpSocket *udpSocket;
GCDAsyncUdpSocket *newSocket;
NSTextField *infoToTheUser;
NSView * infoView;

const int DEFAULT_TIMER = 60;

static OSStatus SendAppleEventToSystemProcess(AEEventID EventToSend);

- (void)applicationDidFinishLaunching:(NSNotification *)aNotification
{
    
    NSError *err;
    
    udpSocket = [[GCDAsyncUdpSocket alloc] initWithDelegate:self delegateQueue:dispatch_get_main_queue()];    [udpSocket bindToPort:10010 error:&err];
    [udpSocket joinMulticastGroup:@"10.0.10.255" error:&err];
    [udpSocket beginReceiving:&err];
    
    newSocket = [[GCDAsyncUdpSocket alloc]initWithDelegate:self delegateQueue:dispatch_get_main_queue()];
    
    //SEND PACKAGES TIMER START
    sendPackagesTimer = [NSTimer scheduledTimerWithTimeInterval:3.0
                                                         target:self
                                                       selector:@selector(sendPackages:)
                                                       userInfo:nil
                                                        repeats:YES];
    
    //RECEIVE PACKAGES TIMER
    receivePackagesTimer = [NSTimer scheduledTimerWithTimeInterval:0.1
                                                            target:self
                                                          selector:@selector(listenForPackages:)
                                                          userInfo:nil
                                                           repeats:YES];
    
    [self setDefault];
    
    [NSEvent addGlobalMonitorForEventsMatchingMask:(NSLeftMouseDownMask | NSRightMouseDownMask | NSOtherMouseDownMask | NSMouseMovedMask | NSKeyDown) handler:^(NSEvent *event)
    {
        ticks = DEFAULT_TIMER;
        
    }];
    
}
-(void)setDefault
{
    if(changeTitle)
    {
        //Information to the user whether app is in autosleep
        [infoTextField setStringValue:@"Autosleep ON ..."];
        [selectButton setTitle:@"Disable"];
        changeTitle = FALSE;
        
        ticks = DEFAULT_TIMER;
        NSString *string = [NSString stringWithFormat:@"Timer: %d",ticks];
        [countdownLabel setStringValue:string];
        
        
        sleepTimer = [NSTimer scheduledTimerWithTimeInterval:1.0
                                                      target:self
                                                    selector:@selector(countTicks:)
                                                    userInfo:nil
                                                     repeats:YES];
    }
    else
    {
        //Information to the user whether app is in autosleep
        [infoTextField setStringValue:@"Autosleep OFF ..."];
        
        
        //[sendPackagesTimer invalidate];
        //sendPackagesTimer = nil;
        
        //[receivePackagesTimer invalidate];
        //receivePackagesTimer = nil;
        
        [sleepTimer invalidate];
        sleepTimer = nil;
        
        [countdownLabel setStringValue:@"Timer: 30"];
        [selectButton setTitle:@"Enable"];
        
        changeTitle = TRUE;
    }

}
-(IBAction)sync:(id)sender
{
    if(changeTitle)
    {
        //Information to the user whether app is in autosleep
        [infoTextField setStringValue:@"Autosleep ON ..."];
        [selectButton setTitle:@"Disable"];
        changeTitle = FALSE;
        
        NSString *string = [NSString stringWithFormat:@"Timer: %d",ticks];
        [countdownLabel setStringValue:string];
        
        
        sleepTimer = [NSTimer scheduledTimerWithTimeInterval:1.0
                                                      target:self
                                                    selector:@selector(countTicks:)
                                                    userInfo:nil
                                                     repeats:YES];
    }
    else
    {
        //Information to the user whether app is in autosleep
        [infoTextField setStringValue:@"Autosleep OFF ..."];
        
        [sleepTimer invalidate];
        sleepTimer = nil;
        
        ticks = DEFAULT_TIMER;
        [countdownLabel setStringValue:[NSString stringWithFormat:@"Timer: %d",ticks]];
        [selectButton setTitle:@"Enable"];
        
        changeTitle = TRUE;
    }
}
-(IBAction)countTicks:(id)sender
{
    ticks--;
    
    NSString *string = [NSString stringWithFormat:@"Timer: %d",ticks];
    [countdownLabel setStringValue:string];
    if(ticks <=0)
    {
        //Restart to default  [infoTextField setStringValue:@"Autosleep OFF ..."];
        
        //[sleepTimer invalidate];
        //sleepTimer = nil;
        
        ticks = DEFAULT_TIMER;
        [countdownLabel setStringValue:[NSString stringWithFormat:@"Timer: %d",ticks]];
        [infoTextField setStringValue:@"Autosleep ON ..."];
        [selectButton setTitle:@"Disable"];
        changeTitle = FALSE;
        SendAppleEventToSystemProcess(kAESleep);
    }
}
-(IBAction)sendPackages:(id)sender
{
    //NSData * packageData = [NSData dataWithContentsOfFile:@"Resources/package.txt"];
    
    NSString *mac = [self getMacAddress];
    //NSString *type= [self getMacVersion];
    NSString *status = @"available";
    
    NSString *str = [NSString stringWithFormat:@"{\"mac\":\"%@\",\"status\":\"%@\"}",mac,status];
    
    NSData *data = [str dataUsingEncoding:NSUTF8StringEncoding];
    //NSData *data = [str dataUsingEncoding:NSUTF8StringEncoding];
    
    [newSocket enableBroadcast:YES error:nil];
    [newSocket sendData:data toHost:@"10.0.10.255" port:10009 withTimeout:-1 tag:0];
    
    //Sync has been started
    NSLog(@"I have send a package");
    
}

-(NSString *)getMacVersion
{
    
    size_t len = 0;
    sysctlbyname("hw.model", NULL, &len, NULL, 0);
    char * model;
    if (len)
    {
        model = malloc(len*sizeof(char));
        sysctlbyname("hw.model", model, &len, NULL, 0);
        printf("%s\n", model);
        free(model);
    }
    
    return [NSString stringWithFormat:@"%c",model];
}
- (NSString *)getMacAddress
{
    int                 mgmtInfoBase[6];
    char                *msgBuffer = NULL;
    size_t              length;
    unsigned char       macAddress[6];
    struct if_msghdr    *interfaceMsgStruct;
    struct sockaddr_dl  *socketStruct;
    NSString            *errorFlag = NULL;
    
    // Setup the management Information Base (mib)
    mgmtInfoBase[0] = CTL_NET;        // Request network subsystem
    mgmtInfoBase[1] = AF_ROUTE;       // Routing table info
    mgmtInfoBase[2] = 0;
    mgmtInfoBase[3] = AF_LINK;        // Request link layer information
    mgmtInfoBase[4] = NET_RT_IFLIST;  // Request all configured interfaces
    
    // With all configured interfaces requested, get handle index
    if ((mgmtInfoBase[5] = if_nametoindex("en0")) == 0)
        errorFlag = @"if_nametoindex failure";
    else
    {
        // Get the size of the data available (store in len)
        if (sysctl(mgmtInfoBase, 6, NULL, &length, NULL, 0) < 0)
            errorFlag = @"sysctl mgmtInfoBase failure";
        else
        {
            // Alloc memory based on above call
            if ((msgBuffer = malloc(length)) == NULL)
                errorFlag = @"buffer allocation failure";
            else
            {
                // Get system information, store in buffer
                if (sysctl(mgmtInfoBase, 6, msgBuffer, &length, NULL, 0) < 0)
                    errorFlag = @"sysctl msgBuffer failure";
            }
        }
    }
    
    // Befor going any further...
    if (errorFlag != NULL)
    {
        NSLog(@"Error: %@", errorFlag);
        return errorFlag;
    }
    
    // Map msgbuffer to interface message structure
    interfaceMsgStruct = (struct if_msghdr *) msgBuffer;
    
    // Map to link-level socket structure
    socketStruct = (struct sockaddr_dl *) (interfaceMsgStruct + 1);
    
    // Copy link layer address data in socket structure to an array
    memcpy(&macAddress, socketStruct->sdl_data + socketStruct->sdl_nlen, 6);
    
    // Read from char array into a string object, into traditional Mac address format
    NSString *macAddressString = [NSString stringWithFormat:@"%02X:%02X:%02X:%02X:%02X:%02X",
                                  macAddress[0], macAddress[1], macAddress[2],
                                  macAddress[3], macAddress[4], macAddress[5]];
    NSLog(@"Mac Address: %@", macAddressString);
    
    // Release the buffer memory
    free(msgBuffer);
    
    return macAddressString;
}

-(IBAction)listenForPackages:(id)sender
{
   //[self udpSocket:udpSocket didReceiveData:NSData *data fromAddress:NSData *address withFilterContext:nil]
}
- (void)udpSocket:(GCDAsyncUdpSocket *)sock didReceiveData:(NSData *)data fromAddress:(NSData *)address withFilterContext:(id)filterContext
{
    NSString *msg = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
    
    NSString *host = nil;
    uint16_t port = 0;
    [GCDAsyncUdpSocket getHost:&host port:&port fromAddress:address];
    
    if (msg)
    {
        SendAppleEventToSystemProcess(kAEWakeUpEvent);
        
        ticks = DEFAULT_TIMER;
        NSString *string = [NSString stringWithFormat:@"Timer: %d",ticks];
        [countdownLabel setStringValue:string];
        NSLog(@"Message = %@, Address = %@ %i",msg,host,port);
    }
    else
    {
        NSLog(@"Error converting received data into UTF-8 String");
    }
}

OSStatus SendAppleEventToSystemProcess(AEEventID EventToSend)
{
    AEAddressDesc targetDesc;
    static const ProcessSerialNumber kPSNOfSystemProcess = { 0, kSystemProcess };
    AppleEvent eventReply = {typeNull, NULL};
    AppleEvent appleEventToSend = {typeNull, NULL};
    
    OSStatus error = noErr;
    
    error = AECreateDesc(typeProcessSerialNumber, &kPSNOfSystemProcess,
                         sizeof(kPSNOfSystemProcess), &targetDesc);
    
    if (error != noErr)
    {
        return(error);
    }
    
    error = AECreateAppleEvent(kCoreEventClass, EventToSend, &targetDesc,
                               kAutoGenerateReturnID, kAnyTransactionID, &appleEventToSend);
    
    AEDisposeDesc(&targetDesc);
    if (error != noErr)
    {
        return(error);
    }
    
    error = AESend(&appleEventToSend, &eventReply, kAENoReply,
                   kAENormalPriority, kAEDefaultTimeout, NULL, NULL);
    
    AEDisposeDesc(&appleEventToSend);
    if (error != noErr)
    {
        return(error);
    }
    
    AEDisposeDesc(&eventReply);
    
    return(error); 
}
@end
