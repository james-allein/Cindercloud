package cloud.cinder.cindercloud.erc20.controller.dto;

import cloud.cinder.cindercloud.token.model.Token;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressTokenDto {
    private Token token;
    private String balance;
    private double rawBalance;
}
