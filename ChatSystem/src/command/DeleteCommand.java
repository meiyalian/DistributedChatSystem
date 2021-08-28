package command;

public class DeleteCommand extends Command{
    private String roomId;

    public DeleteCommand(String roomId){
        this.roomId = roomId;
    }
}
