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
        StringBuilder print = new StringBuilder(roomId + " contains ");

        // if room not exist, no response
        if (this.owner.equals("") && !this.roomId.equals("MainHall")){
            return;
        }

        // if no one in the room, displays "room is empty"
        if (identities.size() == 0 ){
            System.out.println(roomId + " is empty.");
            return;
        }
        //otherwise, display the room information
        for(String str: identities){
            if (str.equals(owner)){
                print.append("*").append(str).append(" ");
            }else{
                print.append(str).append(" ");
            }
        }
        System.out.println(print);

        if (!chatClient.isBundleMsg()){
            chatClient.printPrefix();
        }

    }
}
