package cm.bwgroup.gateway.plugin;

import cm.bwgroup.gateway.plugin.env.ApiHttpClient;
import org.killbill.billing.osgi.api.Healthcheck;
import org.killbill.billing.tenant.api.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MomoFrancophoneFlutterWaveHealthcheck implements Healthcheck {

    private static final Logger logger = LoggerFactory.getLogger(MomoFrancophoneFlutterWaveHealthcheck.class);
    private final MomoFrancophoneFlutterWaveConfigPropertiesConfigurationHandler momoFrancophoneFlutterWaveConfigPropertiesConfigurationHandler;

    public MomoFrancophoneFlutterWaveHealthcheck(MomoFrancophoneFlutterWaveConfigPropertiesConfigurationHandler momoFrancophoneFlutterWaveConfigPropertiesConfigurationHandler) {
        this.momoFrancophoneFlutterWaveConfigPropertiesConfigurationHandler = momoFrancophoneFlutterWaveConfigPropertiesConfigurationHandler;
    }

    @Override
    public HealthStatus getHealthStatus(Tenant tenant, Map map) {
        if (tenant == null) {
            // The plugin is running
            return HealthStatus.healthy("MomoFrancophoneFlutterWave OK");
        } else {
            // Specifying the tenant lets you also validate the tenant configuration
            final MomoFrancophoneFlutterWaveConfigProperties momoFrancophoneFlutterWaveConfigProperties = momoFrancophoneFlutterWaveConfigPropertiesConfigurationHandler.getConfigurable(tenant.getId());
            return pingFlutterWave(momoFrancophoneFlutterWaveConfigProperties);
        }
    }

    private HealthStatus pingFlutterWave(final MomoFrancophoneFlutterWaveConfigProperties momoFrancophoneFlutterWaveConfigProperties){
        ApiHttpClient httpClient = new ApiHttpClient();
        logger.info("Construct the http client");
        try{
            logger.info("Start query execution");
            httpClient.executeSyncGetRequest("https://api.flutterwave.com/v3/meta/charges", momoFrancophoneFlutterWaveConfigProperties.getSecretKey());

            logger.info("Finish executing query");
            return HealthStatus.healthy("MomoFrancophoneFlutterWave OK");
        } catch (Exception exception) {
            logger.warn("Healthcheck error", exception);
            exception.printStackTrace();
            return HealthStatus.unHealthy("MomoFrancophoneFlutterWave error: " + exception.getMessage());
        }
    }

}
