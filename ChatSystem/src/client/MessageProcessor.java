package client;

import command.*;

import java.util.ArrayList;

public class MessageProcessor {

    private Command convertInputToCommand(String userInput){
        String[] inputArray = userInput.split(" ");
        int inputLength = inputArray.length;

        if (inputLength != 0){
            // remove the # at the beginning
            String type = inputArray[0].substring(1);
            String argOne = "";
            if (inputLength > 1){
                argOne = inputArray[1];
            }

            switch(type){
                case "identitychange":
                    return new IdentityChangeCommand(argOne);
                case "join":
                    return new JoinCommand(argOne);
                case "who":
                    return new WhoCommand(argOne);
                case "list":
                    return new ListCommand();
                case "createroom":
                    return new CreateRoomCommand(argOne);
                case "delete":
                    return new DeleteCommand(argOne);
                case "message":
                    return new MessageCommand(argOne);
                case "quit":
                    return new QuitCommand();
                default:
                    return null;
            }
        }
        return null;
    }

}
