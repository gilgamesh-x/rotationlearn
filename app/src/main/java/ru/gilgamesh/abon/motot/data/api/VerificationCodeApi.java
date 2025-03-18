package ru.gilgamesh.abon.motot.data.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import ru.gilgamesh.abon.motot.payload.request.verificationCode.VerificationCodeRequest;
import ru.gilgamesh.abon.motot.payload.request.verificationCode.VerificationCodeRequest2;
import ru.gilgamesh.abon.motot.payload.response.MessageResponse;

public interface VerificationCodeApi {

    @POST("api/code/recoveryCode")
    @Headers("Content-Type: application/json;charset=UTF-8")
    Call<MessageResponse> recoveryCode(@Body VerificationCodeRequest request);

    @POST("api/code/checkCode")
    @Headers("Content-Type: application/json;charset=UTF-8")
    Call<MessageResponse> checkCode(@Body VerificationCodeRequest2 request);
}
