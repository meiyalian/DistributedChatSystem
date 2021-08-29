package command;

import client.ChatClient;
import server.ServerConnection;
import utility.IdentityManager;

/**
 * This class is used when client first connect to server or client requests for an identity change
 * Request message:
 * {"type": "newidentity", "former": "", "identity": "guest3"}
 */
public class NewIdentityCommand extends Command{
    private String former;
    private String identity;
    private static IdentityManager identityManager = IdentityManager.getInstance();
    private final String type = "newidentity";

    public NewIdentityCommand(String former, String identity){
        this.former = former;
        this.identity = identity;
    }

    @Override
    public void execute(ChatClient chatClient) {
        if (identityManager.isIdentityInList(identity) || identityManager.isIdentityValid(identity)){
            System.out.println("Warning: Invalid or currently in use identity is given, no change to identity");
            identity = former;
        } else if (former.equals("")){
            identityManager.addIdentity(identity);
            chatClient.setIdentity(identity);
            System.out.println("Identity Change: " + former + "changes to " + identity);
        } else{
            identityManager.removeIdentity(former);
            identityManager.addIdentity(identity);
            chatClient.setIdentity(identity);
            System.out.println("Identity Change: " + former + "changes to " + identity);
        }


    }
}
