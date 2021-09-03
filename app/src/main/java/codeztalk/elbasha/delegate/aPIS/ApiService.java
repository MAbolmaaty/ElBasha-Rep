package codeztalk.elbasha.delegate.aPIS;


import java.util.List;

import codeztalk.elbasha.delegate.aPIS.requests.AddClientRequest;
import codeztalk.elbasha.delegate.aPIS.requests.AddCreditRequest;
import codeztalk.elbasha.delegate.aPIS.requests.AddInvoiceRequest;
import codeztalk.elbasha.delegate.aPIS.requests.AddLocationRequest;
import codeztalk.elbasha.delegate.aPIS.requests.Report1Request;
import codeztalk.elbasha.delegate.aPIS.requests.ReportRequest;
import codeztalk.elbasha.delegate.aPIS.requests.UpdateClientRequest;
import codeztalk.elbasha.delegate.aPIS.responses.AddClientResponse;
import codeztalk.elbasha.delegate.aPIS.responses.AddCreditResponse;
import codeztalk.elbasha.delegate.aPIS.responses.AddInvoiceResponse;
import codeztalk.elbasha.delegate.aPIS.responses.NoResponse;
import codeztalk.elbasha.delegate.activities.categoryProduct.CategoryProductModel;
import codeztalk.elbasha.delegate.models.ClientInvoiceModel;
import codeztalk.elbasha.delegate.models.ClientModel;
import codeztalk.elbasha.delegate.models.EmployeeModel;
import codeztalk.elbasha.delegate.models.PocketModel;
import codeztalk.elbasha.delegate.models.ProductModel;
import codeztalk.elbasha.delegate.models.Report10Model;
import codeztalk.elbasha.delegate.models.Report5Model;
import codeztalk.elbasha.delegate.models.Report6Model;
import codeztalk.elbasha.delegate.models.Report7Model;
import codeztalk.elbasha.delegate.models.Report8Model;
import codeztalk.elbasha.delegate.models.Report9Model;
import codeztalk.elbasha.delegate.models.ReportModel;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

import static codeztalk.elbasha.delegate.helper.constants.addEmployeeLocation;
import static codeztalk.elbasha.delegate.helper.constants.deleteClient;
import static codeztalk.elbasha.delegate.helper.constants.deleteInvoice;
import static codeztalk.elbasha.delegate.helper.constants.deleteRepresentativeOutcome;
import static codeztalk.elbasha.delegate.helper.constants.getAllInvoices;
import static codeztalk.elbasha.delegate.helper.constants.getCategorizeProducts;
import static codeztalk.elbasha.delegate.helper.constants.getClientsByEmpId;
import static codeztalk.elbasha.delegate.helper.constants.getCreditInvoices;
import static codeztalk.elbasha.delegate.helper.constants.getEmployeeByUserName;
import static codeztalk.elbasha.delegate.helper.constants.getInvoices;
import static codeztalk.elbasha.delegate.helper.constants.getPocket;
import static codeztalk.elbasha.delegate.helper.constants.getProducts;
import static codeztalk.elbasha.delegate.helper.constants.getSalesSummery;
import static codeztalk.elbasha.delegate.helper.constants.postClient;
import static codeztalk.elbasha.delegate.helper.constants.postClientRecipt;
import static codeztalk.elbasha.delegate.helper.constants.postInvoice;
import static codeztalk.elbasha.delegate.helper.constants.postInvoiceImages;
import static codeztalk.elbasha.delegate.helper.constants.putClient;
import static codeztalk.elbasha.delegate.helper.constants.report10;
import static codeztalk.elbasha.delegate.helper.constants.report5;
import static codeztalk.elbasha.delegate.helper.constants.report6;
import static codeztalk.elbasha.delegate.helper.constants.report7;
import static codeztalk.elbasha.delegate.helper.constants.report8;
import static codeztalk.elbasha.delegate.helper.constants.report9;


public interface ApiService {

    @GET(getCategorizeProducts)
    Call<List<CategoryProductModel>> getCategoriesProducts(
            @Query("id") String id,
            @Header("Authorization") String token);

    @GET(getProducts)
    Call<List<ProductModel>> getProducts(
            @Query("id") String id,
            @Header("Authorization") String token);

    @GET(getClientsByEmpId)
    Call<List<ClientModel>> getClients(
            @Query("id") String id,
            @Header("Authorization") String token);




    @GET(getEmployeeByUserName)
    Call<EmployeeModel> getEmployeeProfileByName(
            @Query("id") String id,
            @Header("Authorization") String token);




    @POST(postClient)
    Call<AddClientResponse> addNewClient(
            @Header("Authorization") String token,
            @Body AddClientRequest body);

    @PUT(putClient)
    Call<AddClientResponse> updateClient(
            @Query("id") String id,
            @Header("Authorization") String token,
            @Body UpdateClientRequest body);


    @DELETE(deleteClient)
    Call<NoResponse> deleteClient(
            @Query("id") String id,
            @Header("Authorization") String token);




    @GET(getPocket)
    Call<List<PocketModel>> getEmployeePocket(
            @Query("id") String id,
            @Header("Authorization") String token);


    @DELETE(deleteRepresentativeOutcome)
    Call<NoResponse> deleteEmployeePocket(
            @Query("id") String id,
            @Header("Authorization") String token);

    @POST(postInvoice)
    Call<AddInvoiceResponse> addNewInvoice(
            @Header("Authorization") String token,
            @Body AddInvoiceRequest body);


    @POST(postClientRecipt)
    Call<AddCreditResponse> addUnPaidInvoice(
            @Header("Authorization") String token,
            @Body AddCreditRequest body);


    @GET(getInvoices)
    Call<List<ClientInvoiceModel>> getClientInvoices(
            @Query("id") String id,
            @Header("Authorization") String token);

    @GET(getCreditInvoices)
    Call<List<ClientInvoiceModel>> getCreditInvoices(
            @Query("id") String id,
            @Header("Authorization") String token);

    @GET(getAllInvoices)
    Call<List<ClientInvoiceModel>> getAllInvoices(
            @Query("id") String id,
            @Header("Authorization") String token);

    @DELETE(deleteInvoice)
    Call<NoResponse> deleteInvoice(
            @Query("id") String id,
            @Header("Authorization") String token);


    @POST(getSalesSummery)
    Call<List<ReportModel>> getEmployeeReport(
            @Header("Authorization") String token,
            @Body ReportRequest body);

    @Multipart
    @POST(postInvoiceImages)
    Call<ResponseBody> addSignature(
            @Query("id") String id,
            @Header("Authorization") String token,
            @Part MultipartBody.Part photo);


    @POST(addEmployeeLocation)
    Call<NoResponse> addEmployeeLocation(
            @Header("Authorization") String token,
            @Body AddLocationRequest body);









//    @POST(report1)
//    Call<List<Report1Model>> getReport1(
//            @Header("Authorization") String token,
//            @Body Report1Request body);
//
//    @POST(report2)
//    Call<List<Report2Model>> getReport2(
//            @Header("Authorization") String token,
//            @Body ReportRequest body);
//
//
//    @POST(report3)
//    Call<List<Report3Model>> getReport3(
//            @Header("Authorization") String token,
//            @Body ReportRequest body);
//
//    @POST(report4)
//    Call<List<Report4Model>> getReport4(
//            @Header("Authorization") String token,
//            @Body ReportRequest body);

    @POST(report5)
    Call<List<Report5Model>> getReport5(
            @Header("Authorization") String token,
            @Body Report1Request body);

    @POST(report6)
    Call<List<Report6Model>> getReport6(
            @Header("Authorization") String token,
            @Body Report1Request body);

    @POST(report7)
    Call<List<Report7Model>> getReport7(
            @Header("Authorization") String token,
            @Body Report1Request body);

    @POST(report8)
    Call<List<Report8Model>> getReport8(
            @Header("Authorization") String token,
            @Body Report1Request body);

    @POST(report9)
    Call<List<Report9Model>> getReport9(
            @Header("Authorization") String token,
            @Body ReportRequest body);

    @POST(report10)
    Call<List<Report10Model>> getReport10(
            @Header("Authorization") String token,
            @Body ReportRequest body);

}