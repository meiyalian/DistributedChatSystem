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
        String type = gson.fromJson(jsonMessage, JsonObject.class).get("type").getAsString();

        switch(type){
            case "newidentity":
                return this.gson.fromJson(jsonMessage, NewIdentityCommand.class);
            case "identitychange":
                return this.gson.fromJson(jsonMessage, IdentityChangeCommand.class);
            case "join":
                return this.gson.fromJson(jsonMessage, JoinCommand.class);
            case "roomchange":
                return this.gson.fromJson(jsonMessage, RoomChangeCommand.class);
            case "roomcontents":
                return this.gson.fromJson(jsonMessage, RoomContentsCommand.class);
            case "who":
                return this.gson.fromJson(jsonMessage, WhoCommand.class);
            case "roomlist":
                return this.gson.fromJson(jsonMessage, RoomListCommand.class);
            case "list":
                return this.gson.fromJson(jsonMessage, ListCommand.class);
            case "createroom":
                return this.gson.fromJson(jsonMessage, CreateRoomCommand.class);
            case "delete":
                return this.gson.fromJson(jsonMessage, DeleteCommand.class);
            case "message":
                return this.gson.fromJson(jsonMessage, MessageCommand.class);
            case "quit":
                return this.gson.fromJson(jsonMessage, QuitCommand.class);
            default:
                return null;
        }
    }

}
