using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Runtime.InteropServices;
namespace WindowsDesktop
{

    [StructLayout(LayoutKind.Sequential)]
    public struct RECT
    {
        public int Left;
        public int Top;
        public int Right;
        public int Bottom;
    }

    class CheckFullScreen
    {
        [DllImport("user32.dll")]
        private static extern IntPtr GetForegroundWindow();
        [DllImport("user32.dll")]
        private static extern IntPtr GetDesktopWindow();
        [DllImport("user32.dll")]
        private static extern IntPtr GetShellWindow();
        [DllImport("user32.dll", SetLastError = true)]
        private static extern int GetWindowRect(IntPtr hwnd, out RECT rc);


  

    private IntPtr desktopHandle; //Window handle for the desktop
    private IntPtr shellHandle; //Window handle for the shell
    public CheckFullScreen()
    {
        desktopHandle = GetDesktopWindow();
        shellHandle = GetShellWindow();
    }

        /// <summary>
        /// Method to check for full screen windows running 
        /// </summary>
        /// <returns>true if an application is running in full screen </returns>
    public bool checkFullWindow()
    {
        bool runningFullScreen = false;
        RECT appBounds;
        System.Drawing.Rectangle screenBounds;
        IntPtr hWnd;

        hWnd = GetForegroundWindow();
        if (hWnd != null && !hWnd.Equals(IntPtr.Zero))
        {
            //Check we haven't picked up the desktop or the shell
            if (!(hWnd.Equals(desktopHandle) || hWnd.Equals(shellHandle)))
            {
                GetWindowRect(hWnd, out appBounds);
                //determine if window is fullscreen
                screenBounds = System.Windows.Forms.Screen.FromHandle(hWnd).Bounds;
                if ((appBounds.Bottom - appBounds.Top) == screenBounds.Height && (appBounds.Right - appBounds.Left) == screenBounds.Width)
                {
                    runningFullScreen = true;
                }
            }
        }
        return runningFullScreen;
    }


    }


}