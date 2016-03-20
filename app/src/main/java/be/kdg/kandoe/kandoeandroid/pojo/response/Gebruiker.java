package be.kdg.kandoe.kandoeandroid.pojo.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import be.kdg.kandoe.kandoeandroid.pojo.response.Kaart;

public class Gebruiker {

    @SerializedName("id")
    private int id;
    @SerializedName("gebruikersnaam")
    private String gebruikersnaam;
    @SerializedName("wachtwoord")
    private String wachtwoord;
    @SerializedName("email")
    private String email;
    @SerializedName("voornaam")
    private String voornaam;
    @SerializedName("familienaam")
    private String familienaam;
    @SerializedName("telefoon")
    private String telefoon;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }

    public String getFamilienaam() {
        return familienaam;
    }

    public void setFamilienaam(String familienaam) {
        this.familienaam = familienaam;
    }

    public String getTelefoon() {
        return telefoon;
    }

    public void setTelefoon(String telefoon) {
        this.telefoon = telefoon;
    }
}
