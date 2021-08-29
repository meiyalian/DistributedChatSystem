package client;

import client_command.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import server_command.*;

import java.util.ArrayList;
import java.util.Arrays;


public class CommandFactory {
    private final Gson gson = new Gson();

    /**
     * Message can be in two formats:
     * 1. "Hello World"
     * 2. Hello Word
     * if it is number 2, we need to join them together to produce a new string as input
     * @param inputArray
     * @return
     */
    private String joinMultipleArguments(String[] inputArray){
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(inputArray));
        return String.join(" ", arrayList.subList(1, arrayList.size()));
    }

    /**
     * Covert user inputs to new command
     * Populate user input argument to initialize command object
     * @param userInput
     * @return
     */
    public ServerCommand convertUserInputToCommand(String userInput){
        String[] inputArray = userInput.split(" ");
        int inputLength = inputArray.length;

        if (inputLength != 0){
            // remove the # at the beginning
            String type = inputArray[0].substring(1);
            String arg = "";
            if (inputLength > 1){
                arg = this.joinMultipleArguments(inputArray);
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

    /**
     * Covert json messages that are sent from server at client side to command object
     * @param jsonMessage
     * @return
     */
    public ClientCommand convertServerMessageToCommand(String jsonMessage){
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
