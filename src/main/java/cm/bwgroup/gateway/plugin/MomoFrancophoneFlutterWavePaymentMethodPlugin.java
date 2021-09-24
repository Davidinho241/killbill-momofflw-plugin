package cm.bwgroup.gateway.plugin;

import org.killbill.billing.payment.api.PluginProperty;
import org.killbill.billing.plugin.api.payment.PluginPaymentMethodPlugin;

import java.util.List;
import java.util.UUID;

public class MomoFrancophoneFlutterWavePaymentMethodPlugin extends PluginPaymentMethodPlugin {
    public MomoFrancophoneFlutterWavePaymentMethodPlugin(UUID kbPaymentMethodId,
                                                         String externalPaymentMethodId,
                                                         boolean isDefaultPaymentMethod,
                                                         List<PluginProperty> properties) {
        super(kbPaymentMethodId, externalPaymentMethodId, isDefaultPaymentMethod, properties);
    }
}
