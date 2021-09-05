package client_command;

import client.ChatClient;

import java.util.ArrayList;
import java.util.List;

public class RoomContentsCommand extends ClientCommand{

    private String type = "roomcontents";
    private String roomId;
    private List<String> identities;
    private String owner;

    public RoomContentsCommand(String roomId, List<String> identities, String owner) {
        this.roomId = roomId;
        this.identities = identities;
        this.owner = owner;
    }



    @Override
    public void execute(ChatClient chatClient) {
        String print = "Room ID:" + roomId + "\n" + "Identities:[ ";
        for(String str: identities){
            print += str + ", ";
        }
        System.out.println(print.substring(0, print.length()-2) + " ]\nOwner: " + (owner.equals("")? "None":owner));
    }
}
