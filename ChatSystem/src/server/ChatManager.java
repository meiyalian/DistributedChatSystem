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
        LOGGER.info("Add " + connection.getName() + " to MainHall");
        ArrayList<ServerConnection> mainHall =this.chatRooms.get(defaultRoomName);
        synchronized(mainHall){
            mainHall.add(connection);
        }

        connection.setCurrentChatRoom(defaultRoomName);
        this.sendToOneClient(jsonMessage, connection);
    }

    public void removeClientConnection(ServerConnection connection){

        String roomName = connection.getCurrentChatRoom();
        LOGGER.info("Remove " + connection.getName() + " from " + roomName);

        synchronized (this.chatRooms){
            ArrayList<ServerConnection> currentRoomClientList = this.chatRooms.get(roomName);
            currentRoomClientList.remove(roomName);
        }

        // if the room creator quit, set room owner to null
        synchronized (this.roomOwnership) {
            if (this.roomOwnership.get(roomName).equals(connection)) {
                this.roomOwnership.put(roomName, null);
            }
        }
        // get room owner
        ServerConnection currentOwner;
        synchronized (this.roomOwnership){
            currentOwner = this.roomOwnership.get(roomName);
        }

        //discard the room when the following conditions applied:
        if (getRoomSize(roomName)== 0 && !roomName.equals(ChatManager.defaultRoomName) && currentOwner == null) {
            LOGGER.info("Remove room " +  roomName);
            synchronized (this.chatRooms){
                this.chatRooms.remove(roomName); // remove room
            }
            synchronized (this.roomOwnership){
                this.roomOwnership.remove(roomName); // remove the ownership infor since the room is removed
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


    public synchronized boolean joinRoom (ServerConnection s, String roomid){
        ArrayList<ServerConnection> newRoom = this.chatRooms.getOrDefault(roomid, null);
        // if the room to join exists
        if (newRoom != null){
            LOGGER.info("Client " +  s.getName() + " join the room " + roomid);
            String currentRoom = s.getCurrentChatRoom();
            ArrayList<ServerConnection> currentRoomClientList = this.chatRooms.get(currentRoom);
            currentRoomClientList.remove(s);
            newRoom.add(s);
            s.setCurrentChatRoom(roomid);
            return true;
        }
        return false;
    }

    public synchronized int getRoomSize(String roomid){
        return this.chatRooms.get(roomid).size();
    }



}
