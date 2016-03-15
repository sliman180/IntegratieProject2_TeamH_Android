package be.kdg.kandoe.kandoeandroid.helpers.adaptermodels;


import java.util.Date;

public class DeelnameModel {

    private int icon;
    private String title;
    private String date;
    private String counter;

    private boolean isGroupHeader = false;

    public DeelnameModel(String title,String date) {
        this(-1,title,date,null);
        isGroupHeader = false;
    }
    public DeelnameModel(int icon,String title, String date, String counter) {
        super();
        this.icon = icon;
        this.title = title;
        this.date = date;
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

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }
}
