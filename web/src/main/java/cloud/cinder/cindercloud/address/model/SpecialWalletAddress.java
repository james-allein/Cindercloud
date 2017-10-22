package cloud.cinder.cindercloud.address.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("wallet")
public class SpecialWalletAddress extends SpecialAddress {
}
