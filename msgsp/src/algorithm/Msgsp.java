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
				
		for(Integer item : lSet){
			for(MISValue mVal : misValues){
				if(mVal.getItemNo() == item){
					if(mVal.getActualSupport() >= mVal.getMinItemSupport()){
						
						items = new ItemSet();
						items.addItem(item);
						
						seq = new Sequence();
						seq.addItemSet(items);
						
						frequentset1.add(seq);
					}
				}
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

	
	//Custom comparator
	public class CustomComparator implements Comparator<MISValue> {
	    @Override
	    public int compare(MISValue o1, MISValue o2) {
	        return o1.getMinItemSupport().compareTo(o2.getMinItemSupport());
	    }
	}	
}

