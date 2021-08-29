package client_command;

import client.ChatClient;

/**
 * This class is used when client first connect to server or client requests for an identity change
 * Request message:
 * {"type": "newidentity", "former": "", "identity": "guest3"}
 */
public class NewIdentityCommand extends ClientCommand {
    private String former;
    private String identity;
    private final String type = "newidentity";

    public NewIdentityCommand(String former, String identity){
        this.former = former;
        this.identity = identity;
    }

    @Override
    public void execute(ChatClient chatClient) {
        chatClient.setIdentity(identity);

        if (former.equals("")){
            System.out.println("Connected to localhost as " + identity);
        } else if (former.equals(identity)) {
            System.out.println("Requested identity invalid or in use");
        } else {
            System.out.println(former + " is now " + identity);
        }
        chatClient.printPrefix();
    }
}
