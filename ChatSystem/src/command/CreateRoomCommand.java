package command;

public class CreateRoomCommand extends Command{
    private String roomid;

    public CreateRoomCommand(String roomid){
        this.roomid = roomid;
    }
}
