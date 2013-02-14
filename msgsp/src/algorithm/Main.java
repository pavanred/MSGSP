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

		ArrayList<Sequence> freqSet1 = objMsgsp.computeFrequentSet_1(misValues);		//Compute Frequent Set 1 - F1

		fileHandler.writeOutputFile();				//Write the output to file
	}

}
