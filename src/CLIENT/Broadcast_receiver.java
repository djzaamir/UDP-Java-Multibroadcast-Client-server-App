package CLIENT;

import brdiged_resources.Client_Server_Data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by Aamir on 5/25/2017.
 */
public class Broadcast_receiver implements Runnable {


    @Override
    public void run() {
       while(true){ //continue listening for server broadcast's an Infinite loop

          //Creating Multicast sockets and packets to handle communication from server
           try {
               MulticastSocket multicastSocket =  new MulticastSocket(444);
                   //Creating and joining this multicast socket to particular group
                   InetAddress IP_GROUP =  InetAddress.getByName("228.0.0.1");
                   multicastSocket.joinGroup(IP_GROUP);

                   //Preparing packet to be received from server
                   byte[] buffer =  new byte[1024];
                   DatagramPacket packet =  new DatagramPacket(buffer , buffer.length);


               System.out.println("Waiting for server broadcast...");

                   //Begin receiving here
                   multicastSocket.receive(packet);

                  Client_Server_Data received_obj = convertBytesToObject(packet.getData());

               System.out.println(received_obj.getOPERATION());


           } catch (IOException e) {
               e.printStackTrace();
           } catch (ClassNotFoundException e) {
               e.printStackTrace();
           }

       }
    }


    private static Client_Server_Data convertBytesToObject(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream =  new ByteArrayInputStream(bytes); //Passing in bytes in bytes scream
        ObjectInputStream objectInputStream =  new ObjectInputStream(byteArrayInputStream); //Passing bytes so that they can be converted into a object
        return (Client_Server_Data)objectInputStream.readObject(); //Read the parsed//coverted object from here
    }
}
