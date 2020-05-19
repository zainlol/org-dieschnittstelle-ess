package org.dieschnittstelle.ess.ejb.ejbmodule.erp;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.ejb.ejbmodule.erp.crud.PointOfSaleCRUDLocal;
import org.dieschnittstelle.ess.ejb.ejbmodule.erp.crud.PointOfSaleCRUDStateless;
import org.dieschnittstelle.ess.ejb.ejbmodule.erp.crud.StockItemCRUDLocal;
import org.dieschnittstelle.ess.entities.erp.IndividualisedProductItem;
import org.dieschnittstelle.ess.entities.erp.StockItem;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.List;

@Singleton
public class StockSystemSingleton implements StockSystemLocal {

    @EJB
    StockItemCRUDLocal stockItemCRUDLocal;
    @EJB
    PointOfSaleCRUDLocal pointOfSaleCRUDLocal;

    protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(StockSystemSingleton.class);

    @Override
    public void addToStock(IndividualisedProductItem product, long pointOfSaleId, int units) {
        logger.info("yes i am working");
    }

    @Override
    public void removeFromStock(IndividualisedProductItem product, long pointOfSaleId, int units) {
        logger.info("yes i am working");
    }

    @Override
    public List<IndividualisedProductItem> getProductsOnStock(long pointOfSaleId) {
        logger.info("yes i am working");
        return null;
    }

    @Override
    public List<IndividualisedProductItem> getAllProductsOnStock() {
        logger.info("yes i am working");
        return null;
    }

    @Override
    public int getUnitsOnStock(IndividualisedProductItem product, long pointOfSaleId) {
        logger.info("yes i am working");
        return 0;
    }

    @Override
    public int getTotalUnitsOnStock(IndividualisedProductItem product) {
        logger.info("yes i am working");
        return 0;
    }

    @Override
    public List<Long> getPointsOfSale(IndividualisedProductItem product) {
        logger.info("yes i am working");
        return null;
    }

    @Override
    public List<StockItem> getCompleteStock() {
        logger.info("yes i am working");
        return null;
    }
}
