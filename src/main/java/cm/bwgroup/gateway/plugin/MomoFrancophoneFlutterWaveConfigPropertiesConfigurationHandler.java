package cm.bwgroup.gateway.plugin;

import org.killbill.billing.osgi.libs.killbill.OSGIKillbillAPI;
import org.killbill.billing.plugin.api.notification.PluginTenantConfigurableConfigurationHandler;

import java.util.Properties;

public class MomoFrancophoneFlutterWaveConfigPropertiesConfigurationHandler extends PluginTenantConfigurableConfigurationHandler<MomoFrancophoneFlutterWaveConfigProperties> {

    private final String region;

    public MomoFrancophoneFlutterWaveConfigPropertiesConfigurationHandler(final String pluginName,
                                                      final OSGIKillbillAPI osgiKillbillAPI,
                                                      final String region) {
        super(pluginName, osgiKillbillAPI);
        this.region = region;
    }

    @Override
    protected MomoFrancophoneFlutterWaveConfigProperties createConfigurable(final Properties properties) {
        return new MomoFrancophoneFlutterWaveConfigProperties(properties, region);
    }
}

