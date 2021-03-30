import org.json.JSONObject;
import java.io.*;
import java.net.Socket;

public class Server implements Runnable{
    private static Socket clientSocket;
    private static DataInputStream inStream;
    private static DataOutputStream outStream;
    private static CRUD_DB crud;
    private CatalogCRUD fileCrud;
    private int index,port;
    private String requestFormClient;
    private boolean flag = true;
    private byte[] buffer;
    private JSONObject jsonObject;
    private Exception FileNotFound;
    private String nameClient;

    public Server(Socket client) {
        clientSocket = client;
    }

    public void loadServer() {
        crud = new CRUD_DB();
        crud.connect("Catalog.db","users");
        crud.createDB();
        fileCrud = new CatalogCRUD();
        fileCrud.connect("Catalog.db","fileInfo");
        fileCrud.createDB();
        System.out.println("Server started");
        System.out.println("Port: "+port);
        try {
            while(!clientSocket.isClosed()) {
                if (clientSocket.isConnected()) {
                    inStream = new DataInputStream(clientSocket.getInputStream());
                    outStream = new DataOutputStream((clientSocket.getOutputStream()));
                    nameClient=inStream.readUTF();
                    System.out.println(ServerMessages.MESSAGE_ACCESS + clientSocket.getInetAddress()+"clientName: "+nameClient);
                    outStream.writeUTF( nameClient+ ServerMessages.USER_MESSAGE_ACCESS);
                    outStream.flush();
                }
                requestFormClient = inStream.readUTF();
                jsonObject = new JSONObject(requestFormClient);
                System.out.println(ServerMessages.MESSAGE_REQUEST+jsonObject+" "+nameClient);
                switch (jsonObject.getString("request")) {
                    case Requests.add:
                        index = Integer.parseInt(inStream.readUTF());
                        add(jsonObject);
                        break;
                    case Requests.getId:
                        index = Integer.parseInt(inStream.readUTF());
                        getId(index);
                        break;
                    case Requests.getName:
                        index = Integer.parseInt(inStream.readUTF());
                        getName(jsonObject);
                        break;
                    /*case Requests.edit:
                        index = Integer.parseInt(inStream.readUTF());
                        edit(jsonObject,index);
                        break;

                     */
                    case Requests.remove:
                        index = Integer.parseInt(inStream.readUTF());
                        remove(jsonObject,index);
                        break;
                   /* case Requests.getFile:
                        try {
                            System.out.println("Search file");
                            if ((fileCrud.getName(jsonObject.getString("name")).equals(jsonObject.getString("name")))) {
                                System.out.println("Making jsonObject with fileInfo");
                                JSONObject jsonNeed = new JSONObject(fileCrud.getName(jsonObject.getString("name")));
                                System.out.println("Making file object name: " + jsonObject.getString("name"));
                                File f = new File(jsonNeed.getString("path")+jsonNeed.getString("fileName")+jsonNeed.getString("extension"));
                                System.out.println("Path: "+f.getPath());
                                sendFile(f,jsonNeed.getString("extension"));
                            } else {
                                System.out.println("Throw exception");
                                throw FileNotFound;
                            }
                        } catch (Exception e) {
                            outStream.writeUTF(new JSONObject("{\"request\":\"" + ServerMessages.MESSAGE_REQUEST_NO + "\",\"file\":\"0\"}").toString());
                            outStream.flush();
                        }
                        break;
                    */
                    case Requests.stop:
                        flag = false;
                        break;
                    default:
                        outStream.writeUTF(ServerMessages.MESSAGE_ERROR);
                        outStream.flush();
                        System.out.println(ServerMessages.MESSAGE_USER_INFO + ServerMessages.MESSAGE_RESULT_NO);
                }
            }
        }
        catch (InterruptedIOException e){
            System.out.println("Interrupted");
        }
        catch (IOException e) {
            System.out.println(ServerMessages.MESSAGE_CLIENT_CLOSE+nameClient);
            System.out.println(ServerMessages.MESSAGE_END);
        }
    }

    private void add(JSONObject jsonObject){
        try {
            System.out.println(ServerMessages.MESSAGE_ADD);
            crud.add(jsonObject.getJSONArray("userData"));
            System.out.println(ServerMessages.MESSAGE_USER_INFO + ServerMessages.MESSAGE_RESULT_YES);
            outStream.writeUTF(ServerMessages.MESSAGE_USER_INFO + crud.getName(jsonObject.getJSONArray("userData").getString(0)));
            outStream.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void getId(int index){
        try{
            System.out.println(ServerMessages.MESSAGE_GET+index);
            crud.getId(index);
            System.out.println(ServerMessages.MESSAGE_USER_INFO + ServerMessages.MESSAGE_RESULT_YES);
            outStream.writeUTF(ServerMessages.MESSAGE_USER_INFO + crud.getId(index));
            outStream.flush();

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private void getName(JSONObject jsonObject){
        try{
            System.out.println(ServerMessages.MESSAGE_GET+jsonObject.getString("username"));
            crud.getName(jsonObject.getString("username"));
            System.out.println(ServerMessages.MESSAGE_USER_INFO + ServerMessages.MESSAGE_RESULT_YES);
            outStream.writeUTF(ServerMessages.MESSAGE_USER_INFO + crud.getName(jsonObject.getString("username")));
            outStream.flush();

        }catch (IOException e){
            e.printStackTrace();
        }

    }

   /* private void edit(JSONObject jsonObject,int index){
        try {
            System.out.println(ServerMessages.MESSAGE_EDIT);
            crud.edit(jsonObject,index);
            System.out.println(ServerMessages.MESSAGE_USER_INFO + ServerMessages.MESSAGE_RESULT_YES);
            outStream.writeUTF(ServerMessages.MESSAGE_USER_INFO + crud.getName(jsonObject.getString("username")));
            outStream.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    */

    private void remove(JSONObject jsonObject,int index){
        try {
            System.out.println(ServerMessages.MESSAGE_REMOVE);
            crud.remove(jsonObject,index);
            System.out.println(ServerMessages.MESSAGE_USER_INFO + ServerMessages.MESSAGE_RESULT_YES);
            outStream.writeUTF(ServerMessages.MESSAGE_USER_INFO + crud.getId(index));
            outStream.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void sendFile(File f,String extension){
        try {
            System.out.println("File exist. Sending response to client " + nameClient);
            outStream.writeUTF(new JSONObject("{\"request\":\"OK\",\"file\":\"" + f.length() + "\",\"extension\":\""+extension+"\"}").toString());
            System.out.println(nameClient+" currentClient take response from me");
            outStream.flush();
            buffer = new byte[(int) f.length()];
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
            bis.read(buffer, 0, buffer.length);
            System.out.println("Start sending file " + f.getName() + " to client " + nameClient);
            System.out.println(nameClient+" currentClient take file");
            outStream.write(buffer, 0, buffer.length);
            outStream.flush();
            System.out.println("File was send successful to client " + nameClient);
            inStream.close();
            outStream.close();
        }catch (InterruptedIOException e){
            System.out.println("interrupted in SEND FILE");
        }
        catch (IOException e){
            System.out.println("Send file ERROR");
        }
    }


    @Override
    public void run() {
        loadServer();
    }
}
