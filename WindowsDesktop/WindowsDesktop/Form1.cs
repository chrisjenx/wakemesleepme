using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Threading;
using System.Net;
using System.Net.Sockets;
using System.Management;


namespace WindowsDesktop
{
    public partial class Form1 : Form
    {
        System.Windows.Forms.Timer countTime;
        Thread backgroundCheck;
        Thread backgroundListen;
        bool running = false;
        bool listening = false;
        UdpClient listener = new UdpClient(10010);
        IPEndPoint groupEP = new IPEndPoint(IPAddress.Any, 10010);
        IPAddress localPt = IPAddress.Parse("10.0.10.255");
        int pokeTime;
        
        public Form1()
        {
            InitializeComponent();
            countTime = new System.Windows.Forms.Timer();
            countTime.Interval = 100;
            backgroundListen = new Thread(new ThreadStart(listenOut));
            backgroundCheck = new Thread(new ThreadStart(pollDevice));
            timeSincePoke.Interval = 1000;
            pokeTime = 0;
            

        }

        public void listenOut()
        {
            while (listening == true)
            {
                try
                {

                    string received_data;
                    byte[] receive_byte_array;

                    receive_byte_array = listener.Receive(ref groupEP);
                    received_data = Encoding.ASCII.GetString(receive_byte_array, 0, receive_byte_array.Length);

                    MessageBox.Show(received_data);
                    pokeTime = 0;
                    //if(received_data=="")

                    //Application.SetSuspendState(PowerState.Suspend, true, false); 
                }
                catch
                    (Exception e)
                {

                    MessageBox.Show(e.ToString());
                }
                Thread.Sleep(500);
            }
        }
        public void pollDevice()
        {
            while (running == true)
            {
                IPAddress localPt = IPAddress.Parse("10.0.10.255");
                IPEndPoint endPt = new IPEndPoint(localPt, 10009);
                try
                {


                    ManagementClass mc = new ManagementClass("Win32_NetworkAdapterConfiguration");

                    string MACAddress = "";
                    ManagementObjectCollection moc = mc.GetInstances();

                    foreach (ManagementObject mo in moc)
                    {
                        if (MACAddress == String.Empty)  // only return MAC Address from first card
                        {
                            if ((bool)mo["IPEnabled"] == true) MACAddress = mo["MacAddress"].ToString();
                        }
                        mo.Dispose();
                    }

                    Socket sendingSocket = new Socket(AddressFamily.InterNetwork, SocketType.Dgram, ProtocolType.Udp);


                    string toSend = "mac address:" + MACAddress + ",type:desktop" + ",status:alive";
                    byte[] arraySend = Encoding.ASCII.GetBytes(toSend);

                    sendingSocket.SendTo(arraySend, endPt);



                }
                catch
                (Exception e)
                {

                    MessageBox.Show(e.ToString());
                }
                Thread.Sleep(2000);
            }
           
            }

        private void btnStart_Click(object sender, EventArgs e)
        {
            if (running == false)
            {
                backgroundCheck.Start();
                running = true;
            }
            else
            {
                backgroundCheck.Abort();
                running = false;

            }
            }

        private void button1_Click(object sender, EventArgs e)
        {
            timeSincePoke.Start();
            if (listening == false)
            {
             backgroundListen.Start();
                listening = true;
            }
            else
            {
                backgroundListen.Abort();
                listening = false;
                timeSincePoke.Stop();
            }
        }

        private void timeSincePoke_Tick(object sender, EventArgs e)
        {

            pokeTime++;
            CheckFullScreen checkscreen = new CheckFullScreen();
            lblRemain.Text= pokeTime.ToString() ;
            if (checkscreen.checkFullWindow())
            {
                pokeTime = 0;
            }
            if (pokeTime > 30)
            {
                pokeTime = 0;
                timeSincePoke.Stop();
                Application.SetSuspendState(PowerState.Suspend, true, false);
            }
            }

    }
}
