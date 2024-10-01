package process;

import java.util.InputMismatchException;
import java.util.Scanner;

import client.Client;
import network.NetworkInterface;

public class LocalProcess {
    
    private NetworkInterface net_interface;

    private Client authenticated_client;
    private ClientStatusEnum status = ClientStatusEnum.ANONYMOUS;


    public LocalProcess(){
        
        local_session();

    }

    
    // Mainloop-en til lokalprosessen
    // Når løkken brytes ut av, avslutter programmet
    // Bruker denne "hub-akrkitekturen" for å unngå nøstet kode
    public void local_session(){

        clear_console();
        net_interface = new NetworkInterface();

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


                // Brukeren har ingen forbindelse med serveren
                case OFFLINE:
                    // status = EN CONNECT TYPE METODE
                    break;


                case IN_SESSION:
                    // status = IDK LULE
                    break;


                // Brukeren skal slå av programmet
                case EXITING:
                    
                    return;

                
                default:
                    break;
            }

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
                return ClientStatusEnum.ANONYMOUS;
            }

            System.out.print("Password: ");
            String password = input.nextLine();

            net_interface.send_login_attempt(username);
            net_interface.send_login_attempt(password);
            
            String json_payload = net_interface.listen_and_get_payload_despite_type();
            boolean attempt_successful = json_payload.equals("correct");
            if(attempt_successful){
                
                // Brukeren er autorisert
                authenticated_client = new Client(username);
                
                clear_console();
                system_message("LOGGED IN AS " + username);

                return ClientStatusEnum.AUTHENTICATED;
                
            }
            else{
                process_message("USERNAME OR PASSWORD WAS WRONG");
            }


        }

    }
 

    // Prosessen med serveren etter brukeren har logget inn
    private ClientStatusEnum nonlocal_process(){

        Scanner input = new Scanner(System.in);

        while(true){

            System.out.print(authenticated_client.user_prompt());
            String message = input.nextLine();

            if(message.equals("")) return ClientStatusEnum.UNAUTHENTICATED;

            net_interface.send_client_message(message, authenticated_client);

        }


    }

    // Menyen som dukker opp før brukeren har logget inn
    // Metoden printer ut en meny, så tar den et input fra brukeren
    // Returnerer en status
    private ClientStatusEnum anonymous_menu(){

        System.out.println("--| MENU |--");
        System.out.println("1: LOGIN");
        System.out.println("2: REGISTER");
        System.out.println("OTHER: EXIT");
        System.out.print("\nINPUT: ");

        Scanner input = new Scanner(System.in);

        try{

            // Hent brukerens svar og returner den nye statusen
            // Om noe annet en 1 eller 2, returner EXITING
            int choice = input.nextInt();
            clear_console();

            if     (choice == 1) return ClientStatusEnum.UNAUTHENTICATED;
            else if(choice == 2) return ClientStatusEnum.ANONYMOUS;         // ENDRE TIL REGISTERING
            else                 return ClientStatusEnum.EXITING;

        } catch(InputMismatchException e){
            
            clear_console();
            return ClientStatusEnum.EXITING;
        
        }

    }



    public boolean is_authenticated(){
        return (authenticated_client != null);
    }

    private void sleep(int ms){

        try{
            Thread.sleep(ms);
        } catch(InterruptedException e){
            System.out.println("LOL");
        }

    }

    private void process_message(String message){
        System.out.println("[PROCESS]: " + message);
    }
    
    private void system_message(String message){
        System.out.println("[SYSTEM]: " + message);
    }

    public static void clear_console() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    } 

}
