package hexlet.code.models;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;
import java.time.Instant;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Url extends Model {

    @Id
    private long id;

    private String name;

    @WhenCreated
    private Instant createdAt;

    @OneToMany
    private List<UrlCheck> urlChecks;

    public Url(String name) {
        this.name = name;
    }

    public final long getId() {
        return id;
    }

    public final String getName() {
        return name;
    }

    public final Instant getCreatedAt() {
        return createdAt;
    }
}
