package be.kdg.kandoe.kandoeandroid.helpers;

/**
 * Created by S on 15-3-2016.
 */
public class OrganisatieModel {
    private int icon;
    private String title;
    private String aantalHoofdthemas;
    private String counter;

    private boolean isGroupHeader = false;

    public OrganisatieModel(String title,String date) {
        this(-1,title,date,null);
        isGroupHeader = false;
    }
    public OrganisatieModel(int icon,String title, String aantalHoofdthemas, String counter) {
        super();
        this.icon = icon;
        this.title = title;
        this.aantalHoofdthemas = aantalHoofdthemas;
        this.counter = counter;
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

    public String getAantalHoofdthemas() {
        return aantalHoofdthemas;
    }

    public void setAantalHoofdthemas(String aantalHoofdthemas) {
        this.aantalHoofdthemas = aantalHoofdthemas;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }
}
