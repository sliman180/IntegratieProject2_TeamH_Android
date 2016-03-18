package be.kdg.kandoe.kandoeandroid.pojo.request;


import org.joda.time.DateTime;


public class BerichtRequest {

    private String tekst;

    private DateTime datum;

    private int cirkelsessie;

    private int gebruiker;

    public BerichtRequest(String tekst, int gebruiker) {
        this.tekst = tekst;
        this.gebruiker = gebruiker;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public DateTime getDatum() {
        return datum;
    }

    public void setDatum(DateTime datum) {
        this.datum = datum;
    }

    public int getCirkelsessie() {
        return cirkelsessie;
    }

    public void setCirkelsessie(int cirkelsessie) {
        this.cirkelsessie = cirkelsessie;
    }

    public int getGebruiker() {
        return gebruiker;
    }

    public void setGebruiker(int gebruiker) {
        this.gebruiker = gebruiker;
    }
}
