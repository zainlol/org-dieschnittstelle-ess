package org.dieschnittstelle.ess.ejb.ejbmodule.erp;

import org.dieschnittstelle.ess.ejb.ejbmodule.erp.crud.ProductCRUDRemote;
import org.dieschnittstelle.ess.entities.erp.IndividualisedProductItem;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Singleton;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.List;

@Stateless
@WebService(targetNamespace = "http://dieschnittstelle.org/ess/jws", serviceName = "StockSystemRemoteWebService", endpointInterface = "org.dieschnittstelle.ess.ejb.ejbmodule.erp.StockSystemRESTService")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public class StockSystemServiceImpl implements StockSystemRESTService {

    @EJB
    StockSystemLocal singleton;

    @EJB
    ProductCRUDRemote productRepo;

    @Override
    public void addToStock(long productId, long pointOfSaleId, int units) {
        IndividualisedProductItem product = (IndividualisedProductItem) productRepo.readProduct(productId);
        singleton.addToStock(product, pointOfSaleId, units);
    }

    @Override
    public void removeFromStock(long productId, long pointOfSaleId, int units) {
        IndividualisedProductItem product = (IndividualisedProductItem) productRepo.readProduct(productId);
        singleton.removeFromStock(product,pointOfSaleId,units);
    }

    @Override
    public List<IndividualisedProductItem> getProductsOnStock(long pointOfSaleId) {
        return singleton.getProductsOnStock(pointOfSaleId);
    }

    @Override
    public List<IndividualisedProductItem> getAllProductsOnStock() {
        return singleton.getAllProductsOnStock();
    }

    @Override
    public int getUnitsOnStock(long productId, long pointOfSaleId) {
        IndividualisedProductItem product = (IndividualisedProductItem) productRepo.readProduct(productId);
        return singleton.getUnitsOnStock(product, pointOfSaleId);
    }

    @Override
    public int getTotalUnitsOnStock(long productId) {
        IndividualisedProductItem product = (IndividualisedProductItem) productRepo.readProduct(productId);
        return singleton.getTotalUnitsOnStock(product);
    }

    @Override
    public List<Long> getPointsOfSale(long productId) {
        IndividualisedProductItem product = (IndividualisedProductItem) productRepo.readProduct(productId);
        return singleton.getPointsOfSale(product);
    }
}
