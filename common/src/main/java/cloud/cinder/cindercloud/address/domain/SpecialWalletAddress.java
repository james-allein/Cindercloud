package cloud.cinder.cindercloud.address.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("wallet")
public class SpecialWalletAddress extends SpecialAddress {
}
