package com.sanilk.securedroid.networking.responses;

import org.json.JSONObject;

/**
 * Created by root on 17/4/18.
 */
public class IsEmailTakenResponse extends Response {
    public static final String RESPONSE_TYPE="IS_EMAIL_TAKEN_RESPONSE";
    public static final String EMAIL_KEY="email";
    public static final String EMAIL_AVAILABLE_KEY="email_available";

    private String email;
    private boolean isEmailAvailable;

    public IsEmailTakenResponse(){
        responseType=RESPONSE_TYPE;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailAvailable() {
        return isEmailAvailable;
    }

    public void setEmailAvailable(boolean emailAvailable) {
        isEmailAvailable = emailAvailable;
    }

    public String getJSONString() throws Exception{
        JSONObject jsonObject=new JSONObject();
        jsonObject.put(RESPONSE_TYPE_KEY, RESPONSE_TYPE);
        jsonObject.put(EMAIL_KEY, email);
        jsonObject.put(EMAIL_AVAILABLE_KEY, isEmailAvailable);
        return jsonObject.toString();
    }
}
