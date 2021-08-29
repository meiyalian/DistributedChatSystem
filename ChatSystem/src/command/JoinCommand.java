package command;

public class JoinCommand extends Command{
    private String roomid;

    public JoinCommand(String roomid){
        this.roomid = roomid;
    }
}
