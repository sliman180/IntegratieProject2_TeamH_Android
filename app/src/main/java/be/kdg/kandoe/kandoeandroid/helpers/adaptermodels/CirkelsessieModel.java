package be.kdg.kandoe.kandoeandroid.helpers.adaptermodels;


import be.kdg.kandoe.kandoeandroid.pojo.Subthema;

public class CirkelsessieModel {

    private int icon;
    private String title;
    private String counter;
    private String organisator;
    private long datum;
    private Subthema subthema;

    private boolean isGroupHeader = false;

    public CirkelsessieModel(String title,String organisator,long datum,Subthema subthema) {
        this(-1,title,organisator,null,datum,subthema);
        isGroupHeader = false;
    }
    public CirkelsessieModel(int icon, String title,String organisator, String counter,long datum,Subthema subthema) {
        super();
        this.icon = icon;
        this.title = title;
        this.organisator = organisator;
        this.counter = counter;
        this.datum = datum;
        this.subthema = subthema;
    }

    public int getIcon() {
        return icon;
    }

    public long getDatum() {
        return datum;
    }

    public void setDatum(long datum) {
        this.datum = datum;
    }

    public Subthema getSubthema() {
        return subthema;
    }

    public void setSubthema(Subthema subthema) {
        this.subthema = subthema;
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

    public boolean isGroupHeader() {
        return isGroupHeader;
    }

    public void setIsGroupHeader(boolean isGroupHeader) {
        this.isGroupHeader = isGroupHeader;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public String getOrganisator() {
        return organisator;
    }

    public void setOrganisator(String organisator) {
        this.organisator = organisator;
    }
}
