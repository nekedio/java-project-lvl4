package hexlet.code.models;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public final class UrlCheck extends Model {

    @Id
    private long id;

    private int statusCode;

    private String title;

    private String h1;

    @Lob
    private String description;

    @ManyToOne
    @JoinColumn(name = "url_id")
    private Url url;

    @WhenCreated
    private Instant createdAt;

    public long getId() {
        return id;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getTitle() {
        return title;
    }

    public String getH1() {
        return h1;
    }

    public String getDescription() {
        return description;
    }

    public Url getUrl() {
        return url;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public UrlCheck(int statusCode, String title, String h1, String description, Url url) {
        this.statusCode = statusCode;
        this.title = title;
        this.h1 = h1;
        this.description = description;
        this.url = url;
    }

    public String createdAtToFormat() {

        Date date = Date.from(createdAt);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String formattedDate = formatter.format(date);

        return formattedDate;
    }
}
