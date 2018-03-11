package cloud.cinder.cindercloud.addressbook.service;

import cloud.cinder.cindercloud.addressbook.domain.Contact;
import cloud.cinder.cindercloud.addressbook.repository.ContactRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AddressBookService {

    private final ContactRepository contactRepository;

    public AddressBookService(final ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Transactional(readOnly = true)
    public List<Contact> getContacts(final String owner) {
        return contactRepository.findAllByOwner(owner);
    }

    @Transactional
    public void addContact(final String owner, final String address, final String nickname) {
        if (contactRepository.existsByOwnerAndAddress(owner, address)) {
            throw new IllegalArgumentException("This address is already a contact of yours");
        }
        contactRepository.save(
                Contact.builder()
                        .address(address)
                        .owner(owner)
                        .nickname(nickname)
                        .build()
        );
    }

    @Transactional
    public void updateNickname(final String owner, final String address, final String nickname) {
        contactRepository.findByOwnerAndAddress(owner, address)
                .ifPresent(contact -> {
                    contact.setNickname(nickname);
                    contactRepository.save(contact);
                });
    }

    @Transactional
    public void deleteContact(final String owner, final Long id) {
        Optional<Contact> foundOne = contactRepository.findOne(id);
        foundOne
                .filter(contact -> contact.getOwner().equalsIgnoreCase(owner))
                .ifPresent(contact -> {
                    contactRepository.delete(id);
                });
    }
}
