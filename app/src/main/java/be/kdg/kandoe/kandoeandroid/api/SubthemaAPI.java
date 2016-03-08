package be.kdg.kandoe.kandoeandroid.api;

import java.util.List;

import be.kdg.kandoe.kandoeandroid.pojo.Subthema;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

public interface SubthemaAPI {
    @GET("/api/subthemas")
    Call<List<Subthema>> getSubThemas();

    @GET("/api/subthemas/{id}")
    Call<Subthema> getSubThema(@Path("id") String id);
}
