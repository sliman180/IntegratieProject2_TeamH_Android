package be.kdg.kandoe.kandoeandroid.pojo.response;


import com.google.gson.annotations.SerializedName;


public class Cirkelsessie {

    @SerializedName("id")
    private int id;
    @SerializedName("naam")
    private String naam;
    @SerializedName("status")
    private String status;
    @SerializedName("aantalCirkels")
    private int aantalCirkels;
    @SerializedName("maxAantalKaarten")
    private int maxAantalKaarten;
    @SerializedName("startDatum")
    private long startDatum;
    @SerializedName("subthema")
    private Subthema subthema;
    @SerializedName("gebruiker")
    private Gebruiker gebruiker;

    public long getStartDatum() {
        return startDatum;
    }

    public void setStartDatum(long startDatum) {
        this.startDatum = startDatum;
    }

    public Subthema getSubthema() {
        return subthema;
    }

    public void setSubthema(Subthema subthema) {
        this.subthema = subthema;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Gebruiker getGebruiker() {
        return gebruiker;
    }

    public void setGebruiker(Gebruiker gebruiker) {
        this.gebruiker = gebruiker;
    }

    public int getAantalCirkels() {
        return aantalCirkels;
    }

    public void setAantalCirkels(int aantalCirkels) {
        this.aantalCirkels = aantalCirkels;
    }

    public int getMaxAantalKaarten() {
        return maxAantalKaarten;
    }

    public void setMaxAantalKaarten(int maxAantalKaarten) {
        this.maxAantalKaarten = maxAantalKaarten;
    }
}
