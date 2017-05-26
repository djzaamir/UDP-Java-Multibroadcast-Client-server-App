package CLIENT;

import brdiged_resources.Client_Server_Data;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by Aamir on 5/26/2017.
 */
public class GUI_client implements customEventHandler {


    //region vars
    private static DatagramSocket socket          = null;      //UDP socket
    private static DatagramPacket packet          = null;     //Packet to be transmitted or received over socket
    private static byte[]         data_buffer     = null;    // Data buffer size
    private  static final int      server_port     = 9999;
    private static final int      max_buffer_size = 1024; // Not sure which is the optimal UDP packet size
    private Client_Server_Data broadCasted_client  = null;
    //endregion


    //region Functional Section
    public void launch(){

    }
    //endregion


    //region Overided methods
    //The following method is event listener which will triggered whenever we receive some
    //Information from server
    @Override
    public void reciveivedNewClient(Client_Server_Data received_obj) {
        System.out.println("Hello " + received_obj.getUsername() + " from Main client class");
    }
    //endregion
}
