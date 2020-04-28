package org.dieschnittstelle.ess.jrs;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.entities.GenericCRUDExecutor;
import org.dieschnittstelle.ess.entities.crm.AbstractTouchpoint;
import org.dieschnittstelle.ess.entities.erp.IndividualisedProductItem;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

/*
 * TODO JRS2: implementieren Sie hier die im Interface deklarierten Methoden
 */

public class ProductCRUDServiceImpl implements IProductCRUDService {

	protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(ProductCRUDServiceImpl.class);

	private GenericCRUDExecutor<IndividualisedProductItem> productCRUD;

	public ProductCRUDServiceImpl(@Context ServletContext servletContext, @Context HttpServletRequest request) {
		logger.info("<constructor>: " + servletContext + "/" + request);
		// read out the dataAccessor
		this.productCRUD = (GenericCRUDExecutor<IndividualisedProductItem>)servletContext.getAttribute("productCRUD");

		logger.debug("read out the touchpointCRUD from the servlet context: " + this.productCRUD);
	}

	@Override
	public IndividualisedProductItem createProduct(
			IndividualisedProductItem prod) {
		return productCRUD.createObject(prod);
	}

	@Override
	public List<IndividualisedProductItem> readAllProducts() {
		return productCRUD.readAllObjects();
	}

	@Override
	public IndividualisedProductItem updateProduct(long id,
			IndividualisedProductItem update) {
		return productCRUD.updateObject(update);
	}

	@Override
	public boolean deleteProduct(long id) {
		return productCRUD.deleteObject(id);
	}

	@Override
	public IndividualisedProductItem readProduct(long id) {
		return productCRUD.readObject(id);
	}
	
}
