package cloud.cinder.cindercloud.address.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("crowdsale")
public class SpecialCrowdsaleAddress extends SpecialAddress{
}
