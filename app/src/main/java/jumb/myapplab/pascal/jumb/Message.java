package jumb.myapplab.pascal.jumb;

public class Message {
    private String fromName, message;
    private String isSelf;
    private String privatemsg;

    public Message() {
    }

    public Message(String fromName, String message, String isSelf, String privatemsg) {
        this.fromName = fromName;
        this.message = message;
        this.isSelf = isSelf;
        this.privatemsg = privatemsg;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String isSelf() {
        return isSelf;
    }

    public void setSelf(String isSelf) {
        this.isSelf = isSelf;
    }

    public String isPrivatemsg() { return privatemsg; }

    public void setPrivatemsg(String privatemsg) { this.privatemsg = privatemsg; }
}