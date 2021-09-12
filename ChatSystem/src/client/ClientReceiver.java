package client;

import client_command.ClientCommand;
import client_command.RoomChangeCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReceiver extends Thread{
    private Socket socket;
    private ChatClient chatClient;
    private CommandFactory commandFactory;
    private BufferedReader reader;
    private boolean connection_alive;

    public ClientReceiver(ChatClient chatClient) throws IOException {
        this.chatClient = chatClient;
        this.socket = chatClient.getSocket();
        this.commandFactory = new CommandFactory(this.chatClient);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF8"));
    }

    public void close() throws IOException {
        this.reader.close();
        this.socket.close();
        System.out.println("Warning: Client receiver connection closed.");
    }

    /**
     * Receives messages from server
     */
    public void run(){
        this.connection_alive = true;
        while (connection_alive) {
            try {
                String str = reader.readLine();
                if (str != null){
                    ClientCommand command = commandFactory.convertServerMessageToCommand(str);
//                    System.out.println("receive: " + str);
                    if (command != null){
                        if (command instanceof RoomChangeCommand && ((RoomChangeCommand) command).getRoomid().equals("MainHall")){
                            chatClient.setBundleMsg(true);
                        }
                        command.execute(chatClient);
                    }
                }
            } catch (IOException e) {
                this.connection_alive = false;
                e.printStackTrace();
            }
        }

        try {
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
