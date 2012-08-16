package data;

import java.util.Collection;
import java.util.SortedMap;

public class FeatureVector {

	private Collection<Double> mHitsVector;

	public FeatureVector() {
		clear();
	}
	
	public void clear() {
		mHitsVector = null;
	}

	public void set(SortedMap<String, Double> pHitsMap) {
		mHitsVector = pHitsMap.values();
	}

	public String getAsARFFData() {

		// HITS(C1(W1,W2)):numeric,...,HITS(Cn(W1,W2)):numeric,{positive \ negative}

		String stringVector = "";
		
		for (Double hit : mHitsVector)
			stringVector += "," + hit;
		
		return stringVector.substring(1);
	}

}
