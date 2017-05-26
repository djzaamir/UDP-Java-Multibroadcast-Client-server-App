package SERVER;

import brdiged_resources.Client_Server_Data;

import java.sql.*;

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



    //region function Section

    //Geet connection with Db
    private  static void initDatabaseConnection() throws Exception{
         conn = DriverManager.getConnection(url , username , password);
    }

    //Function to truncate Online users Db At startup and shutdown of server
    public static boolean truncateOnlineUsers() throws Exception {
        initDatabaseConnection();

        String query = "truncate online_users"; //Query

        Statement statement =  conn.createStatement();

        return statement.execute(query) == false?true:false;

    }

     //Function to lookup for username
    public static boolean usernameExist(String username) throws Exception {
        initDatabaseConnection();

        String query  = "SELECT * FROM users WHERE `username`=?";

        PreparedStatement statement =  conn.prepareStatement(query);

        //Inserting data into query
        statement.setString(1,username);

        ResultSet set = statement.executeQuery();
        //if there are some records
        while (set.next()){
            //if the username is matched with incoming username
            if (set.getString("username").equals(username)){
                return  true;
            }
        }
        return false;
    }

    //function to insert data into db

    public static boolean insertUser(Client_Server_Data data) throws Exception {
        initDatabaseConnection();

        String query = "INSERT INTO `users` (`username` , `password`) VALUES(?,?)";

        PreparedStatement statement =  conn.prepareStatement(query);

        //Inserting data  into prepare statement
        statement.setString(1 ,data.getUsername());
        statement.setString(2 ,data.getPassword());

        //Executing query
        return statement.execute() == false ? true : false;

    }

    //endregion

}

