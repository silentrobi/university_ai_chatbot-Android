package model;

public class Message {
    private String text=""; // message body
    private String date="";
    private String category="";
    private long timesamp;

    public Message(){
    }

    public Message(String text,  String  date, String category, long timesamp) {
        this.text = text;
        this.date = date;
        this.category = category; // category: {send,receive}
        this.timesamp = timesamp;
    }

    public String getText() {
        return text;
    }
    public String getDate(){return date;}
    public String getCategory() {
        return category;
    }
    public long getTimesamp() {
        return timesamp;
    }

    public void setText(String text){this.text= text;}
    public void setDate(String date){ this.date= date;}
    public void setCategory(String category){this. category= category;}
    public void setTimesamp(long timesamp){this.timesamp= timesamp;}
}