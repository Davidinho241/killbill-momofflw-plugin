package cm.bwgroup.gateway.plugin;

import cm.bwgroup.gateway.plugin.utils.RandomString;
import com.flutterwave.rave.java.entry.mobileMoney;
import com.flutterwave.rave.java.payload.mobilemoneyPayload;
import com.google.common.collect.ImmutableList;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.killbill.billing.ObjectType;
import org.killbill.billing.catalog.api.Currency;
import org.killbill.billing.osgi.libs.killbill.OSGIConfigPropertiesService;
import org.killbill.billing.osgi.libs.killbill.OSGIKillbillAPI;
import org.killbill.billing.payment.api.Payment;
import org.killbill.billing.payment.api.PaymentMethodPlugin;
import org.killbill.billing.payment.api.PluginProperty;
import org.killbill.billing.payment.api.TransactionType;
import org.killbill.billing.payment.plugin.api.*;
import org.killbill.billing.plugin.api.PluginProperties;
import org.killbill.billing.plugin.api.core.PluginCustomField;
import org.killbill.billing.plugin.api.payment.PluginHostedPaymentPageFormDescriptor;
import org.killbill.billing.plugin.dao.payment.PluginPaymentDao;
import org.killbill.billing.util.api.CustomFieldApiException;
import org.killbill.billing.util.callcontext.CallContext;
import org.killbill.billing.util.callcontext.TenantContext;
import org.killbill.billing.util.entity.Pagination;
import org.killbill.clock.Clock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.*;

public class MomoFrancophoneFlutterWavePaymentPluginApi implements PaymentPluginApi{

    private final MomoFrancophoneFlutterWaveConfigPropertiesConfigurationHandler momoFrancophoneFlutterWaveConfigPropertiesConfigurationHandler;

    private static final Logger logger = LoggerFactory.getLogger(MomoFrancophoneFlutterWavePaymentPluginApi.class);
    private final OSGIKillbillAPI killbillAPI;
    private final Clock clock;

    com.flutterwave.rave.java.entry.mobileMoney mobileMoney = new mobileMoney();
    com.flutterwave.rave.java.payload.mobilemoneyPayload mobilemoneyPayload = new mobilemoneyPayload();

    public static final String PROPERTY_FROM_HPP = "fromHPP";
    public static final String PROPERTY_HPP_COMPLETION = "fromHPPCompletion";
    public static final String PROPERTY_OVERRIDDEN_TRANSACTION_STATUS = "overriddenTransactionStatus";

    static final List<String> metadataFilter = ImmutableList.of("payment_method_types");

    String easy = RandomString.digits + "ACEFGHJKLMNPQRUVWXYabcdefhijkprstuvwx";
    private final RandomString txRef;

    public MomoFrancophoneFlutterWavePaymentPluginApi(final OSGIKillbillAPI killbillAPI, final Clock clock,
                                                      MomoFrancophoneFlutterWaveConfigPropertiesConfigurationHandler momoFrancophoneFlutterWaveConfigPropertiesConfigurationHandler) {
        this.killbillAPI = killbillAPI;
        this.clock = clock;
        this.momoFrancophoneFlutterWaveConfigPropertiesConfigurationHandler = momoFrancophoneFlutterWaveConfigPropertiesConfigurationHandler;
        this.txRef = new RandomString(23, new SecureRandom(), easy);
    }

    @Override
    public PaymentTransactionInfoPlugin authorizePayment(UUID uuid, UUID uuid1, UUID uuid2, UUID uuid3, BigDecimal bigDecimal, Currency currency, Iterable<PluginProperty> iterable, CallContext callContext) throws PaymentPluginApiException {
        return null;
    }

    @Override
    public PaymentTransactionInfoPlugin capturePayment(UUID uuid, UUID uuid1, UUID kbPaymentId, UUID kbTransactionId, BigDecimal amount, Currency currency, Iterable<PluginProperty> iterable, CallContext callContext) throws PaymentPluginApiException {
        PaymentTransactionInfoPlugin paymentTransactionInfoPlugin = new MomoFrancophoneFlutterWavePaymentTransactionInfoPlugin(kbPaymentId, kbTransactionId,
                TransactionType.CAPTURE, amount, currency, PaymentPluginStatus.CANCELED, null,
                null, null, null, new DateTime(), null, null);
        return paymentTransactionInfoPlugin;
    }

    @Override
    public PaymentTransactionInfoPlugin purchasePayment(UUID uuid, UUID uuid1, UUID uuid2, UUID uuid3, BigDecimal bigDecimal, Currency currency, Iterable<PluginProperty> iterable, CallContext callContext) throws PaymentPluginApiException {
        return null;
    }

    @Override
    public PaymentTransactionInfoPlugin voidPayment(UUID kbAccountId, UUID kbPaymentId, UUID kbTransactionId, UUID kbPaymentMethodId, Iterable<PluginProperty> iterable, CallContext callContext) throws PaymentPluginApiException {
        PaymentTransactionInfoPlugin paymentTransactionInfoPlugin = new MomoFrancophoneFlutterWavePaymentTransactionInfoPlugin(kbPaymentId, kbTransactionId,
                TransactionType.VOID, null, null, PaymentPluginStatus.CANCELED, null,
                null, null, null, new DateTime(), null, null);
        return paymentTransactionInfoPlugin;
    }

    @Override
    public PaymentTransactionInfoPlugin creditPayment(UUID kbAccountId, UUID kbPaymentId, UUID kbTransactionId,
                                                      UUID kbPaymentMethodId, BigDecimal amount, Currency currency, Iterable<PluginProperty> iterable, CallContext callContext) throws PaymentPluginApiException {
        PaymentTransactionInfoPlugin paymentTransactionInfoPlugin = new MomoFrancophoneFlutterWavePaymentTransactionInfoPlugin(kbPaymentId, kbTransactionId,
                TransactionType.CREDIT, amount, currency, PaymentPluginStatus.CANCELED, null,
                null, null, null, new DateTime(), null, null);
        return paymentTransactionInfoPlugin;
    }

    @Override
    public PaymentTransactionInfoPlugin refundPayment(UUID kbAccountId, UUID kbPaymentId, UUID kbTransactionId,
                                                      UUID kbPaymentMethodId, BigDecimal amount, Currency currency, Iterable<PluginProperty> iterable, CallContext callContext) throws PaymentPluginApiException {
        PaymentTransactionInfoPlugin paymentTransactionInfoPlugin = new MomoFrancophoneFlutterWavePaymentTransactionInfoPlugin(kbPaymentId, kbTransactionId,
                TransactionType.REFUND, amount, currency, PaymentPluginStatus.CANCELED, null,
                null, null, null, new DateTime(), null, null);
        return paymentTransactionInfoPlugin;
    }

    @Override
    public List<PaymentTransactionInfoPlugin> getPaymentInfo(UUID uuid, UUID uuid1, Iterable<PluginProperty> iterable, TenantContext tenantContext) throws PaymentPluginApiException {
        return null;
    }

    @Override
    public Pagination<PaymentTransactionInfoPlugin> searchPayments(String s, Long aLong, Long aLong1, Iterable<PluginProperty> iterable, TenantContext tenantContext) throws PaymentPluginApiException {
        return null;
    }

    @Override
    public void addPaymentMethod(UUID kbAccountId, UUID kbPaymentMethodId, PaymentMethodPlugin paymentMethodPlugin, boolean b, Iterable<PluginProperty> properties, CallContext context) throws PaymentPluginApiException {

        logger.info("addPaymentMethod, kbAccountId=" + kbAccountId);
//        final Iterable<PluginProperty> allProperties = PluginProperties.merge(paymentMethodPlugin.getProperties(),
//                properties);
//        String redirectFlowId = PluginProperties.findPluginPropertyValue("redirect_flow_id", allProperties); // retrieve the redirect flow id
//        String sessionToken = PluginProperties.findPluginPropertyValue("session_token", allProperties);

        try {

            logger.info("Transaction id = MomoFLW-"+txRef);

            try {
                // save response in the Kill Bill database
                killbillAPI.getCustomFieldUserApi().addCustomFields(ImmutableList.of(new PluginCustomField(kbAccountId,
                        ObjectType.ACCOUNT, "tx_ref", "MomoFLW-"+txRef, clock.getUTCNow())), context);
            } catch (CustomFieldApiException e) {
                logger.warn("Error occurred while saving payment txRef", e);
                throw new PaymentPluginApiException("Error occurred while saving payment txRef ", e);
            }

        } catch (Exception exception) {
            logger.warn("Error occurred while completing the MomoFrancophoneFlutterWave flow "+ exception.getCause(), exception);
            throw new PaymentPluginApiException("Error occurred while completing the MomoFrancophoneFlutterWave flow", exception);

        }
    }

    @Override
    public void deletePaymentMethod(UUID uuid, UUID uuid1, Iterable<PluginProperty> iterable, CallContext callContext) throws PaymentPluginApiException {

    }

    @Override
    public PaymentMethodPlugin getPaymentMethodDetail(UUID uuid, UUID kbPaymentMethodId, Iterable<PluginProperty> iterable, TenantContext tenantContext) throws PaymentPluginApiException {
        return new MomoFrancophoneFlutterWavePaymentMethodPlugin(kbPaymentMethodId, null,false, null);
    }

    @Override
    public void setDefaultPaymentMethod(UUID uuid, UUID uuid1, Iterable<PluginProperty> iterable, CallContext callContext) throws PaymentPluginApiException {

    }

    @Override
    public List<PaymentMethodInfoPlugin> getPaymentMethods(UUID uuid, boolean b, Iterable<PluginProperty> iterable, CallContext callContext) throws PaymentPluginApiException {
        List<PaymentMethodInfoPlugin> result = new ArrayList<PaymentMethodInfoPlugin>();
        return result;
    }

    @Override
    public Pagination<PaymentMethodPlugin> searchPaymentMethods(String s, Long aLong, Long aLong1, Iterable<PluginProperty> iterable, TenantContext tenantContext) throws PaymentPluginApiException {
        return null;
    }

    @Override
    public void resetPaymentMethods(UUID uuid, List<PaymentMethodInfoPlugin> list, Iterable<PluginProperty> iterable, CallContext callContext) throws PaymentPluginApiException {

    }

    @Override
    public HostedPaymentPageFormDescriptor buildFormDescriptor(UUID kbAccountId, Iterable<PluginProperty> customFields, Iterable<PluginProperty> properties, CallContext context) throws PaymentPluginApiException {

        final MomoFrancophoneFlutterWaveConfigProperties momoFrancophoneFlutterWaveConfigProperties = momoFrancophoneFlutterWaveConfigPropertiesConfigurationHandler.getConfigurable(kbAccountId);
        logger.info("Creating MomoFrancophoneFlutterWave session");
        logger.info("Initialize the payment" + mobilemoneyPayload);
        logger.info("Construct the payment loader");

        mobilemoneyPayload.setPBFPubKey(momoFrancophoneFlutterWaveConfigProperties.getMerchantID());
        mobilemoneyPayload.setPayment_type(momoFrancophoneFlutterWaveConfigProperties.getPayemntType());
        mobilemoneyPayload.setEncryption_key(momoFrancophoneFlutterWaveConfigProperties.getEncryptionKey());
        mobilemoneyPayload.setPublic_key(momoFrancophoneFlutterWaveConfigProperties.getPublicKey());
        mobilemoneyPayload.setAmount("1000");
        mobilemoneyPayload.setEmail("kallamaxime@gmail.com");
        mobilemoneyPayload.setPhonenumber("655615620");
        mobilemoneyPayload.setCurrency("XAF");
        mobilemoneyPayload.setTxRef("MomoFLW-"+txRef);
        mobilemoneyPayload.setFirstname("David");
        mobilemoneyPayload.setLastname("KALLA");
        mobilemoneyPayload.setCountry("CM");

        logger.info("Payment params " + mobilemoneyPayload.toString());
        try{

            try {
                String response = mobileMoney.domobilemoney(mobilemoneyPayload);
                logger.info("Response from MomoFrancophoneFlutterWave " + response);
                JSONObject myObject = new JSONObject(response);
                if (myObject.get("data") != null){
                    JSONObject dataResponse = myObject.optJSONObject("data");
                    logger.info("flw_ref from MomoFrancophoneFlutterWave " + dataResponse.getString("flw_ref"));
                    logger.info("tx_ref from MomoFrancophoneFlutterWave " + dataResponse.getString("tx_ref"));
                }
            } catch (UnknownHostException exception) {
                exception.printStackTrace();
                logger.warn("Error occurred while processing the payment to FlutterWave",  exception);
                throw new PaymentPluginApiException("Error occurred while processing the payment to MomoFrancophoneFlutterWave", exception);
            }

            return new PluginHostedPaymentPageFormDescriptor(kbAccountId, null);

        }catch (Exception exception) {
            logger.warn("Error occurred while completing the MomoFrancophoneFlutterWave flow " + exception.getCause(), exception);
            throw new PaymentPluginApiException("Error occurred while completing MomoFrancophoneFlutterWave flow", exception);

        }
    }

    @Override
    public GatewayNotification processNotification(String s, Iterable<PluginProperty> iterable, CallContext callContext) throws PaymentPluginApiException {
        throw new PaymentPluginApiException("INTERNAL", "#processNotification not yet implemented, please contact support@killbill.io");
    }



    /**
     *
     * @param status
     * @return
     */
    private PaymentPluginStatus convertFlutterWaveToKillBillStatus(String status) {
        switch (status) {
            case "pending":
                return PaymentPluginStatus.PENDING;
            case "success": // the payment has been included in a payout
                return PaymentPluginStatus.PROCESSED;
            case "canceled":
                return PaymentPluginStatus.CANCELED;
            case "failed": // the payment failed to be processed.
                return PaymentPluginStatus.ERROR;
            default:
                return PaymentPluginStatus.UNDEFINED;
        }
    }

    /**
     *
     * @param currency
     * @return
     */
    private Currency convertFlutterWaveCurrencyToKillBillCurrency(String currency) {

        switch (currency) {
            case "USD":
                return Currency.USD;
            case "NGN":
                return Currency.NGN;
            case "XAF":
                return Currency.XAF;
            case "XOF":
                return Currency.XOF;
            case "EUR":
                return Currency.EUR;
            case "GBP":
                return Currency.GBP;
            default:
                return null;
        }
    }

    /**
     * Converts Kill Bill currency to GoCardless currency
     *
     * @param currency
     * @return
     */
    private String convertKillBillCurrencyToFlutterWaveCurrency(Currency currency) {

        switch (currency) {
            case USD:
                return "USD";
            case NGN:
                return "NGN";
            case XAF:
                return "XAF";
            case XOF:
                return "XOF";
            case EUR:
                return "EUR";
            case GBP:
                return "GBP";
            default:
                return null;

        }
    }

}
