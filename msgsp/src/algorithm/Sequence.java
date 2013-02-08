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
}
