package cloud.cinder.cindercloud.wallet.controller.command;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PrivateKeyLoginCommand {

    private String privateKey;
}
