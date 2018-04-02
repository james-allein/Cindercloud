package cloud.cinder.cindercloud.parity.registry.signature.contract;

import org.bouncycastle.util.encoders.Hex;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes4;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.rlp.RlpString;
import org.web3j.tx.Contract;

import java.math.BigInteger;
import java.util.Arrays;

import static java.util.Collections.singletonList;

public class ParitySignatureRegistry extends Contract {

    public ParitySignatureRegistry(final String contractAddress,
                                      final Web3j web3j) {
        super(null, contractAddress, web3j, Credentials.create(ECKeyPair.create(BigInteger.ZERO)), BigInteger.ZERO, BigInteger.ZERO);
    }

    public RemoteCall<String> entries(final String input) {
        final String sanitizedInput = sanitize(input);
        byte[] bytes = RlpString.create(Arrays.copyOfRange(Hex.decode(sanitizedInput), 0, 4)).getBytes();
        final Function function = new Function("entries",
                singletonList(new Bytes4(bytes)),
                singletonList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    private String sanitize(final String input) {
        if (input != null && input.toLowerCase().startsWith("0x")) {
            return input.substring(2);
        } else {
            return input;
        }
    }
}
