package account;

import datastructures.LinkedList;

import java.io.Serializable;


public abstract class Account implements Serializable {
    
    private static final long serialVersionUID = 456456456;

    protected String username;
    protected String status = "";
    
    protected LinkedList<Account> friends = new LinkedList<>();


    public Account(String username, String password){

        this.username = username;

    }


    // --| Gettere og settere |--

    public String prompt_format(String message){
        return "[" + username + "]: " + message;
    }

    public void change_username(String new_username){
        username = new_username;
    }

    public String get_username(){
        return username;
    }
    
    public LinkedList<Account> get_friends_list(){
        return friends;
    }

    public String get_status(){
        return status;
    }

    public void set_status(String status){
        this.status = status;
    }

    public String toString(){
        return username;
    }

}
