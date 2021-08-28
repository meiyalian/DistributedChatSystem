package command;

public class JoinCommand extends Command{
    private String roomId;

    public JoinCommand(String roomId){
        this.roomId = roomId;
    }
}
