package command;

public class WhoCommand extends Command{
    private String roomId;

    public WhoCommand(String roomId){
        this.roomId = roomId;
    }
}
