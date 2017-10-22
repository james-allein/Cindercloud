package cloud.cinder.cindercloud.address.repository;

import cloud.cinder.cindercloud.address.model.SpecialAddress;
import cloud.cinder.cindercloud.infrastructure.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpecialAddressRepository extends JpaRepository<SpecialAddress, Long> {


    @Query("select sa from SpecialAddress sa where address LIKE :address")
    Optional<SpecialAddress> findByAddress(@Param("address") final String address);

}
