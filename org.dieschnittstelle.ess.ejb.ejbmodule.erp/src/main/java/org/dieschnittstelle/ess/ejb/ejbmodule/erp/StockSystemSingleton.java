package org.dieschnittstelle.ess.ejb.ejbmodule.erp;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.ejb.ejbmodule.erp.crud.PointOfSaleCRUDLocal;
import org.dieschnittstelle.ess.ejb.ejbmodule.erp.crud.PointOfSaleCRUDStateless;
import org.dieschnittstelle.ess.ejb.ejbmodule.erp.crud.StockItemCRUDLocal;
import org.dieschnittstelle.ess.entities.erp.IndividualisedProductItem;
import org.dieschnittstelle.ess.entities.erp.PointOfSale;
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

    /**
     * adds some units of a product to the stock of a point of sale
     *
     * should check whether stock item for the given combination of product and pointOfSale already exists.
     * Depending on this, the existing stock item will be updated or a new one will be created
     *
     * @param product
     * @param pointOfSaleId
     * @param units
     */
    @Override
    public void addToStock(IndividualisedProductItem product, long pointOfSaleId, int units) {
        PointOfSale pos = pointOfSaleCRUDLocal.readPointOfSale(pointOfSaleId);
        StockItem item = stockItemCRUDLocal.readStockItem(product, pos);
        if(item != null){
            item.setUnits(units);
            stockItemCRUDLocal.updateStockItem(item);
        }
        else{
            StockItem newItem = new StockItem(product,pos,units);
            stockItemCRUDLocal.createStockItem(newItem);
        }
    }

    /**
     * removes some units of a product from the stock of a point of sale
     *
     * here you can reuse the addToStock method passing a negative value for units
     *
     * @param product
     * @param pointOfSaleId
     * @param units
     */
    @Override
    public void removeFromStock(IndividualisedProductItem product, long pointOfSaleId, int units) {
        logger.info("yes i am working");
    }

    /**
     * returns all products on stock of some pointOfSale
     *
     * this method can be implemented on the basis of the readStockItemsForPointOfSale() method
     * in the CRUD EJB for StockItem, note that here and in most of the following methods you
     * first need to read out the PointOfSale object for the given pointOfSaleId
     *
     * @param pointOfSaleId
     * @return
     */
    @Override
    public List<IndividualisedProductItem> getProductsOnStock(long pointOfSaleId) {
        logger.info("yes i am working");
        return null;
    }

    /**
     * returns all products on stock
     *
     * this method can be implemented using the readAllPointsOfSale() method from the PointOfSale
     * CRUD EJB and using getProductsOnStock for each of point of sale. Note that there should be
     * no duplicates in the list that is returned.
     *
     * @return
     */
    @Override
    public List<IndividualisedProductItem> getAllProductsOnStock() {
        logger.info("yes i am working");
        return null;
    }

    /**
     * returns the units on stock for a product at some point of sale
     *
     * this method can very easily be implemented using the readStockItem method of the StockItem EJB.
     * Consider the case that no stock item exists for the given combination to avoid NullPointerException
     *
     * @param product
     * @param pointOfSaleId
     * @return
     */
    @Override
    public int getUnitsOnStock(IndividualisedProductItem product, long pointOfSaleId) {
        logger.info("yes i am working");
        return 0;
    }

    /**
     * returns the total number of units on stock for some product
     *
     * here you can use readStockItemsForProduct() and sum up the units
     *
     * @param product
     * @return
     */
    @Override
    public int getTotalUnitsOnStock(IndividualisedProductItem product) {
        logger.info("yes i am working");
        return 0;
    }

    /**
     * returns the points of sale where some product is available
     *
     * here you can use readStockItemsForProduct() and create a list of the stock items'
     * pointOfSale Ids
     *
     * @param product
     * @return
     */
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
