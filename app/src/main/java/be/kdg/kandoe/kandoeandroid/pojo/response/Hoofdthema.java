package be.kdg.kandoe.kandoeandroid.pojo.response;

import com.google.gson.annotations.SerializedName;

public class Hoofdthema {


    @SerializedName("id")
    private int id;
    @SerializedName("naam")
    private String naam;
    @SerializedName("beschrijving")
    private String beschrijving;
    @SerializedName("organisatie")
    private Organisatie organisatie;
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

    public Organisatie getOrganisatie() {
        return organisatie;
    }

    public void setOrganisatie(Organisatie organisatie) {
        this.organisatie = organisatie;
    }

    public Gebruiker getGebruiker() {
        return gebruiker;
    }

    public void setGebruiker(Gebruiker gebruiker) {
        this.gebruiker = gebruiker;
    }

//    public List<Subthema> getSubthemas() {
//        return subthemas;
//    }
//
//    public void setSubthemas(List<Subthema> subthemas) {
//        this.subthemas = subthemas;
//    }
//
//    public List<Tag> getTags() {
//        return tags;
//    }
//
//    public void setTags(List<Tag> tags) {
//        this.tags = tags;
//    }
}
