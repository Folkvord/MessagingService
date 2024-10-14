package datastructures;

import java.io.Serializable;

public class LLNode<T> implements Serializable {
    
    private static final long serialVersionUID = 321321321;

    protected T data;
    protected LLNode<T> next;


    protected LLNode(T data){

        this.data = data;

    }


}
