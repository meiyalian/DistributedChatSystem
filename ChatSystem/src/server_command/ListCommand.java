package server_command;

import client_command.RoomChangeCommand;
import client_command.RoomListCommand;
import com.google.gson.Gson;
import server.ChatManager;
import server.ServerConnection;

import java.util.HashMap;

public class ListCommand extends ServerCommand{
    private final String type = "list";

    @Override
    public void execute(ServerConnection serverConnection) {
        ChatManager chatManager = serverConnection.getChatManager();
        chatManager.sendToOneClient(buildRoomList(chatManager), serverConnection);

    }

    public static String buildRoomList(ChatManager chatManager){
        Gson gson = new Gson();

        RoomListCommand roomListCommand = new RoomListCommand(chatManager.getRoomsInfo());
        return gson.toJson(roomListCommand);

    }
}
