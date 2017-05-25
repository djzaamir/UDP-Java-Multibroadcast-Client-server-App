package SERVER;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by Aamir on 5/25/2017.
 */
public class alpha_server {

    //region MAIN LOGiC FOR SERVER
        /*
        * Main Logic
        * On startup first of all , truncate online users table
        * Start listening for all kind operations from clients
        * Fixed  Objects will be transmitted over sockets , containing certain parameters and flags for operation
        *
        * */
    //endregion


    //region vars
    private DatagramSocket socket          = null;      //UDP socket
    private DatagramPacket packet          = null;     //Packet to be transmitted or received over socket
    private byte[]         data_buffer     = null;    // Data buffer size
    private final int      listen_port     = 9999;
    private final int      max_buffer_size = 1024; // Not sure which is the optimal UDP packet size
    //endregion


    //region CONSTRUCTOR
    public alpha_server() throws  Exception{
        socket =  new DatagramSocket(listen_port);
        data_buffer = new byte[max_buffer_size];
    }
    //region

    public static void main(String[] args) throws Exception {







        //On startup truncate online_users table
        DBA.truncateOnlineUsers();

        //Sent the server into listening mode for all kind of client operations
        while (true){



            //Listens for incoming connection from clients , after every 50 milli seconds (20 times a second)
            Thread.sleep(50);
        }

    }
}
