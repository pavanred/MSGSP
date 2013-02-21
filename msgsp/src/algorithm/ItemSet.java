package algorithm;

import java.util.ArrayList;

public class ItemSet {

	private ArrayList<Integer> items;
	
	/*
     * Constructor
     * @param allItems list of all items in the ItemSet
     */
    protected ItemSet(ArrayList<Integer> allItems){    	
    	this.items = allItems;
    }
    
    /*
     * Constructor
     */
    protected ItemSet(){    	
    	this.items = new ArrayList<Integer>();
    }    
        
    /*
     * Add Item to the existing list
     * @param itemNo item number
     */
	public void addItem(Integer itemNo){
		this.items.add(itemNo);
	}
    
    //Getter Setter
    public void setItems(ArrayList<Integer> _items){
		
		this.items = _items;		
	}
	
	public ArrayList<Integer> getItems(){
		
		return this.items;
	}
}