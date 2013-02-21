package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Msgsp {
	
	private ArrayList<Item> allItems;
	private String dataFilePath;
	private float SDC;
	
	/*
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

    /*
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
				//candidate_k = objMsgsp.generic_CandidateGeneration(freqSetk_1, misValues, k-1, sdc);
			}
    		
    		Sequence seq = fileHandler.getNextSequence(true, this.dataFilePath);

			int seqCount = 0;
			
			while(seq != null){
				
				seqCount = seqCount + 1;
				
				for(Sequence candidate : candidate_k){ 

					if(candidate.containedIn(seq)){
						candidate.incrementCount();
					}				
				}		
				
				seq = fileHandler.getNextSequence(false, null);
			}
			
			ArrayList<Sequence> frequentSet_k = new ArrayList<Sequence>();
			
			for(Sequence tmpseq : candidate_k){				

				if(((float)tmpseq.getCount()/seqCount) > tmpseq.getMinMIS(this.allItems))
					frequentSet_k.add(tmpseq);				
			}
    		
    		frequentSeq.addAll(frequentSet_k);    	
    	}
    	
    	return frequentSeq;
    }
    
    /*
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
	            	 
	            	 if(( itemi.getActualSupport() >= itemi.getMinItemSupport() && itemj.getActualSupport() >= itemj.getMinItemSupport()) && Math.abs(supportDifference) <= this.SDC)  {
	                	                		 
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
        
        candidate = level2_pruneCandidate(candidate,frequentSeq);
        
    	return candidate;
    }
    
    /*
     * Pruning candidates
     * @param candidate - sequence of k=2 candidates generated
     * @param frequentSeq - F1
     * @return level 2 candidates
     */
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

	/*
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
    
    /*
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

    /*
     * Compute the actual item supports from the data
     */
    public void computeActualItemSupport(){		
		
    	Integer sequenceCount = 0;		
		FileHandler fileHandler = new FileHandler();
		
		Sequence seq = fileHandler.getNextSequence(true, dataFilePath);
		
		while(seq != null){
			
			sequenceCount = sequenceCount + 1;
			
			for(Integer item : seq.getAllItems()){
				
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
    
    /*
     * Sort Items based on minimum support values
     */
    public void sortMinSupportValue(){		
		
	    Collections.sort(this.allItems, new ItemComparator());
	}
    
    /*
     * Custom comparator for Items based on MIS   
     */
  	public class ItemComparator implements Comparator<Item> {
  	    @Override
  	    public int compare(Item o1, Item o2) {
  	        return o1.getMinItemSupport().compareTo(o2.getMinItemSupport());
  	    }
  	}
}