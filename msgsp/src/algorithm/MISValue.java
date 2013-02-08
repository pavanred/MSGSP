package algorithm;

public class MISValue {

	private Integer itemNo; 
	private Float minItemSupport;
	private Float actualSupport;	

	public Integer getItemNo(){
	    return this.itemNo;
	}
	
	public void setitemNo(Integer value){	    
	     this.itemNo = value;
	}
	
	public Float getMinItemSupport(){
	    return this.minItemSupport;
	}
	
	public void setMinItemSupport(Float value){	    
	     this.minItemSupport = value;
	}
	
	public Float getActualSupport(){
	    return this.actualSupport;
	}
	
	public void setActualSupport(Float value){	    
	     this.actualSupport = value;
	}
}
