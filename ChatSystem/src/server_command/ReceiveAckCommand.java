package server_command;

import server.ChatManager;
import server.ServerConnection;

public class ReceiveAckCommand extends ServerCommand {
    private final String type = "ack";

    @Override
    public void execute(ServerConnection serverConnection) {
        ChatManager chatManager = serverConnection.getChatManager();
        chatManager.removeFromClientWaitForAckList(serverConnection);
    }
}
