package shared;

import server.ChatManager;
import server.ServerConnection;

import java.util.ArrayList;

public class Validator {

    /**
     * Requirements:
     * 1. must be an alphanumeric string: character and digits only
     * 2. must start with an upper or lower case character
     * 3. 3 <= length <=16
     * @return boolean
     */
    public static boolean isIdentityInvalid(String identity){
        boolean matchResult = identity.matches("[A-Za-z0-9]+"); // 1
        boolean firstCharacterCheck = Character.isLetter(identity.charAt(0)); // 2
        boolean lengthCheckResult = identity.length() >= 3 && identity.length() <= 16; // 3
        return !(matchResult && lengthCheckResult && firstCharacterCheck);
    }

    public static boolean isRoomIdValid(String roomid){
        boolean matchResult = roomid.matches("[A-Za-z0-9]+");
        boolean firstLetter = Character.isLetter(roomid.charAt(0));
        boolean lengthCheckResult = roomid.length() >= 3 && roomid.length() <= 32;
        return matchResult && firstLetter && lengthCheckResult;

    }

}
