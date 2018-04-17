package com.sanilk.securedroid.networking.responses;

/**
 * Created by root on 17/4/18.
 */
public class CreateUserResponse extends Response {
    public static final String RESPONSE_TYPE="CREATE_USER_RESPONSE";
    public static final String IS_SUCCESSFUL_KEY="is_successful";

    private boolean isSuccessful;

    public CreateUserResponse(){
        responseType=RESPONSE_TYPE;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }
}
