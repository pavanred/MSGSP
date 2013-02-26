package algorithm;

import java.util.ArrayList;

public class Utilities {

	/**
	 * returns all sequences of a specific size
	 * @param frequentSequences - list of sequences
	 * @param size specified size of the sequences
	 * @return list of sequences
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
	 * @return Item
	 */
	protected static Item getItemByItemNo(Integer itemNo, ArrayList<Item> allItems){
			
		Item item = null;
			
		for(Item itm : allItems){
			
			if(itm.getItemNo().equals(itemNo)){
				item = itm;
				break;
			}
		}
			
		return item;
	}
	
	/**
	 * gets the support difference of a sequence
	 * @param candidate - Sequence
	 * @param allItems list of all Items
	 */
	public static Double getSupportDifference(Sequence candidate, ArrayList<Item> allItems) {
		
		Float minMIS = allItems.get(allItems.size() - 1).getMinItemSupport();
		Float maxMIS = 0.0f;
		Float tmp = 0.0f;
		
		for(Integer item : candidate.getAllItems()){
			if((tmp = Utilities.getItemByItemNo(item, allItems).getMinItemSupport()) < minMIS)
				minMIS = tmp;
			
			if((tmp = Utilities.getItemByItemNo(item, allItems).getMinItemSupport()) > maxMIS)
				maxMIS = tmp;
		}
		
		return (double) (maxMIS - minMIS);		
	}
	
	/**
	 * get all k-1 candidate sequences
	 * @param seq - parent sequence
	 * @return a list of sequences
	 */
	public static ArrayList<Sequence> getsubSequences (Sequence seq){		
		
		int n = seq.getAllItems().size();
		int[] masks = new int[n];
		Sequence newSeq = new Sequence();
		ArrayList<Sequence> subSequences = new ArrayList<Sequence>();
		
		for(int i=0; i<n;i++){
			masks[i] = (1<<i);
		}
		
		for(int i=0;i<(1<<n);i++){
				
			ArrayList<Integer> newList = new ArrayList<Integer>();
				
			for(int j=0; j<n;j++){
				if((masks[j] & i) != 0){
					newList.add(seq.getAllItems().get(j));					
				}
			}
			
			if(newList.size() == n-1){
				
				newSeq = new Sequence();
				ItemSet is = new ItemSet();
				
				is.setItems(newList);
				newSeq.addItemSet(is);
				
				subSequences.add(newSeq);
			}			
		}
		
		return subSequences;
	}
	
	/**
	 * removes duplicate sequences
	 * @param allseq - list of sequences
	 * @return unique sequences
	 */
	public static ArrayList<Sequence> removeDuplicates(ArrayList<Sequence> allseq) {
		
		boolean exists = false;
		
		for(int i=0; i<allseq.size(); i++){
			
			for(int j=i+1; j<allseq.size(); j++){
				
				if(allseq.get(i).getAllItems().size() == allseq.get(j).getAllItems().size()
						&& allseq.get(i).getItemsets().size() == allseq.get(j).getItemsets().size()){	
					
					for(int k=0; k < allseq.get(i).getItemsets().size(); k++){
						
						exists = false;
						
						if(allseq.get(i).getItemsets().get(k).getItems().equals((allseq.get(j).getItemsets().get(k).getItems()))){
							exists = true;
						}					
						else{
							exists = false;
						}
					}
					
					if(exists)
						allseq.remove(j);					
				}
			}			
		}
		
		return allseq;
		
	}	
}
