package hexlet.code.models;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;
import java.time.Instant;
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
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
