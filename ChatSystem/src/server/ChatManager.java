package server;

import client_command.RoomChangeCommand;
import client_command.RoomContentsCommand;
import client_command.RoomListCommand;
import com.google.gson.Gson;
import server_command.JoinCommand;
import server_command.ListCommand;
import server_command.WhoCommand;
import shared.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


/**
 * ChatManager class is responsible for performing admin tasks including:
 * managing clients (connections), joining room, changing room, etc.
 */

//TODO: If any room other than MainHall has an em p t y owner and becomes em p t y ( i.e. has no contents ) then the room is deleted immediatel y .
public class ChatManager {
    private HashMap<String, ArrayList<ServerConnection>> chatRooms;// room list
    private HashMap<String, ServerConnection> roomOwnership;
    protected static String defaultRoomName = "MainHall";
    public static final Logger LOGGER = Logger.getLogger(ChatServer.class.getName());
    private Gson gson;


    public ChatManager(){
        chatRooms = new HashMap<>();
        roomOwnership= new HashMap<>();
        chatRooms.put(defaultRoomName, new ArrayList<>());
        roomOwnership.put(defaultRoomName, null); // main hall does not have owner
        gson =  new Gson();
    }

    public void addClientConnection(ServerConnection connection, String jsonMessage){
        // place client to default room
        LOGGER.info("New user:  " + connection.getName());
//        ArrayList<ServerConnection> mainHall =this.chatRooms.get(defaultRoomName);
//        synchronized(mainHall){
//            mainHall.add(connection);
//        }
//        connection.setCurrentChatRoom(defaultRoomName);
        this.sendToOneClient(jsonMessage, connection);
//
//        String roomLstMsg = ListCommand.buildRoomList(this, null, null);
//        String roomContentMsg = WhoCommand.buildRoomContent(this, defaultRoomName);
//        RoomChangeCommand roomChangeCommand = new RoomChangeCommand(connection.getName(), "", defaultRoomName);
//        String newClientMsg = gson.toJson(roomChangeCommand);
//        this.sendToOneClient(roomLstMsg, connection);
//        this.broadCastToCurrentRoom(connection, newClientMsg, null);
//        this.sendToOneClient(roomContentMsg, connection);

        JoinCommand j = new JoinCommand(defaultRoomName);
        j.execute(connection);

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
            if (currentRoomClientList != null) {
                currentRoomClientList.remove(s);
            }
            newRoom.add(s);
            s.setCurrentChatRoom(roomid);
            return true;
        }
        return false;
    }

    public synchronized int getRoomSize(String roomid){
        return this.chatRooms.get(roomid).size();
    }

    public String getRoomOwner(String roomid){
        ServerConnection owner;
        synchronized(this.roomOwnership) {
            owner = this.roomOwnership.getOrDefault(roomid, null);
        }
        return owner == null? "" : owner.getName();
    }

    public synchronized ArrayList<String> getRoomIdentities(String roomid){
        LOGGER.info("Client requests " + roomid + " identities");
        ArrayList<String> identities = new ArrayList<>();
        ArrayList<ServerConnection> clientsInRoom = this.chatRooms.getOrDefault(roomid, null);
        if (clientsInRoom != null){
            for(ServerConnection s: clientsInRoom){
                identities.add(s.getName());
            }
        }
        return identities;
    }

    public HashMap<String, Integer> getRoomsInfo(String ignore, String addition){
        HashMap<String, Integer> roomsInfo = new HashMap<>();
        synchronized (this.chatRooms){
            for (Map.Entry<String, ArrayList<ServerConnection>> entry : this.chatRooms.entrySet()){
                String roomid = entry.getKey();
                if (!roomid.equals(ignore)){
                    roomsInfo.put(entry.getKey(), entry.getValue().size());
                }
            }
        }

        if (addition != null){
            roomsInfo.put(addition, 0);
        }

        return roomsInfo;
    }

    public synchronized boolean createRoom(ServerConnection s, String roomid){

        if (Validator.isRoomIdValid(roomid) && !this.roomOwnership.containsKey(roomid)){
            this.chatRooms.put(roomid, new ArrayList<>());
            this.roomOwnership.put(roomid, s);
            return true;
        }
        else{
            return false;
        }

    }

    //check if the room is valid and the owner is the input identity
    private synchronized boolean isRoomOwner (ServerConnection identity, String roomid){
        ServerConnection owner = this.roomOwnership.get(roomid);
        if (owner == null){
            return false;
        }else{
            return owner.getName().equals(identity.getName());
        }
    }

    public boolean deleteRoom(ServerConnection s, String roomid){
        if (isRoomOwner(s, roomid)){
            ArrayList<ServerConnection> clientsInRoom;
            synchronized (this.chatRooms) {
                clientsInRoom = this.chatRooms.get(roomid);
            }
                if (clientsInRoom == null ){
                    System.out.println("Delete fail because no such room exist ");
                    return false;
                }
                // in order to prevent concurrency change to the array, make a shallow copy
            ArrayList<ServerConnection> copiedClients = new ArrayList<ServerConnection>(clientsInRoom);
            for(ServerConnection connection: copiedClients){
                if (!connection.equals(s)){
                    JoinCommand j = new JoinCommand(defaultRoomName);
                    j.execute(connection);
                }
            }

            synchronized (this.chatRooms) {
                this.chatRooms.remove(roomid);
            }
            synchronized (this.roomOwnership){
                this.roomOwnership.remove(roomid);
            }
            return true;
        }
        System.out.println("Delete fail because you are not an owner ");
        return false;
    }








}
