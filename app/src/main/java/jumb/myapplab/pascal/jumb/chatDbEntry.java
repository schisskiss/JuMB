package jumb.myapplab.pascal.jumb;

/**
 * Created by Pascal on 31.12.2014.
 */
public class chatDbEntry {

    private long id;
    private String msgfrom;
    private String message;
    private String isSelf;
    private String privatemsg;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMsgFrom() {
        return msgfrom;
    }

    public void setFrom(String msgfrom){ this.msgfrom= msgfrom; }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIsSelf() {
        return isSelf;
    }

    public void setIsSelf(String isSelf) {
        this.isSelf = isSelf;
    }

    public String getprivatemsg() { return privatemsg; }

    public void setPrivatemsg(String privatemsg){ this.privatemsg = privatemsg;}
}
