package cloud.cinder.cindercloud.token.solidity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.tx.Contract;
import rx.Observable;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class ERC20 extends Contract {

    public ERC20(final String contractAddress, final Web3j web3j, final BigInteger gasPrice, final BigInteger gasLimit) {
        super("", contractAddress, web3j, Credentials.create(ECKeyPair.create(BigInteger.ZERO)), gasPrice, gasLimit);
    }

    public Observable<Uint8> decimals() throws IOException {
        final Function function = new Function("decimals",
                emptyList(),
                singletonList(new TypeReference<Uint8>() {
                }));
        return Observable.just(executeCallSingleValueReturn(function));
    }

    public Observable<Utf8String> version() throws IOException {
        final Function function = new Function("version",
                Arrays.asList(),
                Arrays.asList(new TypeReference<Utf8String>() {
                }));
        return Observable.just(executeCallSingleValueReturn(function));
    }

    public Observable<Uint256> balanceOf(Address _owner) throws IOException {
        final Function function = new Function("balanceOf",
                Arrays.asList(_owner),
                Arrays.asList(new TypeReference<Uint256>() {
                }));
        return Observable.just(executeCallSingleValueReturn(function));
    }

    public Observable<Utf8String> symbol() throws IOException {
        final Function function = new Function("symbol",
                Arrays.asList(),
                Arrays.asList(new TypeReference<Utf8String>() {
                }));
        return Observable.just(executeCallSingleValueReturn(function));
    }

    public Observable<Uint256> allowance(final Address _owner, final Address _spender) throws IOException {
        final Function function = new Function("allowance",
                Arrays.asList(_owner, _spender),
                Arrays.asList(new TypeReference<Uint256>() {
                }));
        return Observable.just(executeCallSingleValueReturn(function));
    }

    public Observable<TransferEventResponse> transferEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Transfer",
                Arrays.asList(new TypeReference<Address>() {
                }, new TypeReference<Address>() {
                }),
                Arrays.asList(new TypeReference<Uint256>() {
                }));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(log -> {
            final EventValues eventValues = extractEventParameters(event, log);
            return new TransferEventResponse(
                    (Address) eventValues.getIndexedValues().get(0),
                    (Address) eventValues.getIndexedValues().get(1),
                    (Uint256) eventValues.getNonIndexedValues().get(0)
            );
        });
    }

    public Observable<ApprovalEventResponse> approvalEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Approval",
                Arrays.asList(new TypeReference<Address>() {
                }, new TypeReference<Address>() {
                }),
                Arrays.asList(new TypeReference<Uint256>() {
                }));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(log -> {
            final EventValues eventValues = extractEventParameters(event, log);
            return new ApprovalEventResponse(
                    (Address) eventValues.getIndexedValues().get(0),
                    (Address) eventValues.getIndexedValues().get(1),
                    (Uint256) eventValues.getNonIndexedValues().get(0)
            );
        });
    }


    @AllArgsConstructor
    @Getter
    public static class TransferEventResponse {
        private Address from;
        private Address to;
        private Uint256 value;
    }

    @AllArgsConstructor
    @Getter
    public static class ApprovalEventResponse {
        private Address owner;
        private Address spender;
        private Uint256 value;
    }
}
