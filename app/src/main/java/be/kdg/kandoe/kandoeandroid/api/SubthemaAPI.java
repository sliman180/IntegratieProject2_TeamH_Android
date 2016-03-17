package be.kdg.kandoe.kandoeandroid.api;

import java.util.List;

import be.kdg.kandoe.kandoeandroid.pojo.Subthema;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

public interface SubthemaAPI {

    @GET("/api/gebruikers/{id}/subthemas")
    Call<List<Subthema>> getSubThemas(@Path("id") String id);

}
