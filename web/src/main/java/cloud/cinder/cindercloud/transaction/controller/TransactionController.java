package cloud.cinder.cindercloud.transaction.controller;

import cloud.cinder.cindercloud.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/tx")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @RequestMapping(value = "/{hash}", method = GET)
    public DeferredResult<ModelAndView> getTransaction(@PathVariable("hash") final String hash) {
        DeferredResult<ModelAndView> result = new DeferredResult<>();
        final ModelAndView modelAndView = new ModelAndView("transactions/transaction");
        transactionService.getTransaction(hash).subscribe(tx -> {
            modelAndView.addObject("tx", tx);
            result.setResult(modelAndView);
        });
        return result;
    }

}
