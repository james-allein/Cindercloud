package cloud.cinder.cindercloud.arcane.address.service;

import cloud.cinder.cindercloud.arcane.privatekey.domain.WalletSecret;
import cloud.cinder.cindercloud.arcane.privatekey.repository.WalletSecretRepository;
import cloud.cinder.cindercloud.arcane.secret.domain.Wallet;
import cloud.cinder.cindercloud.arcane.secret.repository.SecretRepository;
import cloud.cinder.cindercloud.arcane.user.domain.User;
import cloud.cinder.cindercloud.arcane.user.repository.UserRepository;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {

    @Autowired
    private WalletSecretRepository walletSecretRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecretRepository secretRepository;

    public List<String> list(final String owner) {
        return secretRepository.findByUserExternalId(owner)
                .stream()
                .map(Wallet::getAddress)
                .collect(Collectors.toList());
    }

    public String generate(final String user) {
        try {
            final User secretOwner = createOrFetchUser(user);
            final ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            return persistSecret(secretOwner, ecKeyPair);
        } catch (final Exception ex) {
            throw new IllegalArgumentException("Unable to generate keypair", ex);
        }
    }

    private String persistSecret(final User secretOwner, final ECKeyPair ecKeyPair) {
        final String address = Keys.getAddress(ecKeyPair.getPublicKey());
        final WalletSecret walletSecret = walletSecretRepository.save(
                WalletSecret.builder()
                        .address(address)
                        .privateKey(Hex.toHexString(ecKeyPair.getPrivateKey().toByteArray()))
                        .owner(secretOwner.getId())
                        .build()
        );
        secretRepository.save(
                Wallet.builder()
                        .secretId(walletSecret.getId())
                        .address(address)
                        .user(secretOwner)
                        .build()
        );
        return address;
    }

    private User createOrFetchUser(final String user) {
        return userRepository.findByExternalId(user).orElseGet(() -> userRepository.save(
                User.builder()
                        .externalId(user)
                        .build(
                        )));
    }
}
