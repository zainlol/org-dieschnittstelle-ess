package org.dieschnittstelle.ess.ejb.ejbmodule.crm.shopping;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.ejb.ejbmodule.crm.*;
import org.dieschnittstelle.ess.ejb.ejbmodule.crm.crud.CustomerCRUDLocal;
import org.dieschnittstelle.ess.ejb.ejbmodule.crm.crud.TouchpointCRUDLocal;
import org.dieschnittstelle.ess.ejb.ejbmodule.erp.StockSystemLocal;
import org.dieschnittstelle.ess.ejb.ejbmodule.erp.crud.ProductCRUDRemote;
import org.dieschnittstelle.ess.entities.crm.AbstractTouchpoint;
import org.dieschnittstelle.ess.entities.crm.Customer;
import org.dieschnittstelle.ess.entities.crm.CustomerTransaction;
import org.dieschnittstelle.ess.entities.crm.ShoppingCartItem;
import org.dieschnittstelle.ess.entities.erp.Campaign;
import org.dieschnittstelle.ess.entities.erp.IndividualisedProductItem;
import org.dieschnittstelle.ess.entities.erp.ProductBundle;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class PurchaseShoppingCartServiceImpl implements PurchaseShoppingCartService {

    protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(PurchaseShoppingCartServiceImpl.class);

    @EJB
    CustomerCRUDLocal customerRepo;
    @EJB
    TouchpointCRUDLocal touchpointRepo;
    @EJB
    CampaignTrackingRemote campaignTracking;
    @EJB
    CustomerTrackingRemote customerTracking;
    @EJB
    ProductCRUDRemote productCRUDRemote;
    @EJB
    ShoppingCartServiceLocal shoppingCartRepo;
    @EJB
    StockSystemLocal stockSystemLocal;

    Customer customer;
    AbstractTouchpoint touchpoint;
    ShoppingCartRemote shoppingCart;

    /*
     * verify whether campaigns are still valid
     */
    public void verifyCampaigns() throws ShoppingException {
        if (this.customer == null || this.touchpoint == null) {
            throw new RuntimeException("cannot verify campaigns! No touchpoint has been set!");
        }

        for (ShoppingCartItem item : this.shoppingCart.getItems()) {
            if (item.isCampaign()) {
                int availableCampaigns = this.campaignTracking.existsValidCampaignExecutionAtTouchpoint(
                        item.getErpProductId(), this.touchpoint);
                logger.info("got available campaigns for product " + item.getErpProductId() + ": "
                        + availableCampaigns);
                // we check whether we have sufficient campaign items available
                if (availableCampaigns < item.getUnits()) {
                    throw new ShoppingException("verifyCampaigns() failed for productBundle " + item
                            + " at touchpoint " + this.touchpoint + "! Need " + item.getUnits()
                            + " instances of campaign, but only got: " + availableCampaigns);
                }
            }
        }
    }

    public void purchase()  throws ShoppingException {
        logger.info("purchase()");

        if (this.customer == null || this.touchpoint == null) {
            throw new RuntimeException(
                    "cannot commit shopping session! Either customer or touchpoint has not been set: " + this.customer
                            + "/" + this.touchpoint);
        }

        // verify the campaigns
        verifyCampaigns();

        // remove the products from stock
        checkAndRemoveProductsFromStock();

        // then we add a new customer transaction for the current purchase
        List<ShoppingCartItem> products = this.shoppingCart.getItems();
        CustomerTransaction transaction = new CustomerTransaction(this.customer, this.touchpoint, products);
        transaction.setCompleted(true);
        customerTracking.createTransaction(transaction);

        logger.info("purchase(): done.\n");
    }

    /*
     * PAT2: complete the method implementation in your server-side component for shopping / purchasing
     */
    private void checkAndRemoveProductsFromStock() {
        logger.info("checkAndRemoveProductsFromStock");

        for (ShoppingCartItem item : this.shoppingCart.getItems()) {

            //ermitteln Sie das AbstractProduct für das gegebene ShoppingCartItem. Nutzen Sie dafür dessen erpProductId und die ProductCRUD EJB
            item.setProductObj(productCRUDRemote.readProduct(item.getErpProductId()));
            if (item.isCampaign()) {
                this.campaignTracking.purchaseCampaignAtTouchpoint(item.getErpProductId(), this.touchpoint,
                        item.getUnits());
                // wenn Sie eine Kampagne haben, muessen Sie hier
                // 1) ueber die ProductBundle Objekte auf dem Campaign Objekt iterieren, und
                for(ProductBundle bundle : ((Campaign)item.getProductObj()).getBundles()){
                    // 2) fuer jedes ProductBundle das betreffende Produkt in der auf dem Bundle angegebenen Anzahl, multipliziert mit dem Wert von
                    // item.getUnits() aus dem Warenkorb,
                    // - hinsichtlich Verfuegbarkeit ueberpruefen, und
                    // - falls verfuegbar, aus dem Warenlager entfernen - nutzen Sie dafür die StockSystem EJB
                    // (Anm.: item.getUnits() gibt Ihnen Auskunft darüber, wie oft ein Produkt, im vorliegenden Fall eine Kampagne, im
                    // Warenkorb liegt)
                    int total = bundle.getUnits() * item.getUnits();

                    if(total < stockSystemLocal.getUnitsOnStock((IndividualisedProductItem) bundle.getProduct(), touchpoint.getErpPointOfSaleId())){
                        stockSystemLocal.removeFromStock((IndividualisedProductItem) bundle.getProduct(), touchpoint.getErpPointOfSaleId(), total);
                    }
                }
            } else {
                // andernfalls (wenn keine Kampagne vorliegt) muessen Sie
                // 1) das Produkt in der in item.getUnits() angegebenen Anzahl hinsichtlich Verfuegbarkeit ueberpruefen und
                /// item.getUnits()
                if(item.getUnits() < stockSystemLocal.getUnitsOnStock((IndividualisedProductItem) item.getProductObj(), touchpoint.getErpPointOfSaleId()) ) {
                    // 2) das Produkt, falls verfuegbar, in der entsprechenden Anzahl aus dem Warenlager entfernen
                    stockSystemLocal.removeFromStock((IndividualisedProductItem) item.getProductObj(), touchpoint.getErpPointOfSaleId(), item.getUnits());
                }
            }

        }
    }

    @Override
    public void purchase(long shoppingCartId, long touchpointId, long customerId) throws ShoppingException {
        shoppingCart = shoppingCartRepo.getShoppingCart(shoppingCartId);
        customer = customerRepo.readCustomer(customerId);
        touchpoint = touchpointRepo.readTouchpoint(touchpointId);

        purchase();

    }

}
