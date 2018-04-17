package com.sanilk.securedroid.networking.requests;

import com.sanilk.securedroid.networking.responses.IsClientAuthenticResponse;

/**
 * Created by sanil on 17/4/18.
 */

public interface IsClientAuthenticRequestInterface {
    void onComplete(IsClientAuthenticResponse isClientAuthenticResponse);
}
