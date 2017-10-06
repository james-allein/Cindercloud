package cloud.cinder.cindercloud.utils;

import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

public class WeiUtils {
    public static String format(final BigInteger aBigInt) {
        if (aBigInt.compareTo(Convert.toWei("0.01", Convert.Unit.ETHER).toBigInteger()) < 0) {
            if (aBigInt.compareTo(Convert.toWei("0.01", Convert.Unit.FINNEY).toBigInteger()) < 0) {
                if (aBigInt.compareTo(Convert.toWei("0.01", Convert.Unit.GWEI).toBigInteger()) < 0) {
                    return aBigInt.toString() + " wei";
                } else {
                    return Convert.fromWei(new BigDecimal(aBigInt), Convert.Unit.GWEI).toPlainString() + " gwei";
                }
            } else {
                return Convert.fromWei(new BigDecimal(aBigInt), Convert.Unit.FINNEY).toPlainString() + " finney";
            }
        } else {
            return Convert.fromWei(new BigDecimal(aBigInt), Convert.Unit.ETHER).toPlainString() + " ether";
        }
    }
}
