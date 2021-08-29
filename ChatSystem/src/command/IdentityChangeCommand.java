package command;

import server.ServerConnection;

public class IdentityChangeCommand extends Command{
    private String identity;
    private final String type = "identitychange";

    public IdentityChangeCommand(String identity){
        this.identity = identity;
    }

    @Override
    public void execute(ServerConnection serverConnection){
        String former = serverConnection.getName();
        
    };

}
