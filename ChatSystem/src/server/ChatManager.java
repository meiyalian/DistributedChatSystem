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

    public synchronized void addClientConnection(ServerConnection connection){
        clientConnectionList.add(connection);
    }

    public  synchronized  void removeClientConnection(ServerConnection connection){
        clientConnectionList.remove(connection);
    }

    private synchronized void broadCast(String message, ServerConnection ignored){
        for (ServerConnection conn: clientConnectionList){
            if (!conn.equals(ignored)){
                conn.sendMessage(message);
            }
        }
    }

}
