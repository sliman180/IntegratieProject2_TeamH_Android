package be.kdg.kandoe.kandoeandroid.pojo;

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

}
