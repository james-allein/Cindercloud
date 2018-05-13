package cloud.cinder.cindercloud.arcane.claim;

import cloud.cinder.cindercloud.arcane.privatekey.domain.PrivateKeySecret;
import cloud.cinder.cindercloud.arcane.privatekey.repository.PrivateKeySecretRepository;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import java.util.List;

@Service
public class ClaimAddressService {

    @Autowired
    private PrivateKeySecretRepository privateKeySecretRepository;

    public String claimAddress(final String owner) {
        try {
            final ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            final String address = Keys.getAddress(ecKeyPair.getPublicKey());
            privateKeySecretRepository.save(
                    PrivateKeySecret.builder()
                            .address(address)
                            .privateKey(Hex.toHexString(ecKeyPair.getPrivateKey().toByteArray()))
                            .owner(owner)
                            .build()
            );
            return address;
        } catch (final Exception ex) {
            throw new IllegalArgumentException("Unable to generate keypair", ex);
        }
    }

    public List<PrivateKeySecret> findByUser(final String owner) {
        return privateKeySecretRepository.findByOwner(owner);
    }
}
