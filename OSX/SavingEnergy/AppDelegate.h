//
//  AppDelegate.h
//  SavingEnergy
//
//  Created by Valentin Kalchev on 01/12/2012.
//  Copyright (c) 2012 Valentin Kalchev. All rights reserved.
//

#import <Cocoa/Cocoa.h>

@interface AppDelegate : NSObject <NSApplicationDelegate>
{
    IBOutlet  NSTextField * infoTextField;
    IBOutlet  NSTextField *countdownLabel;
    IBOutlet NSButton *selectButton;
}
@property (nonatomic,retain) IBOutlet NSTextField *infoTextField;
@property (nonatomic,retain) IBOutlet NSTextField *countdownLabel;
@property (nonatomic,retain) IBOutlet NSButton *selectButton;;

@property (assign) IBOutlet NSWindow *window;

-(IBAction)sync:(id)sender;

@end
