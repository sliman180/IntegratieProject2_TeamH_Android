package be.kdg.kandoe.kandoeandroid.pojo.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    @SerializedName("id")
    private int id;
    @SerializedName("naam")
    private String naam;
    @SerializedName("berichten")
    private List<Bericht> berichten = new ArrayList<>();
    @SerializedName("cirkelsessie")
    private Cirkelsessie cirkelsessie;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public List<Bericht> getBerichten() {
        return berichten;
    }

    public void setBerichten(List<Bericht> berichten) {
        this.berichten = berichten;
    }

    public Cirkelsessie getCirkelsessie() {
        return cirkelsessie;
    }

    public void setCirkelsessie(Cirkelsessie cirkelsessie) {
        this.cirkelsessie = cirkelsessie;
    }
}
