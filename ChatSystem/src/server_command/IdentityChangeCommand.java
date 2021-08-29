package server_command;

import client_command.NewIdentityCommand;
import com.google.gson.Gson;
import server.ChatManager;
import server.ServerConnection;
import shared.IdentityValidator;

public class IdentityChangeCommand extends ServerCommand {
    private String identity;
    private final String type = "identitychange";

    public IdentityChangeCommand(String identity){
        this.identity = identity;
    }

    @Override
    public void execute(ServerConnection serverConnection){
        Gson gson = new Gson();
        String formerID = serverConnection.getName();
        String newID = identity;
        ChatManager chatManager = serverConnection.getChatManager();

        if (IdentityValidator.isIdentityInList(chatManager, identity) || IdentityValidator.isIdentityInvalid(identity)){
            newID = formerID;
        }

        serverConnection.setName(newID);
        NewIdentityCommand newIdentityCommand = new NewIdentityCommand(formerID, newID);

        /**
         * if identity does not change
         * - server responds NewIdentity message only to the client
         * if identity changed
         * - server responds NewIdentity message to all currently connected clients in the same room
         */
        String jsonMessage = gson.toJson(newIdentityCommand);
        if (newID.equals(formerID)){
            System.out.println("Requested identity invalid or in use");
            chatManager.sendToOneClient(jsonMessage, serverConnection);
        } else {
            System.out.println(formerID + " is now " + newID);
            chatManager.broadCast(jsonMessage);
        }

    }

}
