package command;

import com.google.gson.Gson;
import server.ChatManager;
import server.ServerConnection;

import java.util.ArrayList;

public class IdentityChangeCommand extends Command{
    private String identity;
    private final Gson gson = new Gson();
    private final String type = "identitychange";

    public IdentityChangeCommand(String identity){
        this.identity = identity;
    }

    @Override
    public void execute(ServerConnection serverConnection){
        String formerID = serverConnection.getName();
        String newID = identity;
        ChatManager chatManager = serverConnection.getChatManager();

        if (isIdentityInList(chatManager, identity) || isIdentityInvalid(identity)){
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
            chatManager.broadCast(jsonMessage);
        }

    }

    private synchronized boolean isIdentityInList(ChatManager chatManager, String identity){
        ArrayList<ServerConnection> serverConnections = chatManager.getClientConnectionList();
        for (ServerConnection serverConnection: serverConnections){
            if (identity.equals(serverConnection.getName())){
                return true;
            }
        }
        return false;
    }

    /**
     * Requirements:
     * 1. must be an alphanumeric string: character and digits only
     * 2. must start with an upper or lower case character
     * 3. 3 <= length <=16
     * @return boolean
     */
    private boolean isIdentityInvalid(String identity){
        boolean matchResult = identity.matches("[A-Za-z0-9]+"); // 1
        boolean firstCharacterCheck = Character.isLetter(identity.charAt(0)); // 2
        boolean lengthCheckResult = identity.length() >= 3 && identity.length() <= 16; // 3
        return !(matchResult && lengthCheckResult && firstCharacterCheck);
    }


}
