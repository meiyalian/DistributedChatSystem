package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import server_command.*;

import java.lang.reflect.Type;

public class CommandFactory {
    private final Gson gson;

    private ServerCommand generateCommand(String jsonMessage, Class commandClass){
        return this.gson.fromJson(jsonMessage, (Type) commandClass);
    }

    public CommandFactory(){
        this.gson = new Gson();
    }

    /**
     * Convert json message sent by client at server side to command object
     * @param jsonMessage
     * @return
     */
    public ServerCommand convertClientMessageToCommand(String jsonMessage){
        String type = this.gson.fromJson(jsonMessage, JsonObject.class).get("type").getAsString();
        System.out.println("Received: " + jsonMessage);

        switch(type){
            case "identitychange":
                return this.generateCommand(jsonMessage, IdentityChangeCommand.class);
            case "message":
                return this.generateCommand(jsonMessage, MessageCommand.class);
            case "createroom":
                return this.generateCommand(jsonMessage, CreateRoomCommand.class);
            case "join":
                return this.generateCommand(jsonMessage, JoinCommand.class);
            case "who":
                return this.generateCommand(jsonMessage, WhoCommand.class);
            case "list":
                return this.generateCommand(jsonMessage, ListCommand.class);
            case "delete":
                return this.generateCommand(jsonMessage, DeleteCommand.class);
            case "quit":
                return this.generateCommand(jsonMessage, QuitCommand.class);
            default:
                return null;
        }
    }

}
