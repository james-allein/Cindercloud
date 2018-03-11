package cloud.cinder.cindercloud.address.domain;

import lombok.Data;

import javax.persistence.*;

@Table(name = "special_addresses")
@DiscriminatorColumn(name = "type")
@Entity
@Data
public class SpecialAddress {

    @Id
    @GeneratedValue
    private Long id;
    private String address;
    private String name;
    private String slug;
    private String chain;
    private String url;
}
