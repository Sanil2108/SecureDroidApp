package com.sanilk.securedroid.networking.requests;

import org.json.JSONObject;

/**
 * Created by sanil on 13/4/18.
 */

public class SimpleRequestForQueries extends Request {
    private static final String CLIENT_EMAIL_KEY="client_email";

    private static final String REQUEST_TYPE="SIMPLE_REQUEST_FOR_QUERIES";

    public String clientEmail;

    public SimpleRequestForQueries(){
        requestType=REQUEST_TYPE;
    }

    public SimpleRequestForQueries(String clientEmail){
        this.clientEmail=clientEmail;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getJSONString(){
        if(clientEmail==null || clientEmail==""){
            throw new RuntimeException("all request data required is not available");
        }
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(REQUEST_TYPE_KEY, REQUEST_TYPE);
            jsonObject.put(CLIENT_EMAIL_KEY, clientEmail);

            return jsonObject.toString();

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
