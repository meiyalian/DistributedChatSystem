package server;

import server_command.ServerCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerConnection extends Thread {
    private Socket socket;
    private ChatManager chatManager;
    private CommandFactory commandFactory;
    private PrintWriter writer;
    private BufferedReader reader;
    private boolean connection_alive;
    private String currentChatRoom;

    public ServerConnection(Socket socket, ChatManager chatManager, CommandFactory commandFactory) throws IOException {
        this.socket = socket;
        this.chatManager = chatManager;
        this.commandFactory = commandFactory;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream());
        setName("");
    }

    public void setCurrentChatRoom(String currentChatRoom) {
        this.currentChatRoom = currentChatRoom;
    }

    public String getCurrentChatRoom() {
        return currentChatRoom;
    }

    public ChatManager getChatManager(){return this.chatManager;}

    private void executeCommand(String jsonMessage){
        ServerCommand command = commandFactory.convertClientMessageToCommand(jsonMessage);
        command.execute(this);
    }

    @Override
    public void run() {
        connection_alive = true;
        while (connection_alive) {
            try {
                String jsonMessage = this.reader.readLine();
                if (jsonMessage != null){
                    executeCommand(jsonMessage);
                }
            } catch (IOException e){
                connection_alive = false;
            }
        }
        close();
    }

    //Todo: leave chatroom
    private void leave(ServerConnection connection) {
        chatManager.removeClientConnection(connection);
    }

    public void close() {
        try {
            socket.close();
            leave(this);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        this.writer.println(message);
        this.writer.flush();
    }

}