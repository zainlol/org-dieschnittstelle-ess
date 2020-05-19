package org.dieschnittstelle.ess.wsv.interpreter;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.util.EntityUtils;

import org.dieschnittstelle.ess.utils.Http;
import org.dieschnittstelle.ess.wsv.interpreter.json.JSONObjectSerialiser;

import static org.dieschnittstelle.ess.utils.Utils.*;


public class JAXRSClientInterpreter implements InvocationHandler {

    // use a logger
    protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(JAXRSClientInterpreter.class);

    // declare a baseurl
    private String baseurl;

    // declare a common path segment
    private String commonPath;

    // use our own implementation JSONObjectSerialiser
    private JSONObjectSerialiser jsonSerialiser = new JSONObjectSerialiser();

    // use an attribute that holds the serviceInterface (useful, e.g. for providing a toString() method)
    private Class serviceInterface;

    // use a constructor that takes an annotated service interface and a baseurl. the implementation should read out the path annotation, we assume we produce and consume json, i.e. the @Produces and @Consumes annotations will not be considered here
    public JAXRSClientInterpreter(Class serviceInterface, String baseurl) {
        this.baseurl = baseurl;
        this.serviceInterface = serviceInterface;
        this.commonPath = ((Path) serviceInterface.getAnnotation(Path.class)).value();
        logger.info("<constructor>: " + serviceInterface + " / " + baseurl + " / " + commonPath);
    }


    @Override
    public Object invoke(Object proxy, Method meth, Object[] args)
            throws Throwable {
        //check whether we handle the toString method and give some appropriate return value
        logger.info("invoke(): meth: " + meth);
        logger.info("invoke(): meth: " + meth.getName());
        if (meth.getName() == "toString") {
            return "TouchpointCrudService";
        }
        // use a default http client
        HttpClient client = Http.createSyncClient();

        // create the url using baseurl and commonpath (further segments may be added if the method has an own @Path annotation)
        String url = baseurl + commonPath;
        logger.info("invoke(): url: " + url);
        //check whether we have a path annotation and append the url (path params will be handled when looking at the method arguments)
        if (meth.getAnnotation(Path.class) != null) {
            url += meth.getAnnotation(Path.class).value();
        }
        logger.info("invoke(): url with path: " + url);
        // a value that needs to be sent via the http request body
        Object bodyValue = null;

        // check whether we have method arguments - only consider pathparam annotations (if any) on the first argument here - if no args are passed, the value of args is null! if no pathparam annotation is present assume that the argument value is passed via the body of the http request
        if (args != null && args.length > 0) {
            if (meth.getParameterAnnotations()[0].length > 0 && meth.getParameterAnnotations()[0][0].annotationType() == PathParam.class) {
                //handle PathParam on the first argument - do not forget that in this case we might have a second argument providing a bodyValue
               PathParam path = (PathParam) meth.getParameterAnnotations()[0][0];
                if (args.length > 1) {
                    bodyValue = args[1];
                }
                //if we have a path param, we need to replace the corresponding pattern in the url with the parameter value
                url = url.replace("{" +path.value() +"}", args[0].toString());
            } else {
                // if we do not have a path param, we assume the argument value will be sent via the body of the request
                bodyValue = args[0];
            }
        }

        // declare a HttpUriRequest variable
        HttpUriRequest request = null;
        RequestBuilder builder = null;
        logger.info("invoke(): meth: " + Arrays.toString(meth.getAnnotations()));
        logger.info("invoke(): meth.getAnnotation:" + meth.getAnnotation(POST.class));
        // check which of the http method annotation is present and instantiate request accordingly passing the url
        if (meth.getAnnotation(GET.class) != null) {
            builder = RequestBuilder.get(url);
        }
        if (meth.getAnnotation(DELETE.class) != null) {
            builder = RequestBuilder.delete(url);
        }
        if (meth.getAnnotation(PATCH.class) != null) {
            builder = RequestBuilder.patch(url);
        }
        if (meth.getAnnotation(POST.class) != null) {
            builder = RequestBuilder.post(url);
        }
        if (meth.getAnnotation(HEAD.class) != null) {
            builder = RequestBuilder.head(url);
        }
        if (meth.getAnnotation(PUT.class) != null) {
            builder = RequestBuilder.put(url);
        }
        if (meth.getAnnotation(OPTIONS.class) != null) {
            builder = RequestBuilder.options(url);
        }
        builder.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        // if we need to send the method argument in the request body we need to declare an entity
        ByteArrayEntity bae = null;

        // if a body shall be sent, convert the bodyValue to json, create an entity from it and set it on the request
        if (bodyValue != null) {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            // write the object to the stream using the jsonSerialiser
            jsonSerialiser.writeObject(bodyValue, outputStream);
            // create an ByteArrayEntity from the stream's content
            ByteArrayEntity entity = new ByteArrayEntity(outputStream.toByteArray());
            // set the entity on the request, which must be cast to HttpEntityEnclosingRequest
            builder.setEntity(entity);
            // and add a content type header for the request
        }
        request = builder.build();
        logger.info("invoke(): executing request: " + request);

        // then send the request to the server and get the response
        HttpResponse response = client.execute(request);

        logger.info("invoke(): received response: " + response);

        // check the response code
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

            // declare a variable for the return value
            Object returnValue = null;
            // convert the response body to a java object of an appropriate type considering the return type of the method and set the object as value of returnValue
            returnValue = jsonSerialiser.readObject(response.getEntity().getContent(), meth.getReturnType());
            // in order to check whether the return type of meth is parameterised generic type, you can use the following expression (meth.getGenericReturnType() instanceof ParameterizedType)

            // don't forget to cleanup the entity using EntityUtils.consume()
            if (bae != null) {
                EntityUtils.consume(bae);
            }

            // and return the return value
            logger.info("invoke(): returning value: " + returnValue);
            return returnValue;

        } else {
            throw new RuntimeException("Got unexpected status from server: " + response.getStatusLine());
        }
    }

}