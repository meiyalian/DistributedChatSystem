package server_command;

import server.ServerConnection;

public class JoinCommand extends ServerCommand{
    private String roomid;

    public JoinCommand(String roomid){
        this.roomid = roomid;
    }

    @Override
    public void execute(ServerConnection serverConnection) {

    }
}
