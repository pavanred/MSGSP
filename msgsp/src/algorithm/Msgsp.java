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
		
	public ArrayList<Sequence> generic_CandidateGeneration(ArrayList<Sequence> freqSetk_1, ArrayList<MISValue> misValues, Integer size, Double sdc) {
		
		ArrayList<Sequence> candidateSeq = new ArrayList<Sequence>();
		Sequence candidate = new Sequence();
		ArrayList<Integer> tmpS1 = new ArrayList<Integer>();
		ArrayList<Integer> tmpS2 = new ArrayList<Integer>();
		
		for(Integer l=0; l < freqSetk_1.size(); l++ ){
			
			for(Integer h=l; h < freqSetk_1.size(); h++){
				
				if(l != h){
				
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
								
								if(getSupportDifference(candidate, misValues) <= sdc)			
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
																		
									//if(getSupportDifference(candidate, misValues) <= sdc)			
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
								
								if(getSupportDifference(candidate, misValues) <= sdc)				
									candidateSeq.add(candidate);							
							}						
						}					
					}
					else if(s2.isLastItemLowestMIS(misValues)){
						
						if(s1.isSeperateItemSet(tmpS1)){
							
							candidate = new Sequence();
							
							candidate.setItemsets(s2.getItemsets());
							ItemSet lastItemSet = new ItemSet();
							lastItemSet.addItem(s1.getAllItems().get(0));
							
							candidate.addItemSet(lastItemSet);
							
							if(getSupportDifference(candidate, misValues) <= sdc)			
								candidateSeq.add(candidate);
							
							if((size == 2 && s1.getAllItems().size() == 2) && 
									(findMISValueByItemNo(tmpS1.get(0),misValues).getMinItemSupport() > findMISValueByItemNo(tmpS2.get(0),misValues).getMinItemSupport())) {
								
								candidate = new Sequence();
								
								for(Integer i=0; i < s2.getItemsets().size(); i++){
									
									if(i == s2.getItemsets().size() - 1){
										
										ItemSet lastItemSet1 = s2.getItemsets().get(i);
										lastItemSet1.addItem(s1.getLastItem());
										
										candidate.addItemSet(lastItemSet1);
									}
									else{
										candidate.addItemSet(s2.getItemsets().get(i));
									}										
								}
																	
								if(getSupportDifference(candidate, misValues) <= sdc)			
									candidateSeq.add(candidate);
							}							
						}
						else if(((size == 2 && s1.getAllItems().size() == 2) && 
								(findMISValueByItemNo(tmpS1.get(0),misValues).getMinItemSupport() > findMISValueByItemNo(tmpS2.get(0),misValues).getMinItemSupport())) 
								|| (s1.getAllItems().size() > 2)){
							
							candidate = new Sequence();
							
							for(Integer i=0; i < s2.getItemsets().size(); i++){
								
								if(i == s2.getItemsets().size() - 1){
									
									ItemSet lastItemSet1 = s2.getItemsets().get(i);
									lastItemSet1.addItem(s1.getLastItem());
									
									candidate.addItemSet(lastItemSet1);
								}
								else{
									candidate.addItemSet(s2.getItemsets().get(i));
								}										
							}
							
							if(getSupportDifference(candidate, misValues) <= sdc)				
								candidateSeq.add(candidate);							
						}						
						
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
								
								if(getSupportDifference(candidate, misValues) <= sdc)						
									candidateSeq.add(candidate);
							}						
						}					
					}
				}
			}			
		}
		
		candidateSeq = pruneCandidates(candidateSeq, freqSetk_1, size);
		
		return candidateSeq;
	}
	
	private Double getSupportDifference(Sequence candidate, ArrayList<MISValue> misValues) {
		
		Float minMIS = misValues.get(misValues.size() - 1).getMinItemSupport();
		Float maxMIS = 0.0f;
		Float tmp = 0.0f;
		
		for(Integer item : candidate.getAllItems()){
			if((tmp = findMISValueByItemNo(item, misValues).getMinItemSupport()) < minMIS)
				minMIS = tmp;
			
			if((tmp = findMISValueByItemNo(item, misValues).getMinItemSupport()) > maxMIS)
				maxMIS = tmp;
		}
		
		return (double) (maxMIS - minMIS);		
	}

	private boolean condition1(Sequence s1, Sequence s2) {
		
		boolean isCondition1 = false;
		
		ArrayList<Integer> tmpS1 = new ArrayList<Integer>(s1.getAllItems());
		ArrayList<Integer> tmpS2 = new ArrayList<Integer>(s2.getAllItems());
				
		if(tmpS1.size() > 1 && tmpS2.size() > 0){
			
			tmpS1.remove(1);
			tmpS2.remove(tmpS2.size() - 1);
			
			if(tmpS1 == tmpS2)
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

	private ArrayList<Sequence> pruneCandidates(ArrayList<Sequence> candidateSeq, ArrayList<Sequence> freqSetk_1, Integer size) {
		
		
			
		
		return candidateSeq;
	}

	public ArrayList<Sequence> level2_CandidateGeneration(ArrayList<Integer> lSet, ArrayList<MISValue> misValues, Double sdc) {
		
		ArrayList<Sequence> candidate = new ArrayList<Sequence>();
		Sequence seq = new Sequence();
		ItemSet items = new ItemSet();
				
		for(Integer l=0; l < lSet.size(); l++){
			
			MISValue mVal = findMISValueByItemNo(lSet.get(l), misValues);
			
			if(mVal.getActualSupport() >= mVal.getMinItemSupport()){
				
				for(Integer h=l;  h< lSet.size(); h++){
					
					if(l != h){
					
						MISValue xItem = findMISValueByItemNo(lSet.get(l), misValues);
						MISValue yItem = findMISValueByItemNo(lSet.get(h), misValues);
						
						Double supportDifference = (double)(xItem.getMinItemSupport() - yItem.getMinItemSupport());
						
						if(Math.abs(supportDifference) <= sdc && yItem.getActualSupport() >= xItem.getMinItemSupport()){
							
							seq = new Sequence();
							items = new ItemSet();					//<{x,y}>
							items.addItem(xItem.getItemNo());
							items.addItem(yItem.getItemNo());	
							
							seq.addItemSet(items);
							//if(getSupportDifference(seq, misValues) <= sdc)			
								candidate.add(seq);
							
							seq = new Sequence();
							items = new ItemSet();					//<{x},{y}>
							items.addItem(xItem.getItemNo());
							seq.addItemSet(items);
							
							items = new ItemSet();			
							items.addItem(yItem.getItemNo());
							seq.addItemSet(items);	
							
							//if(getSupportDifference(seq, misValues) <= sdc)			
								candidate.add(seq);
						}
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

	public Sequence getCandidatePrime(Sequence candidate, ArrayList<MISValue> misValues, ArrayList<Sequence> candSeqs) {
		
		Sequence newseq = null;
		
		MISValue minMISval = new MISValue();
		Float minMIS = misValues.get(misValues.size() - 1).getMinItemSupport();
		
		for(int i = 0; i < candidate.getAllItems().size(); i++){
			
			MISValue tmpMIS = findMISValueByItemNo(candidate.getAllItems().get(i), misValues);
			
			if(tmpMIS.getMinItemSupport() < minMIS){
				minMIS = tmpMIS.getMinItemSupport();	
				minMISval = tmpMIS;
			}
		}	
		
		ArrayList<Integer> x = new ArrayList<Integer>(candidate.getAllItems());
		
		if(x.remove(minMISval.getItemNo())){
			
			for(Sequence s : candSeqs){
				
				if(s.getAllItems().containsAll(x)){
					newseq = s;
					break;
				}				
			}
			
		}
		else
			newseq = null;
		
		return newseq;
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

