package be.kdg.kandoe.kandoeandroid.pojo.response;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Spelkaart spelkaart = (Spelkaart) o;

        return kaart.equals(spelkaart.kaart);

    }

    @Override
    public int hashCode() {
        return kaart.hashCode();
    }
}
