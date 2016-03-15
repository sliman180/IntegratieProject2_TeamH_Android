package be.kdg.kandoe.kandoeandroid.helpers.adaptermodels;


public class OrganisatieModel {
    private int icon;
    private String title;
    private String beschrijving;
//    private String aantalHoofdthemas;

    private boolean isGroupHeader = false;

    public OrganisatieModel(String title, String date) {
        this(-1,title,date);
        isGroupHeader = false;
    }
    public OrganisatieModel(int icon,String title,String beschrijving) {
        super();
        this.icon = icon;
        this.title = title;
        this.beschrijving = beschrijving;
//        this.aantalHoofdthemas = aantalHoofdthemas;
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

//    public String getAantalHoofdthemas() {
//        return aantalHoofdthemas;
//    }
//
//    public void setAantalHoofdthemas(String aantalHoofdthemas) {
//        this.aantalHoofdthemas = aantalHoofdthemas;
//    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }


    public boolean isGroupHeader() {
        return isGroupHeader;
    }
}
