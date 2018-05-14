package cloud.cinder.cindercloud.arcane.address;

import cloud.cinder.cindercloud.arcane.privatekey.domain.WalletSecret;
import cloud.cinder.cindercloud.arcane.privatekey.repository.WalletSecretRepository;
import cloud.cinder.cindercloud.arcane.secret.domain.Secret;
import cloud.cinder.cindercloud.arcane.secret.repository.SecretRepository;
import cloud.cinder.cindercloud.arcane.user.domain.User;
import cloud.cinder.cindercloud.arcane.user.repository.UserRepository;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import java.util.List;

@Service
public class ClaimAddressService {

    @Autowired
    private WalletSecretRepository walletSecretRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecretRepository secretRepository;

    public String claimAddress(final String user) {
        try {
            final User secretOwner = createOrFetchUser(user);
            final ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            final String address = Keys.getAddress(ecKeyPair.getPublicKey());
            persistSecret(secretOwner, ecKeyPair, address);
            return address;
        } catch (final Exception ex) {
            throw new IllegalArgumentException("Unable to generate keypair", ex);
        }
    }

    private void persistSecret(final User secretOwner, final ECKeyPair ecKeyPair, final String address) {
        final WalletSecret walletSecret = walletSecretRepository.save(
                WalletSecret.builder()
                        .address(address)
                        .privateKey(Hex.toHexString(ecKeyPair.getPrivateKey().toByteArray()))
                        .owner(secretOwner.getId())
                        .build()
        );
        secretRepository.save(
                Secret.builder()
                        .id(walletSecret.getId())
                        .user(secretOwner)
                        .build()
        );
    }

    private User createOrFetchUser(final String user) {
        return userRepository.findByExternalId(user).orElseGet(() -> userRepository.save(
                User.builder()
                        .externalId(user)
                        .build(
                        )));
    }
}
