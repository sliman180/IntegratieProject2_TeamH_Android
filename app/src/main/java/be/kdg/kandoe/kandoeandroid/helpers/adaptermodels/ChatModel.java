package be.kdg.kandoe.kandoeandroid.helpers.adaptermodels;

import java.util.Date;

public class ChatModel {
    private int icon;
    private String title;
    private String counter;
    private String organisator;
    private boolean left = false;
    private long date;

    private boolean isGroupHeader = false;


    public ChatModel(boolean left, String title,String organisator,long date) {
        super();
        this.left = left;
        this.title = title;
        this.organisator = organisator;
        this.date = date;
        isGroupHeader = false;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
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
