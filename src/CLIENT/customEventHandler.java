package CLIENT;

import brdiged_resources.Client_Server_Data;

/**
 * Created by Aamir on 5/26/2017.
 */
public interface customEventHandler {

    //This method is primarily used for custom Event listeners
    //What happens is this
    //Client class implemented this class and its methods
    //There is an array list present insdie the Broadcast_receiver class
    //So which class want to get Brodcast updates from this class can do so
    //Upon receiving a new client packet
    //A for loop will update all the present subscirbers inside this class
    public void reciveivedNewClient(Client_Server_Data received_object);
}
