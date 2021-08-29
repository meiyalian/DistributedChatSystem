package command;

public class DeleteCommand extends Command{
    private String roomid;

    public DeleteCommand(String roomid){
        this.roomid = roomid;
    }
}
