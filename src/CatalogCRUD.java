import org.json.JSONObject;

import java.sql.*;

public class CatalogCRUD {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private String nameDB,nameTable;

    public void connect(String nameDB,String nameTable){
        this.nameDB=nameDB;
        this.nameTable=nameTable;
        try{
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:"+nameDB);
            System.out.println("Connection");
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void createDB(){
        try {
            statement = connection.createStatement();
            statement.execute("CREATE TABLE if not exists 'fileInfo' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'fileName' VARCHAR, 'path' VARCHAR,'extension' VARCHAR,'comment' VARCHAR);");
            System.out.println("Table was created or already exist");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String getName(String name){
        try {
            resultSet = statement.executeQuery("SELECT id, username, age, score FROM "+nameTable+" WHERE name='"+name+"';");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet.toString();
    }

    public void closeDB() {
        try {
            connection.close();
            statement.close();
            resultSet.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("Connection close");
    }

}
