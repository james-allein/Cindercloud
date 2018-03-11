package cloud.cinder.cindercloud.block.controller;

import cloud.cinder.cindercloud.address.model.SpecialAddress;
import cloud.cinder.cindercloud.address.service.AddressService;
import cloud.cinder.cindercloud.block.model.Block;
import cloud.cinder.cindercloud.block.service.BlockService;
import cloud.cinder.cindercloud.transaction.model.Transaction;
import cloud.cinder.cindercloud.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping(value = "/blocks")
public class BlockController {

    @Autowired
    private BlockService blockService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AddressService addressService;

    @RequestMapping(method = RequestMethod.GET)
    public String blocks(final ModelMap modelMap,
                         final @RequestParam("q") Optional<String> searchKey,
                         final @RequestParam("mined_by") Optional<String> minedBy,
                         final Pageable pageable) {
        if (searchKey.isPresent() || minedBy.isPresent()) {
            modelMap.put("blocks", blockService.searchBlocks(searchKey.orElse(""), minedBy.orElse(""), pageable));
            modelMap.put("q", searchKey.orElse(""));
            modelMap.put("mined_by", minedBy.orElse(""));
        } else {
            modelMap.put("blocks", blockService.getLastBlocks(pageable));
            modelMap.put("q", "");
            modelMap.put("mined_by", "");
        }
        return "blocks/list";
    }

    @RequestMapping(value = "/{hash}")
    public Mono<String> getBlock(@PathVariable("hash") final String hash,
                                 final Model model) {
        return Mono.fromFuture(CompletableFuture.supplyAsync(() -> {
            final Block block = blockService.getBlock(hash).single().toBlocking().first();
            model.addAttribute("block", block);
            final Optional<SpecialAddress> specialMinedBy = addressService.findByAddress(block.getMinedBy());
            model.addAttribute("isMinedBySpecialName", specialMinedBy.isPresent());
            model.addAttribute("minedBySpecialName", specialMinedBy.map(SpecialAddress::getName).orElse(""));
            return "blocks/block";
        }));
    }

    @RequestMapping(value = "/{hash}/transactions")
    public String getTransactionsForUncle(@PathVariable("hash") final String hash,
                                          final Model model) {
        final Slice<Transaction> transactions = transactionService.getTransactionsForBlock(hash, PageRequest.of(0, 20)).toBlocking().first();
        model.addAttribute("transactions", transactions);
        model.addAttribute("hash", hash);
        return "blocks/transactions :: blockTransactions";
    }
}
