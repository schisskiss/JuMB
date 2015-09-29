package jumb.myapplab.pascal.jumb;

public class RowItem {
    private String title;
    private String date;
    private String desc;
     
    public RowItem(String title, String date, String desc) {
        this.title = title;
        this.date = date;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String toString() {
        return title + "\n" + desc;
    }   
}