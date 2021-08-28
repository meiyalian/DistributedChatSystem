package client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import command.*;


public class CommandFactory {
    private Gson gson = new Gson();

    public Command convertUserInputToCommand(String userInput){
        String[] inputArray = userInput.split(" ");
        int inputLength = inputArray.length;

        if (inputLength != 0){
            // remove the # at the beginning
            String type = inputArray[0].substring(1);
            String arg = "";
            if (inputLength > 1){
                arg = inputArray[1];
            }

            switch(type){
                case "identitychange":
                    return new IdentityChangeCommand(arg);
                case "join":
                    return new JoinCommand(arg);
                case "who":
                    return new WhoCommand(arg);
                case "list":
                    return new ListCommand();
                case "createroom":
                    return new CreateRoomCommand(arg);
                case "delete":
                    return new DeleteCommand(arg);
                case "message":
                    return new MessageCommand(arg);
                case "quit":
                    return new QuitCommand();
                default:
                    // a normal message, not command
                    return new MessageCommand(userInput);
            }
        }
        // if user doesn't input anything
        return new MessageCommand("");
    }

    public Command convertServerMessageToCommand(String jsonMessage){
        String type = gson.fromJson(jsonMessage, JsonObject.class).get("type").getAsString();

        switch(type){
            case "message":
                return gson.fromJson(jsonMessage, MessageRelayCommand.class);
            case "newidentity":
                return gson.fromJson(jsonMessage, NewIdentityCommand.class);
            case "roomchange":
                return gson.fromJson(jsonMessage, RoomChangeCommand.class);
            case "roomcontents":
                return gson.fromJson(jsonMessage, RoomContentsCommand.class);
            case "roomlist":
                return gson.fromJson(jsonMessage, RoomListCommand.class);
            default:
                return null;
        }
    }

}
