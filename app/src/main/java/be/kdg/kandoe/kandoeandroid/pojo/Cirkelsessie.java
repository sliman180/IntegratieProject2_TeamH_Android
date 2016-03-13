package be.kdg.kandoe.kandoeandroid.pojo;


import java.util.List;

public class Cirkelsessie {
    private int id;
    private String naam;
    private int aantalCirkels;
    private int maxAantalKaarten;
//    private List<Spelkaart> spelkaarten;


//    public List<Spelkaart> getSpelkaarten() {
//        return spelkaarten;
//    }
//
//    public void setSpelkaarten(List<Spelkaart> spelkaarten) {
//        this.spelkaarten = spelkaarten;
//    }

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
