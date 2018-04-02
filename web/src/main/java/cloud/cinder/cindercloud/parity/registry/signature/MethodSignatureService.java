package cloud.cinder.cindercloud.parity.registry.signature;

import cloud.cinder.cindercloud.parity.registry.signature.contract.ParitySignatureRegistry;
import cloud.cinder.cindercloud.parity.registry.signature.domain.MethodSignature;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MethodSignatureService {

    private final ParitySignatureRegistry paritySignatureRegistry;

    public MethodSignatureService(final ParitySignatureRegistry paritySignatureRegistry) {
        this.paritySignatureRegistry = paritySignatureRegistry;
    }

    public Optional<MethodSignature> findSignature(final String input) {
        try {
            final String signature = paritySignatureRegistry.entries(input).send();
            if (StringUtils.isNotBlank(signature)) {
                return Optional.of(new MethodSignature(input, signature));
            } else {
                return Optional.empty();
            }
        } catch (final Exception ex) {
            return Optional.empty();
        }
    }
}
