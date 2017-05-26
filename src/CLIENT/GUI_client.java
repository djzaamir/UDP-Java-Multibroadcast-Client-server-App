package CLIENT;

import brdiged_resources.Client_Server_Data;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by Aamir on 5/26/2017.
 */
public class GUI_client implements customEventHandler {


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
