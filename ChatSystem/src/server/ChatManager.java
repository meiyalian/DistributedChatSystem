package server;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * ChatManager class is responsible for performing admin tasks including:
 * managing clients (connections), joining room, changing room, etc.
 */
public class ChatManager {
    private HashMap<String, ArrayList<ServerConnection>> chatRooms;// room list
    private HashMap<String, ServerConnection> roomOwnership;
    protected static String defaultRoomName = "MainHall";



    public ChatManager(){
        chatRooms = new HashMap<>();
        roomOwnership= new HashMap<>();
        chatRooms.put(defaultRoomName, new ArrayList<>());
    }

//    public synchronized ArrayList<ServerConnection> getClientConnectionList() {return clientConnectionList;}

    public synchronized void addClientConnection(ServerConnection connection, String jsonMessage){
        // place client to default room
        this.chatRooms.get(defaultRoomName).add(connection);
        connection.setCurrentChatRoom(defaultRoomName);
        this.sendToOneClient(jsonMessage, connection);
    }

    public synchronized void removeClientConnection(ServerConnection connection){

        String roomName = connection.getCurrentChatRoom();
        ArrayList<ServerConnection> room = this.chatRooms.get(roomName);
        room.remove(connection);
        // if the room creator quit, set room owner to null
        if (this.roomOwnership.get(roomName).equals(connection)){
            this.roomOwnership.put(roomName, null);
        }
        // discard the room when the following conditions applied:
        if (room.size() == 0 && !roomName.equals(ChatManager.defaultRoomName) && roomOwnership.get(roomName) == null ){
            this.chatRooms.remove(roomName);
            this.roomOwnership.remove(roomName);
        }
    }

    public synchronized void broadCastAllRooms(String message, ServerConnection ignore){

        for (ArrayList<ServerConnection> clients : this.chatRooms.values()){
            broadCastAGroup(clients,message, ignore );
        }

    }

    public void broadCastAGroup(ArrayList<ServerConnection> clients, String message, ServerConnection ignore){
        for (ServerConnection s: clients){
            if (ignore != null && !s.equals(ignore)){
                s.sendMessage(message);
            }
        }
    }


    public synchronized void broadCastToCurrentRoom(ServerConnection s, String message ,ServerConnection ignore){
        broadCastAGroup(this.chatRooms.get(s.getCurrentChatRoom()), message,ignore);
    }

    public void sendToOneClient(String message, ServerConnection serverConnection){
        serverConnection.sendMessage(message);
    }

    public synchronized boolean isUniqueIdentity(String identity){
        for (ArrayList<ServerConnection> clients: this.chatRooms.values()){
            for (ServerConnection s : clients){
                if (identity.equals(s.getName())){
                    return false;
                }
            }
        }
        return true;
    }


}
