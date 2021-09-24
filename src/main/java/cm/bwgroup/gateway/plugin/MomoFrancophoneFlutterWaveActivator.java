package cm.bwgroup.gateway.plugin;

import org.killbill.billing.osgi.api.Healthcheck;
import org.killbill.billing.osgi.api.OSGIPluginProperties;
import org.killbill.billing.osgi.libs.killbill.KillbillActivatorBase;
import org.killbill.billing.payment.plugin.api.PaymentPluginApi;
import org.killbill.billing.plugin.api.notification.PluginConfigurationEventHandler;
import org.killbill.billing.plugin.core.resources.jooby.PluginApp;
import org.killbill.billing.plugin.core.resources.jooby.PluginAppBuilder;
import org.osgi.framework.BundleContext;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import java.util.Hashtable;

public class MomoFrancophoneFlutterWaveActivator extends KillbillActivatorBase {

    public static final String PLUGIN_NAME = "killbill-MomoFrancophoneFlutterWave";
    private MomoFrancophoneFlutterWaveConfigPropertiesConfigurationHandler momoFrancophoneFlutterWaveConfigPropertiesConfigurationHandler;

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        momoFrancophoneFlutterWaveConfigPropertiesConfigurationHandler = new MomoFrancophoneFlutterWaveConfigPropertiesConfigurationHandler(PLUGIN_NAME, killbillAPI, "Test");

        final MomoFrancophoneFlutterWaveConfigProperties stripeConfigProperties = momoFrancophoneFlutterWaveConfigPropertiesConfigurationHandler.createConfigurable(
                configProperties.getProperties());
        momoFrancophoneFlutterWaveConfigPropertiesConfigurationHandler.setDefaultConfigurable(stripeConfigProperties);

        // Expose the healthcheck, so other plugins can check on the Momo Francophone FlutterWave status
        final MomoFrancophoneFlutterWaveHealthcheck momoFrancophoneFlutterWaveHealthcheck = new MomoFrancophoneFlutterWaveHealthcheck(momoFrancophoneFlutterWaveConfigPropertiesConfigurationHandler);
        registerHealthcheck(context, momoFrancophoneFlutterWaveHealthcheck);

        // Register the payment plugin
        final MomoFrancophoneFlutterWavePaymentPluginApi pluginApi = new MomoFrancophoneFlutterWavePaymentPluginApi(killbillAPI, clock.getClock(), momoFrancophoneFlutterWaveConfigPropertiesConfigurationHandler);
        registerPaymentPluginApi(context, pluginApi);

        // Register the servlet, which is used as the entry point to generate the Hosted Payment Pages redirect url
        final PluginApp pluginApp = new PluginAppBuilder(PLUGIN_NAME, killbillAPI, dataSource, super.clock, configProperties)
                .withRouteClass(MomoFrancophoneFlutterWaveCheckoutServlet.class)
                .withService(pluginApi)
                .withService(clock)
                .build();
        final HttpServlet goCardlessServlet = PluginApp.createServlet(pluginApp);
        registerServlet(context, goCardlessServlet);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
    }


    public void registerHandlers() {
        final PluginConfigurationEventHandler handler = new PluginConfigurationEventHandler(momoFrancophoneFlutterWaveConfigPropertiesConfigurationHandler);
        dispatcher.registerEventHandlers(handler);
    }

    private void registerServlet(final BundleContext context, final HttpServlet servlet) {
        final Hashtable<String, String> props = new Hashtable<String, String>();
        props.put(OSGIPluginProperties.PLUGIN_NAME_PROP, PLUGIN_NAME);
        registrar.registerService(context, Servlet.class, servlet, props);
    }

    private void registerPaymentPluginApi(final BundleContext context, final PaymentPluginApi api) {
        final Hashtable<String, String> props = new Hashtable<String, String>();
        props.put(OSGIPluginProperties.PLUGIN_NAME_PROP, PLUGIN_NAME);
        registrar.registerService(context, PaymentPluginApi.class, api, props);
    }

    private void registerHealthcheck(final BundleContext context, final MomoFrancophoneFlutterWaveHealthcheck healthcheck) {
        final Hashtable<String, String> props = new Hashtable<String, String>();
        props.put(OSGIPluginProperties.PLUGIN_NAME_PROP, PLUGIN_NAME);
        registrar.registerService(context, Healthcheck.class, healthcheck, props);
    }
}
