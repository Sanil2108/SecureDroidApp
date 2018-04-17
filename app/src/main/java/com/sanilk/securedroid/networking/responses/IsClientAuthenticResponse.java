package com.sanilk.securedroid.networking.responses;

import org.json.JSONObject;

/**
 * Created by root on 17/4/18.
 */
public class IsClientAuthenticResponse extends Response {
    public static final String RESPONSE_TYPE="IS_CLIENT_AUTHENTIC_RESPONSE";

    public static final String IS_SUCCESSFUL_KEY="is_successfu";

    private boolean isSuccessful;

    public IsClientAuthenticResponse(){
        responseType=RESPONSE_TYPE;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public String getJSONString() throws Exception{
        JSONObject jsonObject=new JSONObject();
        jsonObject.put(IS_SUCCESSFUL_KEY, isSuccessful);
        return jsonObject.toString();
    }
}
