package tools;

import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


public class JSON {


    // Lager en JSON-string for å sende en melding fra en klient til en annen
    public static String encode_message(String message, String from){

        Map<String, Object> ignore_dumb_error = new HashMap<String, Object>();
        
        ignore_dumb_error.put("Type", "Message");
        ignore_dumb_error.put("From", from);
        ignore_dumb_error.put("Payload", message);

        JSONObject json_object = new JSONObject(ignore_dumb_error);
        String json_encoded_string = json_object.toJSONString();

        return json_encoded_string;

    }
    
    // Lager en JSON-string for å sende brukerinput
    public static String encode_input(String input){

        Map<String, String> ignore_dumb_error = new HashMap<String, String>();

        ignore_dumb_error.put("Type", "Input");
        ignore_dumb_error.put("Payload", input);

        JSONObject json_object = new JSONObject(ignore_dumb_error);
        String json_encoded_string = json_object.toJSONString();

        return json_encoded_string;

    }

    // Lager en JSON-string for å sende brukerinput
    public static String encode_response(String response){

        Map<String, String> ignore_dumb_error = new HashMap<String, String>();

        ignore_dumb_error.put("Type", "Response");
        ignore_dumb_error.put("Payload", response);

        JSONObject json_object = new JSONObject(ignore_dumb_error);
        String json_encoded_string = json_object.toJSONString();

        return json_encoded_string;

    }

    // Lager en JSON-string med en kommando
    public static String encode_command(String command){

        Map<String, Object> ignore_dumb_error = new HashMap<String, Object>();
        
        ignore_dumb_error.put("Type", "Command");
        ignore_dumb_error.put("Payload", command);

        JSONObject json_object = new JSONObject(ignore_dumb_error);
        String json_encoded_string = json_object.toJSONString();

        return json_encoded_string;

    }

    // Lager en JSON-string for å sende logininfo
    public static String encode_login(String username, String password){
        
        Map<String, Object> ignore_dumb_error = new HashMap<String, Object>();
        
        ignore_dumb_error.put("Type", "Login");
        ignore_dumb_error.put("Username", username);
        ignore_dumb_error.put("Password", password);

        JSONObject json_object = new JSONObject(ignore_dumb_error);
        String json_encoded_string = json_object.toJSONString();

        return json_encoded_string;

    }



    // Dekrypterer en JSON-fil og returnerer payloadet
    public static String decrypt(String json){

        JSONObject json_object = (JSONObject) JSONValue.parse(json);
        String payload = (String) json_object.get("Payload");

        return payload;

    }

    // Dekrypterer en JSON-string med to "payloads"
    public static String[] decrypt_two_types(String get_1, String get_2, String json){

        JSONObject json_object = (JSONObject) JSONValue.parse(json);
        String got_1 = (String) json_object.get(get_1);
        String got_2 = (String) json_object.get(get_2);

        String[] returning = {got_1, got_2};

        return returning;

    }
    
    // Dekrypterer et objekt
    public static Object decrypt_object(String json){

        JSONObject json_object = (JSONObject) JSONValue.parse(json);
        Object payload = json_object.get("Payload");

        return payload;

    }

    // Gir typen
    public static String type(String json){

        JSONObject json_object = (JSONObject) JSONValue.parse(json);
        String type = (String) json_object.get("Type");
        
        return type;

    }

}

/*
 * JSON MELDINGER TIL SERVEREN:
 * 
 * Message:
 * Type             String "Message"
 * To               Client client
 * Payload          String message
 * 
 * 
 * Status:
 * Type             String "Status"
 * Payload          String status
 * 
 * 
 * User input:
 * Type             String "Input"
 * Payload          String input
 * 
 * 
 * Command:
 * Type             String "Command"
 * To               Client client
 * Payload          String command
 * 
 * 
 * Login:
 * Type             String "Login"
 * Username         String username
 * Password         String password
 * 
 * 
 * Session_info_req:
 * Type             String "Session_info_req"
 * From             Client from
 * Payload          int session_id
 * 
 * 
 * Session_req:
 * Type             String "Session_req"
 * From             Client client
 * Payload          int session_id
 * 
 */
