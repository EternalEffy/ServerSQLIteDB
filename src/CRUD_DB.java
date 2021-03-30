import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;

public class CRUD_DB {

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
            statement.execute("CREATE TABLE if not exists '"+nameTable+"' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'username' VARCHAR, 'age' VARCHAR,'score' VARCHAR,'level' VARCHAR);");
            System.out.println("Table was created or already exist");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String add(JSONArray jsonArray){
        if(!jsonArray.equals(null)){
            try {
                statement.execute(new User(jsonArray.getString(0),jsonArray.getString(1),jsonArray.getString(2),jsonArray.getString(3)).requestAdd());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return null;
    }

    public String getId(int index){
        try {
            resultSet = statement.executeQuery("SELECT id, username, age, score FROM "+nameTable+" WHERE id='"+index+"';");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet.toString();
    }

    public String getName(String name){
        try {
            resultSet = statement.executeQuery("SELECT id, username, age, score FROM "+nameTable+" WHERE username='"+name+"';");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet.toString();
    }

    public String edit(JSONObject jsonObject,int index){
        return null;
    }

    public String remove(JSONObject jsonObject,int index){
        try {
            statement.execute("DELETE FROM "+nameTable+" WHERE id='"+index+"';");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return getId(index);
    }
}
