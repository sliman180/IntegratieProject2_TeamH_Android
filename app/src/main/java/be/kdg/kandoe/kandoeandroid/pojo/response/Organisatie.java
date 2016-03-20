package be.kdg.kandoe.kandoeandroid.pojo.response;


import com.google.gson.annotations.SerializedName;

public class Organisatie {

    @SerializedName("id")
    private int id;
    @SerializedName("naam")
    private String naam;
    @SerializedName("beschrijving")
    private String beschrijving;
    @SerializedName("gebruiker")
    private Gebruiker gebruiker;

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

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public Gebruiker getGebruiker() {
        return gebruiker;
    }

    public void setGebruiker(Gebruiker gebruiker) {
        this.gebruiker = gebruiker;
    }


}
