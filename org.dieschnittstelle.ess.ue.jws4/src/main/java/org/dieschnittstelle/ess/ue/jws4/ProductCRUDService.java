package org.dieschnittstelle.ess.ue.jws4;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.entities.GenericCRUDExecutor;
import org.dieschnittstelle.ess.entities.crm.AbstractTouchpoint;
import org.dieschnittstelle.ess.entities.crm.StationaryTouchpoint;
import org.dieschnittstelle.ess.entities.erp.AbstractProduct;
import org.dieschnittstelle.ess.entities.erp.Campaign;
import org.dieschnittstelle.ess.entities.erp.IndividualisedProductItem;
import org.dieschnittstelle.ess.entities.erp.ProductType;


@WebService(targetNamespace = "http://dieschnittstelle.org/ess/jws", name = "IProductCRUDService", serviceName = "ProductCRUDWebService", portName = "IProductCRUDServicePort")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class ProductCRUDService {

	protected static Logger logger = org.apache.logging.log4j.LogManager
			.getLogger(ProductCRUDService.class);

	@Resource
	private WebServiceContext wscontext;

	@WebMethod
	public List<AbstractProduct> readAllProducts() {
		logger.info("readAllTouchpoints()");

		logger.info("readAllTouchpoints(): I am: " + this);

		// we obtain the servlet context from the wscontext
		ServletContext ctx = (ServletContext) wscontext.getMessageContext()
				.get(MessageContext.SERVLET_CONTEXT);
		logger.info("readAllTouchpoints(): servlet context is: " + ctx);
		// we also read out the http request
		HttpServletRequest httpRequest = (HttpServletRequest) wscontext
				.getMessageContext().get(MessageContext.SERVLET_REQUEST);
		logger.info("readAllTouchpoints(): servlet request is: " + httpRequest);

		GenericCRUDExecutor<AbstractProduct> productCRUD = (GenericCRUDExecutor<AbstractProduct>) ctx
				.getAttribute("productCRUD");
		logger.info("readAllTouchpoints(): read touchpointCRUD from servletContext: "
				+ productCRUD);

		return productCRUD.readAllObjects();
	}
	@WebMethod
	public AbstractProduct createProduct(AbstractProduct product) {
		// obtain the CRUD executor from the servlet context
		GenericCRUDExecutor<AbstractProduct> productCRUD = (GenericCRUDExecutor<AbstractProduct>) ((ServletContext) wscontext
				.getMessageContext().get(MessageContext.SERVLET_CONTEXT))
				.getAttribute("productCRUD");

		return (AbstractProduct) productCRUD
				.createObject(product);
	}
	@WebMethod
	public AbstractProduct updateProduct(AbstractProduct update) {
		GenericCRUDExecutor<AbstractProduct> productCRUD = (GenericCRUDExecutor<AbstractProduct>) ((ServletContext) wscontext
				.getMessageContext().get(MessageContext.SERVLET_CONTEXT))
				.getAttribute("productCRUD");

		return (AbstractProduct) productCRUD
				.updateObject(update);
	}
	@WebMethod
	public boolean deleteProduct(long id) {
		logger.info("deleteTouchpoint(): continuing...");

		// obtain the CRUD executor from the servlet context
		GenericCRUDExecutor<AbstractProduct> productCRUD = (GenericCRUDExecutor<AbstractProduct>) ((ServletContext) wscontext
				.getMessageContext().get(MessageContext.SERVLET_CONTEXT))
				.getAttribute("productCRUD");

		return productCRUD.deleteObject(id);
	}
	@WebMethod
	public AbstractProduct readProduct(long id) {
		// obtain the CRUD executor from the servlet context
		GenericCRUDExecutor<AbstractProduct> productCRUD = (GenericCRUDExecutor<AbstractProduct>) ((ServletContext) wscontext
				.getMessageContext().get(MessageContext.SERVLET_CONTEXT))
				.getAttribute("productCRUD");

		return productCRUD.readObject(id);
	}

}
