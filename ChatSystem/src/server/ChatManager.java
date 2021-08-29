package server;

import java.util.ArrayList;

/**
 * ChatManager class is responsible for performing admin tasks including:
 * managing clients (connections), joining room, changing room, etc.
 */
public class ChatManager {
    private ArrayList<ServerConnection> clientConnectionList;

    public ChatManager(){
        clientConnectionList = new ArrayList<>();
    }

    public synchronized ArrayList<ServerConnection> getClientConnectionList() {return clientConnectionList;}

    public synchronized void addClientConnection(ServerConnection connection, String jsonMessage){
        clientConnectionList.add(connection);
        this.broadCast(jsonMessage);
    }

    public  synchronized  void removeClientConnection(ServerConnection connection){
        clientConnectionList.remove(connection);
    }

    public synchronized void sendToOneClient(String message, ServerConnection serverConnection){
        serverConnection.sendMessage(message);
    }

    public synchronized void broadCast(String message){
        for (ServerConnection conn: clientConnectionList){
            conn.sendMessage(message);
        }
    }

    public synchronized void broadCast(String message, ArrayList<ServerConnection> ignoredConnections){
        for (ServerConnection conn: clientConnectionList){
            if (!ignoredConnections.contains(conn)){
                conn.sendMessage(message);
            }
        }
    }

}
