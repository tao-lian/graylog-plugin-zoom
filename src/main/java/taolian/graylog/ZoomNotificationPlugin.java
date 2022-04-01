package taolian.graylog;

import org.graylog2.plugin.*;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Implement the Plugin interface here.
 */
public class ZoomNotificationPlugin implements Plugin {
    @Override
    public PluginMetaData metadata() {
        return new MetaData();
    }

    @Override
    public Collection<PluginModule> modules () {
        return Collections.<PluginModule>singletonList(new ZoomNotificationModule());
    }

    public static class MetaData implements PluginMetaData {
        private static final String PLUGIN_PROPERTIES = "taolian.graylog.graylog-plugin-zoom/graylog-plugin.properties";
    
        @Override
        public String getUniqueId() {
            return "taolian.graylog.ZoomNotificationPlugin";
        }
    
        @Override
        public String getName() {
            return "ZoomNotification";
        }
    
        @Override
        public String getAuthor() {
            return "Tao Lian <liantao@hotmail.com>";
        }
    
        @Override
        public URI getURL() {
            return URI.create("https://github.com/tao-lian/graylog-plugin-zoom");
        }
    
        @Override
        public Version getVersion() {
            return Version.fromPluginProperties(getClass(), PLUGIN_PROPERTIES, "version", Version.from(0, 0, 0, "unknown"));
        }
    
        @Override
        public String getDescription() {
            return "Send Zoom message via Incoming Webhook";
        }
    
        @Override
        public Version getRequiredVersion() {
            return Version.fromPluginProperties(getClass(), PLUGIN_PROPERTIES, "graylog.version", Version.from(0, 0, 0, "unknown"));
        }

        @Override
        public Set<ServerStatus.Capability> getRequiredCapabilities() {
            return Collections.emptySet();
        }
    }
}
