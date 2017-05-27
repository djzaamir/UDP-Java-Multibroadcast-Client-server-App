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
    private static boolean First_Run = true; //to stop multiple initialization of data
    //endregion


    public static void main(String[] args) throws Exception {


        //On startup truncate online_users table
        DBA.truncateOnlineUsers();

        //Following loop is here to recive any connections from clients , and then perform
        //necessary operations according to it
        /*
        * Which may include , performing signup
        *      This will also include letting the user know if the desired username is available
        *      Letting the client know weather or not there Signup Request was succesful
        * Login , and on the event of successful login
        *     Sent some information back to the client who made the login request ,
        *     Also Make an update for the rest of the clients for the status of this NEW client
        * Logoff event  ,incase of successful logoff
        *     Remove the client from online users table
        *     Broadcast an update about the status of the client
        * */
        while (true){

            //This is the inital communication with the client , future actions and communcations will made on the
            //Basis of this Communication
            Client_Server_Data client_server_data =  listenForIncomingConnection();


            //region Handle And Parse Client Request , e.g DBA operation etc
            Client_Server_Data data_to_sent_back  = null; //This data obj will be send back to client

               //Decision Making process here , what to do with client request
               switch (client_server_data.getOPERATION()){
                   case SIGNUP:
                       //Try to Sign up User , returns necessary information for client
                       data_to_sent_back =  tryToSignUp(client_server_data);
                       break;

                   default:
                       //Do nothing
                       break;
               }
            //endregion



            //region Sending data back to relevant Client
            //Now sending back another fresh obj which will contain all the error msgs , and other related  stuff
            socket = new DatagramSocket();
            byte[] data_tr  = convertObjectToBytes(data_to_sent_back);
            InetAddress ia =  InetAddress.getByName(client_server_data.getIp().substring(1));
            System.out.println(client_server_data.getIp().substring(1) + " ::: " + client_server_data.getPort());
            packet =  new DatagramPacket(data_tr,data_tr.length , ia , client_server_data.getPort());
            socket.send(packet);
            System.out.println("Packet Send to");
            //endregion



            //region BroadCasting Events are being handled here , like login and user offline
            //If there is a succesfull login or User going offline event , generate broadcast
            //For login
            if (data_to_sent_back.getOPERATION() == OPERATIONS.login_good ){
                BroadCastClientUpdate(new Client_Server_Data());
            }
            //for user going offline
            else if(client_server_data.getOPERATION() == OPERATIONS.OFFLINE){
                BroadCastClientUpdate(new Client_Server_Data());
            }
            //endregion

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
              First_Run = false;


          //Waiting for request here
          System.out.println("Listening for incoming Connections...");
          socket.receive(packet);


          //Basically conveting bytes to Required Object here
          ByteArrayInputStream byteArrayInputStream =  new ByteArrayInputStream(packet.getData());
          ObjectInputStream objectInputStream =  new ObjectInputStream(byteArrayInputStream);
          Client_Server_Data client_server_data = (Client_Server_Data)objectInputStream.readObject();

          //Storing some more data into this obj like client ip and port
          client_server_data.setIp(packet.getAddress().toString());
          client_server_data.setPort(packet.getPort());

            //Before restarting the loop , leave resources like sockets and packets (THIS IS VERY IMP)
            socket.close();
            packet = null;


        return client_server_data; //Return the obj to Main method for further processing
    }

    //Broadcast over multicasted sockets
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

    //Requirs DBA access
    private static Client_Server_Data tryToSignUp(Client_Server_Data client_server_data) throws Exception {

        //Make a call to db to lookup  for wanted username
        //incase found => Gen error (Username in use)
        //If not found => Perform insertion of the record
        Client_Server_Data to_send_back = new Client_Server_Data(); //Send back to client
        if (!DBA.usernameExist(client_server_data.getUsername())){
            //Perform insert query
            boolean insert_result =  DBA.insertUser(client_server_data);
            if (insert_result){
                //Successful insertion
                to_send_back.setOPERATION(OPERATIONS.signup_good);
            }else{
                //Generate Error , that some kinda error occurred
                to_send_back.setOPERATION(OPERATIONS.err_msg);
                to_send_back.setErr_msg("Some error occurred");
            }

        }else{
            //return Err Msg that username exist
            to_send_back.setOPERATION(OPERATIONS.err_msg);
            to_send_back.setErr_msg("Username already exist!");
        }

        return to_send_back;
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
