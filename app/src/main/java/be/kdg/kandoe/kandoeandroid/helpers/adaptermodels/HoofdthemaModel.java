package be.kdg.kandoe.kandoeandroid.helpers.adaptermodels;


public class HoofdthemaModel {
    private String counter;
    private String title;
    private String beschrijving;
    private String organisatie;
    private int clickIcon;


    private boolean isGroupHeader = false;

    public HoofdthemaModel(String counter,String title,String beschrijving,String organisatie) {
        this(counter,title,beschrijving,organisatie,-1);
        isGroupHeader = false;
    }

    public HoofdthemaModel(String counter,String title, String beschrijving, String organisatie, int clickIcon) {
        super();
        this.counter = counter;
        this.title = title;
        this.beschrijving = beschrijving;
        this.organisatie = organisatie;
        this.clickIcon = clickIcon;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public int getClickIcon() {
        return clickIcon;
    }

    public void setClickIcon(int clickIcon) {
        this.clickIcon = clickIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isGroupHeader() {
        return isGroupHeader;
    }

    public void setIsGroupHeader(boolean isGroupHeader) {
        this.isGroupHeader = isGroupHeader;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public String getOrganisatie() {
        return organisatie;
    }

    public void setOrganisatie(String organisatie) {
        this.organisatie = organisatie;
    }
}
