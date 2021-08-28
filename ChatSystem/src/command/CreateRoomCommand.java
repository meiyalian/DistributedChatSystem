package command;

public class CreateRoomCommand extends Command{
    private String roomId;

    public CreateRoomCommand(String roomId){
        this.roomId = roomId;
    }
}
