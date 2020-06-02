package org.dieschnittstelle.ess.ejb.ejbmodule.erp;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.ejb.ejbmodule.erp.crud.PointOfSaleCRUDLocal;
import org.dieschnittstelle.ess.ejb.ejbmodule.erp.crud.PointOfSaleCRUDStateless;
import org.dieschnittstelle.ess.ejb.ejbmodule.erp.crud.StockItemCRUDLocal;
import org.dieschnittstelle.ess.ejb.ejbmodule.erp.crud.StockItemCRUDStateless;
import org.dieschnittstelle.ess.entities.erp.IndividualisedProductItem;
import org.dieschnittstelle.ess.entities.erp.PointOfSale;
import org.dieschnittstelle.ess.entities.erp.StockItem;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

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
            item.setUnits(item.getUnits() + units);
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
    @Transactional
    @Override
    public void removeFromStock(IndividualisedProductItem product, long pointOfSaleId, int units) {
        this.addToStock(product,pointOfSaleId, - units);

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
        PointOfSale pos = pointOfSaleCRUDLocal.readPointOfSale(pointOfSaleId);
        return stockItemCRUDLocal.readStockItemsForPointOfSale(pos).stream().map(i -> i.getProduct()).collect(Collectors.toList());
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
        List<PointOfSale> pos = pointOfSaleCRUDLocal.readAllPointsOfSale();
        return pos.stream().flatMap(i -> this.getProductsOnStock(i.getId()).stream()).collect(Collectors.toList());
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
        PointOfSale pos = pointOfSaleCRUDLocal.readPointOfSale(pointOfSaleId);
        return stockItemCRUDLocal.readStockItem(product,pos).getUnits();
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
        List<PointOfSale> pos = pointOfSaleCRUDLocal.readAllPointsOfSale();
        return pos.stream().mapToInt(i -> this.getUnitsOnStock(product,i.getId())).sum();
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
        return stockItemCRUDLocal.readStockItemsForProduct(product).stream().map(i -> i.getPos().getId()).collect(Collectors.toList());
    }

    @Override
    public List<StockItem> getCompleteStock() {
        return stockItemCRUDLocal.readAllStockItems();
    }
}
