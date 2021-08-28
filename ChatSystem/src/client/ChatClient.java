package client;

import java.io.IOException;

public class ChatClient {
    private String identity = "";

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getIdentity() {
        return identity;
    }

    public void handle() throws IOException {
        ClientSideConnection clientSideConnection = new ClientSideConnection();
        clientSideConnection.run();
    }

    public static void main(String[] args){
        try {
            new ChatClient().handle();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
