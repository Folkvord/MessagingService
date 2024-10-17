package datastructures;

import java.io.Serializable;

public class LinkedList<T> implements Serializable {
    
    private static final long serialVersionUID = 123123123;    

    protected LLNode<T> head;
    protected LLNode<T> tail;
    protected int size = 0;


    public LinkedList(){}


    // Legger til et element bakerst i listen
    public void add(T data){

        LLNode<T> new_data = new LLNode<T>(data);

        if(is_empty()){

            head = new_data;
            tail = new_data;
            size++;

            return;
        }

        tail.next = new_data;
        tail = new_data;
        size++;

    }

    // Fjerner et element basert på elementet
    public T remove(T element){

        LLNode<T> delete_node = head;
        LLNode<T> previous_node = null;

        while(delete_node.data != element){
            previous_node = delete_node;
            delete_node = delete_node.next;
        }

        previous_node.next = delete_node.next;

        return delete_node.data;

    }

    // Fjerner et element basert på indeksen
    public T remove(int index){

        LLNode<T> delete_node = head;
        LLNode<T> previous_node = null;
        for(int i = 0; i < index; i++){

            previous_node = delete_node;
            delete_node = delete_node.next;

        }

        previous_node.next = delete_node.next;
        size--;

        return delete_node.data;

    }

    // Henter et element fra index
    public T get(int index){

        LLNode<T> return_node = head;
        for(int i = 0; i < index; i++){

            return_node = return_node.next;

        }

        return return_node.data;

    }

    // Henter indeksen til et element
    // Returnerer -1 om det ikke finnes
    public int get_index(T element){

        for(int i = 0; i < size; i++){            
            
            if(get(i).equals(element)){
                return i;
            }

        }

        return -1;
    
    }

    // Setter et element i indeks
    public void set(int index, T element){

        LLNode<T> change_node = head;
        for(int i = 0; i < index; i++){

            change_node = change_node.next;

        }

        change_node.data = element;

    }

    // Ser om et element er i listen
    public boolean in_list(T element){

        LLNode<T> current_node = head;
        while(current_node != null){

            if(current_node.data.equals(element)){
                return true;
            }

            current_node = current_node.next;

        }

        return false;

    }

    // Returnerer listen i form av en tabell
    public Object[] to_array(){

        Object[] new_array = new Object[size];
        for(int i = 0; i < size; i++){

            new_array[i] = get(i);

        }

        return new_array;

    }

    // Printer ut listen
    public String toString(){
        
        String to_return = "[";

        for(int i = 0; i < size; i++){

            if((i + 1) == size){
                to_return = to_return.concat(get(i).toString() + "]");
            }
            else{
                to_return = to_return.concat(get(i).toString() + ", ");
            }

        }

        return to_return;

    }
    
    
    public boolean is_empty(){
        return (size == 0);
    }

    public int size(){
        return size;
    }

}


class LLNode<T> implements Serializable {
    
    private static final long serialVersionUID = 321321321;

    protected T data;
    protected LLNode<T> next;

    protected LLNode(T data){

        this.data = data;

    }

    public String toString(){
        return data.toString();
    }

}