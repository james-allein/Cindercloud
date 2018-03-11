package cloud.cinder.cindercloud.transaction.rest;

import cloud.cinder.cindercloud.transaction.domain.Transaction;
import cloud.cinder.cindercloud.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "/rest/tx")
public class TransactionRestController {

    @Autowired
    private TransactionService transactionService;


    @RequestMapping(method = GET, value = "/{transaction}")
    public DeferredResult<Transaction> getTransaction(@PathVariable("transaction") final String transaction) {
        final DeferredResult<Transaction> transactionDeferredResult = new DeferredResult<>();
        transactionService.getTransaction(transaction).subscribe(transactionDeferredResult::setResult);
        return transactionDeferredResult;
    }
}
