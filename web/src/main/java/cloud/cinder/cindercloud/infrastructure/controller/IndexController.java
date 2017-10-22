package cloud.cinder.cindercloud.infrastructure.controller;

import cloud.cinder.cindercloud.block.service.BlockService;
import cloud.cinder.cindercloud.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/")
public class IndexController {

    @Autowired
    private BlockService blockService;
    @Autowired
    private TransactionService transactionService;

    @RequestMapping(method = GET)
    public String index() {
        return "index";
    }


    @RequestMapping(params = "section=blocks")
    public String getLast10Blocks(final ModelMap model) {
        model.put("blocks", blockService.getLastBlocks(new PageRequest(0, 10)).getContent());
        return "components/index :: blocks";
    }

    @RequestMapping(params = "section=transactions")
    public String getLast10Transactions(final ModelMap model) {
        model.put("transactions", transactionService.getLastTransactions(new PageRequest(0, 20)));
        return "components/index :: transactions";
    }

}
