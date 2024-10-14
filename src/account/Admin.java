package account;

public class Admin extends Account {
    

    public Admin(String username, String password){

        super(username, password);

    }


    public String user_prompt(){
        return "[ (ADMN) " + username + "]: ";
    }

}
