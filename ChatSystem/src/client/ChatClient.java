package client;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Options;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ChatClient {
    private final Socket socket;
    private String identity = "";
    private boolean connected = true;
    private String roomid = "MainHall";
    private ClientSender clientSender = null;
    private ClientReceiver clientReceiver = null;
    private static final int DEFAULT_PORT = 6379;
    private String roomToCreate = null;
    private String roomToDelete = null;
    private boolean isBundleMsg = false;

    public boolean isBundleMsg() {
        return isBundleMsg;
    }

    public void setBundleMsg(boolean bundleMsg) {
        isBundleMsg = bundleMsg;
    }

    public ChatClient(String hostname, int port) throws IOException {
        this.socket = new Socket(hostname, port);
    }

    public Socket getSocket() {
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
        if (args.length == 1){
            hostname = args[0];
        } else if (args.length == 3){
            if (args[0].equals("-p") | args[0].equals("-port")){
                hostname = args[2];
            } else {
                hostname = args[0];
            }
        }
        else {
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
            System.out.println("host name: " + hostname + " port number: " + port);
            e.printStackTrace();
        }
    }
    public void printPrefix() {

        System.out.print("[" + roomid + "] " + identity + "> ");
    }

    public void handle() throws IOException {
        try {
            clientSender = new ClientSender(socket, this);
            clientReceiver = new ClientReceiver(this);

            clientSender.start();
            clientReceiver.start();

            while (connected){
                Thread.sleep(2000);
            }

        } catch (InterruptedException e){
            System.out.println("Connection is interrupted");
            clientReceiver.close();
            clientSender.close();
        } finally {
            if (socket != null){
                System.out.println("Disconnected from localhost");
                socket.close();
            }
        }

    }

    public void disconnect() {
        if (clientReceiver != null){
            clientReceiver.setConnection_alive(false);
        }
        connected = false;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getRoomid() {
        return roomid;
    }

    public void requestNewRoom(String roomid){
        this.roomToCreate = roomid;
    }

    public void processNewRoomResponse(){
        this.roomToCreate = null;
    }

    public String getRoomToCreate(){
        return this.roomToCreate;
    }


    public String getRoomToDelete(){
        return this.roomToDelete;
    }

    public void processRoomToDelete(){
        this.roomToDelete = null;
    }

    public void requestDeleteRoom(String roomid){
        this.roomToDelete = roomid;
    }

}