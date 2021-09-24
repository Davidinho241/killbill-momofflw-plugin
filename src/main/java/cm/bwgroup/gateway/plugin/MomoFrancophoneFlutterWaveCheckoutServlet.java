package cm.bwgroup.gateway.plugin;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import org.jooby.MediaType;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.Status;
import org.jooby.mvc.Local;
import org.jooby.mvc.POST;
import org.killbill.billing.osgi.libs.killbill.OSGIKillbillClock;
import org.killbill.billing.payment.api.PluginProperty;
import org.killbill.billing.payment.plugin.api.HostedPaymentPageFormDescriptor;
import org.killbill.billing.payment.plugin.api.PaymentPluginApiException;
import org.killbill.billing.plugin.api.PluginCallContext;
import org.killbill.billing.tenant.api.Tenant;
import org.killbill.billing.util.callcontext.CallContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.util.Optional;
import java.util.UUID;

public class MomoFrancophoneFlutterWaveCheckoutServlet {

    private final OSGIKillbillClock clock;
    private final MomoFrancophoneFlutterWavePaymentPluginApi momoFrancophoneFlutterWavePaymentPluginApi;
    private static final Logger logger = LoggerFactory.getLogger(MomoFrancophoneFlutterWaveCheckoutServlet.class);

    @Inject
    public MomoFrancophoneFlutterWaveCheckoutServlet(final OSGIKillbillClock clock,
                                     final MomoFrancophoneFlutterWavePaymentPluginApi momoFrancophoneFlutterWavePaymentPluginApi) {
        this.clock = clock;
        this.momoFrancophoneFlutterWavePaymentPluginApi = momoFrancophoneFlutterWavePaymentPluginApi;
    }

    // Setting up Direct Debit mandates using Hosted Payment Pages, before a payment method has been added to the account
    @POST
    public Result createSession(@Named("kbAccountId") final UUID kbAccountId,
                                @Named("success_redirect_url") final Optional<String> successUrl,
                                @Named("redirect_flow_description") final Optional<String> description,
                                @Named("lineItemName") final Optional<String> token,
                                @Local @Named("killbill_tenant") final Tenant tenant) throws PaymentPluginApiException {
        logger.info("Inside createSession");
        final CallContext context = new PluginCallContext(MomoFrancophoneFlutterWaveActivator.PLUGIN_NAME, clock.getClock().getUTCNow(), kbAccountId, tenant.getId());
        final ImmutableList<PluginProperty> properties = ImmutableList.of(
                new PluginProperty("success_redirect_url", successUrl.orElse("https://developer.gocardless.com/example-redirect-uri/"), false),
                new PluginProperty("redirect_flow_description", description.orElse("Kill Bill payment"), false),
                new PluginProperty("session_token", token.orElse("killbill_token"), false));
        final HostedPaymentPageFormDescriptor hostedPaymentPageFormDescriptor = momoFrancophoneFlutterWavePaymentPluginApi.buildFormDescriptor(kbAccountId,
                ImmutableList.of(),
                properties,
                context);
        return Results.with(hostedPaymentPageFormDescriptor, Status.CREATED)
                .type(MediaType.json);
    }
}
