package cloud.cinder.cindercloud.address.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("miner")
public class SpecialMinerAddress extends SpecialAddress {
}
