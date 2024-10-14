package chat;

import account.*;
import datastructures.LinkedList;

import java.io.Serializable;

public class ChatRoom implements Serializable {
    
    private static final long serialVersionUID = 999999999;

    protected LinkedList<Account> active_clients;

    private final int CHATROOM_ID;
    private final String CHATROOM_NAME;


    public ChatRoom(String chatroom_name, Account first_client){

        CHATROOM_ID = (int) (Math.random() * 10000);
        active_clients = new LinkedList<>();

        active_clients.add(first_client);

        this.CHATROOM_NAME = chatroom_name;

    }


    public LinkedList<Account> get_active_chatters(){
        return active_clients;
    }

    public int get_chat_id(){
        return CHATROOM_ID;
    }

    public String get_chat_name(){
        return CHATROOM_NAME;
    }

    public int get_active_user_count(){
        return active_clients.size();
    }

    public String toString(){
        return CHATROOM_NAME;
    }

}
