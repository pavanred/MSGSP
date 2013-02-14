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
		
		Sequence freqSetk_1;
		Sequence candidate_k;
		
		for(int k=2; (freqSetk_1 = freqSet1.get(k-1)).getItemsets().size() > 0 ; k++){	//k,k-1
			
			candidate_k = new Sequence();
			
			if(k==2){
				candidate_k = objMsgsp.level2_CandidateGeneration(lSet);
			}
			else{
				candidate_k = objMsgsp.generic_CandidateGeneration(freqSetk_1);
			}
		}
		
		fileHandler.writeOutputFile();				//Write the output to file
	}

}
