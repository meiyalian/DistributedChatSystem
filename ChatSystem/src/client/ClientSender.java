package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import com.google.gson.Gson;
import server_command.ServerCommand;

class ClientSender extends Thread{
    private Socket socket;
    private ChatClient chatClient;
    private PrintWriter writer;
    private BufferedReader userInput;
    private Gson gson;
    private boolean connection_alive;
    private CommandFactory commandFactory;


    public ClientSender(Socket soc, ChatClient chatClient) throws IOException {
        // Client must know the hostname or IP of teh machine and on which the server is running
        this.socket = soc;
        this.gson = new Gson();
        this.chatClient = chatClient;
        this.commandFactory = new CommandFactory();
        this.userInput = new BufferedReader(new InputStreamReader(System.in));

        // autoFlush = true means send the data immediately when receiving the input
        this.writer = new PrintWriter(this.socket.getOutputStream(), true);
    }

    public void close() throws IOException {
        this.userInput.close();
        this.writer.close();
        this.socket.close();
    }

    /**
     * Send message to server
     */
    public void run() {
        connection_alive = true;
        while (connection_alive) {
            try {
                // [Room] guest5> #join comp90015
                // this is to make sure that prefix "[Room] guest5>" is before the user input
                String clientID = chatClient.getIdentity();
                if (!clientID.equals("")){
                    System.out.println(clientID + "> ");
                }
                String str = userInput.readLine();
                if (str != null){
                    ServerCommand command = commandFactory.convertUserInputToCommand(str);
                    String jsonMessage = gson.toJson(command);
                    // convert user input to command and convert command to json object
                    // then send this json command object to server
                    this.writer.println(jsonMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
                connection_alive = false;
            }
        }

        try {
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}