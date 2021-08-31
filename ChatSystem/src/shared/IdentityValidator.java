package shared;

import server.ChatManager;
import server.ServerConnection;

import java.util.ArrayList;

public class IdentityValidator {

//    public static synchronized boolean isIdentityInList(ChatManager chatManager, String identity){
//        ArrayList<ServerConnection> serverConnections = chatManager.getClientConnectionList();
//        for (ServerConnection serverConnection: serverConnections){
//            if (identity.equals(serverConnection.getName())){
//                return true;
//            }
//        }
//        return false;
//    }

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

}
