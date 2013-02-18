package algorithm;

import java.util.ArrayList;

public class Sequence {

	private ArrayList<ItemSet> itemsets = new ArrayList<ItemSet>();
	
	private Integer count = 0;
	
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
	
	public void incrementCount(){
		this.count = this.count + 1;
	}
	
	public boolean isFirstItemLowestMIS(ArrayList<MISValue> misValues) {
		
		boolean isFirstItemLowestMIS = false;
		
		if(this.getAllItems().size() <= 0)
			return isFirstItemLowestMIS;
		
		Integer firstItemNo = this.getAllItems().get(0);
		
		MISValue firstItem = findMISValueByItemNo(firstItemNo, misValues);
		Float minMISVal = 0.0f;
		Float tmpMISVal = 0.0f;
		
		for(int i=1; i < this.getAllItems().size(); i++){
			if(minMISVal > (tmpMISVal = findMISValueByItemNo(i, misValues).getMinItemSupport())){
				minMISVal = tmpMISVal;
			}
		}
		
		if(firstItem.getMinItemSupport() < minMISVal){
			isFirstItemLowestMIS = true;
		}
		
		return isFirstItemLowestMIS;
	}
	
	@Deprecated
	public boolean isNotEmpty() {
		
		boolean isNotEmpty;
		
		if(this.itemsets.size() > 0)
			isNotEmpty = true;
		else
			isNotEmpty = false;
		
		return isNotEmpty;
	}
	

	public boolean isLastItemLowestMIS(ArrayList<MISValue> misValues) {
		
		boolean isLastItemLowestMIS = false;
		
		if(this.getAllItems().size() <= 0)
			return isLastItemLowestMIS;
		
		Integer lastItemNo = this.getAllItems().get(this.getAllItems().size() - 1);
		
		MISValue lastItem = findMISValueByItemNo(lastItemNo, misValues);
		Float minMISVal = 0.0f;
		Float tmpMISVal = 0.0f;
		
		for(int i=0; i < this.getAllItems().size() - 1; i++){
			if(minMISVal > (tmpMISVal = findMISValueByItemNo(i, misValues).getMinItemSupport())){
				minMISVal = tmpMISVal;
			}
		}
		
		if(lastItem.getMinItemSupport() < minMISVal){
			isLastItemLowestMIS = true;
		}
		
		
		return isLastItemLowestMIS;		
	}
	
	//find MISValue object from item Id
	private MISValue findMISValueByItemNo(Integer itemNo, ArrayList<MISValue> misValues){
				
		MISValue misVal = null;
				
		for(MISValue mVal : misValues){
			if(mVal.getItemNo() == itemNo)
				misVal = mVal;
		}
				
		return misVal;
	}
	
	public boolean isSeperateItemSet(ArrayList<Integer> tmpS2) {
		
		boolean isSeperateItemSet = false;
		
		ItemSet lastItemSet = this.getItemsets().get(this.getItemsets().size() - 1);
		
		if(lastItemSet.getItems().size() == 1)
			isSeperateItemSet = true;
		else 
			isSeperateItemSet = false;
		
		return isSeperateItemSet;		
	}
	
	public Integer getLastItem() {
		
		return this.getAllItems().get(this.getAllItems().size()-1);
	}
		
	public boolean containedIn(Sequence seq) {
		
		boolean isContainedIn = false;		
					
		if(seq.getItemsets().containsAll(this.itemsets)){
			isContainedIn = true;
		}
		else{
			isContainedIn = false;
		}
		
		return isContainedIn;
	}
}
