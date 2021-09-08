package client_command;

import client.ChatClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomListCommand extends ClientCommand{

    private List<RoomInfo> rooms = new ArrayList<>();
    private String type = "roomlist";

    public RoomListCommand(HashMap<String, Integer> info) {
        for (Map.Entry<String, Integer> entry : info.entrySet()){
            this.rooms.add(new RoomInfo(entry.getKey(), entry.getValue()));
            System.out.println();
        }
    }

    @Override
    public void execute(ChatClient chatClient) {
        StringBuilder print = new StringBuilder();
        for(RoomInfo r: rooms){
            print.append(r.getRoomid()).append(": ").append(r.getCount()).append(" guests\n");
        }
        System.out.println(print.substring(0, print.length()-2));
        chatClient.printPrefix();
    }


    public class RoomInfo {
        private String roomid;
        private int count;

        public RoomInfo(String roomid, int count) {
            this.roomid = roomid;
            this.count = count;
        }
        public String getRoomid() {
            return roomid;
        }

        public int getCount() {
            return count;
        }
    }





}
