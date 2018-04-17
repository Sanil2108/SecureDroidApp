package com.sanilk.securedroid.networking.requests;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 17/4/18.
 */
public class IsClientAuthenticRequest extends Request {
    private static final String REQUEST_TYPE="IS_CLIENT_AUTHENTIC_REQUEST";

    private static final String EMAIL_KEY="email";
    private static final String PASSWORD_KEY="password";

    public String email;
    public String password;

    public IsClientAuthenticRequest(){
        requestType=REQUEST_TYPE;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJSONString() throws JSONException{
        JSONObject jsonObject=new JSONObject();
        jsonObject.put(Request.REQUEST_TYPE_KEY, REQUEST_TYPE);
        jsonObject.put(EMAIL_KEY, email);
        jsonObject.put(PASSWORD_KEY, password);
        return jsonObject.toString();
    }
}
