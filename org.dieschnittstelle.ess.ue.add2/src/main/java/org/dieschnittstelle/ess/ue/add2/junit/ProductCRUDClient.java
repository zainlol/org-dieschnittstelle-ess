package org.dieschnittstelle.ess.ue.add2.junit;

import org.dieschnittstelle.ess.ejb.ejbmodule.erp.crud.ProductCRUDRemote;
import org.dieschnittstelle.ess.entities.erp.ws.AbstractProduct;
import org.dieschnittstelle.ess.jws.ProductCRUDRemoteWebService;

import java.util.List;


public class ProductCRUDClient {

	private ProductCRUDRemote proxy;

	public ProductCRUDClient() throws Exception {
		ProductCRUDRemoteWebService service = new ProductCRUDRemoteWebService();
		// obtain an interface to the operations provided by the service
		proxy = service.getProductCRUDStatelessPort();
	}

	public AbstractProduct createProduct(AbstractProduct prod) {
		AbstractProduct created = proxy.createProduct(prod);
		// as a side-effect we set the id of the created product on the argument before returning
		prod.setId(created.getId());
		return created;
	}

	public List<AbstractProduct> readAllProducts() {
		return proxy.readAllProducts();
	}

	public AbstractProduct updateProduct(AbstractProduct update) {
		return proxy.updateProduct(update);
	}

	public AbstractProduct readProduct(long productID) {
		return proxy.readProduct(productID);
	}

	public boolean deleteProduct(long productID) {
		return proxy.deleteProduct(productID);
	}

}
