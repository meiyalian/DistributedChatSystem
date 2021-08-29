package client_command;

import client.ChatClient;

public class MessageRelayCommand extends ClientCommand{
    private String identity;
    private String content;

    public MessageRelayCommand(String identity, String content){
        this.identity = identity;
        this.content = content;
    }

    @Override
    public void execute(ChatClient chatClient) {

    }
}
