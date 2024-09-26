package main;

import client.ClientProcess;
import gui.Window;

public class Main {
    
    public static void main(String[] args) {
        
        //new Window();
        
        ClientProcess client = new ClientProcess();
        client.start_session();

    }

}
