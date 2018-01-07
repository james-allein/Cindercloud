package cloud.cinder.cindercloud.wallet.controller;

import cloud.cinder.cindercloud.address.service.AddressService;
import cloud.cinder.cindercloud.coinmarketcap.dto.Currency;
import cloud.cinder.cindercloud.coinmarketcap.service.PriceService;
import cloud.cinder.cindercloud.security.domain.PrivateKeyAuthentication;
import cloud.cinder.cindercloud.transaction.service.TransactionService;
import cloud.cinder.cindercloud.utils.WeiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigInteger;
import java.text.DecimalFormat;

@Controller
@RequestMapping("/wallet")
public class WalletController {

    private static final DecimalFormat currencyFormat = new DecimalFormat("#.00");

    @Autowired
    private AddressService addressService;
    @Autowired
    private PriceService priceService;
    @Autowired
    private TransactionService transactionService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(final ModelMap modelMap) {
        requireAuthenticated();
        final String address = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final BigInteger balance = addressService.getBalance(address).toBlocking().first();
        modelMap.put("balance", WeiUtils.format(balance));
        modelMap.put("balEUR", formatCurrency(priceService.getPrice(Currency.EUR) * WeiUtils.asEth(balance)));
        modelMap.put("balUSD", formatCurrency(priceService.getPrice(Currency.USD) * WeiUtils.asEth(balance)));
        modelMap.put("hasBalance", balance.longValue() != 0);
        modelMap.put("address", address);
        return "wallets/index";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/transactions")
    public String transactions(final ModelMap modelMap) {
        requireAuthenticated();
        final String address = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        modelMap.put("transactions", transactionService.findByAddress(address, new PageRequest(0, 25)).toBlocking().first());
        modelMap.put("address", address);
        return "wallets/transactions";
    }

    private String formatCurrency(final double v) {
        return currencyFormat.format(v);
    }

    private void requireAuthenticated() {
        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof PrivateKeyAuthentication)) {
            throw new IllegalArgumentException("Not authenticated");
        }
    }

}
