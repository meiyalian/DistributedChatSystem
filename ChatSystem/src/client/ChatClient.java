package client;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Options;

import java.io.IOException;
import java.net.Socket;

public class ChatClient {
    private final Socket socket;
    private String identity = "";
    private String roomid = "MainHall";
    private static final int DEFAULT_PORT = 6379;

    //todo: localhost must be changed to IP address for multiple clients to connect
    public ChatClient(String hostname, int port) throws IOException {
        this.socket = new Socket(hostname, port);
    }

    public Socket getSocket(){
        return this.socket;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getIdentity() {
        return identity;
    }

    public static void main(String[] args){
        int port;
        String hostname;

        CommandLineParser parser = new BasicParser();
        Options options = new Options();
        options.addOption("p", "port", true, "Port");

        /** get command line input: hostname or IP address */
        if (args.length != 0){
            hostname = args[0];
        } else {
            hostname = "localhost";
        }

        /** get command line input: port number */
        try {
            CommandLine commandLine = parser.parse(options, args);
            String portNum = commandLine.getOptionValue("p");

            if (portNum != null){
                port = Integer.parseInt(portNum);
            } else {
                port = DEFAULT_PORT;
            }
        } catch (ParseException e){
            e.printStackTrace();
            System.out.println("Default port " + DEFAULT_PORT + " will be used.");
            port = DEFAULT_PORT;
        }
        try {
            new ChatClient(hostname, port).handle();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printPrefix(){
        System.out.print("[" + roomid + "] " + identity + "> ");
    }

    public void handle() throws IOException {
        ClientSender clientSender = new ClientSender(socket, this);
        ClientReceiver clientReceiver = new ClientReceiver(this);

        clientSender.start();
        clientReceiver.start();
    }
}
