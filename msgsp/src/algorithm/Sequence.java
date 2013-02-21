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
    
    /*
	 * Copy Constructor
     */
    protected Sequence(Sequence s){    	
		 
    	ItemSet itemset;
    	ArrayList<Integer> items;
    	
    	for(ItemSet is : s.getItemsets()){
    		
    		itemset = new ItemSet();
    		items = new ArrayList<Integer>(is.getItems());
    		
    		itemset.setItems(items);
    		this.addItemSet(itemset);    		
    	}    	
	 }
	
	/*
	 * Add an ItemSet to the Sequence
	 * @param itemset - list of Items	 
	 */
	public void addItemSet(ItemSet itemset){
		this.itemsets.add(itemset);
	}
	
	/*
	 * returns all items in a sequence
	 */
	public ArrayList<Integer> getAllItems(){
		
		ArrayList<Integer> allItems = new ArrayList<Integer>();
		
		for(ItemSet itemset : this.itemsets){
			allItems.addAll(itemset.getItems());
		}	
		
		return allItems;
	}
	
	/*
	 * increments count of the sequence by 1
	 */
	public void incrementCount(){
		this.count = this.count + 1;
	}
	
	/*
	 * Gets the minimum MIS in a given sequence
	 * @param list of all items
	 * @return the minimum MIS value
	 */
	public Float getMinMIS(ArrayList<Item> allItems){
		
		Float minMIS = allItems.get(allItems.size() - 1).getMinItemSupport();
		Float tmp = 0.0f;
		
		for(Integer item : this.getAllItems()){
			
			if((tmp = Utilities.getItemByItemNo(item, allItems).getMinItemSupport()) < minMIS)
				minMIS = tmp;			
		}	
		
		return minMIS;
	}
	
	
	/*
	 * Checks if a sequence is contained in any k-1 subsequence of another sequence
	 * @param seq the sequence that is to be checked for
	 * @return true or false based on whether it is contained or not 
	 */
	public boolean containedIn(Sequence seq) {

		ArrayList<Integer> parentIndexFlag = new ArrayList<Integer>();
		ArrayList<Integer> subIndexFlag = new ArrayList<Integer>();		
		
		for(int j=0; j< this.itemsets.size(); j++){
		
			for(int i=0; i < seq.getItemsets().size(); i++){
				
				if(!parentIndexFlag.contains(i) && !subIndexFlag.contains(j)){
				
					if(seq.getItemsets().get(i).getItems().containsAll(this.getItemsets().get(j).getItems())){
						parentIndexFlag.add(i);
						subIndexFlag.add(j);
					}
				}				
			}			
		}
	
		if(subIndexFlag.size() == this.itemsets.size())
			return true;
		else
			return false;
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
