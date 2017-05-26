package CLIENT;
import java.io.IOException;
/**
 * Created by Aamir on 5/25/2017.
 */
public class alpha_client {

    /*Class information
    * This is the entry class , will be joining rest of the CLIENT package classes together
    * Broadcast Receiver will be handling broadcast messages from the server/
    * */



    public static void main(String[] args) throws IOException {

        //Main GUI window , in which we will displaying all info
        GUI_client GUI = new GUI_client();

            //Broadcast listener is the class which will running background thread to handle Incoming Background Brodcasts
            Broadcast_receiver broadcast_receiver = new Broadcast_receiver();
            broadcast_receiver.addBroadCastListener(GUI);
            //Run inside  a seperate thread
            Thread t1 =  new Thread(broadcast_receiver);
            t1.start();

    }


}
