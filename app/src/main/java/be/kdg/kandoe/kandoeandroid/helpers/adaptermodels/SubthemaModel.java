package be.kdg.kandoe.kandoeandroid.helpers.adaptermodels;

public class SubthemaModel {
    private String counter;
    private String title;
    private String beschrijving;
    private String organisatie;
    private String hoofdthema;


    //    private String aantalHoofdthemas;

    private boolean isGroupHeader = false;

    public SubthemaModel(String counter, String title, String beschrijving, String organisatie, String hoofdthema) {
        super();
        this.hoofdthema = hoofdthema;
        this.organisatie = organisatie;
        this.beschrijving = beschrijving;
        this.title = title;
        this.counter = counter;
        isGroupHeader = false;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public String getAantalHoofdthemas() {
//        return aantalHoofdthemas;
//    }
//
//    public void setAantalHoofdthemas(String aantalHoofdthemas) {
//        this.aantalHoofdthemas = aantalHoofdthemas;
//    }


    public String getOrganisatie() {
        return organisatie;
    }

    public void setOrganisatie(String organisatie) {
        this.organisatie = organisatie;
    }

    public String getHoofdthema() {
        return hoofdthema;
    }

    public void setHoofdthema(String hoofdthema) {
        this.hoofdthema = hoofdthema;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }


    public boolean isGroupHeader() {
        return isGroupHeader;
    }
}
