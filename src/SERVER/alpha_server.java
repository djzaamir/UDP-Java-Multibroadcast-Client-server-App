package SERVER;

import CLIENT.Broadcast_receiver;
import brdiged_resources.Client_Server_Data;
import brdiged_resources.OPERATIONS;
import com.sun.deploy.util.SessionState;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

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
    private static DatagramSocket socket          = null;      //UDP socket
    private static DatagramPacket packet          = null;     //Packet to be transmitted or received over socket
    private static byte[]         data_buffer     = null;    // Data buffer size
    private static final int      listen_port     = 9999;
    private static final int      max_buffer_size = 1024; // Not sure which is the optimal UDP packet size
    private ArrayList<Client_Server_Data> clients = new ArrayList<>(); //List of clients connected right now
    //endregion


    public static void main(String[] args) throws Exception {


        //On startup truncate online_users table
        DBA.truncateOnlineUsers();

        //Sent the server into listening mode for all kind of client operations
        while (true){

            //Client_Server_Data client_server_data =  listenForIncomingConnection();

            Client_Server_Data cli=  new Client_Server_Data();
            cli.setOPERATION(OPERATIONS.OFFLINE);
            cli.setIp("192.168.0.2");
            cli.setPort(1200);
            cli.setPassword("wegreh");
            cli.setUsername("aamir");


            BroadCastClientUpdate(cli);
            //Listens for incoming connection from clients , after every 50 milli seconds (20 times a second)
            Thread.sleep(1000);
        }

    }


    //region Functional Section
    //Listen for Incoming User connections
    /*
    * This functions listens for incoming client requests , and then
    * Converts the data into client-server-data object because that's
    * the contract between the server and the client to make communications
    * Returns the object  after Deserialization
    * */
    private static Client_Server_Data listenForIncomingConnection() throws  Exception{

          //Preparing Socket connection and socket receiving data
          data_buffer = new byte[max_buffer_size];
          socket =  new DatagramSocket(listen_port);
          packet =  new DatagramPacket(data_buffer , data_buffer.length);

          //Waiting for request here
          System.out.println("Listening for incoming Connections...");
          socket.receive(packet);

          ByteArrayInputStream byteArrayInputStream =  new ByteArrayInputStream(packet.getData());
              ObjectInputStream objectInputStream =  new ObjectInputStream(byteArrayInputStream);
          Client_Server_Data client_server_data = (Client_Server_Data)objectInputStream.readObject();

          //Storing some more data into this obj like client ip and port
          client_server_data.setIp(packet.getAddress().toString());
          client_server_data.setPort(packet.getPort());


        return client_server_data; //Return the obj to Main method for further processing
    }

    private static void BroadCastClientUpdate(Client_Server_Data client_to_broadcast) throws IOException {

        //The functionalities and resources used by this function will be totally local and not the linked with
        //Other functionalities of server ,
        //SOle purpose of this function is to make a broadcast for all clients on a particular group

        DatagramSocket broadcast_socket  = new DatagramSocket();
        DatagramPacket broadcast_packet;
        byte[] broadcast_buffer;

        broadcast_buffer = convertObjectToBytes(client_to_broadcast);

        InetAddress IP_GROUP = InetAddress.getByName("228.0.0.1");

        //creating packet to be broadcast
        broadcast_packet = new DatagramPacket(broadcast_buffer , broadcast_buffer.length , IP_GROUP , 444);

        //Transmitting packet here
        broadcast_socket.send(broadcast_packet);
        System.out.println("Packet Broadcasted...");
    }



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
