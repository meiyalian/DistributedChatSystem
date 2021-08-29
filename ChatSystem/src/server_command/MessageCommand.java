package server_command;

import server.ServerConnection;

public class MessageCommand extends ServerCommand{
    private String content;

    public MessageCommand(String content){
        this.content = content;
    }

    @Override
    public void execute(ServerConnection serverConnection) {

    }
}
