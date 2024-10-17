package process;

import java.util.InputMismatchException;
import java.util.Scanner;

import chat.ChatRoom;
import datastructures.LinkedList;
import network.NetworkInterface;
import tools.Display;

public class LocalProcess {
    
    private NetworkInterface net_interface;

    private String client_username;
    private ClientStatusEnum status = ClientStatusEnum.ANONYMOUS;


    public LocalProcess(){

        clear_console();
        net_interface = new NetworkInterface();

        local_session();

    }

    
    // Mainloop-en til lokalprosessen
    // Når løkken brytes ut av, avslutter programmet
    // Bruker denne "hub-akrkitekturen" for å unngå nøstet kode
    public void local_session(){

        while(true){

            switch(status) {


                // Brukeren har ikke logget inn enda; brukeren er anonym
                case ANONYMOUS:
                    status = anonymous_menu();
                    break;


                // Brukeren skal logge på
                case UNAUTHENTICATED:
                    status = login();
                    break;


                // Brukeren er autorisert av serveren
                case AUTHENTICATED:
                    status = nonlocal_process();
                    break;


                // Brukeren er i en slags sesjon med andre klienter
                case IN_SESSION:
                    // status = IDK LULE
                    break;


                // Brukeren har ingen forbindelse med serveren
                case OFFLINE:
                    // status = EN CONNECT TYPE METODE
                    break;


                // Brukeren skal slå av programmet
                case EXITING:
                    
                    return;

                
                default:
                    break;
            }

        }


    }


    // Menyen som dukker opp før brukeren har logget inn
    // Metoden printer ut en meny, så tar den et input fra brukeren
    // Returnerer en status
    private ClientStatusEnum anonymous_menu(){

        String menu = Display.make_start_menu();
        System.out.println(menu);

        int choice = get_int_input(1, 2);
        
        clear_console();
        
        // Login
        if(choice == 1){
            net_interface.send_input("Login");
            return ClientStatusEnum.UNAUTHENTICATED;
        }
        // Register
        else if(choice == 2){
            net_interface.send_input("Registering");
            return ClientStatusEnum.ANONYMOUS;
        }
        else if(choice == -1){
            net_interface.send_input("Exit");
            return ClientStatusEnum.EXITING;
        }
        // Exit
        else{
            return ClientStatusEnum.ANONYMOUS;
        }

    }

    // Login protokollen
    // Tar inn et brukernavn og passord fra brukeren og sender det til serveren
    // Serveren sender en statusmelding tilbake med "correct" eller "wrong"
    // Metoden bryr seg ikke om typen, fordi lokalprosessen forvendter ingen andre meldinger enn denne
    // Om "correct" setter den "authenticated_client" til et nytt clientobjekt med brukernavnet
    private ClientStatusEnum login(){

        Scanner input = new Scanner(System.in);

        while(true){
            
            System.out.print("Username: ");
            String username = input.nextLine();

            // Returner om brukernavn er tomt
            if(username.equals("")){
                net_interface.send_login_attempt("", "");
                return ClientStatusEnum.ANONYMOUS;
            }

            System.out.print("Password: ");
            String password = input.nextLine();

            net_interface.send_login_attempt(username, password);
            
            String json_payload = net_interface.recv_payload();
            boolean attempt_successful = json_payload.equals("correct");
            if(attempt_successful){
                
                // Brukeren er autorisert
                client_username = username;
                
                clear_console();

                return ClientStatusEnum.AUTHENTICATED;
                
            }
            else{
                system_message("USERNAME OR PASSWORD WAS WRONG");
            }


        }

    }
 

    // Prosessen med serveren etter brukeren har logget inn
    private ClientStatusEnum nonlocal_process(){

        // 1: Lager og printer menyen
        String menu = Display.make_main_menu(client_username);
        Display.new_display_message(menu);

        // 2: Tar inn et valg fra brukeren, sender det når det er et gyldig svar 
        while(true){
            
            int choice = get_int_input(1, 3);

            switch(choice){


                // Om brukeren vil se på chat-ene
                case 1:
                    net_interface.send_input(choice+"");

                    new ChatProcess(client_username, net_interface);
                    break;
    

                // Om brukeren vil se vennene sine
                case 2:
                    //net_interface.send_input(choice+"");

                    break;
                

                // Om brukeren vil logge ut
                case 3:
                    Display.new_display_message("LOGGED OFF");
    
                    return ClientStatusEnum.ANONYMOUS;
    
            }

            Display.new_display_message(menu);

        }


    }

    
 
    // Tar inn et int fra brukeren med en fin prompt
    // Returnerer -2 om det ikke er en int
    // Returnerer -1 om det er utenfor rangen
    private int get_int_input(int min, int max){

        Scanner input = new Scanner(System.in);

        try{

            int user_input = input.nextInt();
            if(user_input < min || user_input > max) return -1;

            return user_input;

        } catch(InputMismatchException e){
            return -2;
        }
            
    }

    // Tar inn et input og returnerer true om det er en gyldig indeks, ellers false
    public boolean is_valid_menu_index(String input, int min, int max){

        try{

            int index = Integer.parseInt(input);
            return !(index < min || index > max);

        } catch(InputMismatchException e){
            return false;
        }


    }


    private void system_message(String message){
        System.out.println("[SYSTEM]: " + message);
    }

    public static void clear_console() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    } 

}
