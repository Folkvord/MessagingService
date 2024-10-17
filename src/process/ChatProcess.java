package process;

import java.util.Scanner;

import chat.ChatRoom;
import datastructures.LinkedList;
import network.NetworkInterface;
import tools.Display;


public class ChatProcess {
    
    private NetworkInterface net_interface;

    private String client_username;


    public ChatProcess(String client_username, NetworkInterface net_interface){

        this.client_username = client_username;
        this.net_interface = net_interface;

        chat_process();

    }


    private void chat_process(){

        while(true){

            // 1: Velg et rom, om ingen velges forlates chatprosessen
            boolean chose_a_room = choose_chatroom();
            if(!chose_a_room) return;

            // 2: Henter entréstatusen fra serveren
            String entry_status = net_interface.recv_status();
            
            // 3: Om brukeren er bannlyst fra chatrommet
            if(entry_status.equals("You are banned")){

                system_message("YOU ARE BANNED FROM THIS ROOM");
                continue;

            }

            // 4: Om chatrommet trenger et passord
            if(entry_status.equals("Needs password")){
                
                boolean logged_in = chat_login();
                if(!logged_in) continue;

            }



        }

    }


    // Lar brukeren finne et chatrom
    private boolean choose_chatroom(){
        
        // 1: Hent chatrom
        LinkedList<ChatRoom> chatrooms = net_interface.recv_chatrooms();
        String menu = Display.make_chat_menu(chatrooms);

        Scanner input = new Scanner(System.in);
        while(true){
            Display.new_display_message(menu);
            
            // 2: Hent brukerinput
            String user_input = input.nextLine();
            
            // 3: Om brukeren vil tilbake
            if(user_input.equals("back")){
                net_interface.send_input("Back");
                Display.clear_console();
                return false;
            }

            // 4: Sjekk om input er en gyldig indeks
            boolean valid_index = is_valid_menu_index(user_input, 1, chatrooms.size());
            if(!valid_index) continue;
            
            // 5: Send indeks til serveren og bryt ut av inputloopen
            net_interface.send_input(user_input);
            return true;

        }

    }

    // Lar brukeren prøve å logge inn til et chatrom
    private boolean chat_login(){

        Scanner input = new Scanner(System.in);

        // 1: Ny inputloop
        while(true){

            Display.new_display_message("PASSWORD REQUIRED: ");

            // 2: Ta bruker input
            String attempt = input.nextLine();
            net_interface.send_login_attempt(null, attempt);

            // 3: Prosesser statusen
            String status = net_interface.recv_status();
            if(status.equals("Correct")){
                Display.new_delayed_message("ENTERING", 1000);
                return true;
            }
            else if(status.equals("GTFO")){
                Display.new_delayed_message("ACCESS DENIED", 1000);
                return false;
            }

        }

    }


    // Tar inn et input og returnerer true om det er en gyldig indeks, ellers false
    public boolean is_valid_menu_index(String input, int min, int max){

        try{

            int index = Integer.parseInt(input);
            return !(index < min || index > max);

        } catch(NumberFormatException e){
            return false;
        }


    }

    private void system_message(String message){
        System.out.println("[SYSTEM]: " + message);
    }


}
