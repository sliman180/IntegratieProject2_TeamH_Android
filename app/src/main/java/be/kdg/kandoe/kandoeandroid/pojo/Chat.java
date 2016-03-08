package be.kdg.kandoe.kandoeandroid.pojo;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private int id;
    private String naam;
    private List<Bericht> berichten = new ArrayList<>();
    private Cirkelsessie cirkelsessie;

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

    public List<Bericht> getBerichten() {
        return berichten;
    }

    public void setBerichten(List<Bericht> berichten) {
        this.berichten = berichten;
    }

    public Cirkelsessie getCirkelsessie() {
        return cirkelsessie;
    }

    public void setCirkelsessie(Cirkelsessie cirkelsessie) {
        this.cirkelsessie = cirkelsessie;
    }
}
