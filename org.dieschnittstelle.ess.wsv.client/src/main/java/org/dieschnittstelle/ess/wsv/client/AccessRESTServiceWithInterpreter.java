package org.dieschnittstelle.ess.wsv.client;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.entities.crm.Address;
import org.dieschnittstelle.ess.entities.crm.StationaryTouchpoint;
import org.dieschnittstelle.ess.wsv.client.service.ITouchpointCRUDService;

import org.dieschnittstelle.ess.wsv.interpreter.JAXRSClientInterpreter;

import static org.dieschnittstelle.ess.utils.Utils.*;

public class AccessRESTServiceWithInterpreter {

    protected static Logger logger = org.apache.logging.log4j.LogManager
            .getLogger(AccessRESTServiceWithInterpreter.class);

    /**
     * @param args
     */
    public static void main(String[] args) {

		/*
		 * create an instance of the invocation handler passing the service
		 * interface and the base url
		 */
        JAXRSClientInterpreter invocationHandler = new JAXRSClientInterpreter(ITouchpointCRUDService.class,
                "http://localhost:8888/org.dieschnittstelle.ess.jrs/api");

		/*
		 * create a client for the web service using Proxy.newProxyInstance()
		 */
        ITouchpointCRUDService serviceProxy = (ITouchpointCRUDService)
                Proxy.newProxyInstance(ITouchpointCRUDService.class.getClassLoader(),
                new Class[] { ITouchpointCRUDService.class },
                invocationHandler);

        show("serviceProxy: " + serviceProxy);


        // 1) read out all touchpoints

        List<StationaryTouchpoint> tps = serviceProxy.readAllTouchpoints();
        show("read all: " + tps);



		// 2) delete the touchpoint if there is one
		if (tps.size() > 0) {
          step();
			show("deleted: "
					+ serviceProxy.deleteTouchpoint(tps.get(0).getId()));
		}

		// 3) create a new touchpoint


        Address addr = new Address("Luxemburger Strasse", "10", "13353",
                "Berlin");
        StationaryTouchpoint tp = new StationaryTouchpoint(-1,
                "BHT Verkaufsstand", addr);
        tp = (StationaryTouchpoint)serviceProxy.createTouchpoint(tp);
        //show("created: " + tp);

		/*
		 * 4) read out the new touchpoint
		 */
		show("read created: " + serviceProxy.readTouchpoint(tp.getId()));

		/*
		 * 5) update the touchpoint
		 */
		// change the name
		step();
		tp.setName("BHT Mensa");


		tp = serviceProxy.updateTouchpoint(tp.getId(), tp);
		show("updated: " + tp);
}

    public static void step() {
        try {
            System.out.println("/>");
            System.in.read();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

