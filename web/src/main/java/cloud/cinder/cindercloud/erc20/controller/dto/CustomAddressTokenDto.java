package cloud.cinder.cindercloud.erc20.controller.dto;

import cloud.cinder.cindercloud.erc20.domain.CustomERC20;
import cloud.cinder.cindercloud.token.domain.Token;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomAddressTokenDto {
    private CustomERC20 token;
    private String balance;
    private double rawBalance;
    private String eurPrice;
    private String dollarPrice;
}
