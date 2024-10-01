package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;


import client.Client;
import tools.JSON;

public class NetworkInterface {
    
    private final String IP_ADRESS = "localhost";
    private final int PORT = 4949;

    private Socket sock;
    private BufferedReader reader;
    private BufferedWriter writer;

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

            
            system_message("CONNECTION FAILED, RETRYING...");

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






    public void send_login_attempt(String data){

        String encoded_message = JSON.login_message(data);

        talk_to_server(encoded_message);

    }

    public void send_client_message(String message, Client from){

        String encoded_message = JSON.client_message(message, from);

        talk_to_server(encoded_message);

    }

    // Sender en melding til serveren, brukes bare av andre "send_x" metoder
    // Tar en JSON-string
    private void talk_to_server(String encoded_message){

        try{

            writer.write(encoded_message);
            writer.newLine();
            writer.flush();

        } catch(IOException e){
            system_message("FAILED");
            return;
        }

    }


    // Lytter til serveren, returnerer JSON stringen
    public String listen_to_server(){

        try{
            
            String json_message = reader.readLine();
            return json_message;

        } catch(IOException e){
            return null;
        }

    }

    // Gir payload-et uansett typen
    // Brukes når man bare forvendter en type melding
    public String listen_and_get_payload_despite_type(){

        String json_message = listen_to_server();
        String payload = JSON.decrypt(json_message);
        
        return payload;

    }



    public boolean is_connected(){

        // HEARTBEAT HER

        return connected_to_server;
    }

    private void system_message(String message){
        System.out.println("[SYSTEM]: " + message);
    }

    private void sleep(int ms){

        try{
            Thread.sleep(ms);
        } catch(InterruptedException e){
            System.out.println("LOL");
        }

    }

}
