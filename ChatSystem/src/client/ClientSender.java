package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientSender {
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader userInput;
    private BufferedReader reader;
    private boolean connection_alive;


    public ClientSender(Socket soc) throws IOException {
        System.out.println("Client started");
        // Client must know the hostname or IP of teh machine and on which the server is running
        this.socket = soc;
        this.userInput = new BufferedReader(new InputStreamReader(System.in));
        // autoFlush = true means send the data immediately when receiving the input
        this.writer = new PrintWriter(this.socket.getOutputStream(), true);
        this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    public void close() throws IOException {
        this.socket.close();
        System.out.println("Connection closed.");
    }

    public void run() throws IOException {
        connection_alive = true;
        while (connection_alive) {
            System.out.println("Enter a string to send message or type Q to quit:");
            String str = userInput.readLine();
            // send the data to server
            System.out.println("--Send data from client to server");
            writer.println(str);

            String returnValue= reader.readLine();
            // read the data server has sent
            System.out.println("--Message received at client: " + returnValue);
            if (returnValue.equals("Q")){
                connection_alive = false;
            }
        }
        close();
    }
}