package cloud.cinder.cindercloud.tracing;

import org.web3j.protocol.core.Response;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ParityTracingResponse extends Response<ParityTracingResponse.TracingResponse> {

    public TracingResponse trace() {
        return getResult();
    }

    public static class TracingResponse {

        private String output;
        private List<Trace> trace = new ArrayList<>();

        public TracingResponse() {
        }

        public String getOutput() {
            return output;
        }

        public TracingResponse setOutput(final String output) {
            this.output = output;
            return this;
        }

        public List<Trace> getTrace() {
            return trace;
        }

        public TracingResponse setTrace(final List<Trace> trace) {
            this.trace = trace;
            return this;
        }
    }

    public static class Trace {
        private Action action;
        private Result result;
        private long subtraces;
        private List<String> traceAddress = new ArrayList<>();
        private String type;

        public Trace() {
        }

        public Action getAction() {
            return action;
        }

        public Trace setAction(final Action action) {
            this.action = action;
            return this;
        }

        public Result getResult() {
            return result;
        }

        public Trace setResult(final Result result) {
            this.result = result;
            return this;
        }

        public long getSubtraces() {
            return subtraces;
        }

        public Trace setSubtraces(final long subtraces) {
            this.subtraces = subtraces;
            return this;
        }

        public List<String> getTraceAddress() {
            return traceAddress;
        }

        public Trace setTraceAddress(final List<String> traceAddress) {
            this.traceAddress = traceAddress;
            return this;
        }

        public String getType() {
            return type;
        }

        public Trace setType(final String type) {
            this.type = type;
            return this;
        }
    }

    public static class Result {
        private String output;
        private String gasUsed;
        private String address;
        private String code;

        public Result() {
        }

        public String getOutput() {
            return output;
        }

        public Result setOutput(final String output) {
            this.output = output;
            return this;
        }

        public String getGasUsed() {
            return gasUsed;
        }

        public Result setGasUsed(final String gasUsed) {
            this.gasUsed = gasUsed;
            return this;
        }

        public String getAddress() {
            return address;
        }

        public Result setAddress(final String address) {
            this.address = address;
            return this;
        }

        public String getCode() {
            return code;
        }

        public Result setCode(final String code) {
            this.code = code;
            return this;
        }
    }

    public static class Action {

        public Action() {
        }

        private String callType;
        private String from;
        private String to;
        private String input;
        private String value;
        private String gas;
        private String init;

        public String getGas() {
            return gas;
        }

        public Action setGas(final String gas) {
            this.gas = gas;
            return this;
        }

        public String getInit() {
            return init;
        }

        public Action setInit(final String init) {
            this.init = init;
            return this;
        }

        public String getCallType() {
            return callType;
        }

        public Action setCallType(final String callType) {
            this.callType = callType;
            return this;
        }

        public String getFrom() {
            return from;
        }

        public Action setFrom(final String from) {
            this.from = from;
            return this;
        }

        public String getTo() {
            return to;
        }

        public Action setTo(final String to) {
            this.to = to;
            return this;
        }

        public String getInput() {
            return input;
        }

        public Action setInput(final String input) {
            this.input = input;
            return this;
        }

        public String getValue() {
            return value;
        }

        public Action setValue(final String value) {
            this.value = value;
            return this;
        }
    }

}
