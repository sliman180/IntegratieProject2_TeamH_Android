package be.kdg.kandoe.kandoeandroid.helpers.adaptermodels;


public class CirkelsessieModel {

    private int icon;
    private String title;
    private String counter;
    private String organisator;

    private boolean isGroupHeader = false;

    public CirkelsessieModel(String title,String organisator) {
        this(-1,title,organisator,null);
        isGroupHeader = false;
    }
    public CirkelsessieModel(int icon, String title,String organisator, String counter) {
        super();
        this.icon = icon;
        this.title = title;
        this.organisator = organisator;
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
