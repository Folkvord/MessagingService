package tools;

import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import client.Client;

/*
 * JSON design:
 * 
 * Type:    Hvilken type melding    (message, command, status, osv)
 * To:      Hvem den skal til       (Kan etterlates, kan også være en tabell med Account objekter)
 * From:    Hvem den er fra         (Kan etterlates)
 * Payload: Selve meldingen / kommandoen / status
 * 
 */


public class JSON {


    // Lager en JSON-string for å sende en melding fra en klient til en annen
    public static String client_message(String message, Client from){

        Map<String, Object> ignore_dumb_error = new HashMap<String, Object>();
        
        ignore_dumb_error.put("Type", "Message");
        ignore_dumb_error.put("From", from.get_username());
        ignore_dumb_error.put("Payload", message);

        JSONObject json_object = new JSONObject(ignore_dumb_error);
        String json_encoded_string = json_object.toJSONString();

        return json_encoded_string;

    }


    // Lager en JSON-string for å sende en statusmelding til en klient
    public static String status_message(String status){

        Map<String, Object> ignore_dumb_error = new HashMap<String, Object>();
        
        ignore_dumb_error.put("Type", "Status");
        ignore_dumb_error.put("Payload", status);

        JSONObject json_object = new JSONObject(ignore_dumb_error);
        String json_encoded_string = json_object.toJSONString();

        return json_encoded_string;

    }


    // Lager en JSON-string med en kommando
    public static String command_message(String command){

        Map<String, Object> ignore_dumb_error = new HashMap<String, Object>();
        
        ignore_dumb_error.put("Type", "Command");
        ignore_dumb_error.put("Payload", command);

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

    // Gir typen
    public static String type(String json){

        JSONObject json_object = (JSONObject) JSONValue.parse(json);
        String type = (String) json_object.get("Type");
        
        return type;

    }

}
