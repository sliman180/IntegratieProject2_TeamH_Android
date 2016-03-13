package be.kdg.kandoe.kandoeandroid.pojo;

import java.util.ArrayList;
import java.util.List;

public class Organisatie {
    private int id;
    private String naam;
    private String beschrijving;
    private Gebruiker gebruiker;
//    private List<Hoofdthema> hoofdthemas = new ArrayList<>();

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

//    public List<Hoofdthema> getHoofdthemas() {
//        return hoofdthemas;
//    }
//
//    public void setHoofdthemas(List<Hoofdthema> hoofdthemas) {
//        this.hoofdthemas = hoofdthemas;
//    }
}
