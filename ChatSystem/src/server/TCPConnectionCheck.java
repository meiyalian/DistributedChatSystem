package server;

import client_command.AskAckCommand;
import com.google.gson.Gson;
import server_command.QuitCommand;

import java.util.ArrayList;

public class TCPConnectionCheck extends Thread{
    private boolean tcpCheckFlag = true;
    private final ChatManager chatManager;
    private final Gson gson;
    private boolean waiting = true;

    public TCPConnectionCheck(ChatManager chatManager){
        this.gson = new Gson();
        this.chatManager = chatManager;
    }

    public void setTcpCheckFlag(boolean tcpCheckFlag) {
        this.tcpCheckFlag = tcpCheckFlag;
    }


    private synchronized void checkConnection() throws InterruptedException {
        AskAckCommand askAckCommand = new AskAckCommand();
        String jsonMessage = gson.toJson(askAckCommand);

        synchronized (chatManager){
            ArrayList<ServerConnection> serverConnections = chatManager.getClientConnectionList();

            chatManager.resetClientWaitForAckList();
            ArrayList<ServerConnection> waitConnections = chatManager.getClientWaitForAckList();

            synchronized (serverConnections){
                chatManager.broadCastAllRooms(jsonMessage, null);
            }
        }
        this.waiting = true;
        while (waiting){
            Thread.sleep(3000);
            ArrayList<ServerConnection> clientWaitForAckConnections = chatManager.getClientWaitForAckList();
            synchronized (clientWaitForAckConnections){
                for (ServerConnection serverConnection: clientWaitForAckConnections){
                    synchronized (serverConnection){
                        QuitCommand quitCommand = new QuitCommand();
                        quitCommand.execute(serverConnection);
                    }
                }
                this.waiting = false;
            }
        }
        Thread.sleep(1000);
    }

    public void run(){
        while (tcpCheckFlag){
            try {
                Thread.sleep(5000);
                checkConnection();
            } catch (InterruptedException e) {
                e.printStackTrace();
                tcpCheckFlag = false;
            }
        }
    }

}
