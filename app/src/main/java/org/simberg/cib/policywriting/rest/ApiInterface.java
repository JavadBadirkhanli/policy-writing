package org.simberg.cib.policywriting.rest;

import org.simberg.cib.policywriting.models.Auth;
import org.simberg.cib.policywriting.models.AuthResult;
import org.simberg.cib.policywriting.models.Authorization;
import org.simberg.cib.policywriting.models.ContractDetails;
import org.simberg.cib.policywriting.models.ContractOperation;
import org.simberg.cib.policywriting.models.ContractPrice;
import org.simberg.cib.policywriting.models.Contracts;
import org.simberg.cib.policywriting.models.Person;
import org.simberg.cib.policywriting.models.SearchContract;
import org.simberg.cib.policywriting.models.SignContract;
import org.simberg.cib.policywriting.models.TerminateContract;
import org.simberg.cib.policywriting.models.TerminationResult;
import org.simberg.cib.policywriting.models.Transaction;
import org.simberg.cib.policywriting.models.Vehicle;
import org.simberg.cib.policywriting.models.request.Blank;
import org.simberg.cib.policywriting.models.response.ContractTermination;
import org.simberg.cib.policywriting.models.response.Result;
import org.simberg.cib.policywriting.models.response.SigningResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by javadbadirkhanly on 8/18/17.
 */

public interface ApiInterface {

    @POST("mobile-proxy/start-registration")
    Call<String> startRegistration();

    @Headers("Content-Type: application/json")
    @POST("mobile-proxy/auth-params")
    Call<Transaction> authParams(@Header("operationId") String operationId, @Body Auth auth);

    @Headers("Content-Type: application/json")
    @GET("mobile-proxy/auth-result/{transactionId}")
    Call<AuthResult> authResultRegistration(@Header("operationId") String operationId, @Path("transactionId") long transactionId);

    @Headers("Content-Type: application/json")
    @GET("mobile-proxy/auth-result/{transactionId}")
    Call<AuthResult> authResultAuthentication(@Header("operationId") String operationId, @Header("appCode") String appCode, @Path("transactionId") long transactionId);

    @Headers("Content-Type: application/json")
    @POST("mobile-proxy/mismatch-person-info")
    Call<Object> mismatchPersonInfo(@Header("operationId") String operationId, @Header("token") String token);

    @Headers("Content-Type: application/json")
    @POST("mobile-proxy/register-app")
    Call<Object> registerApp(@Header("operationId") String operationId, @Header("appCode") String appCode);

    @Headers("Content-Type: application/json")
    @POST("mobile-proxy/start-authentication")
    Call<String> startAuthentication(@Header("appCode") String appCode);

    @Headers("Content-Type: application/json")
    @GET("mobile-proxy/authorizations")
    Call<Authorization> authorizations(@Header("operationId") String operationId, @Header("token") String token, @Header("appCode") String appCode, @Query("phoneNumber") String phoneNumber, @Query("asanID") String asanID);

    @Headers("Content-Type: application/json")
    @POST("mobile-proxy/start-contract-operation")
    Call<String> startContractOperation(@Header("token") String token, @Header("appCode") String appCode, @Body ContractOperation contractOperation);

    @Headers("Content-Type: application/json")
    @GET("mobile-proxy/vehicle-info")
    Call<Vehicle> vehicleInfo(@Header("operationId") String operationId, @Header("token") String token, @Header("appCode") String appCode, @Query("certificateNumber") String certificateNumber, @Query("carNumber") String carNumber);

    @Headers("Content-Type: application/json")
    @POST("mobile-proxy/non-mia-vehicle-info")
    Call<Vehicle> nonMiaVehicleInfo(@Header("operationId") String operationId, @Header("appCode") String appCode, @Header("token") String token, @Body Vehicle vehicle);

    @Headers("Content-Type: application/json")
    @POST("mobile-proxy/mismatch-vehicle-info")
    Call<Object> mismatchVehicleInfo(@Header("operationId") String operationId, @Header("token") String token, @Header("appCode") String appCode);

    @Headers("Content-Type: application/json")
    @GET("mobile-proxy/natural-person-info")
    Call<Person> naturalPersonInfo(@Header("operationId") String operationId, @Header("token") String token, @Header("appCode") String appCode, @Query("idDocument") String idDocument);

    @Headers("Content-Type: application/json")
    @GET("mobile-proxy/juridical-person-info")
    Call<Person> juridicalPersonInfo(@Header("operationId") String operationId, @Header("token") String token, @Header("appCode") String appCode, @Query("tin") String tin);

    @Headers("Content-Type: application/json")
    @GET("mobile-proxy/non-resident-person-info")
    Call<Person> nonResidentPersonInfo(@Header("operationId") String operationId, @Header("token") String token, @Header("appCode") String appCode, @Query("rcNumber") String rcNumber, @Query("docType") String docType);

    @Headers("Content-Type: application/json")
    @POST("/mobile-proxy/non-resident-person-info")
    Call<Person> sendNonResidentPersonInfo(@Header("operationId") String operationId, @Header("token") String token, @Header("appCode") String appCode, @Body Person nonResidentPerson);

    @Headers("Content-Type: application/json")
    @GET("mobile-proxy/contract-price")
    Call<ContractPrice> contractPrice(@Header("operationId") String operationId, @Header("token") String token, @Header("appCode") String appCode);

    @Headers("Content-Type: application/json")
    @GET("mobile-proxy/contract-price")
    Call<ContractPrice> borderContractPrice(@Header("operationId") String operationId, @Header("token") String token, @Header("appCode") String appCode, @Query("period") String period);

    @Headers("Content-Type: application/json")
    @POST("mobile-proxy/sign-contract")
    Call<Transaction> signContract(@Header("operationId") String operationId, @Header("token") String token, @Header("appCode") String appCode, @Body SignContract signContract);

    @Headers("Content-Type: application/json")
    @GET("mobile-proxy/signing-result")
    Call<SigningResult> signingResult(@Header("operationId") String operationId, @Header("token") String token, @Header("appCode") String appCode);

    @Headers("Content-Type: application/json")
    @POST("mobile-proxy/cancel-contract-operation")
    Call<Object> cancelContractOperation(@Header("operationId") String operationId, @Header("token") String token, @Header("appCode") String appCode);

    @Headers("Content-Type: application/json")
    @POST("mobile-proxy/search-authorized-contracts")
    Call<Contracts> searchAuthorizedContracts(@Header("operationId") String operationId, @Header("token") String token, @Header("appCode") String appCode, @Body SearchContract searchContract);

    @Headers("Content-Type: application/json")
    @POST("mobile-proxy/search-my-contracts")
    Call<Contracts> searchMyContracts(@Header("token") String token, @Header("appCode") String appCode, @Body SearchContract searchContract);

    @GET("mobile-proxy/contract/{contractNumber}")
    Call<ContractDetails> getContract(@Header("token") String token, @Header("appCode") String appCode, @Path("contractNumber") String contractNumber);

    @GET("mobile-proxy/contract-document/{contractNumber}")
    Call<String> getContractDocument(@Header("token") String token, @Header("appCode") String appCode, @Path("contractNumber") String contractNumber);

    @Headers("Content-Type: application/json")
    @GET("mobile-proxy/termination-contract")
    Call<ContractTermination> getTerminationContract(@Header("token") String token, @Header("appCode") String appCode, @Query("contractNumber") String contractNumber, @Query("operationType") String operationType);

    @Headers("Content-Type: application/json")
    @POST("mobile-proxy/terminate-contract")
    Call<TerminationResult> terminateContract(@Header("operationId") String operationId, @Header("token") String token, @Header("appCode") String appCode, @Body TerminateContract terminateContract);

    @Headers("Content-Type: application/json")
    @POST("mobile-proxy/blank")
    Call<Result> blankOperation(@Header("operationId") String operationId, @Header("appCode") String appCode, @Header("token") String token, @Body Blank blank);

    @Headers("Content-Type: application/json")
    @POST("mobile-proxy/vehicle-horse-power/{horsePower}")
    Call<Vehicle> horsePower(@Header("operationId") String operationId, @Header("appCode") String appCode, @Header("token") String token, @Path("horsePower") int horsePower);

    @Headers("Content-Type: application/json")
    @POST("mobile-proxy/green-card-contract")
    Call<SigningResult> createContract(@Header("operationId") String operationId, @Header("appCode") String appCode, @Header("token") String token, @Body SignContract signContract);
}
