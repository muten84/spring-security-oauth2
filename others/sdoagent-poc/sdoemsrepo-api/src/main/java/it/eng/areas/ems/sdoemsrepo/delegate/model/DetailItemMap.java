/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.delegate.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DetailItemMap implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, String> detailMap = new HashMap<String, String>();
	
	private DetailItem[] detailArray;

	public Map<String, String> getDetailMap() {
		return detailMap;
	}

	public void setDetailMap(Map<String, String> detailMap) {
		this.detailMap = detailMap;
	}	
	
	public DetailItem[] getDetailArray(){
		this.detailArray = new DetailItem[this.detailMap.size()];
		
		Iterator dMap = this.detailMap.entrySet().iterator();
		int i = 0;
		 while (dMap.hasNext()) {			
		    Map.Entry detailMap = (Map.Entry)dMap.next();
		    this.detailArray[i] = new DetailItem(detailMap.getKey().toString(), detailMap.getValue().toString());
		    i++;
		  }
	
		return this.detailArray;
	}

}
