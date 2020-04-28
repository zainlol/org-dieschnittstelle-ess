package org.dieschnittstelle.ess.jrs;

import org.dieschnittstelle.ess.entities.erp.AbstractProduct;
import org.dieschnittstelle.ess.entities.erp.IndividualisedProductItem;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/*
 * UE JRS2: 
 * deklarieren Sie hier Methoden fuer:
 * - die Erstellung eines Produkts
 * - das Auslesen aller Produkte
 * - das Auslesen eines Produkts
 * - die Aktualisierung eines Produkts
 * - das Loeschen eines Produkts
 * und machen Sie diese Methoden mittels JAX-RS Annotationen als WebService verfuegbar
 */


@Path("/products")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public interface IProductCRUDService {

	@POST
	public AbstractProduct createProduct(AbstractProduct  prod);

	@GET
	public List<AbstractProduct> readAllProducts();

	@PUT
	@Path("/{productId}")
	public AbstractProduct  updateProduct(@PathParam("productId") long id,
										  AbstractProduct  update);
	@DELETE
	@Path("/{productId}")
	boolean deleteProduct(@PathParam("productId")long id);

	@GET
	@Path("/{productId}")
	public AbstractProduct readProduct(@PathParam("productId")long id);
			
}
