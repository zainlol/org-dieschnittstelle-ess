package org.dieschnittstelle.ess.basics;

public interface IStockItem {
	
	public void initialise(int units, String brandname);
	
	public void purchase(int units);
	
	public int getUnits();
	
	public String getBrandname();
		
}
