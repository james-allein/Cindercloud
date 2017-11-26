package cloud.cinder.cindercloud.wallet.controller.command;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class KeystoreLoginCommand {

    private String keystoreValue;
    private String password;

}
