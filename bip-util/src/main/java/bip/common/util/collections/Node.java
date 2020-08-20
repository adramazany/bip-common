package bip.common.util.collections;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {
	String text;
	T value;
	List<Node<T>> children=new ArrayList<Node<T>>();
	
	
	public Node(){}
	
	public Node(String text,T value) {
		super();
		this.text = text;
		this.value = value;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public List<Node<T>> getChildren() {
		return children;
	}
	public void setChildren(List<Node<T>> children) {
		this.children = children;
	}
	
	public void add(Node<T> child){
		this.children.add(child);
	}
	public Node<T> get(int index){
		return children.get(index);
	}
	
	public int size(){
		return children.size(); 
	}
	

}
