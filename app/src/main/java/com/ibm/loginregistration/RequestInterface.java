package com.ibm.loginregistration;

import com.ibm.loginregistration.models.ServerRequest;
import com.ibm.loginregistration.models.ServerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestInterface {

    @POST("wp-content/uploads/ibmtestauth/")
    Call<ServerResponse> operation(@Body ServerRequest request);

}
