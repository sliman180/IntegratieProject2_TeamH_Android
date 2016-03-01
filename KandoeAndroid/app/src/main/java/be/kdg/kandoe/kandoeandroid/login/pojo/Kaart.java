package be.kdg.kandoe.kandoeandroid.login.pojo;



public class Kaart {

    private int id;

    private String tekst;

    private String imageUrl;

    private boolean commentsToelaatbaar;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTekst() {
        return tekst;
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
}
