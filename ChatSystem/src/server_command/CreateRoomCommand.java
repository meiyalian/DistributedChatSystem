package server_command;

import server.ServerConnection;

public class CreateRoomCommand extends ServerCommand{
    private String roomid;

    public CreateRoomCommand(String roomid){
        this.roomid = roomid;
    }

    @Override
    public void execute(ServerConnection serverConnection) {

    }
}
