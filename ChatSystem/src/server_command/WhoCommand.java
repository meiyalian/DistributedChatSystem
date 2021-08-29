package server_command;

import server.ServerConnection;

public class WhoCommand extends ServerCommand{
    private String roomid;

    public WhoCommand(String roomid){
        this.roomid = roomid;
    }

    @Override
    public void execute(ServerConnection serverConnection) {

    }
}
