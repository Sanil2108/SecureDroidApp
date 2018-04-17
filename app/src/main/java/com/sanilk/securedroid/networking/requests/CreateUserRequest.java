package com.sanilk.securedroid.networking.requests;

import org.json.JSONObject;

/**
 * Created by root on 16/4/18.
 */
public class CreateUserRequest extends Request {
    public static final String REQUEST_TYPE="CREATE_USER_REQUEST";
    public static final String EMAIL_KEY="email";
    public static final String PASSWORD_KEY="password";

    private String password;
    private String email;

    public CreateUserRequest(){
        requestType=REQUEST_TYPE;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        jsonObject.put(PASSWORD_KEY, password);
        return jsonObject.toString();
    }
}
