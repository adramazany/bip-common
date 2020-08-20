package bip.common.util.collections;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bip.common.util.persian.PersianCollator;
import org.apache.commons.lang.StringUtils;

import aip.util.NVL;

public class Tree<T> {
	
	List<Node<T>> roots=new ArrayList<Node<T>>();
	Collator collator = PersianCollator.getPersianInstance();
	String levelIndent = " ";

	public List<Node<T>> getRoots() {
		return roots;
	}

	public void setRoots(List<Node<T>> roots) {
		this.roots = roots;
	}
	
	public void addRoot(Node<T> root){
		roots.add(root);
	}
	
	public void addChild(Node<T> parent,Node<T> child){
		parent.add(child);
	}
	
	public void sortTree(){
		sortTree(0,roots.size());
	}
	public void sortTree(int fromIndex,int toIndex){
		sortList(roots.subList(fromIndex, toIndex));
		for (int i = fromIndex; i < toIndex; i++) {
			sortChildrenRecursive(roots.get(i));
		}
	}
	public void sortChildrenRecursive(Node<T> parent){
		sortList(parent.getChildren());
		for (int i = 0; i < parent.size(); i++) {
			sortChildrenRecursive(parent.get(i));
		}
	}
	
	public void sortList(List<Node<T>> list){
		Collections.sort(list, new Comparator<Node<T>>() {

			public int compare(Node<T> o1, Node<T> o2) {
				return collator.compare(o1.getText(), o2.getText());
			}
		});
	}

	public Collator getCollator() {
		return collator;
	}

	public void setCollator(Collator collator) {
		this.collator = collator;
	}
	
	public int size(){
		return roots.size(); 
	}

	public Node<T> get(int index){
		return roots.get(index);
	}
	
	
	public List<T> getTreeList(){
		List<T> values = new ArrayList<T>();
		for (int i = 0; i < size(); i++) {
			_fillTree2List(get(i), values,0);
		}
		return values;
	}
	
	private void _fillTree2List(Node<T> node,List<T> values,int level){
		String text = StringUtils.repeat(levelIndent, level) + NVL.getString( node.getText() ).trim();
		node.setText(text);
		values.add(node.value);
		level++;
		for (int i = 0; i < node.size(); i++) {
			_fillTree2List(node.get(i), values,level);
		}
	}
	

}
