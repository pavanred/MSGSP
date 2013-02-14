package algorithm;

import java.util.ArrayList;

public class ItemSet {

	private ArrayList<Integer> items;
	
	public ItemSet(){
		this.items = new ArrayList<Integer>();
	}
	
	public void setItems(ArrayList<Integer> _items){
		
		items = _items;		
	}
	
	public ArrayList<Integer> getItems(){
		
		return items;
	}	
	
	public void addItem(Integer item){
		this.items.add(item);
	}
}
