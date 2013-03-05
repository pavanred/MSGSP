package algorithm;

import java.util.ArrayList;

public class ItemSet {

	private ArrayList<Integer> items;
	
	/**
     * Constructor
     * @param allItems list of all items in the ItemSet
     */
    protected ItemSet(ArrayList<Integer> allItems){    	
    	this.items = allItems;
    }
    
    /**
     * Constructor
     */
    protected ItemSet(){    	
    	this.items = new ArrayList<Integer>();
    }    
        
    /**
     * Add Item to the existing list
     * @param itemNo item number
     */
	public void addItem(Integer itemNo){
		this.items.add(itemNo);
	}
	
	/**
     * Check if this itemset contains an itemset
     * @param subItemSet - child itemset
     * @return true or false
     */
	public boolean contains(ItemSet subItemSet){
					
		if(subItemSet.getItems().size() > this.getItems().size())
			return false;
		
		int count = 0;
		
		for(int i=0, j=0; j < this.getItems().size() && i < subItemSet.getItems().size(); j++ ){
			
			if(this.getItems().get(j).equals(subItemSet.getItems().get(i))){
				count = count + 1;
				i++;
			}
		}
		
		if(count == subItemSet.getItems().size())		
			return true;
		else
			return false;
	}
	
	/**
     * Check if this itemset contains (exact equal) an itemset
     * @param subItemSet - child itemset
     * @return true or false
     */
	public boolean isEqualTo(ItemSet subItemSet){
					
		if(subItemSet.getItems().size() != this.getItems().size())
			return false;
		
		int count = 0;
		
		for(int i=0; i < this.getItems().size(); i++ ){
			
			if(this.getItems().get(i).equals(subItemSet.getItems().get(i))){
				count = count + 1;
			}
		}
		
		if(count == subItemSet.getItems().size())		
			return true;
		else
			return false;
	}
    
    //Getter Setter
    public void setItems(ArrayList<Integer> _items){
		
		this.items = _items;		
	}
	
	public ArrayList<Integer> getItems(){
		
		return this.items;
	}
}