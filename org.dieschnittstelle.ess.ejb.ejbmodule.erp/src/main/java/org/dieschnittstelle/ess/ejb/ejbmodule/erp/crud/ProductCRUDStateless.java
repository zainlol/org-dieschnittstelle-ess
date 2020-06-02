package org.dieschnittstelle.ess.ejb.ejbmodule.erp.crud;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.entities.erp.AbstractProduct;
import org.dieschnittstelle.ess.entities.erp.PointOfSale;
import org.dieschnittstelle.ess.entities.erp.StockItem;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Stateless
@WebService(targetNamespace = "http://dieschnittstelle.org/ess/jws", serviceName = "ProductCRUDRemoteWebService", endpointInterface = "org.dieschnittstelle.ess.ejb.ejbmodule.erp.crud.ProductCRUDRemote")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public class ProductCRUDStateless implements ProductCRUDRemote {

    protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(AbstractProduct.class);

    @PersistenceContext(unitName = "erp_PU")
    private EntityManager em;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public AbstractProduct createProduct(AbstractProduct prod) {
        em.persist(prod);
        return prod;
    }

    @Override
    public List<AbstractProduct> readAllProducts() {
        TypedQuery<AbstractProduct> query =
                em.createQuery("SELECT c FROM AbstractProduct c", AbstractProduct.class);
        return query.getResultList();
    }

    @Override
    public AbstractProduct updateProduct(AbstractProduct update) {
        return em.merge(update);
    }

    @Override
    public AbstractProduct readProduct(long productID) {
        return em.find(AbstractProduct.class,productID);
    }

    @Override
    public boolean deleteProduct(long productID) {
        em.remove(em.find(AbstractProduct.class,productID));
        return true;
    }
}
