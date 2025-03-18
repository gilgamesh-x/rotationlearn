package ru.gilgamesh.abon.motot.data.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import ru.gilgamesh.abon.motot.payload.request.auth.LoginRequest;
import ru.gilgamesh.abon.motot.payload.request.auth.LoginWORRequest;
import ru.gilgamesh.abon.motot.payload.request.auth.RecoveryPasswordRequest2Request;
import ru.gilgamesh.abon.motot.payload.request.auth.SignupRequest;
import ru.gilgamesh.abon.motot.payload.request.auth.TokenRefreshRequest;
import ru.gilgamesh.abon.motot.payload.response.MessageResponse;
import ru.gilgamesh.abon.motot.payload.response.auth.RefreshTokenResponse;
import ru.gilgamesh.abon.motot.payload.response.auth.SigninResponse;

public interface AuthApi {
    @POST("api/auth/signin")
    @Headers("Content-Type: application/json;charset=UTF-8")
    Call<SigninResponse> signin(@Body LoginRequest request);

    @POST("api/auth/signinWOR")
    @Headers("Content-Type: application/json;charset=UTF-8")
    Call<SigninResponse> signinWOR(@Body LoginWORRequest request);

    @POST("api/auth/signup")
    @Headers("Content-Type: application/json;charset=UTF-8")
    Call<MessageResponse> signup(@Body SignupRequest request);

    @POST("api/auth/signupGuest")
    @Headers("Content-Type: application/json;charset=UTF-8")
    Call<MessageResponse> signupGuest(@Body SignupRequest request);

    @POST("api/auth/refreshtoken")
    @Headers("Content-Type: application/json;charset=UTF-8")
    Call<RefreshTokenResponse> refreshAccessToken(@Body TokenRefreshRequest body);

    @POST("api/auth/signout")
    @Headers("Content-Type: application/json;charset=UTF-8")
    Call<MessageResponse> signout();

    @POST("api/auth/recoveryPasswordNA")
    @Headers("Content-Type: application/json;charset=UTF-8")
    Call<MessageResponse> recoveryPasswordNA(@Body RecoveryPasswordRequest2Request request);

    @GET("api/auth/getRoles")
    @Headers("Content-Type: application/json;charset=UTF-8")
    Call<SigninResponse> getRoles();
}
