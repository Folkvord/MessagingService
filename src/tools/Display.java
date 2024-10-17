package tools;

import chat.ChatRoom;
import datastructures.LinkedList;

public class Display {
    
    // Lager startmenyen som vises før brukeren har logget inn
    public static String make_start_menu(){

        String title = "";
        String menu_body = "[1]:\tLOGIN\n[2]:\tREGISTER\n[3]:\tEXIT\n";
        
        String menu = title.concat(menu_body);

        return menu;

    }


    // Lager menyen som vises når brukeren er innlogget
    public static String make_main_menu(String username){

        String title = make_pretty_title(username.toUpperCase());
        String menu_body = "[1]:\tSESSIONS\n[2]:\tFRIENDS\n[3]:\tLOGOUT\n";

        String menu = title.concat(menu_body);

        return menu;
    }


    // Lager menyen som klienten bruker for å navigere chatrom
    public static String make_chat_menu(LinkedList<ChatRoom> active_chatrooms){

        String title = make_pretty_title("CHATROOMS");
        String menu_body = "";
        String menu;

        // Om det ikke er noen sesjoner
        if(active_chatrooms.is_empty()){

            menu = title.concat("There are currently no chatrooms for you to join...\n");
            return menu;
        
        }

        // Finner det lengste sesjonnavnet (for å kunne bestemme antall mellomrom bak stringene)
        int longest_session_name = 0;
        for(int i = 0; i < active_chatrooms.size(); i++){
            String name = active_chatrooms.get(i).get_chat_name();
            
            if(name.length() > longest_session_name){
                longest_session_name = name.length();
            }

        }

        for(int i = 0; i < active_chatrooms.size(); i++){
            ChatRoom session = active_chatrooms.get(i);
            String session_name = space_amount(session.get_chat_name(), longest_session_name);

            menu_body = menu_body.concat("[" + (i+1) + "]:\t " + session_name + "\t | ID: (" + session.get_chat_id() + ")\t | Active users: " + session.get_active_user_count() + "\t | " + ((session.is_private()) ? "PRIVATE" : "PUBLIC") + "\n");

        }

        menu = title.concat(menu_body);

        return menu;

    }

    
    
    // Klargjør konsollen og printer en melding
    public static void new_display_message(String message){
        clear_console();
        System.out.println(message);
    }    
    
    // Samme som new_display_message, men venter ms, millisekunder før noe annet skjer
    public static void new_delayed_message(String message, int ms){
        new_display_message(message);
        sleep(ms);
    }

    // Klargjør konsollen
    public static void clear_console() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    } 



    // Finner ut hvor mange mellomrom det trengs for å gjøre en mindre string like lang som den lengste i samme kolonne
    private static String space_amount(String this_string, int largest_size){
        int this_size = this_string.length();
        int difference = largest_size - this_size;
        
        String return_string = this_string;

        for(int i = 0; i < difference; i++){
            return_string = return_string.concat(" ");
        }

        return return_string;

    }

    // Lager en fin tittel
    private static String make_pretty_title(String title){
        
        String pretty_title = "-~<[ " + title + " ]>~-\n";
        return pretty_title;

    }

    // Sleeper ms
    private static void sleep(int ms){

        try{
            Thread.sleep(ms);
        } catch(InterruptedException e){
            System.out.println("LOL");
        }

    }

}
