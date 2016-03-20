package be.kdg.kandoe.kandoeandroid.helpers.adaptermodels;


public class DeelnemersModel {

    private int icon;
    private String title;
    private boolean beurt;
    private String date;
    private String aantalKaarten;

    private boolean isGroupHeader = false;

    public DeelnemersModel(int icon,String title,boolean beurt, String date, String aantalKaarten) {
        super();
        this.icon = icon;
        this.title = title;
        this.beurt = beurt;
        this.date = date;
        this.aantalKaarten = aantalKaarten;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isGroupHeader() {
        return isGroupHeader;
    }

    public void setIsGroupHeader(boolean isGroupHeader) {
        this.isGroupHeader = isGroupHeader;
    }

    public boolean getBeurt() {
        return beurt;
    }

    public void setBeurt(boolean beurt) {
        this.beurt = beurt;
    }

    public String getAantalKaarten() {
        return aantalKaarten;
    }

    public void setAantalKaarten(String aantalKaarten) {
        this.aantalKaarten = aantalKaarten;
    }
}
