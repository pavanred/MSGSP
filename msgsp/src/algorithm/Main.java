package algorithm;

import java.util.ArrayList;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		FileHandler fileHandler;
		Msgsp objMsgsp;
		
		String dataFilePath = System.getProperty("user.dir") + "/data/data.txt";
		String parameterFilePath = System.getProperty("user.dir") + "/data/para.txt";
		
		ArrayList<Sequence> frequentSets = new ArrayList<Sequence>();		
		ArrayList<Sequence> data = null;
		ArrayList<MISValue> misValues;
		Double sdc;
						
		fileHandler = new FileHandler();
		
		//Input data 
		//data = fileHandler.getInputData(dataFilePath);
		misValues = fileHandler.getMISValues(parameterFilePath);
		sdc = fileHandler.getSDC(parameterFilePath);
		
		//DEBUG - verify input data
		//fileHandler.printData(data);
		//fileHandler.printMISValues(misValues);
		//System.out.println("SDC = " + sdc);
		
		objMsgsp = new Msgsp();	
		
		misValues = objMsgsp.sortMinSupportValue(misValues);		//Sort min support values

		ArrayList<Integer> lSet = objMsgsp.initPass(misValues, dataFilePath);		//Init Pass - compute L

		ArrayList<Sequence> freqSet1 = objMsgsp.computeFrequentSet_1(misValues, lSet);		//Compute Frequent Set 1 - F1
		frequentSets.addAll(freqSet1);
		
		ArrayList<Sequence> freqSetk_1;
		ArrayList<Sequence> candidate_k;
		
		MISValue minMISValue = misValues.get(0); //sorted. First item has minMISValue
		
		
		for(int k=2; (freqSetk_1 = objMsgsp.getOfSize(frequentSets, k-1)).size() > 0 ; k++){	//k,k-1
			
			candidate_k = new ArrayList<Sequence>();
			
			if(k==2){
				candidate_k = objMsgsp.level2_CandidateGeneration(lSet, misValues, sdc);				
			}
			else{
				candidate_k = objMsgsp.generic_CandidateGeneration(freqSetk_1, misValues, k-1);
			}
			
			//DEBUG
			//fileHandler.printSequence(candidate_k);
			//fileHandler.printData(candidate_k);
			
			Sequence seq = fileHandler.getNextSequence(true, dataFilePath);
			Sequence candidate_prime = new Sequence();
			Integer seqCount = 0;
			
			while(seq != null){
				
				seqCount = seqCount + 1;
				
				for(Sequence candidate : candidate_k){ 
					
					if(candidate.containedIn(seq)){
						candidate.incrementCount();
					}
					
					candidate_prime = objMsgsp.getCandidatePrime(candidate, minMISValue);
					
					if(candidate_prime != null){				//check if candidate doesn't contain minMISValue item
						if(candidate_prime.containedIn(seq)){
							candidate_prime.incrementCount();
						}
					}
				}
				
				seq = fileHandler.getNextSequence(false, null);
			}
			
			ArrayList<Sequence> frequentSet_k = new ArrayList<Sequence>();
			
			/*for(ItemSet itemset : objMsgsp.getAllItemSets(candidate_k)){
				if(((float)itemset.getCount()/seqCount) >= minMISValue.getMinItemSupport()){
					frequentSet_k.addItemSet(itemset);
				}
			}*/
			
			for(Sequence tmpseq : candidate_k){
				
				if(((float)tmpseq.getCount()/seqCount) >= minMISValue.getMinItemSupport())
					frequentSet_k.add(tmpseq);
			}
			
			frequentSets.addAll(frequentSet_k);
		}
						
		//DEBUG
		fileHandler.printFrequentSets(frequentSets);

		fileHandler.writeOutputFile();				//Write the output to file
		
		System.out.println("Process Complete");		
	}
}
