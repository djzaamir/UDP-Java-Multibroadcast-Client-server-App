package brdiged_resources;

import java.io.Serializable;

/**
 * Created by Aamir on 5/25/2017.
 */
public class Client_Server_Data implements Serializable {

    //region VARS
      private String ip;          // client ip no
      private int port;          // client port no
      private String username;  /*client username , this can be used multiple purposes e.g sign-up ,
                                         continue =>  login , online / offline status update*/
      private String password; //client password , this can be used multiple purposes e.g sign-up or login
      private OPERATIONS OPERATION; // the type operation clients wants to perform on server
    //endregion


    //region Auto generated code
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public OPERATIONS getOPERATION() {
        return OPERATION;
    }

    public void setOPERATION(OPERATIONS OPERATION) {
        this.OPERATION = OPERATION;
    }
    //endregion
}
