package CLIENT;
import brdiged_resources.Client_Server_Data;
import brdiged_resources.OPERATIONS;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Aamir on 5/25/2017.
 */
public class alpha_client {

    /*Class information
    * This is the entry class , will be joining rest of the CLIENT package classes together
    * Broadcast Receiver will be handling broadcast messages from the server/
    * */

    //region vars
    private static DatagramSocket socket          = null;      //UDP socket
    private static DatagramPacket packet          = null;     //Packet to be transmitted or received over socket
    private static byte[]         data_buffer     = null;    // Data buffer size
    private  static final int      server_port     = 9999;
    private static final int      max_buffer_size = 1024; // Not sure which is the optimal UDP packet size
    private Client_Server_Data broadCasted_client  = null;
    //endregion


    public static void main(String[] args) throws IOException {

        //Main GUI window , in which we will displaying all info
        GUI_client GUI = new GUI_client();

        //region Create Brodcast thread and perform necessary Bindings for event handling
        //Broadcast listener is the class which will running background thread to handle Incoming Background Brodcasts
            Broadcast_receiver broadcast_receiver = new Broadcast_receiver();
            broadcast_receiver.addBroadCastListener(GUI);
            //Run inside  a seperate thread
            Thread t1 =  new Thread(broadcast_receiver);
            t1.start();
        //endregion


        //lets create a socket and transmist new request for signup
        Client_Server_Data obj =  new Client_Server_Data();
        obj.setOPERATION(OPERATIONS.SIGNUP); //Server should perform signup operation
        obj.setPassword("djzaamir");
        obj.setUsername("djzaamir");

        socket =  new DatagramSocket();
        data_buffer = convertObjectToBytes(obj);
        InetAddress ia =  InetAddress.getLocalHost();
        packet  = new DatagramPacket(data_buffer , data_buffer.length ,ia ,9999 );
        socket.send(packet);

    }


    //region functional section
    //this function wil convert a single object into bytes
    private static byte[] convertObjectToBytes(Client_Server_Data object_to_convert) throws IOException {
        //Serializing obj into bytes
        ByteArrayOutputStream byteArrayOutputStream =  new ByteArrayOutputStream(); //This is stream on which object will be serialized

        ObjectOutputStream objectOutputStream =  new ObjectOutputStream(byteArrayOutputStream); //We are passing BytesArayStream as input here into ObjectoutputStream
        //this will set bytesArrayStream as target to write on for objectOutStream(which actually serializes the object)
        objectOutputStream.writeObject(object_to_convert); //Writing serialized Object onto BytesArrayStream
        objectOutputStream.flush();//flushing just in case
        return byteArrayOutputStream.toByteArray();
    }

    //this function will convert bytes array to a single object
    private static Client_Server_Data convertBytesToObject(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream =  new ByteArrayInputStream(bytes); //Passing in bytes in bytes scream
        ObjectInputStream objectInputStream =  new ObjectInputStream(byteArrayInputStream); //Passing bytes so that they can be converted into a object
        return (Client_Server_Data)objectInputStream.readObject(); //Read the parsed//coverted object from here
    }

    //endregion

}
