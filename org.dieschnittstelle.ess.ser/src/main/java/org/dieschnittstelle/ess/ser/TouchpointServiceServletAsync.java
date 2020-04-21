package org.dieschnittstelle.ess.ser;

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.entities.crm.AbstractTouchpoint;

import javax.servlet.AsyncContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.dieschnittstelle.ess.utils.Utils.show;

@WebServlet(urlPatterns = "/api/async/touchpoints", asyncSupported = true)
public class TouchpointServiceServletAsync extends HttpServlet {

	protected static Logger logger = org.apache.logging.log4j.LogManager
			.getLogger(TouchpointServiceServletAsync.class);

	public TouchpointServiceServletAsync() {
		show("TouchpointServiceServlet: constructor invoked\n");
	}
	
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{

		logger.info("doGet()");

		AsyncContext asyncCtx = request.startAsync();
		RequestDispatcher dispatcher = asyncCtx.getRequest().getRequestDispatcher("/api/touchpoints");

		new Thread(()->{
			logger.info("doGet(): sleeping...");
			try {
				Thread.sleep(2000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			logger.info("doGet(): continuing...");
			try {
				dispatcher.forward(asyncCtx.getRequest(), asyncCtx.getResponse());
			}
			catch (Exception e) {
				response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			}
		}).start();

	}

	@Override
	protected void doDelete(HttpServletRequest request,
							 HttpServletResponse response) throws ServletException, IOException{
		logger.info("doGet()");

		AsyncContext asyncCtx = request.startAsync();
		RequestDispatcher dispatcher = asyncCtx.getRequest().getRequestDispatcher("/api/touchpoints");

		new Thread(()->{
			logger.info("doDelete(): sleeping...");
			try {
				Thread.sleep(2000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			logger.info("doDelete(): continuing...");
			try {
				dispatcher.forward(asyncCtx.getRequest(), asyncCtx.getResponse());
			}
			catch (Exception e) {
				response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			}
		}).start();
	}

	@Override
	protected void doPost(HttpServletRequest request,
						  HttpServletResponse response) throws ServletException, IOException {

		logger.info("doPost()");

		AsyncContext asyncCtx = request.startAsync();
		RequestDispatcher dispatcher = asyncCtx.getRequest().getRequestDispatcher("/api/touchpoints");

		new Thread(()->{
			logger.info("doPost(): sleeping...");
			try {
				Thread.sleep(2000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			logger.info("doPost(): continuing...");
			try {
				dispatcher.forward(asyncCtx.getRequest(), asyncCtx.getResponse());
			}
			catch (Exception e) {
				response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			}
		}).start();

	}


	
}
