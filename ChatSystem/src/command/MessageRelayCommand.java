package command;

public class MessageRelayCommand extends Command{
    private String identity;
    private String content;

    public MessageRelayCommand(String identity, String content){
        this.identity = identity;
        this.content = content;
    }
}
