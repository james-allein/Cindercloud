package cloud.cinder.cindercloud.address.controller;

import cloud.cinder.cindercloud.address.controller.vo.AddressVO;
import cloud.cinder.cindercloud.address.model.SpecialAddress;
import cloud.cinder.cindercloud.address.service.AddressService;
import cloud.cinder.cindercloud.block.model.Block;
import cloud.cinder.cindercloud.block.service.BlockService;
import cloud.cinder.cindercloud.coinmarketcap.dto.Currency;
import cloud.cinder.cindercloud.coinmarketcap.service.PriceService;
import cloud.cinder.cindercloud.transaction.model.Transaction;
import cloud.cinder.cindercloud.transaction.service.TransactionService;
import cloud.cinder.cindercloud.utils.WeiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;
import rx.Observable;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static cloud.cinder.cindercloud.utils.WeiUtils.format;

@Controller
@RequestMapping("/address")
public class AddressController {


    @Autowired
    private TransactionService transactionService;
    @Autowired
    private BlockService blockService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private PriceService priceService;

    @RequestMapping("/{hash}")
    public DeferredResult<ModelAndView> getAddress(@PathVariable("hash") final String hash) {
        if (hash == null || hash.isEmpty() || noValidAddress(hash)) {
            throw new IllegalArgumentException("Not a valid address");
        }
        final String address = prettifyAddress(hash);
        final DeferredResult<ModelAndView> result = new DeferredResult<>();
        final ModelAndView modelAndView = new ModelAndView("addresses/address");
        final Observable<String> code = addressService.getCode(address);
        final Observable<Slice<Transaction>> transactions = transactionService.findByAddress(address, new PageRequest(0, 10));
        final Observable<Slice<Block>> minedBlocks = blockService.findByMiner(address, new PageRequest(0, 10));
        final Observable<BigInteger> transactionCount = addressService.getTransactionCount(address);
        final Observable<BigInteger> balance = addressService.getBalance(address);
        final Optional<SpecialAddress> specialAddress = addressService.findByAddress(address);
        Observable.zip(code, transactions, minedBlocks, transactionCount, balance, (cde, tx, blocks, count, bal) -> {
            modelAndView.addObject("address", new AddressVO(cde, format(bal), count, tx, blocks));
            modelAndView.addObject("balEUR", priceService.getPrice(Currency.EUR) * WeiUtils.asEth(bal));
            modelAndView.addObject("balUSD", priceService.getPrice(Currency.USD) * WeiUtils.asEth(bal));
            modelAndView.addObject("isSpecial", specialAddress.isPresent());
            modelAndView.addObject("hash", address);
            modelAndView.addObject("specialName", specialAddress.map(SpecialAddress::getName).orElse(""));
            return modelAndView;
        }).subscribe(result::setResult);
        return result;
    }

    private boolean noValidAddress(final String hash) {
        return !(hash.length() == 40 || hash.length() == 42);
    }

    private String prettifyAddress(final String address) {
        if (!address.startsWith("0x")) {
            return String.format("0x%s", address);
        } else {
            return address;
        }
    }
}
