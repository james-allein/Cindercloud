package cloud.cinder.cindercloud.address.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("dapp")
public class SpecialDappAddress extends SpecialAddress {
}
