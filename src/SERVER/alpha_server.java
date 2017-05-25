package SERVER;

/**
 * Created by Aamir on 5/25/2017.
 */
public class alpha_server {

    public static void main(String[] args) throws Exception {
        /*
        * Main Logic
        * On startup first of all , truncate online users table
        * */

        //On startup truncate online_users table
        DBA.truncateOnlineUsers();


    }
}
