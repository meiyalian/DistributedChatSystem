package utility;

import java.util.ArrayList;

public class IdentityManager {
    private static IdentityManager identityManager = new IdentityManager();
    private static ArrayList<String> identityList = new ArrayList<>();

    public static IdentityManager getInstance(){
        return identityManager;
    }

    public synchronized void addIdentity(String identity){
        identityList.add(identity);
    }

    public synchronized  void removeIdentity(String identity){
        identityList.remove(identity);
    }

    public synchronized boolean isIdentityInList(String identity){
        return identityList.contains(identity);
    }

    /**
     * Requirements:
     * 1. must be an alphanumeric string: character and digits only
     * 2. must start with an upper or lower case character
     * 3. 3 <= length <=16
     * @return boolean
     */
    public boolean isIdentityValid(String identity){
        boolean matchResult = identity.matches("[A-Za-z0-9]+"); // 1
        boolean firstCharacterCheck = Character.isLetter(identity.charAt(0)); // 2
        boolean lengthCheckResult = identity.length() >= 3 && identity.length() <= 16; // 3
        return matchResult && lengthCheckResult && firstCharacterCheck;
    }
}
