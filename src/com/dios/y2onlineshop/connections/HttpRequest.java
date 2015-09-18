/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dios.y2onlineshop.connections;

/*
 * REVISION HISTORY:
 *
 * Date         Author(s)
 * CR           Headline
 * =============================================================================
 * 05/05/2010   Douglas Daniel Del Frari
 * Initial version to Send the Animation Poke to server site.
 * =============================================================================
 * 17/05/2010   Douglas Daniel Del Frari
 * Adding a tip documentation in this class.  
 * =============================================================================
 * 27/05/2010   Douglas Daniel Del Frari
 * Enhancement to try/catch error throw at send method
 * ========================================================================================
 */

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import net.rim.device.api.io.transport.ConnectionDescriptor;
import net.rim.device.api.io.transport.ConnectionFactory;

/**
 * This class encapsulated the Send process of Data to Server Site,
 * using the HTTP protocol.
 * 
 * <p>Example of use: <br><br>
 * 
 * Hashtable params = new Hashtable();
 * params.put("gesture", "1:3,1:3,1:3,1:-3,1:-3,1:-3");
 * params.put("message", "Ola  simone... pessoal! viva o GREMIO!");
 * params.put("sound", "teste.mp3"); // option field
 * params.put("texture", "textura.bmp");
 * String URL = MIDletController.getInstance().getAppProperty(HttpRequest.SERVER_URL_PROPERTY_KEY);
 * HttpRequest req = new HttpRequest(URL, params );
 * byte[] response = req.send();
 */
public class HttpRequest {

    static final String BOUNDARY = "-------BUSINESS-here---V2ymHFg03ehbqgZCaKO6jy";

//    public static final String SERVER_URL_PROPERTY_KEY = "ServerURL";

    byte[] postBytes = null;
    String url = null;
    
    

    /**
     * This constructor create an object used aim to a file transfer.
     * 
     * @param url
     *            the URL of server
     * @param params
     *            one or more parameters encapsulated on Hashtable object
     * @param fileField
     *            argument to identify of this field
     * @param fileName
     *            the name of file with extension (e.g. audio/amr)
     * @param fileType
     *            the mime type know
     * @param fileBytes
     *            the byte array
     * 
     * @throws Exception
     */
    public HttpRequest(String url, Hashtable params, String fileField,
            String fileName, String fileType, byte[] fileBytes)
            throws Exception {
        this.url = url;

        String boundary = getBoundaryString();
        String boundaryMessage = getBoundaryMessage(boundary, params,
                fileField, fileName, fileType);

        String endBoundary = "\r\n--" + boundary + "--\r\n";
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bos.write(boundaryMessage.getBytes());
        bos.write(fileBytes);
        bos.write(endBoundary.getBytes());

        this.postBytes = bos.toByteArray();
        bos.close();
    }

    /**
     * 
     * This constructor create an object used aim to a file transfer.
     * 
     * @param url
     *            the URL of server
     * @param params
     *            one or more parameters encapsulated on Hashtable object
     * 
     * @throws Exception
     *             some problem.
     */
    public HttpRequest(String url, Hashtable params) throws Exception {
        this.url = url;

        String boundary = getBoundaryString();
        String boundaryMessage = getBoundaryMessage(boundary, params);
        String endBoundary = "\r\n--" + boundary + "--\r\n";

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(boundaryMessage.getBytes());
        bos.write(endBoundary.getBytes());

        this.postBytes = bos.toByteArray();
        bos.close();
    }

    /**
     * get the Boundary string
     * @return
     */
    private String getBoundaryString() {
        return BOUNDARY;
    }

    /**
     * 
     * @param boundary
     * @param params
     * @param fileField
     * @param fileName
     * @param fileType
     * @return
     */
    private String getBoundaryMessage(String boundary, Hashtable params,
            String fileField, String fileName, String fileType) {
        StringBuffer res = new StringBuffer("--").append(boundary).append(
                "\r\n");

        Enumeration keys = params.keys();

        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String value = (String) params.get(key);

            res.append("Content-Disposition: form-data; name=\"").append(key)
                    .append("\"\r\n").append("\r\n").append(value).append(
                            "\r\n").append("--").append(boundary)
                    .append("\r\n");
        }
        res.append("Content-Disposition: form-data; name=\"").append(fileField)
                .append("\"; filename=\"").append(fileName).append("\"\r\n")
                .append("Content-Type: ").append(fileType).append("\r\n\r\n");

        return res.toString();
    }

    /**
     * 
     * @param boundary
     * @param params
     * @return
     */
    private String getBoundaryMessage(String boundary, Hashtable params) {
        StringBuffer res = new StringBuffer("--").append(boundary).append(
                "\r\n");

        Enumeration keys = params.keys();

        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String value = (String) params.get(key);

            res.append("Content-Disposition: form-data; name=\"").append(key)
                    .append("\"\r\n").append("\r\n").append(value).append(
                            "\r\n").append("--").append(boundary)
                    .append("\r\n");
        }

        return res.toString();
    }

    /**
     * Send the data to the URL of Server Site using the POST connection.
     * 
     * @return the response of server.
     * @throws Exception
     */
    public byte[] send() throws Exception {
        HttpConnection hc = null;
        InputStream is = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[] res = null;

        try {
        	System.out.println("<<HttpRequest.send>> url: "+url);
            hc = (HttpConnection) Connector.open(url);

            hc.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + getBoundaryString());

            hc.setRequestMethod(HttpConnection.POST);



            OutputStream dout = hc.openOutputStream();

            dout.write(postBytes);
            if (dout!=null) {
                dout.close();
                dout = null;
            }

            int ch;
            is = hc.openInputStream();

            while ((ch = is.read()) != -1) {
                bos.write(ch);
            }
            res = bos.toByteArray();
        } catch (Exception e) {
            // if an error occurred connecting to the server.
            throw new Exception(e.getMessage());

        } finally {
            try {
                if (bos != null)
                    bos.close();

                if (is != null)
                    is.close();

                if (hc != null)
                    hc.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return res;
    }

    public void sendWithCallback(final ConnectionCallback callback){
        new Thread(new Runnable() {

            public void run() {

            	ConnectionCallback callbackObject = callback;
                
                if(callbackObject != null)
                    callbackObject.onBegin();
                
                HttpConnection hc = null;
                InputStream is = null;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                byte[] res = null;

                /* Ritual, don't forget!!!! */
        		ConnectionFactory f = new ConnectionFactory();
        		f.setTimeoutSupported(true);
        		f.setConnectionTimeout(1);
        		ConnectionDescriptor descr = f.getConnection(url);
        		
        		if (descr==null) {
        			callback.onFail("Unable to connect to: "+url);
        		}
        		
                try {
                    hc = (HttpConnection) Connector.open(descr.getUrl());

                    hc.setRequestProperty("Content-Type",
                            "multipart/form-data; boundary=" + getBoundaryString());

                    hc.setRequestMethod(HttpConnection.POST);



                    OutputStream dout = hc.openOutputStream();

//                    dout.write(postBytes);

                    int index = 0; 
                    int size = 1024; 
                    do{ 
                        System.out.println("write:" + index); 
                        if((index+size)>postBytes.length){ 
                            size = postBytes.length - index;  
                        } 

                        dout.write(postBytes, index, size); 
                        index+=size; 
                        if(callbackObject != null){
                            callbackObject.onProgress(new Integer(index), new Integer(postBytes.length));
                        }
                    }while(index<postBytes.length);

                    if (dout!=null) {
                        dout.close();
                        dout = null;
                    }

                    int ch;
                    is = hc.openInputStream();

                    while ((ch = is.read()) != -1) {
                        bos.write(ch);
                    }
                    res = bos.toByteArray();
                } catch (Exception e) {
                    // if an error occurred connecting to the server.
                    if(callbackObject != null)
                        callbackObject.onFail(e);
                    callbackObject = null;
                } finally {
                    try {
                        if (bos != null)
                            bos.close();

                        if (is != null)
                            is.close();

                        if (hc != null)
                            hc.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        if(callbackObject != null)
                            callbackObject.onFail(e2);
                    }
                }
                if(res != null){
                    try {
                        String response = new String(res, "UTF-8");
                        if(response != null){
                            if(callbackObject != null)
                                callbackObject.onSuccess(response);
                            callbackObject = null;
                        }
//                        else{
//                            if(callbackObject != null)
//                                callbackObject.onFail(response);
//                        }                 
                    } catch (UnsupportedEncodingException ex) {
                        ex.printStackTrace();
                        if(callbackObject != null)
                            callbackObject.onFail(ex);
                        callbackObject = null;
                    }
                }
            }
        }).start();        
    }

}
