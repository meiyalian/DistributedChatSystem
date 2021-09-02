package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;


/**
 * ChatManager class is responsible for performing admin tasks including:
 * managing clients (connections), joining room, changing room, etc.
 */
public class ChatManager {
    private HashMap<String, ArrayList<ServerConnection>> chatRooms;// room list
    private HashMap<String, ServerConnection> roomOwnership;
    protected static String defaultRoomName = "MainHall";
    public static final Logger LOGGER = Logger.getLogger(ChatServer.class.getName());


    public ChatManager(){
        chatRooms = new HashMap<>();
        roomOwnership= new HashMap<>();
        chatRooms.put(defaultRoomName, new ArrayList<>());
        roomOwnership.put(defaultRoomName, null); // main hall does not have owner
    }

    public void addClientConnection(ServerConnection connection, String jsonMessage){
        // place client to default room
        ArrayList<ServerConnection> mainHall =this.chatRooms.get(defaultRoomName);
        LOGGER.info("Add " + connection.getName() + " to mainHall");
        synchronized(mainHall){
            mainHall.add(connection);
        }

        connection.setCurrentChatRoom(defaultRoomName);
        this.sendToOneClient(jsonMessage, connection);
    }

    public void removeClientConnection(ServerConnection connection){

        String roomName = connection.getCurrentChatRoom();
        ArrayList<ServerConnection> room = this.chatRooms.get(roomName);
        LOGGER.info("Remove " + connection.getName() + " from " + roomName);
        synchronized(room) {
            room.remove(connection);

            // if the room creator quit, set room owner to null
            if (this.roomOwnership.get(roomName).equals(connection)) {
                this.roomOwnership.put(roomName, null);
            }
            // discard the room when the following conditions applied:
            if (room.size() == 0 && !roomName.equals(ChatManager.defaultRoomName) && roomOwnership.get(roomName) == null) {
                LOGGER.info("Remove room " +  roomName);
                this.chatRooms.remove(roomName);
                this.roomOwnership.remove(roomName);
            }
        }
    }

    public void broadCastAllRooms(String message, ServerConnection ignore){
        LOGGER.info("Broadcast msg to all rooms");
        for (ArrayList<ServerConnection> clients : this.chatRooms.values()){
            broadCastAGroup(clients,message, ignore );
        }

    }

    public void broadCastAGroup(ArrayList<ServerConnection> clients, String message, ServerConnection ignore){
        synchronized (clients){
            for (ServerConnection s: clients){
                if (ignore == null || !s.equals(ignore)){
                    s.sendMessage(message);
                }
            }
        }
    }


    public void broadCastToCurrentRoom(ServerConnection s, String message ,ServerConnection ignore){
        String roomName = s.getCurrentChatRoom();
        LOGGER.info("Broadcast msg to " +roomName );
        broadCastAGroup(this.chatRooms.get(roomName), message,ignore);
    }

    public void sendToOneClient(String message, ServerConnection serverConnection){
        LOGGER.info("Send msg to " +  serverConnection.getName());
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
