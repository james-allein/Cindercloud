package cloud.cinder.cindercloud.arcane.transaction;

import cloud.cinder.cindercloud.arcane.transaction.dto.RawTransactionDto;
import cloud.cinder.cindercloud.arcane.transaction.dto.SignedTransactionDto;
import cloud.cinder.cindercloud.arcane.transaction.service.EthereumSignatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ethereum/transaction")
public class TransactionRestController {

    @Autowired
    private EthereumSignatureService signatureService;

    @PostMapping("/sign")
    public SignedTransactionDto sign(@RequestBody RawTransactionDto rawTransactionDto) {
        return signatureService.sign(rawTransactionDto);
    }

    @PostMapping("/submit")
    public RawTransactionDto sign(@RequestBody RawTransactionDto rawTransactionDto) {

    }
}
