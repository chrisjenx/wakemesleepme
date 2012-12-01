package com.jenxsol.wakemesleepme.utils;

import java.net.DatagramPacket;

public class PacketSupport
{

    public static final int PORT_WOL = 9;

    /**
     * Will create a packet without an address specified, you need to attach the
     * broadcast address to the packet
     * 
     * @param macAddressString
     * @return
     */
    public static final DatagramPacket createWoLANPacket(String macAddressString)
    {
        try
        {
            byte[] macBytes = getMacAddressBytes(macAddressString);
            byte[] bytes = new byte[6 + 16 * macBytes.length];
            for (int i = 0; i < 6; i++)
            {
                bytes[i] = (byte) 0xff;
            }
            for (int i = 6; i < bytes.length; i += macBytes.length)
            {
                System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
            }
            final DatagramPacket p = new DatagramPacket(bytes, bytes.length);
            p.setPort(PORT_WOL);
            return p;
        }
        catch (Exception e)
        {
            QLog.e("Failed to create wol packet", e);
            return null;
        }

        // // WOL packet contains a 6-bytes trailer and 16 times a 6-bytes
        // sequence
        // // containing the MAC address.
        // byte[] packet = new byte[17 * 6];
        //
        // // Trailer of 6 times 0xFF.
        // for (int i = 0; i < 6; i++)
        // packet[i] = (byte) 0xFF;
        //
        // // Body of magic packet contains 16 times the MAC address.
        // for (int i = 1; i <= 16; i++)
        // for (int j = 0; j < 6; j++)
        // packet[i * 6 + j] = address[j];
        //
        // // Broadcast address for WOL
        // InetAddress broadcastWOLAddress = null;
        // try
        // {
        // broadcastWOLAddress = InetAddress.getByAddress(new byte[] { (byte)
        // 0xFF, (byte) 0xFF,
        // (byte) 0xFF, (byte) 0x00 });
        // }
        // catch (UnknownHostException e)
        // {
        // e.printStackTrace();
        // }
        // return new DatagramPacket(packet, packet.length, broadcastWOLAddress,
        // 40000);
    }

    private static byte[] getMacAddressBytes(String macStr) throws IllegalArgumentException
    {
        byte[] bytes = new byte[6];
        String[] hex = macStr.split("(\\:|\\-)");
        if (hex.length != 6)
        {
            throw new IllegalArgumentException("Invalid MAC address.");
        }
        try
        {
            for (int i = 0; i < 6; i++)
            {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        }
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException("Invalid hex digit in MAC address.");
        }
        return bytes;
    }

    private PacketSupport()
    {
    }

}
