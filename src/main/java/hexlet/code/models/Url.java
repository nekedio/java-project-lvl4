package hexlet.code.models;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public final class Url extends Model {

    @Id
    private long id;

    private String name;

    @WhenCreated
    private Instant createdAt;

    public Url() {

    }

    public Url(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String createdAtToFormat() {

        Date date = Date.from(createdAt);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String formattedDate = formatter.format(date);

        return formattedDate;
    }
}
