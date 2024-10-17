package chat;

import account.*;
import datastructures.LinkedList;

import java.io.Serializable;

public class ChatRoom implements Serializable {
    
    private static final long serialVersionUID = 999999999;

    protected LinkedList<Account> active_clients;
    protected LinkedList<Account> banned_clients;

    private final int CHATROOM_ID;
    private String chatroom_name;
    
    private boolean is_private;


    public ChatRoom(String chatroom_name, Account first_client){

        CHATROOM_ID = (int) (Math.random() * 10000);
        active_clients = new LinkedList<>();

        active_clients.add(first_client);

        this.chatroom_name = chatroom_name;

    }


    public LinkedList<Account> get_active_chatters(){
        return active_clients;
    }

    public int get_chat_id(){
        return CHATROOM_ID;
    }

    public String get_chat_name(){
        return chatroom_name;
    }

    public int get_active_user_count(){
        return active_clients.size();
    }

    public String toString(){
        return chatroom_name;
    }

    public boolean is_private(){
        return is_private;
    }

    private void chatroom_message(String message){
        System.out.println("[CHAT] (" + chatroom_name + "): " + message);
    }

}
