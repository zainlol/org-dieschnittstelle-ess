package org.dieschnittstelle.ess.ejb.ejbmodule.erp.crud;

import java.util.List;

import org.dieschnittstelle.ess.entities.erp.AbstractProduct;

import javax.ejb.Remote;
import javax.jws.WebService;
import javax.ws.rs.*;

/*
 * this interface shall be implemented using a stateless EJB with an EntityManager.
 * See TouchpointCRUDStateless for an example EJB with a similar scope of functionality
 */
@Remote
@WebService
@Path("/products")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface ProductCRUDRemote {

	@POST
	public AbstractProduct createProduct(AbstractProduct prod);

	@GET
	public List<AbstractProduct> readAllProducts();

	@PUT
	public AbstractProduct updateProduct(AbstractProduct update);

	@GET
	@Path("/{productId}")
	public AbstractProduct readProduct(@PathParam("productId") long productID);

	@DELETE
	@Path("/{productId}")
	public boolean deleteProduct(@PathParam("productId")long productID);

}
