package com.sanilk.securedroid.networking.responses;

import org.json.JSONObject;

/**
 * Created by root on 14/4/18.
 */
public class UpdateLocationResponse extends Response {
    public static final String RESPONSE_TYPE="UPDATE_LOCATION_RESPONSE";
    public static final String SUCCESSFUL_KEY="successful";

    private boolean successful;

    public UpdateLocationResponse(){
        responseType=RESPONSE_TYPE;
    }

    public String getJSONString(){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put(RESPONSE_TYPE_KEY, responseType);
            jsonObject.put(SUCCESSFUL_KEY, successful);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }
}
