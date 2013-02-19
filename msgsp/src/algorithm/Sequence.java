package algorithm;

import java.util.ArrayList;

public class Sequence {
	
	private ArrayList<ItemSet> itemsets;
	private Integer count;
	
	/*
	 * Constructor
     */
    protected Sequence(){    	
		 this.itemsets = new ArrayList<ItemSet>();
		 this.count = 0;
	 }
	
	/**
	 * Add an ItemSet to the Sequence
	 * @param itemset - list of Items	 
	 */
	public void addItemSet(ItemSet itemset){
		this.itemsets.add(itemset);
	}
	
	/**
	 * returns all items in a sequence
	 */
	public ArrayList<Integer> getAllItems(){
		
		ArrayList<Integer> allItems = new ArrayList<Integer>();
		
		for(ItemSet itemset : this.itemsets){
			allItems.addAll(itemset.getItems());
		}	
		
		return allItems;
	}
	
	/**
	 * increments count of the sequence by 1
	 */
	public void incrementCount(){
		this.count = this.count + 1;
	}
	
	//Getter Setter
	public Integer getCount(){
		return this.count;
	}
		
	public void setCount(Integer _count){
		this.count = _count;
	}
	
	public void setItemsets(ArrayList<ItemSet> value){
		
		this.itemsets = value;		
	}	
	public ArrayList<ItemSet> getItemsets(){
		
		return this.itemsets;
	}

}
