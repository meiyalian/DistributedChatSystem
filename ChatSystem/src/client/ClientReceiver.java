package client;

import command.Command;

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
        socket = chatClient.getSocket();
        commandFactory = new CommandFactory();
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF8"));
        connection_alive = true;
        while (connection_alive) {
            String str = reader.readLine();
            if (str != null){
                Command command = commandFactory.convertServerMessageToCommand(str);
                command.execute(chatClient);
            }
        }
    }

    public void run(){

    }
}
