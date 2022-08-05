package hexlet.code.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Url {

    @Id
    private long id;

    private String name;

    private String createdAt;

}
