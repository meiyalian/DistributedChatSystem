package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import command.*;

public class CommandFactory {
    private final Gson gson;

    public CommandFactory(){
        this.gson = new Gson();
    }

    public Command convertJsonToCommand(String jsonMessage){
        JsonObject jsonObject = gson.fromJson(jsonMessage, JsonObject.class);
        String type = jsonObject.get("type").getAsString();

        switch(type){
            case "newidentity":
                return new NewIdentityCommand();
            case "identitychange":
                return new IdentityChangeCommand();
            case "join":
                return new JoinCommand();
            case "roomchange":
                return new RoomChangeCommand();
            case "roomcontents":
                return new RoomContentsCommand();
            case "who":
                return new WhoCommand();
            case "roomlist":
                return new RoomListCommand();
            case "list":
                return new ListCommand();
            case "createroom":
                return new CreateRoomCommand();
            case "delete":
                return new DeleteCommand();
            case "message":
                return new MessageCommand();
            case "quit":
                return new QuitCommand();
            default:
                return null;
        }
    }

}
