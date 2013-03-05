package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Msgsp {
	
	private ArrayList<Item> allItems;
	private String dataFilePath;
	private float SDC;
	
	/**
     * MSGSP constructor
     * @param allItems list of all items 
     * @param dataFilePath path of the data file
     * @param sdc support difference constraint
     */
    protected Msgsp(ArrayList<Item> allItems, String dataFilePath, float sdc){
    	
    	this.allItems = allItems;
    	this.dataFilePath = dataFilePath;
    	this.SDC = sdc;
    }  

    /**
     * MSGSP algorithm implementation
     * @return list of association rule sequences
     */
    public ArrayList<Sequence> main(){
    	
    	FileHandler fileHandler = new FileHandler();
    	ArrayList<Integer> seedSet;
    	ArrayList<Sequence> freqSet1;
    	ArrayList<Sequence> frequentSeq = new ArrayList<Sequence>();
    	ArrayList<Sequence> freqSetk_1;
		ArrayList<Sequence> candidate_k;
    	
    	sortMinSupportValue();		//M
    	seedSet = initPass();		//L
    	freqSet1 = computeFrequentSet_1(seedSet);		//F1
    	
    	frequentSeq.addAll(freqSet1);
    	
    	for(int k=2; (freqSetk_1 = Utilities.getOfSize(frequentSeq, k-1)).size() > 0 ; k++){	//k,k-1
    		
    		candidate_k = new ArrayList<Sequence>();
    		
    		if(k==2){
				candidate_k = level2_CandidateGeneration(seedSet, frequentSeq);				
			}
			else{
				candidate_k = generic_CandidateGeneration(freqSetk_1, k-1);
			}
    		    		   		
    		Sequence seq = fileHandler.getNextSequence(true, this.dataFilePath);

			int seqCount = 0;
			
			while(seq != null){
				
				seqCount = seqCount + 1;
												
				for(Sequence candidate : candidate_k){ 
				
					if(seq.contains(candidate)){
						candidate.incrementCount();
					}
				}		
				
				seq = fileHandler.getNextSequence(false, null);
			}
			
			ArrayList<Sequence> frequentSet_k = new ArrayList<Sequence>();
			
			for(Sequence tmpseq : candidate_k){
				
				if(((float)tmpseq.getCount()/seqCount) >= tmpseq.getMinMIS(this.allItems))
					frequentSet_k.add(tmpseq);
			}
			
    		frequentSeq.addAll(frequentSet_k);     
    	}
    	
    	//return Utilities.removeDuplicates(frequentSeq);
    	return frequentSeq;
    }
    
    /**
     * Generates candidates for any level >2
     * @param freqSetk_1 - k-1th frequent set
     * @param size - size of the sequence
     * @return candidate sequences
     */
    public ArrayList<Sequence> generic_CandidateGeneration(ArrayList<Sequence> freqSetk_1, Integer size){
    	
    	ArrayList<Sequence> candidateSeq = new ArrayList<Sequence>();
    	ArrayList<Sequence> prundedCandidates = new ArrayList<Sequence>();
		Sequence candidate = new Sequence();
		ArrayList<Integer> tmpS1 = new ArrayList<Integer>();
		ArrayList<Integer> tmpS2 = new ArrayList<Integer>();
		
		for(Integer l=0; l < freqSetk_1.size(); l++ ){
			
			for(Integer h=0; h < freqSetk_1.size(); h++){
				
				//if(l != h){
				
					Sequence s1 = freqSetk_1.get(l);
					Sequence s2 = freqSetk_1.get(h);
					
					tmpS1 = new ArrayList<Integer>(s1.getAllItems());
					tmpS2 = new ArrayList<Integer>(s2.getAllItems());
					
					if(s1.isFirstItemLowestMIS(this.allItems)){						
						
						candidateSeq.addAll(joinStep_FirstLowest(s1, s2, tmpS1, tmpS2, size));						
					}
					else if(s2.isLastItemLowestMIS(this.allItems)){
						
						candidateSeq.addAll(joinStep_LastLowest(s1, s2, tmpS1, tmpS2, size));	
					}
					else{	
						
						if((candidate = joinStep(s1, s2)) != null)
							candidateSeq.add(candidate);
					}						
				//}				
			}			
		}
		
		prundedCandidates = pruneCandidates(candidateSeq, freqSetk_1);
    	
    	return prundedCandidates;
    }
    
    /**
     * prunes the candidates
     * @param candidateSeq - candidate sequence to be pruned
     * @param freqSetk_1 - k-1th frequent set
     * @param size - size of the sequence
     * @return pruned candidates
     */
    private ArrayList<Sequence> pruneCandidates(ArrayList<Sequence> candidateSeq, ArrayList<Sequence> freqSetk_1) {

    	ArrayList<Sequence> prunedSeq = new ArrayList<Sequence>();
    	ArrayList<Sequence> subSeq =  new ArrayList<Sequence>();    	
    	
    	int size = 0;
    	
    	if(freqSetk_1.size() > 0){
    		size = freqSetk_1.get(0).getAllItems().size();
    	}
    	
    	for(Sequence seq : candidateSeq){
    		    		
    		if(seq.getAllDistinctItems().contains(this.allItems.get(0))){
    			
    			ArrayList<ItemSet> seeds = Utilities.getsubSequences(seq);     			
    			
    			for(int i=0; i < seeds.size(); i++){
    				
    				int pointer = 0;
    				
    				if(i != pointer){
    				
	    				Sequence newseq = new Sequence();
	    				
	    				while(newseq.getAllItems().size() < size && pointer < seeds.size()){
	    					
	    					ItemSet is = new ItemSet();
	    					is.setItems(new ArrayList<Integer>(seeds.get(pointer).getItems()));
	    					newseq.addItemSet(is);   
	    					
	    					pointer = pointer + 1;
	    					
	    					if(newseq.getAllItems().size() == size){
	    						subSeq.add(newseq);
	    					}
	    				} 
    				}
    			}

    			int count = 0;
    			
    			for(int j=0; j<subSeq.size() ; j++){
    				
    				if(Utilities.ListContains(freqSetk_1, subSeq.get(j))){    				
    					count = count++;
    				}    				
    			}
    			
    			if(count == subSeq.size()){
    				if(!Utilities.ListHas(prunedSeq, seq))
    					prunedSeq.add(seq);
    			}
    		}
    		else{
    			if(!Utilities.ListHas(prunedSeq, seq))
    				prunedSeq.add(seq);
    		}    		
    	}
    	
		return prunedSeq;
	}

	/**
     * Generates level 2 candidates
     * @param seedSet - seeds generated L
     * @param frequentSeq - F1
     * @return level 2 candidates
     */
    public ArrayList<Sequence> level2_CandidateGeneration(ArrayList<Integer> seedSet, ArrayList<Sequence> frequentSeq){
    	
    	ArrayList<Sequence> candidate = new ArrayList<Sequence>();
		
        for (int i=0; i< seedSet.size();i++){    
        	
             for (int j=i;j< seedSet.size();j++){
            	 
            	 if(i != j){
            		 
	            	 Item itemi = Utilities.getItemByItemNo(seedSet.get(i), this.allItems);
	            	 Item itemj = Utilities.getItemByItemNo(seedSet.get(j), this.allItems);   
	            	 	
	            	float supportDifference =(itemj.getActualSupport() - itemi.getActualSupport());
	            	 
	            	 Item minItem;
	            	 
	            	 if(itemi.getMinItemSupport() <= itemj.getMinItemSupport())
	            		 minItem = itemi;
	            	 else
	            		 minItem = itemj;
	            	 
	            	 //if(( itemi.getActualSupport() >= itemi.getMinItemSupport() && itemj.getActualSupport() >= itemj.getMinItemSupport()) && Math.abs(supportDifference) <= this.SDC)  {
	            	 if( minItem.getActualSupport() >= minItem.getMinItemSupport() && Math.abs(supportDifference) <= this.SDC)  {
	                	                		 
	                	 Sequence seq1 = new Sequence();         
		                 ItemSet is1 = new ItemSet();         
		                 is1.addItem(itemi.getItemNo()); 	                     
		                 seq1.addItemSet(is1);                
		                     
		                 ItemSet is2 = new ItemSet();         
		                 is2.addItem(itemj.getItemNo());     
		                 seq1.addItemSet(is2);                
		                        
		                 candidate.add(seq1);                         //<{x},{y}> 
	
	                     Sequence seq2 = new Sequence();         
		                 ItemSet is3 = new ItemSet();        
		                 is3.addItem(itemi.getItemNo()); 
		                 is3.addItem(itemj.getItemNo());     
		                 seq2.addItemSet(is3);                //<{x,y}>
		
		                 candidate.add(seq2); 
	            	 }
	             }
             }        
        }
        
        //candidate = level2_pruneCandidate(candidate,frequentSeq);
        candidate = pruneCandidates(candidate,frequentSeq);
        
    	return candidate;
    }
    
    /**
     * join step when the first item in the sequence has the lowest minimum item support
     * @param s1 - sequence 1
     * @param s2 - sequence 2
     * @param tmpS1 - temporary S1
     * @param tmpS2 - temporary S2
     * @param size- size of the sequence
     * @return candidate
     */
    private ArrayList<Sequence> joinStep_LastLowest(Sequence s1, Sequence s2,ArrayList<Integer> tmpS1, ArrayList<Integer> tmpS2, int size){

    	Sequence candidate = new Sequence();
		ArrayList<Sequence> candidates = new ArrayList<Sequence>();
		float supportDifference = 0.0f;
		
		if(condition1(s1,s2) &&  condition2(s1,s2)){
		
			if(s1.getItemsets().get(0).getItems().size() == 1){
				
				candidate = new Sequence();
				
				ItemSet firstItemSet = new ItemSet();
				ArrayList<Integer> newfirstItem = new ArrayList<Integer>();
				newfirstItem.add(new Integer(s2.getLastItem()));
				
				firstItemSet.setItems(newfirstItem);
				
				candidate.addItemSet(firstItemSet);
				
				for(ItemSet is : s1.getItemsets()){
					ItemSet newItemSet = new ItemSet();
					ArrayList<Integer> newItems = new ArrayList<Integer>(is.getItems());
					newItemSet.setItems(newItems);
					candidate.addItemSet(newItemSet);
				}
				
				supportDifference = Utilities.getSupportDifference(firstItemSet, this.allItems);
				
				//if(Utilities.getSupportDifference(candidate, this.allItems) <= this.SDC){
				if(supportDifference <= this.SDC){
						candidates.add(candidate);
				}				
				
				if((s2.getlength() == 2 && s2.getSize() == 2) && 
						(Utilities.getItemByItemNo(tmpS1.get(0), this.allItems).getMinItemSupport() > Utilities.getItemByItemNo(tmpS2.get(0), this.allItems).getMinItemSupport())) {
					
					candidate = new Sequence();
					
					for(int i=0; i<s2.getItemsets().size();i++){
						ItemSet newItemSet = new ItemSet();
						
						ArrayList<Integer> newItems = new ArrayList<Integer>();
						
						if(i == 0){
							newItems = new ArrayList<Integer>();
							newItems.add(new Integer(s1.getAllItems().get(0)));
							newItems.addAll(s2.getItemsets().get(i).getItems());
						}
						else{
							newItems = new ArrayList<Integer>(s2.getItemsets().get(i).getItems());
						}
							
						newItemSet.setItems(newItems);
						candidate.addItemSet(newItemSet);
					}
														
					//if(Utilities.getSupportDifference(candidate, this.allItems) <= this.SDC){
					if(supportDifference <= this.SDC){
							candidates.add(candidate);
					}
				}					
			}
			else if(((s2.getlength() == 2 && s2.getSize() == 1) && 
					(Utilities.getItemByItemNo(tmpS1.get(tmpS1.size() - 1), this.allItems).getMinItemSupport() > Utilities.getItemByItemNo(tmpS2.get(tmpS2.size() - 1), this.allItems).getMinItemSupport())) 
					|| (s2.getlength() > 2)){
				
				candidate = new Sequence();
				
				for(int i=0; i<s2.getItemsets().size();i++){
					ItemSet newItemSet = new ItemSet();
					
					ArrayList<Integer> newItems = new ArrayList<Integer>();
					
					if(i == 0){
						newItems = new ArrayList<Integer>();
						newItems.add(new Integer(s1.getAllItems().get(0)));
						newItems.addAll(s2.getItemsets().get(i).getItems());
					}
					else{
						newItems = new ArrayList<Integer>(s2.getItemsets().get(i).getItems());
					}
						
					newItemSet.setItems(newItems);
					candidate.addItemSet(newItemSet);
				}
													
				//if(Utilities.getSupportDifference(candidate, this.allItems) <= this.SDC){
				if(supportDifference <= this.SDC){
						candidates.add(candidate);
				}
			}
			
		}		
		
    	return candidates;
    }
    
    /**
     * join step when the last item in the sequence has the lowest minimum item support
     * @param s1 - sequence 1
     * @param s2 - sequence 2
     * @return candidate
     */
    private ArrayList<Sequence> joinStep_FirstLowest(Sequence s1, Sequence s2,ArrayList<Integer> tmpS1, ArrayList<Integer> tmpS2, int size){

		Sequence candidate = new Sequence();
		ArrayList<Sequence> candidates = new ArrayList<Sequence>();
		float supportDifference = 0.0f;
		
		if(condition1(s1,s2) &&  condition2(s1,s2)){
		
			if(s2.getItemsets().get(s2.getItemsets().size() - 1).getItems().size() == 1){
				
				candidate = new Sequence();
				
				for(ItemSet is : s1.getItemsets()){
					ItemSet newItemSet = new ItemSet();
					ArrayList<Integer> newItems = new ArrayList<Integer>(is.getItems());
					newItemSet.setItems(newItems);
					candidate.addItemSet(newItemSet);
				}
				
				ItemSet lastItemSet = new ItemSet();
				ArrayList<Integer> newLastItem = new ArrayList<Integer>();
				newLastItem.add(new Integer(s2.getLastItem()));

				lastItemSet.setItems(newLastItem);
				
				candidate.addItemSet(lastItemSet);
				
				supportDifference = Utilities.getSupportDifference(lastItemSet, this.allItems);
				
				//if(Utilities.getSupportDifference(candidate, this.allItems) <= this.SDC){
				if(supportDifference <= this.SDC){
						candidates.add(candidate);
				}				
				
				//if((s1.getItemsets().size() == 2 && s1.getAllItems().size() == 2) &&
				if((s1.getSize() == 2 && s1.getlength() == 2) &&
						(Utilities.getItemByItemNo(tmpS2.get(tmpS2.size() - 1), this.allItems).getMinItemSupport() > Utilities.getItemByItemNo(tmpS1.get(tmpS1.size() - 1), this.allItems).getMinItemSupport())) {
					
					candidate = new Sequence();
					
					for(ItemSet is : s1.getItemsets()){
						ItemSet newItemSet = new ItemSet();
						ArrayList<Integer> newItems = new ArrayList<Integer>(is.getItems());
						newItemSet.setItems(newItems);
						candidate.addItemSet(newItemSet);
					}
					
					candidate.getItemsets().get(candidate.getItemsets().size() - 1).addItem(new Integer(s2.getLastItem()));
					
					supportDifference = Utilities.getSupportDifference(candidate.getItemsets().get(candidate.getItemsets().size() - 1), this.allItems);
														
					//if(Utilities.getSupportDifference(candidate, this.allItems) <= this.SDC){
					if(supportDifference <= this.SDC){
							candidates.add(candidate);
					}
				}					
			}
			//else if(((s1.getItemsets().size() == 2 && s1.getAllItems().size() == 1) &&
			else if(((s1.getlength() == 2 && s1.getSize() == 1) &&
					(Utilities.getItemByItemNo(tmpS2.get(tmpS2.size() - 1), this.allItems).getMinItemSupport() > Utilities.getItemByItemNo(tmpS1.get(tmpS1.size() - 1), this.allItems).getMinItemSupport())) 
					|| (s1.getlength() > 2)){  //|| (s1.getItemsets().size() > 2)){
				
				candidate = new Sequence();
				
				for(ItemSet is : s1.getItemsets()){
					ItemSet newItemSet = new ItemSet();
					ArrayList<Integer> newItems = new ArrayList<Integer>(is.getItems());
					newItemSet.setItems(newItems);
					candidate.addItemSet(newItemSet);
				}
				
				candidate.getItemsets().get(candidate.getItemsets().size() - 1).addItem(new Integer(s2.getLastItem()));
					
				supportDifference = Utilities.getSupportDifference(candidate.getItemsets().get(candidate.getItemsets().size() - 1), this.allItems);
				
				//if(Utilities.getSupportDifference(candidate, this.allItems) <= this.SDC){
				if(supportDifference <= this.SDC){
						candidates.add(candidate);
				}
			}
			
		}		
		
    	return candidates;
    }
    
    /**
     * Generic join step
     * @param s1 - sequence 1
     * @param s2 - sequence 2
     * @return candidate
     */
    private Sequence joinStep(Sequence s1, Sequence s2){
    	
		ArrayList<Integer> tmpS1;
		ArrayList<Integer> tmpS2;
		Sequence candidate = new Sequence();
		float supportDifference = 0.0f;
    	
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
					
					supportDifference = Utilities.getSupportDifference(lastItemSet, this.allItems);
				}
				else{
					candidate = new Sequence();
					
					for(Integer i=0; i < s1.getItemsets().size(); i++){
						
						if(i == s1.getItemsets().size() - 1){
							
							ItemSet lastItemSet = s1.getItemsets().get(i);
							lastItemSet.addItem(s2.getLastItem());
							
							candidate.addItemSet(lastItemSet);
							
							supportDifference = Utilities.getSupportDifference(lastItemSet, this.allItems);
						}
						else{
							candidate.addItemSet(s1.getItemsets().get(i));
						}										
					}					
				}
				
				//if(Utilities.getSupportDifference(candidate, this.allItems) > this.SDC)
				if(supportDifference <= this.SDC)
					candidate = null;
			}			
		}
    	
    	return candidate;
    }
    
    /**
     * Condition 1 in join step
     * @param s1 - sequence 1
     * @param s2 - sequence 2
     * @return true or false
     */
    private boolean condition1(Sequence s1, Sequence s2) {
		
		boolean isCondition1 = false;
		
		ArrayList<Integer> tmpS1 = new ArrayList<Integer>(s1.getAllItems());
		ArrayList<Integer> tmpS2 = new ArrayList<Integer>(s2.getAllItems());
				
		if(tmpS1.size() > 1 && tmpS2.size() > 0){
			
			tmpS1.remove(1);
			tmpS2.remove(tmpS2.size() - 1);
			
			//if(tmpS1 == tmpS2)
			if(tmpS1.equals(tmpS2))
				isCondition1 = true;
			else
				isCondition1 = false;			
		}
		
		return isCondition1;
	}

    /**
     * Condition 2 in join step
     * @param s1 - sequence 1
     * @param s2 - sequence 2
     * @return true or false
     */
	private boolean condition2(Sequence s1, Sequence s2) {
		
		boolean isCondition2 = false;
		
		ArrayList<Integer> tmpS1 = new ArrayList<Integer>(s1.getAllItems());
		ArrayList<Integer> tmpS2 = new ArrayList<Integer>(s2.getAllItems());
		
		if(tmpS1.size() > 0 && tmpS2.size() > 0){
		
			//if(misValues.get(tmpS2.get(tmpS2.size() - 1)).getMinItemSupport() > misValues.get(tmpS1.get(0)).getMinItemSupport())
			if(Utilities.getItemByItemNo(tmpS2.get(tmpS2.size() - 1), this.allItems).getMinItemSupport() > Utilities.getItemByItemNo(tmpS1.get(0), this.allItems).getMinItemSupport())
				isCondition2 = true;
			else
				isCondition2 = false;
		}
				
		return isCondition2;
	}
    
    /**
     * Pruning candidates
     * @param candidate - sequence of k=2 candidates generated
     * @param frequentSeq - F1
     * @return level 2 candidates
     */
    @SuppressWarnings("unused")
	private ArrayList<Sequence> level2_pruneCandidate(ArrayList<Sequence> candidate, ArrayList<Sequence> frequentSeq) {
		
    	ArrayList<Integer> frequentFlag = new ArrayList<Integer>();
    	ArrayList<Sequence> pruned_candidate = new ArrayList<Sequence>();
    		
    	for(Sequence seq : candidate){
    	    		
    		for(Sequence frSeq : frequentSeq){
    			
    			if(frequentFlag.size() < 2){
    			
	    			if(frSeq.getAllItems().contains(seq.getAllItems().get(0)))
	    				frequentFlag.add(0);
	    			
	    			if(frSeq.getAllItems().contains(seq.getAllItems().get(1)))
	    				frequentFlag.add(1);
    			}
    			else{
    				pruned_candidate.add(seq);
    				frequentFlag = new ArrayList<Integer>();
    			}
     		}
    		
    		frequentFlag = new ArrayList<Integer>();
    	}
    	
		return pruned_candidate;
	}

	/**
     * Compute the frequent set 1 from the seed set L and 
     * go through the data to update the counts of each set
     * @return list of association rule sequences
     */
    public ArrayList<Sequence> computeFrequentSet_1(ArrayList<Integer> seedSet){
		
		ArrayList<Sequence> frequentset1 = new ArrayList<Sequence>();
		Sequence seq = new Sequence();
		ItemSet items = new ItemSet();		
		Item tmpItm = new Item();
		
		for(Integer item : seedSet){
		
			tmpItm = Utilities.getItemByItemNo(item, this.allItems);
			
			if(tmpItm.getActualSupport() >= tmpItm.getMinItemSupport()){
				
				items = new ItemSet();
				items.addItem(item);						
				
				seq = new Sequence();
				seq.addItemSet(items);
				
				frequentset1.add(seq);				
			}			
		}
		
		//Update counts
		
		Sequence sequence; 
		FileHandler fileHandler = new FileHandler();		
		Integer seqCount = 0;
		
		sequence = fileHandler.getNextSequence(true, dataFilePath);

		while(sequence != null){	
				
			seqCount = seqCount + 1;
				
			for(Sequence candidate : frequentset1){ 
				
				if(candidate != null && sequence != null){
					
					if(sequence.getAllItems().containsAll(candidate.getAllItems())){
						
						candidate.incrementCount();
					}					
				}
			}
			
			sequence = fileHandler.getNextSequence(false, null);
		}
		
		return frequentset1;
	}
    
    /**
     * Initial pass over the data to generate the seed set L
     * @return list of item numbers
     */
    public ArrayList<Integer> initPass(){		
		
		Integer minSupportItem = 0;
		Float minMIS = 0.0f;
		ArrayList<Integer> seedSet = new ArrayList<Integer>(); 
		
		computeActualItemSupport();		//Compute actual item support
		
		for(Item itm : this.allItems){
			
			if(minSupportItem == 0){
				if(itm.getActualSupport() >= itm.getMinItemSupport()){
					minSupportItem = itm.getItemNo();
					minMIS = itm.getMinItemSupport();
					seedSet.add(minSupportItem);
				}		
			}
			else{
				if(itm.getActualSupport() >= minMIS){
					seedSet.add(itm.getItemNo());
				}
			}	
		}
				
		return seedSet;
	}

    /**
     * Compute the actual item supports from the data
     */
    public void computeActualItemSupport(){		
		
    	Integer sequenceCount = 0;		
		FileHandler fileHandler = new FileHandler();
		
		Sequence seq = fileHandler.getNextSequence(true, dataFilePath);
		
		while(seq != null){
			
			sequenceCount = sequenceCount + 1;
			
			for(Integer item : seq.getAllDistinctItems()){
				
				for(Item itm : this.allItems){
					
					if(itm.getItemNo() == item){
						
						itm.setSupportCount(itm.getSupportCount() + 1);
					}
				}
			}
			
			seq = fileHandler.getNextSequence(false, null);
		}
		
		for(Item itm : this.allItems){
			
			itm.setActualSupport((float)itm.getSupportCount()/sequenceCount);
		}
	}
    
    /**
     * Sort Items based on minimum support values
     */
    public void sortMinSupportValue(){		
		
	    Collections.sort(this.allItems, new ItemComparator());
	}
    
    /**
     * Custom comparator for Items based on MIS   
     */
  	public class ItemComparator implements Comparator<Item> {
  	    @Override
  	    public int compare(Item o1, Item o2) {
  	        return o1.getMinItemSupport().compareTo(o2.getMinItemSupport());
  	    }
  	}
}