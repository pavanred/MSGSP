package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Msgsp {
		
	public ArrayList<MISValue>sortMinSupportValue(ArrayList<MISValue> misValues){		
		
	    Collections.sort(misValues, new CustomComparator());
	    
		return misValues;
	}
	
	public ArrayList<MISValue> computeItemSupport(ArrayList<MISValue> misValues,String dataFilePath){		
		
		Integer sequenceCount = 0;
		
		FileHandler fileHandler = new FileHandler();
		
		Sequence seq = fileHandler.getNextSequence(true, dataFilePath);
		
		while(seq != null){
			
			sequenceCount = sequenceCount + 1;
			
			for(Integer item : seq.getAllItems()){
				for(MISValue mVal : misValues){
					if(mVal.getItemNo() == item){
						mVal.setSupportCount(mVal.getSupportCount() + 1);
					}
				}
			}
			
			seq = fileHandler.getNextSequence(false, null);
		}
		
		for(MISValue mVal : misValues){
			mVal.setActualSupport((float)mVal.getSupportCount()/sequenceCount);
		}
		
		return misValues;
	}
	
	public ArrayList<Sequence> computeFrequentSet_1(ArrayList<MISValue> misValues, ArrayList<Integer> lSet){
		
		ArrayList<Sequence> frequentset1 = new ArrayList<Sequence>();
		Sequence seq = new Sequence();
		ItemSet items = new ItemSet();		
		MISValue tmpmisval = new MISValue();
		
		/*for(Integer item : lSet){
			for(MISValue mVal : misValues){
				if(mVal.getItemNo() == item){
					if(mVal.getActualSupport() >= mVal.getMinItemSupport()){
						
						items = new ItemSet();
						items.addItem(item);						
						
						seq.addItemSet(items);
						
						frequentset1.add(seq);
					}
				}
			}*/
		
		for(Integer item : lSet){
		
			tmpmisval = findMISValueByItemNo(item, misValues);
			
			if(tmpmisval.getActualSupport() >= tmpmisval.getMinItemSupport()){
				
				items = new ItemSet();
				items.addItem(item);						
				
				seq = new Sequence();
				seq.addItemSet(items);
				
				frequentset1.add(seq);				
			}			
		}
		
		return frequentset1;
	}
	
	public ArrayList<Integer> initPass(ArrayList<MISValue> misValues, String dataFilePath){		
		
		Integer minSupportItem = 0;
		Float minMIS = 0.0f;
		ArrayList<Integer> lSet = new ArrayList<Integer>(); 
		
		misValues = computeItemSupport(misValues, dataFilePath);		//Compute actual item support
		
		for(MISValue mVal : misValues){
			
			//nesting ifs to make the code readable
			
			if(minSupportItem == 0){
				if(mVal.getActualSupport() >= mVal.getMinItemSupport()){
					minSupportItem = mVal.getItemNo();
					minMIS = mVal.getMinItemSupport();
					lSet.add(minSupportItem);
				}		
			}
			else{
				if(mVal.getActualSupport() >= minMIS){
					lSet.add(mVal.getItemNo());
				}
			}	
		}
				
		return lSet;
	}
		
	public ArrayList<Sequence> generic_CandidateGeneration(ArrayList<Sequence> freqSetk_1, ArrayList<MISValue> misValues, Integer size) {
		
		ArrayList<Sequence> candidateSeq = new ArrayList<Sequence>();
		Sequence candidate = new Sequence();
		ArrayList<Integer> tmpS1 = new ArrayList<Integer>();
		ArrayList<Integer> tmpS2 = new ArrayList<Integer>();
		
		for(Integer l=0; l < freqSetk_1.size(); l++ ){
			
			for(Integer h=l; h < freqSetk_1.size(); h++){
				
				Sequence s1 = freqSetk_1.get(l);
				Sequence s2 = freqSetk_1.get(h);
				
				tmpS1 = new ArrayList<Integer>(s1.getAllItems());
				tmpS2 = new ArrayList<Integer>(s2.getAllItems());
				
				if(s1.isFirstItemLowestMIS(misValues)){
					
					if(condition1(s1,s2) &&  condition2(s1,s2,misValues)){
						
						if(s2.isSeperateItemSet(tmpS2)){
							
							candidate = new Sequence();
							
							candidate.setItemsets(s1.getItemsets());
							ItemSet lastItemSet = new ItemSet();
							lastItemSet.addItem(s2.getLastItem());
							
							candidate.addItemSet(lastItemSet);
							
							candidateSeq.add(candidate);
							
							if((size == 2 && s1.getAllItems().size() == 2) && 
									(findMISValueByItemNo(tmpS2.get(tmpS2.size() - 1),misValues).getMinItemSupport() > findMISValueByItemNo(tmpS1.get(tmpS1.size() - 1),misValues).getMinItemSupport())) {
								
								candidate = new Sequence();
								
								for(Integer i=0; i < s1.getItemsets().size(); i++){
									
									if(i == s1.getItemsets().size() - 1){
										
										ItemSet lastItemSet1 = s1.getItemsets().get(i);
										lastItemSet1.addItem(s2.getLastItem());
										
										candidate.addItemSet(lastItemSet1);
									}
									else{
										candidate.addItemSet(s1.getItemsets().get(i));
									}										
								}
								
								candidateSeq.add(candidate);
							}							
						}
						else if(((size == 2 && s1.getAllItems().size() == 2) && 
								(findMISValueByItemNo(tmpS2.get(tmpS2.size() - 1),misValues).getMinItemSupport() > findMISValueByItemNo(tmpS1.get(tmpS1.size() - 1),misValues).getMinItemSupport())) 
								|| (s1.getAllItems().size() > 2)){
							
							candidate = new Sequence();
							
							for(Integer i=0; i < s1.getItemsets().size(); i++){
								
								if(i == s1.getItemsets().size() - 1){
									
									ItemSet lastItemSet1 = s1.getItemsets().get(i);
									lastItemSet1.addItem(s2.getLastItem());
									
									candidate.addItemSet(lastItemSet1);
								}
								else{
									candidate.addItemSet(s1.getItemsets().get(i));
								}										
							}
							
							candidateSeq.add(candidate);							
						}						
					}					
				}
				else if(s2.isLastItemLowestMIS(misValues)){
					
				}
				else{					
					
					tmpS1 = new ArrayList<Integer>(s1.getAllItems());
					tmpS2 = new ArrayList<Integer>(s2.getAllItems());
					
					if(tmpS1.size() > 0 && tmpS2.size()>0){
						
						tmpS1.remove(0);
						tmpS2.remove(tmpS2.size() - 1);
						
						if(tmpS1 == tmpS2){
							
							if(s2.isSeperateItemSet(tmpS2)){
								
								candidate = new Sequence();
								
								candidate.setItemsets(s1.getItemsets());
								ItemSet lastItemSet = new ItemSet();
								lastItemSet.addItem(s2.getLastItem());
								
								candidate.addItemSet(lastItemSet);
							}
							else{
								candidate = new Sequence();
								
								for(Integer i=0; i < s1.getItemsets().size(); i++){
									
									if(i == s1.getItemsets().size() - 1){
										
										ItemSet lastItemSet = s1.getItemsets().get(i);
										lastItemSet.addItem(s2.getLastItem());
										
										candidate.addItemSet(lastItemSet);
									}
									else{
										candidate.addItemSet(s1.getItemsets().get(i));
									}										
								}
								
							}
							
						}						
					}
					
				}
				
				candidate = pruneCandidate(candidate);
				
				candidateSeq.add(candidate);
			}			
		}
		
		
		return candidateSeq;
	}
	
	private boolean condition1(Sequence s1, Sequence s2) {
		
		boolean isCondition1 = false;
		
		ArrayList<Integer> tmpS1 = new ArrayList<Integer>(s1.getAllItems());
		ArrayList<Integer> tmpS2 = new ArrayList<Integer>(s2.getAllItems());
				
		if(tmpS1.size() > 0 && tmpS2.size() > 0){
		
			Integer s1_Second = tmpS1.get(1);
			Integer s2_Last = tmpS2.get(tmpS2.size() - 1);
			
			ItemSet tmpItemSet1 = new ItemSet();
			ItemSet tmpItemSet2 = new ItemSet();
			
			for(ItemSet itemset : s1.getItemsets()){
				
				if(itemset.getItems().contains(s1_Second)){	
					
					tmpItemSet1.setItems(itemset.getItems());
					
					tmpItemSet1.getItems().remove(s1_Second);
					break;
				}
			}
			
			for(ItemSet itemset : s2.getItemsets()){
				
				if(itemset.getItems().contains(s2_Last)){					
					
					tmpItemSet2.setItems(itemset.getItems());
					
					tmpItemSet2.getItems().remove(s2_Last);
					break;					
				}				
			}
			
			if(tmpItemSet1 == tmpItemSet2)
				isCondition1 = true;
			else
				isCondition1 = false;
		}
		
		return isCondition1;
	}

	private boolean condition2(Sequence s1, Sequence s2, ArrayList<MISValue> misValues) {
		
		boolean isCondition2 = false;
		
		ArrayList<Integer> tmpS1 = new ArrayList<Integer>(s1.getAllItems());
		ArrayList<Integer> tmpS2 = new ArrayList<Integer>(s2.getAllItems());
		
		if(tmpS1.size() > 0 && tmpS2.size() > 0){
		
			//if(misValues.get(tmpS2.get(tmpS2.size() - 1)).getMinItemSupport() > misValues.get(tmpS1.get(0)).getMinItemSupport())
			if(findMISValueByItemNo(tmpS2.get(tmpS2.size() - 1),misValues).getMinItemSupport() > findMISValueByItemNo(tmpS1.get(0),misValues).getMinItemSupport())
				isCondition2 = true;
			else
				isCondition2 = false;
		}
				
		return isCondition2;
	}

	private Sequence pruneCandidate(Sequence candidate) {
		
		//TODO
		
		return null;
	}

	public ArrayList<Sequence> level2_CandidateGeneration(ArrayList<Integer> lSet, ArrayList<MISValue> misValues, Double sdc) {
		
		ArrayList<Sequence> candidate = new ArrayList<Sequence>();
		Sequence seq = new Sequence();
		ItemSet items = new ItemSet();
				
		for(Integer l=0; l < lSet.size(); l++){
			
			MISValue mVal = findMISValueByItemNo(lSet.get(l), misValues);
			
			if(mVal.getActualSupport() >= mVal.getMinItemSupport()){
				
				for(Integer h=l;  h< lSet.size(); h++){
					
					MISValue xItem = findMISValueByItemNo(lSet.get(l), misValues);
					MISValue yItem = findMISValueByItemNo(lSet.get(h), misValues);
					
					Double supportDifference = (double)(xItem.getMinItemSupport() - yItem.getMinItemSupport());
					
					if(Math.abs(supportDifference) <= sdc && yItem.getActualSupport() >= xItem.getMinItemSupport()){
						
						seq = new Sequence();
						items = new ItemSet();					//<{x,y}>
						items.addItem(xItem.getItemNo());
						items.addItem(yItem.getItemNo());						
						seq.addItemSet(items);
						candidate.add(seq);
						
						seq = new Sequence();
						items = new ItemSet();					//<{x},{y}>
						items.addItem(xItem.getItemNo());
						seq.addItemSet(items);
						
						items = new ItemSet();			
						items.addItem(yItem.getItemNo());
						seq.addItemSet(items);	
						candidate.add(seq);
					}					
				}
			}			
		}
		
		return candidate;
	}	
	
	//find MISValue object from item Id
	protected MISValue findMISValueByItemNo(Integer itemNo, ArrayList<MISValue> misValues){
		
		MISValue misVal = null;
		
		for(MISValue mVal : misValues){
			if(mVal.getItemNo() == itemNo)
				misVal = mVal;
		}
		
		return misVal;
	}
	
	//Custom comparator
	public class CustomComparator implements Comparator<MISValue> {
	    @Override
	    public int compare(MISValue o1, MISValue o2) {
	        return o1.getMinItemSupport().compareTo(o2.getMinItemSupport());
	    }
	}

	public Sequence getCandidatePrime(Sequence candidate, MISValue minMISValue) {
		
		Sequence seq = new Sequence();
		
		seq = candidate;
		
		for(ItemSet itemset : seq.getItemsets()){
			
			if(itemset.getItems().remove((minMISValue.getItemNo()))){
				break;
			}			
		}
		
		if(candidate == seq)
			seq = null;
		
		return seq;
		
		/*ItemSet itemset = null;
		
		if(candidate.getItems().remove(minMISValue.getItemNo())){
			itemset = candidate;
		}
		else{
			itemset = null;
		}		
		
		return itemset;*/
	}

	public ArrayList<Sequence> getOfSize(ArrayList<Sequence> frequentSets, int size) {
		
		ArrayList<Sequence> setOfSize = new ArrayList<Sequence>();
		
		for(Sequence seq : frequentSets){
			if(seq.getAllItems().size() == size){
				setOfSize.add(seq);
			}
		}
		
		return setOfSize;
	}

	public ArrayList<ItemSet> getAllItemSets(ArrayList<Sequence> candidate_k) {
		
		ArrayList<ItemSet> allItemSets = new ArrayList<ItemSet>();
		
		for(Sequence seq : candidate_k){
			allItemSets.addAll(seq.getItemsets());
		}
		
		return allItemSets;		
	}
		
}

