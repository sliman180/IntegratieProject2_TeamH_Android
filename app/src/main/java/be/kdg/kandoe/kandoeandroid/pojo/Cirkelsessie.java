package be.kdg.kandoe.kandoeandroid.pojo;


import java.util.List;

public class Cirkelsessie {
    private int id;
    private String naam;
    private String status;
    private int aantalCirkels;
    private int maxAantalKaarten;
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
