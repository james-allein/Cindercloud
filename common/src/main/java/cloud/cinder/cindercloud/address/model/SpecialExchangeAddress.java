package cloud.cinder.cindercloud.address.model;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("exchange")
public class SpecialExchangeAddress extends SpecialAddress {
}
