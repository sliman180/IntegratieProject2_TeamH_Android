package be.kdg.kandoe.kandoeandroid.api;


import be.kdg.kandoe.kandoeandroid.pojo.Spelkaart;
import retrofit.Call;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 *  interface om de spelkaarten web api aan te spreken
 */
public interface SpelkaartAPI {

    @POST("/api/spelkaarten/{id}/verschuif")
    Call<Spelkaart> verschuif(@Path("id") String id);

}
