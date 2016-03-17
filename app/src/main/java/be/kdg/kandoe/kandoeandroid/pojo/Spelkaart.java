package be.kdg.kandoe.kandoeandroid.pojo;


import be.kdg.kandoe.kandoeandroid.pojo.response.Kaart;

public class Spelkaart {

    private int id;

    private Kaart kaart;

    private int positie;


    public Spelkaart(int id, Kaart kaart, int positie) {
        this.id = id;
        this.kaart = kaart;
        this.positie = positie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Kaart getKaart() {
        return kaart;
    }

    public void setKaart(Kaart kaart) {
        this.kaart = kaart;
    }

    public int getPositie() {
        return positie;
    }

    public void setPositie(int positie) {
        this.positie = positie;
    }
}
