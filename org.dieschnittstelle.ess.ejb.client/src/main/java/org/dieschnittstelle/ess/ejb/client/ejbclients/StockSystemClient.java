package org.dieschnittstelle.ess.ejb.client.ejbclients;

import java.util.ArrayList;
import java.util.List;

import org.dieschnittstelle.ess.ejb.ejbmodule.erp.StockSystemRESTService;
import org.dieschnittstelle.ess.ejb.ejbmodule.erp.StockSystemRemote;
import org.dieschnittstelle.ess.entities.erp.IndividualisedProductItem;

public class StockSystemClient implements StockSystemRemote {

	private StockSystemRemote ejbProxy;

	private StockSystemRESTService serviceProxy;
	
	public StockSystemClient() throws Exception {

		// TODO: if the REST API shall be accessed, only the service interface needs to be specified when obtaining the proxy
		//  (starting from S20, this is the default behaviour - note that the base url of the web api is specified in ess-ejb-client.properties)
		if (EJBProxyFactory.getInstance().usesWebAPIAsDefault()) {
			this.serviceProxy = EJBProxyFactory.getInstance().getProxy(StockSystemRESTService.class,null,true);
		}
		// TODO: if EJBs are used, the ejb interface and uri need to be specified
		else {
			this.ejbProxy = EJBProxyFactory.getInstance().getProxy(StockSystemRemote.class, null, false);
		}
	}

	@Override
	public void addToStock(IndividualisedProductItem product, long pointOfSaleId, int units) {
		if (ejbProxy != null) {
			this.ejbProxy.addToStock(product, pointOfSaleId, units);
		}
		else {
			this.serviceProxy.addToStock(product.getId(),pointOfSaleId,units);
		}
	}

	@Override
	public void removeFromStock(IndividualisedProductItem product, long pointOfSaleId,
			int units) {
		if (ejbProxy != null) {
			this.ejbProxy.removeFromStock(product, pointOfSaleId, units);
		}
		else {
			this.serviceProxy.removeFromStock(product.getId(),pointOfSaleId,units);
		}
	}

	@Override
	public List<IndividualisedProductItem> getProductsOnStock(long pointOfSaleId) {
		if (ejbProxy != null) {
			return this.ejbProxy.getProductsOnStock(pointOfSaleId);
		}
		else {
			return this.serviceProxy.getProductsOnStock(pointOfSaleId);
		}
	}

	@Override
	public List<IndividualisedProductItem> getAllProductsOnStock() {
		if (ejbProxy != null) {
			return this.ejbProxy.getAllProductsOnStock();
		}
		else {
			return this.serviceProxy.getAllProductsOnStock();
		}
	}

	@Override
	public int getUnitsOnStock(IndividualisedProductItem product, long pointOfSaleId) {
		if (ejbProxy != null) {
			return this.ejbProxy.getUnitsOnStock(product,pointOfSaleId);
		}
		else {
			return this.serviceProxy.getUnitsOnStock(product.getId(),pointOfSaleId);
		}
	}

	@Override
	public int getTotalUnitsOnStock(IndividualisedProductItem product) {
		if (ejbProxy != null) {
			return this.ejbProxy.getTotalUnitsOnStock(product);
		}
		else {
			return this.serviceProxy.getTotalUnitsOnStock(product.getId());
		}
	}

	@Override
	public List<Long> getPointsOfSale(IndividualisedProductItem product) {
		if (ejbProxy != null) {
			return this.ejbProxy.getPointsOfSale(product);
		}
		else {
			return this.serviceProxy.getPointsOfSale(product.getId());
		}
	}


}
