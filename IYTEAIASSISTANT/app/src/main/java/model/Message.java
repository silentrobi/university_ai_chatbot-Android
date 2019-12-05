package model;

public class Message {
    private String text; // message body
    private String senderName; // is this message sent by us?
    private String senderEmail;
    private String date;

    public Message(){


    }
    public Message(String text, String senderName, String senderEmail , String  date) {
        this.text = text;
        this.senderName = senderName;
        this.senderEmail = senderEmail;
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public String  getSenderName() {
        return senderName;
    }

    public String getSenderEmail(){return senderEmail;}
    public String getDate(){return date;}
    public void setText(String text){this.text= text;}
    public void setSenderName(String senderName) {this.senderName= senderName;}
    public void setSenderEmail(String senderEmail){this.senderEmail= senderEmail;}
    public void setDate(String date){ this.date= date;}
}