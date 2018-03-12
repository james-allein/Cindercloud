package cloud.cinder.cindercloud.utils;

import javax.annotation.Nonnull;

public final class AddressUtils {

    public static String prettifyAddress(final @Nonnull String address) {
        if (!address.startsWith("0x")) {
            return String.format("0x%s", address);
        } else {
            return address;
        }
    }
}
