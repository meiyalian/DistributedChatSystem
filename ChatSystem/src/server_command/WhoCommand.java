package server_command;

import client_command.RoomChangeCommand;
import client_command.RoomContentsCommand;
import com.google.gson.Gson;
import server.ChatManager;
import server.ServerConnection;

import java.util.ArrayList;

public class WhoCommand extends ServerCommand{
    private String roomid;
    private final String type = "who";

    public WhoCommand(String roomid){
        this.roomid = roomid;
    }

    @Override
    public void execute(ServerConnection serverConnection) {

        ChatManager chatManager = serverConnection.getChatManager();
        String jsonMessage = buildRoomContent(chatManager, roomid);
//        System.out.println("json msg:" +  jsonMessage);
//        System.out.println("room size: " + chatManager.getRoomSize(roomid));
        chatManager.sendToOneClient(jsonMessage, serverConnection);
    }


    public static String buildRoomContent(ChatManager chatManager, String roomid){
        Gson gson = new Gson();
        String owner = chatManager.getRoomOwner(roomid);
        ArrayList<String> identities = chatManager.getRoomIdentities(roomid);
        RoomContentsCommand roomContentsCommand = new RoomContentsCommand(roomid, identities, owner);
        return gson.toJson(roomContentsCommand);
    }

}
