package be.kdg.kandoe.kandoeandroid.pojo;

import java.util.ArrayList;
import java.util.List;

public class Subthema {
    private int id;
    private String naam;
    private String beschrijving;
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

    //    private List<Cirkelsessie> cirkelsessies = new ArrayList<>();
//    private List<Kaart> kaarten = new ArrayList<>();

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

//    public Hoofdthema getHoofdthema() {
//        return hoofdthema;
//    }
//
//    public void setHoofdthema(Hoofdthema hoofdthema) {
//        this.hoofdthema = hoofdthema;
//    }
//
//    public List<Cirkelsessie> getCirkelsessies() {
//        return cirkelsessies;
//    }
//
//    public void setCirkelsessies(List<Cirkelsessie> cirkelsessies) {
//        this.cirkelsessies = cirkelsessies;
//    }
//
//    public List<Kaart> getKaarten() {
//        return kaarten;
//    }
//
//    public void setKaarten(List<Kaart> kaarten) {
//        this.kaarten = kaarten;
//    }
}
