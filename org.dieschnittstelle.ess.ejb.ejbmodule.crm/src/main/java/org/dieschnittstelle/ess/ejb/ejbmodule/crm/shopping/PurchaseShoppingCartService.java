package org.dieschnittstelle.ess.ejb.ejbmodule.crm.shopping;

import org.dieschnittstelle.ess.ejb.ejbmodule.crm.ShoppingException;
import org.dieschnittstelle.ess.entities.crm.AbstractTouchpoint;
import org.dieschnittstelle.ess.entities.crm.Customer;
import org.dieschnittstelle.ess.entities.erp.AbstractProduct;

import javax.ejb.Remote;
import javax.jws.WebService;
import javax.ws.rs.*;

// TODO: PAT1: this is the interface to be provided as a rest service if rest service access is used
@Remote
@WebService
@Path("/shoppingcart")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface PurchaseShoppingCartService {
	@GET
	@Path("/purchase")
	public void purchase(@QueryParam("shoppingCartId")long shoppingCartId, @QueryParam("touchpointId")long touchpointId, @QueryParam("customerId")long customerId) throws ShoppingException;
	
}
