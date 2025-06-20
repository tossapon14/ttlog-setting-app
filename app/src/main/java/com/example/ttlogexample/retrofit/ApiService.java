package com.example.ttlogexample.retrofit;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by
 */
public interface ApiService {

    public static final String CLIENT_ID = "83cc7038047b42f7bb3c14628dd54c35";
    public static final String CLIENT_SECRET = "ecd830bef3ada74ba8ab3edfa8bc0f0d";


    @POST("/lockRecords/fromLock")
    @FormUrlEncoded
    Call<Error> uploadRecords(@Field("lockId") int lockId, @Field("records") String records);

//    @POST("/check/getNbPlatformIpAndPort")
//    Call<String> getNBData();

    @POST("/room/registerNb")
    @FormUrlEncoded
    Call<Error> registerNb(@Field("lockId") int lockId, @Field("nbNodeId") String nbNodeId, @Field("nbCardNumber") String nbCardNumber, @Field("nbRssi") int nbRssi, @Field("nbOperator") String nbOperator);

    //
//    @POST("/plug/uploadDetail")
//    @FormUrlEncoded
//    Call<Error> plugUploadDetail(@Field("plugId") int plugId, @Field("modelNum") String modelNum, @Field("hardwareRevision") String hardwareRevision, @Field("firmwareRevision") String firmwareRevision, @Field("networkName") String networkName);
//
//    @POST("/plug/upgradeSuccess")
//    @FormUrlEncoded
//    Call<Error> plugUpgradeSuccess(@Field("plugId") int plugId);
//
    @POST("/oauth2/token")
    @FormUrlEncoded
    Call<String> auth(@Field("client_id") String clientId, @Field("client_secret") String clientSecret, @Field("username") String username, @Field("password") String password);

    @POST("/v3/lock/list")
    @FormUrlEncoded
    Call<String> getLockList(@Field("clientId") String clientId, @Field("accessToken") String accessToken, @Field("pageNo") int pageNo, @Field("pageSize") int pageSize, @Field("date") long date);

    @POST("/v3/lock/initialize")
    @FormUrlEncoded
    Call<ResponseBody> lockInit(@Field("clientId") String clientId, @Field("accessToken") String accessToken, @Field("lockData") String lockData, @Field("lockAlias") String alias, @Field("date") long date);

    @GET("/v3/key/list")
    Call<ResponseBody> getUserKeyList(@QueryMap Map<String, String> params);

    @POST("/v3/lock/resetKey")
    @FormUrlEncoded
    Call<ResponseBody> restEkey(@Field("clientId") String clientId, @Field("accessToken") String accessToken, @Field("lockId") int lockId, @Field("date") long date);

    @POST("/v3/lock/delete")
    @FormUrlEncoded
    Call<ResponseBody> deleteLock(@Field("clientId") String clientId, @Field("accessToken") String accessToken, @Field("lockId") int lockId, @Field("date") long date);

    @POST("/v3/lock/resetKeyboardPwd")
    @FormUrlEncoded
    Call<ResponseBody> resetPasscode(@Field("clientId") String clientId, @Field("accessToken") String accessToken, @Field("lockId") int lockId, @Field("pwdInfo") String pwdInfo, @Field("timestamp") long timestamp, @Field("date") long date);

    @POST("/v3/lock/updateLockData")
    @FormUrlEncoded
    Call<ResponseBody> updateLockData(@Field("clientId") String clientId, @Field("accessToken") String accessToken, @Field("lockId") int lockId, @Field("lockData") String lockData, @Field("date") long date);

    @POST("/v3/lock/changeAdminKeyboardPwd")
    @FormUrlEncoded
    Call<ResponseBody> changeAdminPasscode(@FieldMap Map<String, String> params);

    @POST("/v3/fingerprint/add")
    @FormUrlEncoded
    Call<ResponseBody> addFingerprint(@FieldMap Map<String, String> params);

    @GET("/v3/fingerprint/list")
    Call<ResponseBody> getUserFingerprintList(@QueryMap Map<String, String> params);

    @POST("/v3/fingerprint/delete")
    @FormUrlEncoded
    Call<ResponseBody> deleteFingerprint(@FieldMap Map<String, String> params);

    @POST("/v3/fingerprint/clear")
    @FormUrlEncoded
    Call<ResponseBody> clearFingerprints(@Field("clientId") String clientId, @Field("accessToken") String accessToken, @Field("lockId") int lockId, @Field("date") long date);

    @POST("/v3/identityCard/add")
    @FormUrlEncoded
    Call<ResponseBody> addICCard(@FieldMap Map<String, String> params);

    @GET("/v3/identityCard/list")
    Call<ResponseBody> getUserICCardList(@QueryMap Map<String, String> params);

    @POST("/v3/identityCard/delete")
    @FormUrlEncoded
    Call<ResponseBody> deleteICCard(@FieldMap Map<String, String> params);

    @POST("/v3/identityCard/changePeriod ")
    @FormUrlEncoded
    Call<ResponseBody> modifyICCardPeriod(@FieldMap Map<String, String> params);

    @POST("/v3/identityCard/clear")
    @FormUrlEncoded
    Call<ResponseBody> clearICCards(@Field("clientId") String clientId, @Field("accessToken") String accessToken, @Field("lockId") int lockId, @Field("date") long date);

    @POST("/v3/gateway/upgradeCheck")
    @FormUrlEncoded
    Call<String> gatewayUpgradeCheck(@Field("clientId") String clientId, @Field("accessToken") String accessToken, @Field("gatewayId") int gatewayId, @Field("date") long date);

    @POST("/v3/gateway/isInitSuccess")
    @FormUrlEncoded
    Call<String> gatewayIsInitSuccess(@Field("clientId") String clientId, @Field("accessToken") String accessToken, @Field("gatewayNetMac") String gatewayNetMac, @Field("date") long date);

    @POST("/v3/gateway/uploadDetail")
    @FormUrlEncoded
    Call<String> uploadGatewayDetail(@Field("clientId") String clientId, @Field("accessToken") String accessToken, @Field("gatewayId") int gatewayId, @Field("modelNum") String modelNum, @Field("hardwareRevision") String hardwareRevision, @Field("firmwareRevision") String firmwareRevision, @Field("networkName") String networkName, @Field("date") long date);

    @POST("/v3/gateway/list")
    @FormUrlEncoded
    Call<String> getGatewayList(@Field("clientId") String clientId, @Field("accessToken") String accessToken, @Field("pageNo") int pageNo, @Field("pageSize") int pageSize, @Field("date") long date);

    @POST("/v3/lock/upgradeCheck")
    @FormUrlEncoded
    Call<String> lockUpgradeCheck(@Field("clientId") String clientId, @Field("accessToken") String accessToken, @Field("lockId") int lockId, @Field("date") long date);

    @POST("/v3/lock/upgradeRecheck")
    @FormUrlEncoded
    Call<String> lockUpgradeCheckAgain(@Field("clientId") String clientId, @Field("accessToken") String accessToken, @Field("firmwareInfo") String firmwareInfo, @Field("lockId") int lockId, @Field("date") long date);

    @POST("/v3/wirelessKeyboard/add ")
    @FormUrlEncoded
    Call<ResponseBody> addWirelessKeypad(@FieldMap Map<String, String> params);

    @POST("/v3/lock/updateLockData")
    @FormUrlEncoded
    Call<ResponseBody> updateLockData(@FieldMap Map<String, String> params);


    @POST("/auth/login")
    @FormUrlEncoded
    Call<String> authNest(@Field("email") String email, @Field("password") String password);

    @POST("/auth/login/hotel")
    @FormUrlEncoded
    Call<String> authWithHotelNest(@Field("email") String email, @Field("password") String password, @Field("hotel") String hotel);

    @POST("/user/register")
    @FormUrlEncoded
    Call<String> registerNest(@Field("email") String email, @Field("password") String password, @Field("name") String name, @Field("hotel") String hotel, @Field("tel") String tel);

    @GET("/lock/list")
    Call<String> getLockListNest(@Header("Authorization") String token);

    @POST("/lock/initialize")
    @FormUrlEncoded
    Call<ResponseBody> lockInitNest(@Header("Authorization") String token, @Field("lockData") String lockData, @Field("mac") String mac,@Field("building") String building ,@Field("floor") String floor,@Field("roomNumber") String room, @Field("lockAlias") String alias,  @Field("date") long date);

    @GET("/gateways/list")
    Call<String> getGatewayListNest(@Header("Authorization") String token);

    @POST("/user/getinfo")
    Call<String> userInfoNest(@Header("Authorization") String token);

    @POST("/gateways/isInitSuccess")
    @FormUrlEncoded
    Call<String> gatewayIsInitSuccessNest(@Header("Authorization") String token, @Field("gatewayNetMac") String gatewayNetMac, @Field("date") long date);

    @POST("/gateways/uploadDetail")
    @FormUrlEncoded
    Call<String> uploadGatewayDetailNest(@Header("Authorization") String token, @Field("gatewayId") int gatewayId, @Field("modelNum") String modelNum, @Field("hardwareRevision") String hardwareRevision, @Field("firmwareRevision") String firmwareRevision, @Field("networkName") String networkName, @Field("date") long date);

    @GET("/auth/hotelList")
    Call<String> getHotelNest(@Header("Authorization") String token);
    @GET("/user/get-hotels-building")
    Call<String> getUserBuildingNest(@Header("Authorization") String token);

    @POST("/lock/delete")
    @FormUrlEncoded
    Call<String> deleteLockNest(@Header("Authorization") String token,@Field("lockId") int lock,@Field("date") long date);
    @POST("cards/identityCard/add/mobile")
    @FormUrlEncoded
    Call<String> addICCardNest(@Header("Authorization") String token,@FieldMap Map<String, String> params);
    @POST("/cards/identityCard/clear")
    @FormUrlEncoded
    Call<String> clearICCardsNest(@Header("Authorization") String token, @Field("lockId") int lockId, @Field("date") long date);

}
