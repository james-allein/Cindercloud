package cloud.cinder.cindercloud.contract.service;

import cloud.cinder.cindercloud.contract.domain.VerifiedABI;
import cloud.cinder.cindercloud.contract.repository.VerifiedABIRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VerifiedABIService {

    private VerifiedABIRepository verifiedABIRepository;

    public VerifiedABIService(final VerifiedABIRepository verifiedABIRepository) {
        this.verifiedABIRepository = verifiedABIRepository;
    }

    @Transactional(readOnly = true)
    public Optional<VerifiedABI> findByAddress(final String address) {
        return verifiedABIRepository.findByAddress(address);
    }
}
