package brdiged_resources;

/**
 * Created by Aamir on 5/25/2017.
 */
public enum OPERATIONS {
    SIGNUP ,     //Sign up event  , when a user makes a Sign up request
    LOGIN ,     //Login event    ,  when a user makes a login request
    OFFLINE ,  //OFFLINE EVENT  ,   when a user goes offline
    err_msg,   // Error occured , read error msg ,  this is a signal for client that some error occured so read  error msg
    signup_good, //Incase of succesful signup => set by server for client
    login_good // incase of successful login  => set by server for client
}
