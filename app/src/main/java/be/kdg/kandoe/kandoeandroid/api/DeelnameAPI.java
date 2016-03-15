package be.kdg.kandoe.kandoeandroid.api;

import java.util.List;

import be.kdg.kandoe.kandoeandroid.pojo.Deelname;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface DeelnameAPI {

    @POST("/api/deelnames/{id}")
    Call<Void> doeDeelname(@Path("id") String Id);

    @GET("/api/gebruikers/deelnames")
    Call<List<Deelname>> getDeelnames();



}
