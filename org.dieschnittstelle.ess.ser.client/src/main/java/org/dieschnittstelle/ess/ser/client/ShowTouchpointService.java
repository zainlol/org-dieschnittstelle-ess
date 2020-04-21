package org.dieschnittstelle.ess.ser.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.entities.crm.AbstractTouchpoint;
import org.dieschnittstelle.ess.entities.crm.Address;
import org.dieschnittstelle.ess.entities.crm.StationaryTouchpoint;
import org.dieschnittstelle.ess.utils.Http;

import static org.dieschnittstelle.ess.utils.Utils.*;

public class ShowTouchpointService {

	protected static Logger logger = org.apache.logging.log4j.LogManager
			.getLogger(ShowTouchpointService.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ShowTouchpointService service = new ShowTouchpointService();
		service.run();
	}

	/**
	 * the http client that can be used for accessing the service on tomcat - note that we are usying an async client here
	 */
	private CloseableHttpAsyncClient client;
	
	/**
	 * the attribute that controls whether we are running through (when called from the junit test) or not
	 */
	private boolean stepwise = true;

	/**
	 * constructor
	 */
	public ShowTouchpointService() {

	}

	/*
	 * create the http client - this will be done for each request
	 */
	public void createClient() {
		if (client != null && client.isRunning()) {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		client = Http.createAsyncClient();
		client.start();
	}

	/**
	 * run
	 */
	public void run() {
		// 1) read out all touchpoints
		List<AbstractTouchpoint> touchpoints = readAllTouchpoints();

		// 2) delete the touchpoint after next console input
		if (touchpoints != null && touchpoints.size() > 0) {
			if (stepwise)
				step();

			deleteTouchpoint(touchpoints.get(0));
		}

		// 3) wait for input and create a new touchpoint
		if (stepwise) {
			step();
		}

		Address addr = new Address("Luxemburger Strasse", "10", "13353",
				"Berlin");
		StationaryTouchpoint tp = new StationaryTouchpoint(-1,
				"BHT Verkaufsstand", addr);

		createNewTouchpoint(tp);

		try {
			client.close();
		}
		catch (IOException ioe) {
			logger.error("got IOException trying to close client: " + ioe,ioe);
		}

		show("TestTouchpointService: done.\n");
	}

	/**
	 * read all touchpoints
	 * 
	 * @return
	 */
	public List<AbstractTouchpoint> readAllTouchpoints() {

		logger.info("readAllTouchpoints()");

		createClient();

		logger.debug("client running: {}",client.isRunning());

		// demonstrate access to the asynchronously running servlet (client-side access is asynchronous in any case)
		boolean async = false;


		try {

			// create a GetMethod

			// UE SER1: Aendern Sie die URL von api->gui
			HttpGet get = new HttpGet(
					"http://localhost:8888/org.dieschnittstelle.ess.ser/api/" + (async ? "async/touchpoints" : "touchpoints"));

			logger.info("readAllTouchpoints(): about to execute request: " + get);

			// mittels der <request>.setHeader() Methode koennen Header-Felder
			// gesetzt werden

			// execute the method and obtain the response - for AsyncClient this will be a future from
			// which the response object can be obtained synchronously calling get() - alternatively, a FutureCallback can
			// be passed to the execute() method
			Future<HttpResponse> responseFuture = client.execute(get, null);
			logger.info("readAllTouchpoints(): received response future...");

			HttpResponse response = responseFuture.get();
			logger.info("readAllTouchpoints(): received response value");

			// check the response status
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				// try to read out an object from the response entity
				ObjectInputStream ois = new ObjectInputStream(response
						.getEntity().getContent());

				List<AbstractTouchpoint> touchpoints = (List<AbstractTouchpoint>) ois
						.readObject();

				logger.info("read touchpoints: " + touchpoints);

				// this is necessary to be able to use the client for
				// subsequent requests
				EntityUtils.consume(response.getEntity());

				return touchpoints;

			} else {
				String err = "could not successfully execute request. Got status code: "
						+ response.getStatusLine().getStatusCode();
				logger.error(err);
				throw new RuntimeException(err);
			}

		} catch (Exception e) {
			String err = "got exception: " + e;
			logger.error(err, e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * TODO SER3
	 * 
	 * @param tp
	 */
	public void deleteTouchpoint(AbstractTouchpoint tp) {
		logger.info("deleteTouchpoint(): will delete: " + tp);

		createClient();

		logger.debug("client running: {}",client.isRunning());
		try {
			HttpDelete delete = new HttpDelete("http://localhost:8888/org.dieschnittstelle.ess.ser/api/touchpoints/" + tp.getId());
			Future<HttpResponse> responseFuture = client.execute(delete, null);
			// get the response from the Future object
			HttpResponse response = responseFuture.get();
			// log the status line
			logger.info("deleteTouchpoint():" + response.getStatusLine());
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// once you have received a response this is necessary to be able to
				// use the client for subsequent requests:
				EntityUtils.consume(response.getEntity());
			}
		}
		catch (Exception e) {
			logger.error("got exception: " + e, e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * TODO SER4
	 * 
	 * fuer das Schreiben des zu erzeugenden Objekts als Request Body siehe die
	 * Hinweise auf:
	 * http://stackoverflow.com/questions/10146692/how-do-i-write-to
	 * -an-outpustream-using-defaulthttpclient
	 * 
	 * @param tp
	 */
	public AbstractTouchpoint createNewTouchpoint(AbstractTouchpoint tp) {
		logger.info("createNewTouchpoint(): will create: " + tp);

		createClient();

		logger.debug("client running: {}",client.isRunning());

		try {

			// create post request for the api/touchpoints uri
			HttpPost post = new HttpPost(
					"http://localhost:8888/org.dieschnittstelle.ess.ser/api/touchpoints/");
			logger.info("createNewTouchpoint(): about to execute request: " + post);
			// create an ObjectOutputStream from a ByteArrayOutputStream - the
			// latter must be accessible via a variable
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(bos);
			// write the object to the output stream
			objectOutputStream.writeObject(tp);
			// create a ByteArrayEntity and pass it the byte array from the
			// output stream
			ByteArrayEntity entity = new ByteArrayEntity(bos.toByteArray());
			//objectOutputStream.flush();
			// set the entity on the request
			post.setEntity(entity);
			// execute the request, which will return a Future<HttpResponse> object
			Future<HttpResponse> responseFuture = client.execute(post, null);

			// get the response from the Future object
			HttpResponse response = responseFuture.get();
			// log the status line
			logger.info("createNewTouchpoints():" + response.getStatusLine());
			// evaluate the result using getStatusLine(), use constants in
			// HttpStatus
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				// create an object input stream using getContent() from the
				// response entity (accessible via getEntity())
				ObjectInputStream objectResponse = new ObjectInputStream(response.getEntity().getContent());
				// read the touchpoint object from the input stream
				AbstractTouchpoint responseTouchpoint = (AbstractTouchpoint) objectResponse.readObject();
				// cleanup the request
				EntityUtils.consume(post.getEntity());

				// return the object that you have read from the response
				return responseTouchpoint;
			}
			return null;

		} catch (Exception e) {
			logger.error("got exception: " + e, e);
			throw new RuntimeException(e);
		}

	}

	/**
	 * 
	 * @param stepwise
	 */
	public void setStepwise(boolean stepwise) {
		this.stepwise = stepwise;
	}

	/**
	 * utility...
	 */
	private void step() {
		try {
			System.out.println("/>");
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
