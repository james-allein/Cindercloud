package cloud.cinder.cindercloud.infrastructure.controller;

import cloud.cinder.cindercloud.block.service.BlockService;
import cloud.cinder.cindercloud.coinmarketcap.dto.Currency;
import cloud.cinder.cindercloud.coinmarketcap.service.PriceService;
import cloud.cinder.cindercloud.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private PriceService priceService;

    @RequestMapping(method = GET)
    public String index(final ModelMap modelMap) {
        modelMap.put("ethPriceUsd", priceService.getPriceAsString(Currency.USD));
        modelMap.put("ethPriceEur", priceService.getPriceAsString(Currency.EUR));
        modelMap.put("lastBlock", blockService.getLastBlock().toBlocking().first().getBlockNumber().longValue());
        return "index";
    }


    @RequestMapping(params = "section=blocks")
    public String getLast10Blocks(final ModelMap model) {
        model.put("blocks", blockService.getLast10IndexedBlocks());
        return "components/index :: blocks";
    }

    @RequestMapping(params = "section=transactions")
    public String getLast10Transactions(final ModelMap model) {
        model.put("transactions", transactionService.getLast20Transactions());
        return "components/index :: transactions";
    }
}
