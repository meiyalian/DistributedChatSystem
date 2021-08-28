package command;

import server.ServerSideConnection;

/**
 * This class is used when client first connect to server or client requests for an identity change
 * Request message:
 * {"type": "newidentity", "former": "", "identity": "guest3"}
 */
public class NewIdentityCommand extends Command{
    private String former;
    private String identity;
    private final String type = "newidentity";

    public NewIdentityCommand(String former, String identity){
        this.former = former;
        this.identity = identity;
    }

    @Override
    public void execute(ServerSideConnection serverSideConnection) {

    }
}
