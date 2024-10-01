package client;

public class Client {
    
    private String username;


    public Client(String username){

        this.username = username;

    }


    public String get_username(){
        return username;
    }

    public String user_prompt(){
        return "[" + username + "]: ";
    }

}
