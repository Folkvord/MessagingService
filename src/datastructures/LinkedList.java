package datastructures;

public class LinkedList<T> {
    
    protected LLNode<T> head;
    protected LLNode<T> tail;
    protected int size = 0;


    public LinkedList(){}


    public void add(T data){

        LLNode<T> new_data = new LLNode<T>(data);

        if(is_empty()){

            head = new_data;
            tail = new_data;
            size++;

            return;
        }

        new_data.next = head;
        head = new_data;

    }

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

    public T get(int index){

        LLNode<T> return_node = head;
        for(int i = 0; i < index; i++){

            return_node = return_node.next;

        }

        return return_node.data;

    }

    public void set(int index, T element){

        LLNode<T> change_node = head;
        for(int i = 0; i < index; i++){

            change_node = change_node.next;

        }

        change_node.data = element;

    }

    public boolean in_list(T element){

        LLNode<T> current_node = head;
        while(current_node != null){
            
            if(current_node.data == element){
                return true;
            }

            current_node = current_node.next;

        }

        return false;

    }

    public boolean is_empty(){
        return (size == 0);
    }

    public int size(){
        return size;
    }

}
