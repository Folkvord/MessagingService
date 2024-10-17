package datastructures;

public class Stack<T> {
    
    private StackNode<T> top;
    private int size = 0;

    
    public Stack(){}


    // Legger til et element på toppen av stacken
    public void add(T data){
        
        StackNode<T> new_node = new StackNode<T>(data);

        new_node.under = top;
        top = new_node;
        size++;

    }

    // Fjerner og returnerer topelementet
    public T remove(){

        StackNode<T> old_top = top;
        top = top.under;
        size--;

        return old_top.data;

    }

    // Henter topelementet
    public T get(){
        return top.data;
    }

    // Ser om stacken er tom
    public boolean is_empty(){
        return (size == 0);
    }
    
    // Printer stacken
    public String toString(){
        return "TOP: " + top.data + "\t | SIZE: " + size;
    }

}

class StackNode<T> {

    protected T data;
    protected StackNode<T> under;

    protected StackNode(T data){
        this.data = data;
    }

    public String toString(){
        return data.toString();
    }

}