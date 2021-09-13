package server_command;

import client_command.NewIdentityCommand;
import client_command.RoomChangeCommand;
import com.google.gson.Gson;
import server.ChatManager;
import server.ServerConnection;

public class JoinCommand extends ServerCommand{
    private String roomid;
    private final String type = "join";

    public JoinCommand(String roomid){
        this.roomid = roomid;
    }

    @Override
    public void execute(ServerConnection serverConnection) {
        Gson gson = new Gson();
        String identity = serverConnection.getName();
        String former = serverConnection.getCurrentChatRoom();

        ChatManager chatManager = serverConnection.getChatManager();
        chatManager.removeEmptyRoomWithOwnerDropped();

        /**
         * if join room successfully
         * - server responds roomchange message to all currently connected clients in the same room
         * if to mainhall
         * - server responds roomchange message with a roomlist message to all currently connected clients in the same room
         * -otherwise
         * server responds message only to the client
         */
        String jsonMessage;
        RoomChangeCommand roomChangeCommand;
        if (!this.roomid.equals(former) && chatManager.joinRoom(serverConnection, roomid)){
            roomChangeCommand = new RoomChangeCommand(identity, former, roomid);
            jsonMessage = gson.toJson(roomChangeCommand);
            System.out.println("Send: " + jsonMessage);
            chatManager.broadCastToCurrentRoom(serverConnection, jsonMessage, null);
            serverConnection.setCurrentChatRoom(roomid);
            //if it's mainHall send roomContent and room list
            if (this.roomid.equals("MainHall")){
                jsonMessage = WhoCommand.buildRoomContent(chatManager,"MainHall" );
                System.out.println("Send: " + jsonMessage);
                chatManager.sendToOneClient(jsonMessage,serverConnection );
                jsonMessage = ListCommand.buildRoomList(chatManager, null, null);
                System.out.println("Send: " + jsonMessage);
                chatManager.sendToOneClient(jsonMessage, serverConnection);
            }

        }
        else{ // if requested room is the current room or the requested does not exist, the request is invalid
            roomChangeCommand = new RoomChangeCommand(identity, former, former);
            jsonMessage = gson.toJson(roomChangeCommand);
            System.out.println("Send: " + jsonMessage);
            chatManager.sendToOneClient(jsonMessage, serverConnection);
        }

    }
}
