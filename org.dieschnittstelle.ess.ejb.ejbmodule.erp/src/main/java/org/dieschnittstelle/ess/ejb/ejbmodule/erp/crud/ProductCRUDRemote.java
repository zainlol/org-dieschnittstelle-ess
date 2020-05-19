package org.dieschnittstelle.ess.ejb.ejbmodule.erp.crud;

import java.util.List;

import org.dieschnittstelle.ess.entities.erp.AbstractProduct;

import javax.ejb.Remote;
import javax.ws.rs.*;

/*
 * TODO EJB+JPA1/2/5:
 * this interface shall be implemented using a stateless EJB with an EntityManager.
 * See TouchpointCRUDStateless for an example EJB with a similar scope of functionality
 */
@Remote
@Path("/products")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface ProductCRUDRemote {

	@POST
	public AbstractProduct createProduct(AbstractProduct prod);

	@GET
	public List<AbstractProduct> readAllProducts();

	@PUT
	@Path("/{productId}")
	public AbstractProduct updateProduct(@PathParam("productId") AbstractProduct update);

	@GET
	@Path("/{productId}")
	public AbstractProduct readProduct(@PathParam("productId") long productID);

	@DELETE
	@Path("/{productId}")
	public boolean deleteProduct(@PathParam("productId")long productID);

}
