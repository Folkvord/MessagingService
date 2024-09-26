package client;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class ClientProcess {
    
    private Socket sock;
    private BufferedReader reader;
    private BufferedWriter writer;

    private Client current_user;
    private ArrayList<Client> active_clients = new ArrayList<>();

    private final String IP_ADRESS = "localhost";
    private final int PORT = 4949;

    public ClientProcess(){}


    // Hovedmetoden for forbindelsen
    public void start_session(){

        boolean connected = connect();
        if(!connected) return;

        boolean login = login();
        if(!login) return;

        system_message("LOGGED IN");

    }

    // Brukes for å starte forbindelsen når klienten booter
    private boolean connect(){

        try{

            establish_connection();
            return true;

        } catch(IOException e){

            system_message("CONNECTION FAILED, RETRYING...");

            boolean reconnected = retry_connection(5);
            return reconnected;

        }

    }

    // Login protokollen
    private boolean login(){

        Scanner input = new Scanner(System.in);
        while(true){
            
            System.out.print("Username: ");
            String username = input.nextLine();
            
            System.out.print("Password: ");
            String password = input.nextLine();

            send_message(username);
            send_message(password);
            
            String attempt_result = status_message();
            if(attempt_result.equals("correct")){
                
                current_user = new Client(username);
                input.close();
                
                return true;
                
            }
            else{
                system_message("USERNAME OR PASSWORD WAS WRONG");
            }


        }


    }

    // Brukes til å lage en fobindelse mellom klienten og serveren
    private void establish_connection() throws IOException {
        
        sock = new Socket(IP_ADRESS, PORT);

        InputStreamReader instream = new InputStreamReader(sock.getInputStream());
        reader = new BufferedReader(instream);

        OutputStreamWriter outstream = new OutputStreamWriter(sock.getOutputStream());
        writer = new BufferedWriter(outstream);

    }

    private boolean retry_connection(int max_retry_attempts){

        int attempts_made = 0;
        while(attempts_made != max_retry_attempts){

            sleep(2000);

            try{

                establish_connection();
                system_message("CONNECTED");
                return true;

            } catch(IOException e){
                attempts_made++;
                system_message("CONNECTION FAILED, RETRYING...");
            }

        }

        system_message("COULD NOT ESTABLISH CONNECTION WITH SERVER, SHUTTING DOWN");
        return false;
    
    }

    // Samme som retry_connection(int), men printer ikke noe ut
    private boolean silently_retry_connection(int max_retry_attempts){
        
        int attempts_made = 0;
        while(attempts_made != max_retry_attempts){

            sleep(2000);

            try{

                sock = new Socket(IP_ADRESS, PORT);
                return true;

            } catch(IOException e){
                attempts_made++;
            }

        }

        return false;
    
    }

    // leser en statusmelding
    // Om forbindelsen brytes, prøver den på nytt
    private String status_message(){

        try{
            
            String status_message = reader.readLine();
            return status_message;

        } catch(IOException e){
            return null;
        }

  }

    private boolean send_message(String message){

        try{

            writer.write(message);
            writer.newLine();
            writer.flush();

        } catch(IOException e){
            system_message(message+" -> FAILED");
            return false;
        }

        return true;

    }


    public static void system_message(String message){
        System.out.println("[SYSTEM]: " + message);
    }

    public static void sleep(int ms){

        try{
            Thread.sleep(ms);
        } catch(InterruptedException e){
            System.out.println("LOL");
        }

    }

}
