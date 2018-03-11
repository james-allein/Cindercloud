package cloud.cinder.cindercloud.address.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("miner")
public class SpecialMinerAddress extends SpecialAddress {
}
