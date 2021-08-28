package server;

import java.util.ArrayList;

/**
 * ChatManager class is responsible for performing admin tasks including:
 * managing clients (connections), joining room, changing room, etc.
 */
public class ChatManager {
    private ArrayList<ChatConnection> clientConnectionList;

    public ChatManager(){
        clientConnectionList = new ArrayList<>();
    }

    public synchronized void addClientConnection(ChatConnection connection){
        clientConnectionList.add(connection);
    }

    public  synchronized  void removeClientConnection(ChatConnection connection){
        clientConnectionList.remove(connection);
    }

    private synchronized void broadCast(String message, ChatConnection ignored){
        for (ChatConnection conn: clientConnectionList){
            if (!conn.equals(ignored)){
                conn.sendMessage(message);
            }
        }
    }

}
