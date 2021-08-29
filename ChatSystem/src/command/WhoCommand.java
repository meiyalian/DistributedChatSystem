package command;

public class WhoCommand extends Command{
    private String roomid;

    public WhoCommand(String roomid){
        this.roomid = roomid;
    }
}
