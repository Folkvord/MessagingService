package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import account.Account;
import chat.ChatRoom;
import datastructures.LinkedList;
import tools.JSON;

public class NetworkInterface {
    
    private final String IP_ADRESS = "localhost";
    private final int PORT = 4949;

    private Socket sock;
    private BufferedReader reader;
    private BufferedWriter writer;
    private ObjectInputStream object_;

    private boolean connected_to_server = false;


    public NetworkInterface(){

        connected_to_server = connect();

    }


    // Brukes for å starte forbindelsen når klienten booter
    public boolean connect(){

        try{

            establish_connection();
            return true;

        } catch(IOException e){

            
            interface_message("CONNECTION FAILED, RETRYING...");

            boolean reconnected = retry_connection(5);
            return reconnected;

        }

    }

    // Brukes til å lage en fobindelse mellom klienten og serveren
    private void establish_connection() throws IOException {
        
        sock = new Socket(IP_ADRESS, PORT);

        InputStreamReader instream = new InputStreamReader(sock.getInputStream());
        reader = new BufferedReader(instream);

        OutputStreamWriter outstream = new OutputStreamWriter(sock.getOutputStream());
        writer = new BufferedWriter(outstream);

        object_ = new ObjectInputStream(sock.getInputStream());

    }

    // Brukes for å prøve å gjenskape forbindelsen
    private boolean retry_connection(int max_retry_attempts){

        int attempts_made = 0;
        while(attempts_made != max_retry_attempts){

            sleep(2000);

            try{

                establish_connection();
                
                interface_message("CONNECTED");
                return true;

            } catch(IOException e){
                attempts_made++;
                
                interface_message("CONNECTION FAILED, RETRYING...");
            }

        }

        
        interface_message("COULD NOT ESTABLISH CONNECTION WITH SERVER, SHUTTING DOWN");
        return false;
    
    }



    // Sender en melding til en klient
    public void send_client_message(String message, String from){

        String encoded_message = JSON.encode_message(message, from);

        talk_to_server(encoded_message);

    }

    // Sender en response til serveren ()
    public void send_response(String response){

        String encoded_message = JSON.encode_response(response);

        talk_to_server(encoded_message);

    }

    // Sender brukerinputtet til en bruker (menynavigasjon)
    public void send_input(String input){
        
        String encoded_message = JSON.encode_input(input);

        talk_to_server(encoded_message);

    }

    // Sender en login forsøk
    public void send_login_attempt(String username, String password){

        String encoded_message = JSON.encode_login(username, password);

        talk_to_server(encoded_message);

    }


    
    // Gir payload-et uansett typen
    // Brukes når man bare forvendter en type melding
    public String recv_payload(){

        String json_message = listen_to_server();
        String payload = JSON.decrypt(json_message);
        
        return payload;

    }
    
    // Deadass det samme som det over
    public String recv_status(){

        String json_message = listen_to_server();
        String payload = JSON.decrypt(json_message);
        
        return payload;

    }

    // Brukes for å hente klientobjekter
    @SuppressWarnings("Unchecked")
    public LinkedList<Account> recv_clients(){

        client_is_ready();

        try{

            LinkedList<Account> clients = (LinkedList<Account>) object_.readObject();
            return clients;

        } 
        catch(IOException e){
            interface_message("OBJECT LOST");
        }
        catch(ClassNotFoundException e){
            interface_message("OBJECT IDENTITY CRISIS");
        }

        return null;

    }

    // Brukes for å hente chatrom
    @SuppressWarnings("Unchecked")
    public LinkedList<ChatRoom> recv_chatrooms(){

        client_is_ready();

        try{

            LinkedList<ChatRoom> chatrooms = (LinkedList<ChatRoom>) object_.readObject();
            return chatrooms;

        } 
        catch(IOException e){
            interface_message("CHATROOM LOST");
        }
        catch(ClassNotFoundException e){
            interface_message("CHATROOM IDENTITY CRISIS");
        }

        return null;

    }


   
    // Sender en melding til serveren, brukes bare av andre "send_x" metoder
    private void talk_to_server(String encoded_message){

        try{

            writer.write(encoded_message);
            writer.newLine();
            writer.flush();

        } catch(IOException e){
            interface_message("FAILED");
            return;
        }

    }

    // Lytter til serveren, returnerer JSON stringen
    private String listen_to_server(){

        try{
            
            String json_message = reader.readLine();
            return json_message;

        } catch(IOException e){
            return null;
        }

    }

    // Brukes for å si til serveren at man er klar for å hente objekter (Brukes i recv_clients/chatrooms)
    private void client_is_ready(){

        talk_to_server("Ready");

    }



    // SKAL brukes til å pinge serveren for å se om det er en forbindelse
    public boolean is_connected(){

        // HEARTBEAT HER

        return connected_to_server;
    }

    private void interface_message(String message){
        System.out.println("[NETWORK]: " + message);
    }

    private void sleep(int ms){

        try{
            Thread.sleep(ms);
        } catch(InterruptedException e){
            System.out.println("LOL");
        }

    }

}
