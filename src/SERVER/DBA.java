package SERVER;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by Aamir on 5/25/2017.
 */
public class DBA {

    //region vars
    private static String url = "jdbc:mysql://localhost:3306/udpserver";
    private static String username  = "root";
    private static String password  = "";
    private static  Connection conn;
    //endregion


    public static boolean truncateOnlineUsers() throws Exception {
        initDatabaseConnection();

        String query = "truncate online_users"; //Query

        Statement statement =  conn.createStatement();

        return statement.execute(query) == false?true:false;

    }


    //region function Section
    private  static void initDatabaseConnection() throws Exception{
         conn = DriverManager.getConnection(url , username , password);
    }
    //endregion

}

