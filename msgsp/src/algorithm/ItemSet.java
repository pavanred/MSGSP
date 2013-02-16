package algorithm;

import java.util.ArrayList;

public class ItemSet {

	private ArrayList<Integer> items;
	private Integer count = 0;
	
	public Integer getCount(){
		return this.count;
	}
	
	public void setCount(Integer _count){
		this.count = _count;
	}
	
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
	
	public void incrementCount(){
		this.count = this.count + 1;
	}
	
	public boolean containedIn(Sequence seq, boolean minMISItemRemoved) {
		// TODO Auto-generated method stub
		return false;
	}

	public ItemSet getCandidatePrime() {
		// TODO Auto-generated method stub
		return new ItemSet();
	}
}
