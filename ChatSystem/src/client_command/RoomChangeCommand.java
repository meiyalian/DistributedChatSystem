package client_command;

import client.ChatClient;

public class RoomChangeCommand extends ClientCommand{
    private String roomid;
    private String identity;
    private String former;
    private String type = "roomchange";

    public RoomChangeCommand(String identity, String former, String roomid){
        this.identity = identity;
        this.former   = former;
        this.roomid   = roomid;
    }

    @Override
    public void execute(ChatClient chatClient) {

        if(!former.equals(roomid)){
            if (identity.equals(chatClient.getIdentity())){
                chatClient.setRoomid(roomid);
            }
            System.out.println(identity + " moved from " + former + " to " + roomid);
        }else{
            System.out.println("The requested room is invalid or non existent");
        }

        chatClient.printPrefix();
    }
}
