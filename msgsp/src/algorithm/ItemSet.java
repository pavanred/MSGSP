package algorithm;

import java.util.ArrayList;

public class ItemSet {

	private ArrayList<Integer> items;
	//private Integer count = 0;
	
	/*public Integer getCount(){
		return this.count;
	}
	
	public void setCount(Integer _count){
		this.count = _count;
	}*/
	
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

	public boolean checkEquals(ItemSet s_is) {
		
		boolean checkEquals = false;
		
		if(this.items.size() == s_is.getItems().size()){
		
			for(int i=0; i< this.items.size(); i++){
				
				if(this.items.get(i) == s_is.getItems().get(i)){
					checkEquals = true;
				}
				else{
					checkEquals = false;
					break;
				}
			}
			
		}		
		
		return checkEquals;
	}
	
	/*public void incrementCount(){
		this.count = this.count + 1;
	}*/
	
	public boolean isSuperSetOf(ItemSet is){
		
		boolean isSuperSetOf = false;
		
		/*if(this.items.containsAll(is.getItems()))
			isSuperSetOf = true;*/
		
		for(Integer it : is.getItems()){
			if(this.items.contains(it))
				isSuperSetOf = true;
			else{
				isSuperSetOf = false;
				break;
			}
		}
		
		return isSuperSetOf;		
	}
}
