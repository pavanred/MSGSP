package algorithm;

import java.util.ArrayList;

public class Sequence {

	private ArrayList<ItemSet> itemsets = new ArrayList<ItemSet>();
	
	public void setItemsets(ArrayList<ItemSet> value){
		
		this.itemsets = value;		
	}	
	public ArrayList<ItemSet> getItemsets(){
		
		return this.itemsets;
	}	
	
	public void addItemSet(ItemSet itemset){
		this.itemsets.add(itemset);
	}
	
	public ArrayList<Integer> getAllItems(){
		
		ArrayList<Integer> allItems = new ArrayList<Integer>();
		
		for(ItemSet itemset : this.itemsets){
			allItems.addAll(itemset.getItems());
		}	
		
		return allItems;
	}
	public boolean isNotEmpty() {
		
		boolean isNotEmpty;
		
		if(this.itemsets.size() > 0)
			isNotEmpty = true;
		else
			isNotEmpty = false;
		
		return isNotEmpty;
	}
}
