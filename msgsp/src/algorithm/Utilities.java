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
	public static float getSupportDifference(ItemSet is, ArrayList<Item> allItems) {
		
		Float minMIS = allItems.get(allItems.size() - 1).getMinItemSupport();
		Float maxMIS = 0.0f;
		Float tmp = 0.0f;
		
		for(Integer item : is.getItems()){
			if((tmp = Utilities.getItemByItemNo(item, allItems).getActualSupport()) < minMIS)
				minMIS = tmp;
			
			if((tmp = Utilities.getItemByItemNo(item, allItems).getActualSupport()) > maxMIS)
				maxMIS = tmp;
		}
		
		return (maxMIS - minMIS);		
	}
	
	/**
	 * get all k-1 candidate sequences
	 * @param seq - parent sequence
	 * @return a list of sequences
	 */
	public static ArrayList<ItemSet> getsubSequences (Sequence seq){		
		
		int n = seq.getAllItems().size();
		int[] masks = new int[n];
		ArrayList<ItemSet> subSequences = new ArrayList<ItemSet>();
		
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
				
				ItemSet is = new ItemSet();
				
				is.setItems(newList);				
				
				subSequences.add(is);
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
		
		/*ArrayList<Sequence> sequences = new ArrayList<Sequence>();
		
		for(Sequence seq : allseq){
			
			if(!sequences.contains(seq))
				sequences.add(seq);			
		}
		
		return sequences;*/
		
		boolean exists = false;
		ArrayList<Integer> flag = new ArrayList<Integer>();

		for(int i=0; i<allseq.size(); i++){

			for(int j=i+1; j<allseq.size(); j++){

				if(allseq.get(i).getAllItems().size() == allseq.get(j).getAllItems().size()
						&& allseq.get(i).getItemsets().size() == allseq.get(j).getItemsets().size()){	

					for(int k=0; k < allseq.get(i).getItemsets().size(); k++){

						exists = false;

						if(allseq.get(i).getItemsets().get(k).getItems().containsAll((allseq.get(j).getItemsets().get(k).getItems()))){
							exists = true;
						}					
						else{
							exists = false;
						}
					}

					if(exists)
						flag.add(j);
						//allseq.remove(j);					
				}
			}			
		}

		ArrayList<Sequence> seq = new ArrayList<Sequence>();

		for(int i=0; i < allseq.size(); i++){

			if(!flag.contains(i)){
				seq.add(allseq.get(i));
			}
		}

		return seq;
	}

	/**
	 * checks if a list of sequences contain a sequence, skipping the itemsets that dont contain lowest MIS value(pruning)
	 * @param sequences - list of sequences
	 * @param seq - sequence to be checked
	 * @param allItems - list of all items
	 * @return true or false
	 * @deprecated
	 */
	public static boolean ListContains(ArrayList<Sequence> sequences, Sequence subSequence, boolean prune) {
		
		boolean contains = false;

		for(Sequence s : sequences){

			if(s.contains(subSequence)){
				
				contains = true;
				break;				
			}				
		}

		return contains;
	}
	
	/**
	 * checks if a list of sequences contain a sequence
	 * @param sequences - list of sequences
	 * @param seq - sequence to be checked
	 * @return true or false
	 */
	public static boolean ListContains(ArrayList<Sequence> sequences, Sequence seq) {

		boolean contains = false;

		for(Sequence s : sequences){

			if(s.contains(seq)){
				
				contains = true;
				break;				
			}				
		}

		return contains;
	}	
	
	/**
	 * checks if a list of sequences contains (exact match) a sequence
	 * @param sequences - list of sequences
	 * @param seq - sequence to be checked
	 * @return true or false
	 */
	public static boolean ListHas(ArrayList<Sequence> sequences, Sequence seq) {

		boolean contains = false;

		for(Sequence s : sequences){

			if(s.isEqualTo(seq)){
				
				contains = true;
				break;				
			}				
		}

		return contains;
	}	
}
