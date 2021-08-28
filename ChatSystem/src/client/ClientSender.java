package client;

import command.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import com.google.gson.Gson;

class ClientSender extends Thread{
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader userInput;
    private Gson gson;
    private boolean connection_alive;
    private CommandFactory commandFactory;


    public ClientSender(Socket soc) throws IOException {
        System.out.println("Client started");
        // Client must know the hostname or IP of teh machine and on which the server is running
        this.socket = soc;
        this.gson = new Gson();
        this.commandFactory = new CommandFactory();
        this.userInput = new BufferedReader(new InputStreamReader(System.in));

        // autoFlush = true means send the data immediately when receiving the input
        this.writer = new PrintWriter(this.socket.getOutputStream(), true);
    }

    public void close() throws IOException {
        this.userInput.close();
        this.writer.close();
        this.socket.close();
        System.out.println("Connection closed.");
    }

    public void run() {
        connection_alive = true;
        while (connection_alive) {
            try {
                String str = userInput.readLine();
                if (str != null){
                    Command command = commandFactory.convertUserInputToCommand(str);
                    String jsonMessage = gson.toJson(command);
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