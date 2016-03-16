package be.kdg.kandoe.kandoeandroid.helpers.adaptermodels;


public class OrganisatieModel {
    private String counter;
    private String title;
    private String beschrijving;
    private int clickIcon;

//    private String aantalHoofdthemas;

    private boolean isGroupHeader = false;

    public OrganisatieModel(String counter, String title, String date) {
        this(counter,title,date,-1);
        isGroupHeader = false;
    }
    public OrganisatieModel(String counter,String title,String beschrijving,int clickIcon) {
        super();
        this.counter = counter;
        this.title = title;
        this.beschrijving = beschrijving;
        this.clickIcon = clickIcon;
//        this.aantalHoofdthemas = aantalHoofdthemas;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getClickIcon() {
        return clickIcon;
    }

    public void setClickIcon(int clickIcon) {
        this.clickIcon = clickIcon;
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
