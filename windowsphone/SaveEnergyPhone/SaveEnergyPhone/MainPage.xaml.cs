using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using SaveEnergyPhone.Resources;
using Microsoft.Phone.Scheduler;
using System.Threading.Tasks;
using System.Net.Sockets;
using System.Text;

namespace SaveEnergyPhone
{
    public partial class MainPage : PhoneApplicationPage
    {
        PeriodicTask pollOut;
        public bool backgroundAgent = true;
        // Constructor
        public MainPage()
        {
            InitializeComponent();
           startBackground();
            
           
            
        }

        

        public void startBackground()
        {
            pollOut = new PeriodicTask("ScheduledAgent");

           
            try
            {

                pollOut = ScheduledActionService.Find("PollOut") as PeriodicTask;
                if (pollOut != null)
                    ScheduledActionService.Remove("PollOut");

                pollOut = new PeriodicTask("PollOut");
                pollOut.Description = "waiting to shut down or wake your pc";
             
                ScheduledActionService.Add(pollOut);

                ScheduledActionService.LaunchForTest("PollOut", TimeSpan.FromSeconds(10));              

            }
            catch (Exception e)
            {
                MessageBox.Show(e.ToString());
            }
        }

        public void stopBackground()
        {


        }

        public void testToast()
        {
            ShellToast toastPop = new ShellToast();
            toastPop.Title = "Test";
            toastPop.Content = "Hello world";
            toastPop.Show();
            ScheduledActionService.LaunchForTest("energyPoll", TimeSpan.FromSeconds(60));   
        }

        private void Button_Click_1(object sender, RoutedEventArgs e)
        {
            Socket socket = new Socket(AddressFamily.InterNetwork, SocketType.Dgram, ProtocolType.Udp);
           
            
            byte[] data = Encoding.UTF8.GetBytes("test data");

            SocketAsyncEventArgs a = new SocketAsyncEventArgs();

            a.RemoteEndPoint = new IPEndPoint(IPAddress.Parse("10.0.10.255"), 10009);
            a.SetBuffer(data, 0, data.Length);

            a.Completed += new EventHandler<SocketAsyncEventArgs>(delegate(object s, SocketAsyncEventArgs g)
            {
                Console.WriteLine(g.SocketError);
            });

            socket.ConnectAsync(a);
        }
     

    }
}