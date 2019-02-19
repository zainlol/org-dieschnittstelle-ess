package org.dieschnittstelle.ess.ejb.client.demos;

import org.dieschnittstelle.ess.ejb.client.TotalUsecase;
import org.dieschnittstelle.ess.ejb.client.ejbclients.EJBProxyFactory;

import static org.dieschnittstelle.ess.ejb.client.Constants.WEB_API_BASE_URL;

public class CreateTouchpoints {

	public static void main(String[] args) {
		EJBProxyFactory.initialise();

		try {
			TotalUsecase uc = new TotalUsecase();
			uc.createTouchpoints();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
