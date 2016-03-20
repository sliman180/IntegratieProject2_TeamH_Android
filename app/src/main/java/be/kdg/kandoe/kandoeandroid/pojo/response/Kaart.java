package be.kdg.kandoe.kandoeandroid.pojo.response;


import com.google.gson.annotations.SerializedName;

public class Kaart {

    @SerializedName("id")
    private int id;
    @SerializedName("tekst")
    private String tekst;
    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("commentsToelaatbaar")
    private boolean commentsToelaatbaar;
    @SerializedName("gebruiker")
    private Gebruiker gebruiker;

    public Gebruiker getGebruiker() {
        return gebruiker;
    }

    public void setGebruiker(Gebruiker gebruiker) {
        this.gebruiker = gebruiker;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTekst() {
        return tekst;
    }

    public Kaart(String tekst, String imageUrl, boolean commentsToelaatbaar) {
        this.tekst = tekst;
        this.imageUrl = imageUrl;
        this.commentsToelaatbaar = commentsToelaatbaar;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isCommentsToelaatbaar() {
        return commentsToelaatbaar;
    }

    public void setCommentsToelaatbaar(boolean commentsToelaatbaar) {
        this.commentsToelaatbaar = commentsToelaatbaar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Kaart kaart = (Kaart) o;

        return tekst.equals(kaart.tekst);

    }

    @Override
    public int hashCode() {
        return tekst.hashCode();
    }
}
