package be.kdg.kandoe.kandoeandroid.pojo;

import com.google.gson.annotations.SerializedName;

public class Deelname {

    @SerializedName("id")
    private int id;
    @SerializedName("aangemaakteKaarten")
    private int aangemaakteKaarten;
    @SerializedName("medeorganisator")
    private boolean medeorganisator;
    @SerializedName("cirkelsessie")
    private Cirkelsessie cirkelsessie;
    @SerializedName("gebruiker")
    private Gebruiker gebruiker;
    @SerializedName("datum")
    private long datum;
    @SerializedName("aanDeBeurt")
    private boolean isAanDeBeurt;

    public boolean isAanDeBeurt() {
        return isAanDeBeurt;
    }

    public void setIsAanDeBeurt(boolean isAanDeBeurt) {
        this.isAanDeBeurt = isAanDeBeurt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAangemaakteKaarten() {
        return aangemaakteKaarten;
    }

    public void setAangemaakteKaarten(int aangemaakteKaarten) {
        this.aangemaakteKaarten = aangemaakteKaarten;
    }

    public boolean isMedeorganisator() {
        return medeorganisator;
    }

    public void setMedeorganisator(boolean medeorganisator) {
        this.medeorganisator = medeorganisator;
    }

    public Cirkelsessie getCirkelsessie() {
        return cirkelsessie;
    }

    public void setCirkelsessie(Cirkelsessie cirkelsessie) {
        this.cirkelsessie = cirkelsessie;
    }

    public Gebruiker getGebruiker() {
        return gebruiker;
    }

    public void setGebruiker(Gebruiker gebruiker) {
        this.gebruiker = gebruiker;
    }

    public long getDatum() {
        return datum;
    }

    public void setDatum(long datum) {
        this.datum = datum;
    }
}
