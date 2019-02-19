package org.dieschnittstelle.ess.ejb.client;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.ejb.client.ejbclients.*;
import org.dieschnittstelle.ess.ejb.client.shopping.ShoppingBusinessDelegate;
import org.dieschnittstelle.ess.ejb.client.shopping.ShoppingSession;
import org.dieschnittstelle.ess.ejb.ejbmodule.crm.ShoppingException;
import org.dieschnittstelle.ess.entities.crm.CampaignExecution;
import org.dieschnittstelle.ess.entities.crm.Customer;
import org.dieschnittstelle.ess.entities.crm.CustomerTransaction;

import java.util.Collection;

import static org.dieschnittstelle.ess.ejb.client.Constants.*;
import static org.dieschnittstelle.ess.utils.Utils.step;

public class TotalUsecaseREST {

	protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(TotalUsecaseREST.class);

	public static void main(String[] args) {
		// here, we will use ejb proxies for accessing the server-side components
		EJBProxyFactory.initialise(WEB_API_BASE_URL,true);

		try {
			(new TotalUsecase()).runAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
