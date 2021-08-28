package command;

public class IdentityChangeCommand extends Command{
    private String identity;

    public IdentityChangeCommand(String identity){
        this.identity = identity;
    }
}
