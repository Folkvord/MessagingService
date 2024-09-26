package client;

public class Client {
    
    private String username;


    public Client(String username){

        this.username = username;

    }


    public void user_prompt(){
        System.out.println("[" + username + "]: ");
    }

}
