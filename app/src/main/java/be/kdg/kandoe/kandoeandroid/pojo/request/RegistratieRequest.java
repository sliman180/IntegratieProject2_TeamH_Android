package be.kdg.kandoe.kandoeandroid.pojo.request;


public class RegistratieRequest {

    private String gebruikersnaam;
    private String wachtwoord;
    private String confirmatie;
    private String email;

    public RegistratieRequest() {
        //
    }

    public RegistratieRequest(String gebruikersnaam, String wachtwoord, String confirmatie, String email) {
        this.gebruikersnaam = gebruikersnaam;
        this.wachtwoord = wachtwoord;
        this.confirmatie = confirmatie;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
