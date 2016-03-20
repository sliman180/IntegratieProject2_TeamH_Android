package be.kdg.kandoe.kandoeandroid.pojo.response;

public class RegisterDetails extends Credentials{

    private String email;

    public RegisterDetails(String gebruikersnaam, String wachtwoord, String email) {
        super(gebruikersnaam, wachtwoord);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
