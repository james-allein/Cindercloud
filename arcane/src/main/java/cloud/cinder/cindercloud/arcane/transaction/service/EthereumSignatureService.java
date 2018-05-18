package cloud.cinder.cindercloud.arcane.transaction.service;

import cloud.cinder.cindercloud.arcane.privatekey.repository.WalletSecretRepository;
import cloud.cinder.cindercloud.arcane.secret.repository.SecretRepository;
import cloud.cinder.cindercloud.arcane.transaction.dto.RawTransactionDto;
import cloud.cinder.cindercloud.arcane.transaction.dto.SignedTransactionDto;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.TransactionEncoder;

import java.util.Optional;

@Component
public class EthereumSignatureService {

    @Autowired
    private WalletSecretRepository walletSecretRepository;
    @Autowired
    private SecretRepository secretRepository;

    public SignedTransactionDto sign(final RawTransactionDto rawTransactionDto) {
        return secretRepository.findByAddress(rawTransactionDto.getFrom())
                .map(x -> walletSecretRepository.findById(x.getSecretId())).filter(Optional::isPresent)
                .map(Optional::get)
                .map(x -> TransactionEncoder.signMessage(rawTransactionDto.toRawTransaction(), Credentials.create(x.getPrivateKey())))
                .map(x -> SignedTransactionDto.builder()
                        .rawTransactionDto(rawTransactionDto)
                        .signedBytes(Hex.toHexString(x))
                        .build())
                .orElseThrow(() -> new IllegalArgumentException("Unable to create transaction"));
    }
}
