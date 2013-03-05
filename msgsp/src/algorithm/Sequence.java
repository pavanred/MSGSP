package algorithm;

import java.util.ArrayList;
import java.util.HashSet;

public class Sequence {
	
	private ArrayList<ItemSet> itemsets;
	private Integer count;
	
	/**
	 * Constructor
     */
    protected Sequence(){    	
		 this.itemsets = new ArrayList<ItemSet>();
		 this.count = 0;
	 }
    
    /**
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
	 * get the size of the sequence
	 */
	public int getSize(){
		
		return this.getItemsets().size();
	}
	
	/**
	 * get the length of the sequence
	 */
	public int getlength(){
		
		return this.getAllItems().size();
	}
	
	/**
	 * increments count of the sequence by 1
	 */
	public void incrementCount(){
		this.count = this.count + 1;
	}
	
	/**
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
	
	
	/**
	 * Checks if a sequence is contained in any k-1 subsequence of another sequence
	 * @param seq the sequence that is to be checked for
	 * @return true or false based on whether it is contained or not
	 * @deprecated use contains() 
	 */
	public boolean containedIn(Sequence seq) {

		ArrayList<Integer> parentIndexFlag = new ArrayList<Integer>();
		ArrayList<Integer> subIndexFlag = new ArrayList<Integer>();		
		
		for(int i=0; i < seq.getItemsets().size(); i++){
		
			for(int j=0; j< this.getItemsets().size(); j++){
				
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
	
	/**
	 * Checks if this sequence contains another sequence
	 * @param subSequence - sub sequence that is/isn't contained in this sequence
	 * @return true or false 
	 */
	public boolean contains(Sequence subSequence){
		
		if(subSequence.getItemsets().size() > this.getItemsets().size())
			return false;
		
		int count = 0;
		
		for(int i=0, j=0; j < this.getItemsets().size() && i < subSequence.getItemsets().size(); j++ ){
			
			if(this.getItemsets().get(j).contains(subSequence.getItemsets().get(i))){
				count = count + 1;
				i++;
			}
		}
		
		if(count == subSequence.getItemsets().size())		
			return true;
		else
			return false;
	}
	
	
	/**
	 * Returns the list of all distinct items
	 * @return list of all distinct items
	 */
	public ArrayList<Integer> getAllDistinctItems() {
		
		ArrayList<Integer> allItems = new ArrayList<Integer>();
		
		for(ItemSet itemset : this.itemsets){
			allItems.addAll(itemset.getItems());
		}	
		
		//removing duplicates 
		return new ArrayList<Integer>(new HashSet<Integer>(allItems));
		
	}
	
	/**
	 * Is the first item having the lowest minimum item support
	 * @return true or false
	 */
	public boolean isFirstItemLowestMIS(ArrayList<Item> allItems) {
		
		boolean isFirstItemLowestMIS = false;
		
		if(this.getAllItems().size() <= 0)
			return isFirstItemLowestMIS;
		
		Integer firstItemNo = this.getAllItems().get(0);
		
		Item firstItem = Utilities.getItemByItemNo(firstItemNo, allItems);
		Float minMISVal = Utilities.getItemByItemNo(allItems.get(allItems.size()-1).getItemNo(), allItems).getMinItemSupport();
		Float tmpMISVal = 0.0f;
		
		for(int i=1; i < this.getAllItems().size(); i++){
			if(minMISVal > (tmpMISVal = Utilities.getItemByItemNo(this.getAllItems().get(i), allItems).getMinItemSupport())){
				minMISVal = tmpMISVal;
			}
		}
		
		if(firstItem.getMinItemSupport() < minMISVal){
			isFirstItemLowestMIS = true;
		}
		
		return isFirstItemLowestMIS;
	}
	
	/**
	 * Is the last item having the lowest minimum item support
	 * @return true or false
	 */
	public boolean isLastItemLowestMIS(ArrayList<Item> allItems) {
		
		boolean isLastItemLowestMIS = false;
		
		if(this.getAllItems().size() <= 0)
			return isLastItemLowestMIS;
		
		Integer lastItemNo = this.getAllItems().get(this.getAllItems().size() - 1);
		
		Item lastItem = Utilities.getItemByItemNo(lastItemNo, allItems);
		Float minMISVal = Utilities.getItemByItemNo(allItems.get(allItems.size()-1).getItemNo(), allItems).getMinItemSupport();
		Float tmpMISVal = 0.0f;
		
		for(int i=0; i < this.getAllItems().size() - 1; i++){
			if(minMISVal > (tmpMISVal = Utilities.getItemByItemNo(this.getAllItems().get(i), allItems).getMinItemSupport())){
				minMISVal = tmpMISVal;
			}
		}
		
		if(lastItem.getMinItemSupport() < minMISVal){
			isLastItemLowestMIS = true;
		}
		
		
		return isLastItemLowestMIS;		
	}
	
	/**
	 * Checks if the last itemset is a single item
	 * @return true or false
	 */
	public boolean isSeperateItemSet(ArrayList<Integer> tmpS2) {
		
		boolean isSeperateItemSet = false;
		
		ItemSet lastItemSet = this.getItemsets().get(this.getItemsets().size() - 1);
		
		if(lastItemSet.getItems().size() == 1)
			isSeperateItemSet = true;
		else 
			isSeperateItemSet = false;
		
		return isSeperateItemSet;		
	}
	
	/**
	 * returns the last item of the sequence
	 * @return last item
	 */
	public Integer getLastItem() {		
		return this.getAllItems().get(this.getAllItems().size()-1);
	}
	
	/**
	 * gets the minimum MIS item
	 * @return min MIS item
	 */
	public int getMinMISItem(ArrayList<Item> allItems) {
		
		Float minMIS = allItems.get(allItems.size() - 1).getMinItemSupport();
		Float tmp = 0.0f;
		int itm = 0;
		
		for(Integer item : this.getAllDistinctItems()){
			
			if((tmp = Utilities.getItemByItemNo(item, allItems).getMinItemSupport()) < minMIS){
				minMIS = tmp;	
				itm = item;
			}
		}	
		
		return itm;
	}
	
	/**
	 * Checks if this sequence contains (exact match) another sequence
	 * @param subSequence - sub sequence that is/isn't contained in this sequence
	 * @return true or false 
	 */
	public boolean isEqualTo(Sequence subSequence){
		
		if(subSequence.getItemsets().size() != this.getItemsets().size())
			return false;
		
		int count = 0;
		
		for(int i=0; i < this.getItemsets().size(); i++ ){
			
			if(this.getItemsets().get(i).isEqualTo(subSequence.getItemsets().get(i))){
				count = count + 1;
			}
		}
		
		if(count == subSequence.getItemsets().size())		
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
