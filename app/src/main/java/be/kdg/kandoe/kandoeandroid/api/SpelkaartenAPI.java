package be.kdg.kandoe.kandoeandroid.api;


import be.kdg.kandoe.kandoeandroid.pojo.Spelkaart;
import retrofit.Call;
import retrofit.http.POST;
import retrofit.http.Path;

public interface SpelkaartenAPI {

    @POST("/api/spelkaarten/verschuif/{id}")
    Call<Spelkaart> verschuif(@Path("id") String id);
}
