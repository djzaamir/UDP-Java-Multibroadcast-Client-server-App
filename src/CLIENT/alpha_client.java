package CLIENT;

import brdiged_resources.Client_Server_Data;
import brdiged_resources.OPERATIONS;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Aamir on 5/25/2017.
 */
public class alpha_client {

    //region vars
    private static DatagramSocket socket          = null;      //UDP socket
    private static DatagramPacket packet          = null;     //Packet to be transmitted or received over socket
    private static byte[]         data_buffer     = null;    // Data buffer size
    private  static final int      server_port     = 9999;
    private static final int      max_buffer_size = 1024; // Not sure which is the optimal UDP packet size
    private Client_Server_Data broadCasted_client  = null;
    //endregion


    public static void main(String[] args) throws IOException {

        Broadcast_receiver broadcast_receiver = new Broadcast_receiver();
        Thread t1 =  new Thread(broadcast_receiver);
        t1.start();
    }



    //endregion
}
