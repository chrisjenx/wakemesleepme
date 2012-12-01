package com.jenxsol.wakemesleepme.server;

import java.net.DatagramPacket;

interface OnDataReceivedListener
{

    void onDataReceived(final DatagramPacket packet);
}