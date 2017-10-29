package cloud.cinder.cindercloud.token.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String slug;
    private String address;
    private String image;
    private int decimals;
}
