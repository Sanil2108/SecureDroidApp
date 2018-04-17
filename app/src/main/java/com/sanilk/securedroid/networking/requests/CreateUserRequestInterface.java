package com.sanilk.securedroid.networking.requests;

import com.sanilk.securedroid.networking.responses.CreateUserResponse;

/**
 * Created by sanil on 17/4/18.
 */

public interface CreateUserRequestInterface {
    void onComplete(CreateUserResponse createUserResponse);
}
