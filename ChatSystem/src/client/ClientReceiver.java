package client;

import client_command.ClientCommand;

import client_command.RoomChangeCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientReceiver extends Thread{
    private Socket socket;
    private ChatClient chatClient;
    private CommandFactory commandFactory;
    private BufferedReader reader;
    private boolean connection_alive;
    private PrintWriter writer;

    public ClientReceiver(ChatClient chatClient) throws IOException {
        this.connection_alive = true;
        this.chatClient = chatClient;
        this.socket = chatClient.getSocket();
        this.commandFactory = new CommandFactory(this.chatClient);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF8"));
        this.writer = new PrintWriter(this.socket.getOutputStream(), true);
    }

    public void setConnection_alive(boolean connection_alive) {
        this.connection_alive = connection_alive;
    }

    public void close() throws IOException {
        this.connection_alive = false;
        this.reader.close();
        this.writer.close();
    }

    /**
     * Receives messages from server
     */
    public void run(){
        while (connection_alive) {
            try {
                String str = reader.readLine();
                if (str == null){
                    System.out.println("\nWARNING: Server has closed the connection!");
                    this.connection_alive = false;
                } else {
                    ClientCommand command = commandFactory.convertServerMessageToCommand(str);
                    if (command != null){
                        if (command instanceof RoomChangeCommand && ((RoomChangeCommand) command).getRoomid().equals("MainHall")){
                            chatClient.setBundleMsg(true);
                        }
                        command.execute(chatClient);
                    }
                }
            } catch (IOException e) {
                this.connection_alive = false;
                System.out.println("\nWARNING: Server has closed the connection!");
            }
        }

        try {
            this.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
