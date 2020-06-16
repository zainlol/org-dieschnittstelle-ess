package org.dieschnittstelle.ess.ejb.ejbmodule.crm;

import javax.ejb.Local;
import javax.ws.rs.PathParam;

@Local
public interface ShoppingCartServiceLocal {
    public ShoppingCartRemote getShoppingCart(long id);
}
