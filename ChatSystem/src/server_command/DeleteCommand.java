package server_command;

import server.ServerConnection;

public class DeleteCommand extends ServerCommand{
    private String roomid;

    public DeleteCommand(String roomid){
        this.roomid = roomid;
    }

    @Override
    public void execute(ServerConnection serverConnection) {

    }
}
