package uj.ac.monitus_mobile.data.model;

/*Java class stores data specific to newsletter
 *which is displayed in NewsletterAdapter recyclerview */
public class NewsletterModel {
    private int news_id;
    private String headline, message;
    private String date_published;

    public NewsletterModel(String headline, String message, String date_published) {
        this.headline = headline;
        this.message = message;
        this.date_published = date_published;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        headline = headline;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        message = message;
    }

    public String getDate_published() {
        return date_published;
    }

    public void setDate_published(String date_published) {
        this.date_published = date_published;
    }

    @Override
    public String toString() {
        return "NewsletterModel{" +
                "Headline='" + headline + '\'' +
                ", Message='" + message + '\'' +
                ", date_published='" + date_published + '\'' +
                '}';
    }
}
