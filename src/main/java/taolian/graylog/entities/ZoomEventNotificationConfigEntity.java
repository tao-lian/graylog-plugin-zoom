package taolian.graylog.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;

import taolian.graylog.ZoomEventNotificationConfig;
import taolian.graylog.ZoomEventNotificationConfig.Builder;

import org.graylog.events.contentpack.entities.EventNotificationConfigEntity;
import org.graylog.events.notifications.EventNotificationConfig;
import org.graylog2.contentpacks.model.entities.EntityDescriptor;
import org.graylog2.contentpacks.model.entities.references.ValueReference;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@AutoValue
@JsonTypeName(ZoomEventNotificationConfigEntity.TYPE_NAME)
@JsonDeserialize(builder = ZoomEventNotificationConfigEntity.Builder.class)
public abstract class ZoomEventNotificationConfigEntity implements EventNotificationConfigEntity {

    public static final String TYPE_NAME = "zoom-notification-v1";
    private static final String FIELD_WEBHOOK = "webhook";
    private static final String FIELD_TOKEN = "token";
    private static final String FIELD_GRAYLOG_URL = "graylog_url";
    private static final String FIELD_JSON_FORMAT = "json_format";
    private static final String FIELD_MESSAGE_TEMPLATE = "message_template";
    private static final String FIELD_JSON_TEMPLATE = "json_template";
    private static final String FIELD_PROXY_ADDRESS = "proxy_address";
    private static final String FIELD_PROXY_USER = "proxy_user";
    private static final String FIELD_PROXY_PASSWORD = "proxy_password";

    @JsonProperty(FIELD_WEBHOOK)
    @NotBlank
    public abstract ValueReference webhook();

    
    @JsonProperty(FIELD_TOKEN)
    @NotBlank
    public abstract ValueReference token();

    @JsonProperty(FIELD_GRAYLOG_URL)
    @NotBlank
    public abstract ValueReference graylogURL();

    @JsonProperty(FIELD_JSON_FORMAT)
    public abstract ValueReference jsonFormat();
    
    @JsonProperty(FIELD_JSON_TEMPLATE)
    public abstract ValueReference jsonTemplate();
    
    @JsonProperty(FIELD_MESSAGE_TEMPLATE)
    public abstract ValueReference messageTemplate();

    @JsonProperty(FIELD_PROXY_ADDRESS)
    public abstract ValueReference proxyAddress();

    @JsonProperty(FIELD_PROXY_USER)
    public abstract ValueReference proxyUser();

    @JsonProperty(FIELD_PROXY_PASSWORD)
    public abstract ValueReference proxyPassword();

    public static Builder builder() {
        return Builder.create();
    }

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public static abstract class Builder implements EventNotificationConfigEntity.Builder<Builder> {

        @JsonCreator
        public static Builder create() {
            return new AutoValue_ZoomEventNotificationConfigEntity.Builder()
                    .type(TYPE_NAME);
        }

        @JsonProperty(FIELD_WEBHOOK)
        public abstract Builder webhook(ValueReference webhook);

        @JsonProperty(FIELD_TOKEN)
        public abstract Builder token(ValueReference token);

        @JsonProperty(FIELD_GRAYLOG_URL)
        public abstract Builder graylogURL(ValueReference graylogURL);

        @JsonProperty(FIELD_JSON_FORMAT)
        public abstract Builder jsonFormat(ValueReference jsonFormat);
        
        @JsonProperty(FIELD_JSON_TEMPLATE)
        public abstract Builder jsonTemplate(ValueReference jsonTemplate);
        
        @JsonProperty(FIELD_MESSAGE_TEMPLATE)
        public abstract Builder messageTemplate(ValueReference messageTemplate);

        @JsonProperty(FIELD_PROXY_ADDRESS)
        public abstract Builder proxyAddress(ValueReference proxyAddress);

        @JsonProperty(FIELD_PROXY_USER)
        public abstract Builder proxyUser(ValueReference proxyUser);

        @JsonProperty(FIELD_PROXY_PASSWORD)
        public abstract Builder proxyPassword(ValueReference proxyPassword);

        public abstract ZoomEventNotificationConfigEntity build();
    }

    @Override
    public EventNotificationConfig toNativeEntity(Map<String, ValueReference> parameters, Map<EntityDescriptor, Object> nativeEntities) {
        return ZoomEventNotificationConfig.builder()
        		.webhook(webhook().asString(parameters))
        		.token(token().asString(parameters))
                .graylogURL(graylogURL().asString(parameters))
                .jsonFormat(jsonFormat().asBoolean(parameters))
                .messageTemplate(messageTemplate().asString(parameters))
                .jsonTemplate(jsonTemplate().asString(parameters))
                .proxyAddress(proxyAddress().asString(parameters))
                .proxyUser(proxyUser().asString(parameters))
                .proxyPassword(proxyPassword().asString(parameters))
                .build();
    }
}
