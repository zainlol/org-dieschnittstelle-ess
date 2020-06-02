package org.dieschnittstelle.ess.ejb.ejbmodule.erp;

import jdk.nashorn.internal.objects.annotations.Getter;
import org.dieschnittstelle.ess.entities.erp.IndividualisedProductItem;

import javax.ejb.Remote;
import javax.jws.WebService;
import javax.ws.rs.*;
import java.util.List;

/**
 *
 * - declare the web api for this interface using JAX-RS
 * - implement the interface as an EJB of an appropriate type
 * - in the EJB implementation, delegate method invocations to the corresponding methods of the StockSystem EJB via the local interface
 * - let the StockSystemClient in the client project access the web api via this interface - see ShoppingCartClient for an example
 */
@Remote
@WebService
@Path("/stocksystem")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface StockSystemRESTService {

	/**
	 * adds some units of a product to the stock of a point of sale
	 */
	@POST
    void addToStock(@QueryParam("productId") long productId,@QueryParam("pointOfSaleId") long pointOfSaleId,@QueryParam("units") int units);

	/**
	 * removes some units of a product from the stock of a point of sale
	 */
	@DELETE
    void removeFromStock(@QueryParam("productId") long productId,@QueryParam("pointOfSaleId") long pointOfSaleId,@QueryParam("units") int units);

	/**
	 * returns all products on stock of some pointOfSale
	 */
	@GET
    List<IndividualisedProductItem> getProductsOnStock(@QueryParam("pointOfSaleId") long pointOfSaleId);

	/**
	 * returns all products on stock
	 */
	@GET
    List<IndividualisedProductItem> getAllProductsOnStock();

	/**
	 * returns the units on stock for a product at some point of sale
	 */
	@GET
    int getUnitsOnStock(@QueryParam("productId") long productId, @QueryParam("pointOfSaleId") long pointOfSaleId);

	/**
	 * returns the total number of units on stock for some product
	 */
	@GET
    int getTotalUnitsOnStock(@QueryParam("productId") long productId);

	/**
	 * returns the points of sale where some product is available
	 */
	@GET
    List<Long> getPointsOfSale(@QueryParam("productId") long productId);

}
