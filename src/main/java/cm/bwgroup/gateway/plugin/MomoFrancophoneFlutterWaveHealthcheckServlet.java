package cm.bwgroup.gateway.plugin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.Inject;
import org.jooby.Result;
import org.jooby.mvc.GET;
import org.jooby.mvc.Local;
import org.killbill.billing.plugin.core.resources.PluginHealthcheck;
import org.killbill.billing.tenant.api.Tenant;

import javax.inject.Named;
import java.util.Optional;

public class MomoFrancophoneFlutterWaveHealthcheckServlet extends PluginHealthcheck {

    private final MomoFrancophoneFlutterWaveHealthcheck healthcheck;

    @Inject
    public MomoFrancophoneFlutterWaveHealthcheckServlet(final MomoFrancophoneFlutterWaveHealthcheck healthcheck) {
        this.healthcheck = healthcheck;
    }

    @GET
    public Result check(@Local @Named("killbill_tenant") final Optional<Tenant> tenant) throws JsonProcessingException {
        return check(healthcheck, tenant.orElse(null), null);
    }
}
