package be.kdg.kandoe.kandoeandroid.pojo.response;

import com.google.gson.annotations.SerializedName;

public class Subthema {
    @SerializedName("id")
    private int id;
    @SerializedName("naam")
    private String naam;
    @SerializedName("beschrijving")
    private String beschrijving;
    @SerializedName("hoofdthema")
    private Hoofdthema hoofdthema;

    public Organisatie getOrganisatie() {
        return organisatie;
    }

    public void setOrganisatie(Organisatie organisatie) {
        this.organisatie = organisatie;
    }

    private Organisatie organisatie;

    public Hoofdthema getHoofdthema() {
        return hoofdthema;
    }

    public void setHoofdthema(Hoofdthema hoofdthema) {
        this.hoofdthema = hoofdthema;
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

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }


}
