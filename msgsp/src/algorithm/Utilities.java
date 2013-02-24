package algorithm;

import java.util.ArrayList;

public class Utilities {

	/**
	 * returns all sequences of a specific size
	 * @param frequentSequences - list of sequences
	 * @param size specified size of the sequences
	 */
	public static ArrayList<Sequence> getOfSize(ArrayList<Sequence> frequentSequences, int size) {
		
		ArrayList<Sequence> setOfSize = new ArrayList<Sequence>();
		
		for(Sequence seq : frequentSequences){

			if(seq.getAllItems().size() == size){
				setOfSize.add(seq);
			}
		}
		
		return setOfSize;
	}
	
	/**
	 * get the Item from Item number
	 * @param itemNo - Item number
	 * @param allItems list of all Items
	 */
	protected static Item getItemByItemNo(Integer itemNo, ArrayList<Item> allItems){
			
		Item item = null;
			
		for(Item itm : allItems){
			
			if(itm.getItemNo() == itemNo)
				item = itm;
		}
			
		return item;
	}
	
	public static Double getSupportDifference(Sequence candidate, ArrayList<Item> allItems) {
		
		Float minMIS = allItems.get(allItems.size() - 1).getMinItemSupport();
		Float maxMIS = 0.0f;
		Float tmp = 0.0f;
		
		for(Integer item : candidate.getAllItems()){
			if((tmp = getItemByItemNo(item, allItems).getMinItemSupport()) < minMIS)
				minMIS = tmp;
			
			if((tmp = getItemByItemNo(item, allItems).getMinItemSupport()) > maxMIS)
				maxMIS = tmp;
		}
		
		return (double) (maxMIS - minMIS);		
	}
	
}
