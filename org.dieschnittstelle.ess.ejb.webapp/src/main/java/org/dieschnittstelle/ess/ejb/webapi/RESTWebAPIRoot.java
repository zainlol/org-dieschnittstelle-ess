package org.dieschnittstelle.ess.ejb.webapi;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.apache.logging.log4j.Logger;

@ApplicationPath("/api")
public class RESTWebAPIRoot extends Application {

	protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(RESTWebAPIRoot.class);
	
	public RESTWebAPIRoot() {
		logger.info("<constructor>");
	}

}
