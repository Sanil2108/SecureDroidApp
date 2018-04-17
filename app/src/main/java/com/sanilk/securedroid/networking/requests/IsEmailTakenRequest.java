package com.sanilk.securedroid.networking.requests;

import org.json.JSONObject;

/**
 * Created by root on 17/4/18.
 */
public class IsEmailTakenRequest extends Request {
    public final static String REQUEST_TYPE="IS_EMAIL_TAKEN_REQUEST";
    public final static String EMAIL_KEY="email";

    private String email;

    public IsEmailTakenRequest(){
        requestType=REQUEST_TYPE;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJSONString() throws Exception{
        JSONObject jsonObject=new JSONObject();
        jsonObject.put(REQUEST_TYPE_KEY, REQUEST_TYPE);
        jsonObject.put(EMAIL_KEY, email);
        return jsonObject.toString();
    }
}
