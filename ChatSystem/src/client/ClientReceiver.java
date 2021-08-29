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
        this.socket = chatClient.getSocket();
        this.commandFactory = new CommandFactory();
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF8"));
    }

    public void close() throws IOException {
        this.reader.close();
        this.socket.close();
        System.out.println("Warning: Client receiver connection closed.");
    }


    public void run(){
        this.connection_alive = true;
        while (connection_alive) {
            try {
                String str = reader.readLine();
                if (str != null){
                    Command command = commandFactory.convertServerMessageToCommand(str);
                    if (command != null){
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
