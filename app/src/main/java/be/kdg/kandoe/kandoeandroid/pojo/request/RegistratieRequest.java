package be.kdg.kandoe.kandoeandroid.pojo.request;


public class RegistratieRequest {

    private String gebruikersnaam;
    private String wachtwoord;
    private String confirmatie;

    public RegistratieRequest() {
        //
    }

    public RegistratieRequest(String gebruikersnaam, String wachtwoord, String confirmatie) {
        this.gebruikersnaam = gebruikersnaam;
        this.wachtwoord = wachtwoord;
        this.confirmatie = confirmatie;
    }

    public String getGebruikersnaam() {
        return gebruikersnaam;
    }

    public void setGebruikersnaam(String gebruikersnaam) {
        this.gebruikersnaam = gebruikersnaam;
    }

    public String getWachtwoord() {
        return wachtwoord;
    }

    public void setWachtwoord(String wachtwoord) {
        this.wachtwoord = wachtwoord;
    }

    public String getConfirmatie() {
        return confirmatie;
    }

    public void setConfirmatie(String confirmatie) {
        this.confirmatie = confirmatie;
    }
}
