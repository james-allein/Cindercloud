package cloud.cinder.cindercloud.contract.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "verified_abis")
@Data
public class VerifiedABI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "address")
    private String address;

    @Column(name = "abi")
    private String abi;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "added_on")
    private Date addedOn;
}
