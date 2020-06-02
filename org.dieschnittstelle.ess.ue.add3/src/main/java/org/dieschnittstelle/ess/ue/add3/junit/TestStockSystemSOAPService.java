package org.dieschnittstelle.ess.ue.add3.junit;

import static org.junit.Assert.*;

import org.dieschnittstelle.ess.ejb.ejbmodule.erp.StockSystemRESTService;
import org.dieschnittstelle.ess.entities.erp.ws.IndividualisedProductItem;
import org.dieschnittstelle.ess.jws.StockSystemRemoteWebService;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/*
 * 
 */
public class TestStockSystemSOAPService {

	private StockSystemRESTService serviceProxy;

	@Before
	public void prepareContext() {
		// create an instance of the client-side web service class
		StockSystemRemoteWebService service = new StockSystemRemoteWebService();
		// obtain an interface to the operations provided by the service
		serviceProxy =  service.getStockSystemServiceImplPort();
	}

	// TODO change interface to understand objects instead of ids
	@Test
	public void stockSystemServiceWorks() {
		// read out all products
    	List<IndividualisedProductItem> products = serviceProxy.getAllProductsOnStock();
		assertTrue("stock exists and can be read", products.size() > 0);

		// we are using the first product for the tests...
		IndividualisedProductItem testprod = products.get(0);

		// obtain the poss where the first product is available
		List<Long> poss = serviceProxy.getPointsOfSale(testprod.getId());
		assertTrue("selected product is associated with at least one point of sale", poss.size() > 0);

		// we are using the first pos for the tests...
		long testpos = poss.get(0);

		// obtain the local and total units
		int unitsAtPos = serviceProxy.getUnitsOnStock(testprod.getId(), testpos);
		int unitsTotal = serviceProxy.getTotalUnitsOnStock(testprod.getId());

		// add units for the first pos
		int unitsToAdd = 5;
		serviceProxy.addToStock(testprod.getId(), testpos, unitsToAdd);

		// compare the final units
		assertEquals("after adding units, units at pos correctly incremented", unitsAtPos + unitsToAdd, serviceProxy.getUnitsOnStock(testprod.getId(), testpos));
		assertEquals("after adding units, total units correctly incremented", unitsTotal + unitsToAdd, serviceProxy.getTotalUnitsOnStock(testprod.getId()));


	}
	
}
