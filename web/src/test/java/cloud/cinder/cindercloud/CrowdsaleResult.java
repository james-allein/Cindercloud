package cloud.cinder.cindercloud;

import java.util.List;

public class CrowdsaleResult {

    private List<Transaction> result;

    public CrowdsaleResult() {
    }

    public List<Transaction> getResult() {
        return result;
    }

    public CrowdsaleResult setResult(final List<Transaction> result) {
        this.result = result;
        return this;
    }

    public static class Transaction {
        private String input;

        public Transaction() {
        }

        public String getInput() {
            return input;
        }

        public Transaction setInput(final String input) {
            this.input = input;
            return this;
        }
    }

}
