package cm.bwgroup.gateway.plugin;

import com.google.common.base.Ascii;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import org.joda.time.Period;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class MomoFrancophoneFlutterWaveConfigProperties {


    private static final String PROPERTY_PREFIX = "cm.bwgroup.gateway.plugin.mffw.";

    public static final String DEFAULT_PENDING_PAYMENT_EXPIRATION_PERIOD = "P3d";

    private static final String ENTRY_DELIMITER = "|";
    private static final String KEY_VALUE_DELIMITER = "#";
    private static final String DEFAULT_CONNECTION_TIMEOUT = "30000";
    private static final String DEFAULT_READ_TIMEOUT = "60000";

    private final String region;
    private final String merchantID;
    private final String secretKey;
    private final String publicKey;
    private final String payemntType;
    private final String encryptionKey;
    private final String connectionTimeout;
    private final String readTimeout;
    private final Period pendingPaymentExpirationPeriod;
    private final Map<String, Period> paymentMethodToExpirationPeriod = new LinkedHashMap<String, Period>();
    private final String chargeDescription;
    private final String chargeStatementDescriptor;

    public MomoFrancophoneFlutterWaveConfigProperties(final Properties properties, final String region) {
        this.region = region;
        this.payemntType = properties.getProperty(PROPERTY_PREFIX + "payment_type");
        this.merchantID = properties.getProperty(PROPERTY_PREFIX + "merchant_id");
        this.secretKey = properties.getProperty(PROPERTY_PREFIX + "secret_key");
        this.publicKey = properties.getProperty(PROPERTY_PREFIX + "public_key");
        this.encryptionKey = properties.getProperty(PROPERTY_PREFIX + "encryption_key");
        this.connectionTimeout = properties.getProperty(PROPERTY_PREFIX + "connectionTimeout", DEFAULT_CONNECTION_TIMEOUT);
        this.readTimeout = properties.getProperty(PROPERTY_PREFIX + "readTimeout", DEFAULT_READ_TIMEOUT);
        this.pendingPaymentExpirationPeriod = readPendingExpirationProperty(properties);
        this.chargeDescription = Ascii.truncate(MoreObjects.firstNonNull(properties.getProperty(PROPERTY_PREFIX + "chargeDescription"), "Kill Bill charge"), 22, "...");
        this.chargeStatementDescriptor = Ascii.truncate(MoreObjects.firstNonNull(properties.getProperty(PROPERTY_PREFIX + "chargeStatementDescriptor"), "Kill Bill charge"), 22, "...");
    }

    public String getMerchantID() { return merchantID; }

    public String getSecretKey() { return secretKey; }

    public String getPublicKey() { return publicKey; }

    public String getEncryptionKey() { return encryptionKey; }

    public String getPayemntType() { return payemntType; }

    public String getConnectionTimeout() { return connectionTimeout; }

    public String getReadTimeout() { return readTimeout; }

    public String getChargeDescription() { return chargeDescription; }

    public String getChargeStatementDescriptor() {
        return chargeStatementDescriptor;
    }

    public Period getPendingPaymentExpirationPeriod(@Nullable final String paymentMethod) {
        if (paymentMethod != null && paymentMethodToExpirationPeriod.get(paymentMethod.toLowerCase()) != null) {
            return paymentMethodToExpirationPeriod.get(paymentMethod.toLowerCase());
        } else {
            return pendingPaymentExpirationPeriod;
        }
    }

    private Period readPendingExpirationProperty(final Properties properties) {
        final String pendingExpirationPeriods = properties.getProperty(PROPERTY_PREFIX + "pendingPaymentExpirationPeriod");
        final Map<String, String> paymentMethodToExpirationPeriodString = new HashMap<String, String>();
        refillMap(paymentMethodToExpirationPeriodString, pendingExpirationPeriods);
        // No per-payment method override, just a global setting
        if (pendingExpirationPeriods != null && paymentMethodToExpirationPeriodString.isEmpty()) {
            try {
                return Period.parse(pendingExpirationPeriods);
            } catch (final IllegalArgumentException e) { /* Ignore */ }
        }

        // User has defined per-payment method overrides
        for (final Map.Entry<String, String> entry : paymentMethodToExpirationPeriodString.entrySet()) {
            try {
                paymentMethodToExpirationPeriod.put(entry.getKey().toLowerCase(), Period.parse(entry.getValue()));
            } catch (final IllegalArgumentException e) { /* Ignore */ }
        }

        return Period.parse(DEFAULT_PENDING_PAYMENT_EXPIRATION_PERIOD);
    }

    private synchronized void refillMap(final Map<String, String> map, final String stringToSplit) {
        map.clear();
        if (!Strings.isNullOrEmpty(stringToSplit)) {
            for (final String entry : stringToSplit.split("\\" + ENTRY_DELIMITER)) {
                final String[] split = entry.split(KEY_VALUE_DELIMITER);
                if (split.length > 1) {
                    map.put(split[0], split[1]);
                }
            }
        }
    }
}
