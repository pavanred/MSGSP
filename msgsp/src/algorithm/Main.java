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
		frequentSets.add(new Sequence());		//leaving 0 empty
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

		Sequence freqSet1 = objMsgsp.computeFrequentSet_1(misValues, lSet);		//Compute Frequent Set 1 - F1
		frequentSets.add(freqSet1);
		
		Sequence freqSetk_1;
		Sequence candidate_k;
		
		MISValue minMISValue = misValues.get(0); //sorted. First item has minMISValue
		
		
		for(int k=2; (freqSetk_1 = frequentSets.get(k-1)).isNotEmpty() ; k++){	//k,k-1
			
			candidate_k = new Sequence();
			
			if(k==2){
				candidate_k = objMsgsp.level2_CandidateGeneration(lSet, misValues, sdc);
			}
			else{
				candidate_k = objMsgsp.generic_CandidateGeneration(freqSetk_1);
			}
			
			//DEBUG
			//fileHandler.printSequence(candidate_k);
			
			Sequence seq = fileHandler.getNextSequence(true, dataFilePath);
			ItemSet candidate_prime = new ItemSet();
			boolean minMISItemRemoved = false;
			Integer seqCount = 0;
			
			while(seq != null){
				
				seqCount = seqCount + 1;
				
				for(ItemSet candidate : candidate_k.getItemsets()){
					
					if(candidate.containedIn(seq, minMISItemRemoved)){
						candidate.incrementCount();
					}
					
					candidate_prime = candidate.getCandidatePrime();
					minMISItemRemoved = true;
					
					if(candidate_prime.containedIn(seq, minMISItemRemoved)){
						candidate_prime.incrementCount();
					}
				}
				
				seq = fileHandler.getNextSequence(false, null);
			}
			
			Sequence frequentSet_k = new Sequence();
			
			for(ItemSet itemset : candidate_k.getItemsets()){
				if(((float)itemset.getCount()/seqCount) >= minMISValue.getMinItemSupport()){
					frequentSet_k.addItemSet(itemset);
				}
			}
			
			frequentSets.add(frequentSet_k);
		}
				
		fileHandler.writeOutputFile();				//Write the output to file
		
		//DEBUG
		//fileHandler.printFrequentSets(frequentSets);
		System.out.println("out");
		fileHandler.printData(frequentSets);
	}

}
