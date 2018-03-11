package cloud.cinder.cindercloud.addressbook.repository;

import cloud.cinder.cindercloud.addressbook.domain.Contact;
import cloud.cinder.cindercloud.infrastructure.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findAllByOwner(final @Param("owner") String owner);

    boolean existsByOwnerAndAddress(final @Param("owner") String owner, final @Param("address") String address);

    Optional<Contact> findByOwnerAndAddress(final @Param("owner") String owner, final @Param("address") String address);
}
