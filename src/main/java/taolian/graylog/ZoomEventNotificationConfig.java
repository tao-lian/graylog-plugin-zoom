package taolian.graylog;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;

import taolian.graylog.entities.ZoomEventNotificationConfigEntity;

import org.graylog.events.contentpack.entities.EventNotificationConfigEntity;
import org.graylog.events.event.EventDto;
import org.graylog.events.notifications.EventNotificationConfig;
import org.graylog.events.notifications.EventNotificationExecutionJob;
import org.graylog.scheduler.JobTriggerData;
import org.graylog2.contentpacks.EntityDescriptorIds;
import org.graylog2.contentpacks.model.entities.references.ValueReference;
import org.graylog2.plugin.rest.ValidationResult;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@AutoValue
@JsonTypeName(ZoomEventNotificationConfig.TYPE_NAME)
@JsonDeserialize(builder = ZoomEventNotificationConfig.Builder.class)
public abstract class ZoomEventNotificationConfig implements EventNotificationConfig {
    public static final String TYPE_NAME = "zoom-notification-v1";

    private static final String FIELD_WEBHOOK = "webhook";
    private static final String FIELD_TOKEN = "token";
    private static final String FIELD_GRAYLOG_URL = "graylog_url";
    private static final String FIELD_MESSAGE_TEMPLATE = "message_template";
    private static final String FIELD_PROXY_ADDRESS = "proxy_address";
    private static final String FIELD_PROXY_USER = "proxy_user";
    private static final String FIELD_PROXY_PASSWORD = "proxy_password";

    @JsonProperty(FIELD_WEBHOOK)
    @NotBlank
    public abstract String webhook();
    
    @JsonProperty(FIELD_TOKEN)
    @NotBlank
    public abstract String token();

    @JsonProperty(FIELD_GRAYLOG_URL)
    @NotBlank
    public abstract String graylogURL();

    @JsonProperty(FIELD_MESSAGE_TEMPLATE)
    @NotBlank
    public abstract String messageTemplate();

    @JsonProperty(FIELD_PROXY_ADDRESS)
    public abstract String proxyAddress();

    @JsonProperty(FIELD_PROXY_USER)
    public abstract String proxyUser();

    @JsonProperty(FIELD_PROXY_PASSWORD)
    public abstract String proxyPassword();

    @Override @JsonIgnore
    public JobTriggerData toJobTriggerData(EventDto dto) {
        return EventNotificationExecutionJob.Data.builder().eventDto(dto).build();
    }

    public static Builder builder() {
        return Builder.create();
    }

    @Override @JsonIgnore
    public ValidationResult validate() {
        final ValidationResult validation = new ValidationResult();

        if (webhook().isEmpty()) {
            validation.addError(FIELD_TOKEN, "Zoom Incoming Webhook Endpoint cannot be empty.");
        }
        
        if (token().isEmpty()) {
            validation.addError(FIELD_TOKEN, "Zoom Incoming Webhook Token cannot be empty.");
        }
        if (graylogURL().isEmpty()) {
            validation.addError(FIELD_GRAYLOG_URL, "Telegram Notification Graylog URL cannot be empty.");
        }
        if (messageTemplate().isEmpty()) {
            validation.addError(FIELD_MESSAGE_TEMPLATE, "Telegram Notification message template cannot be empty.");
        }

        return validation;
    }

    @AutoValue.Builder
    public static abstract class Builder implements EventNotificationConfig.Builder<Builder> {
        @JsonCreator
        public static Builder create() {
            return new AutoValue_ZoomEventNotificationConfig.Builder()
                    .type(TYPE_NAME);
        }

        @JsonProperty(FIELD_WEBHOOK)
        public abstract Builder webhook(String webhook);
        
        @JsonProperty(FIELD_TOKEN)
        public abstract Builder token(String token);

        @JsonProperty(FIELD_GRAYLOG_URL)
        public abstract Builder graylogURL(String graylogURL);

        @JsonProperty(FIELD_MESSAGE_TEMPLATE)
        public abstract Builder messageTemplate(String messageTemplate);

        @JsonProperty(FIELD_PROXY_ADDRESS)
        public abstract Builder proxyAddress(String proxyAddress);

        @JsonProperty(FIELD_PROXY_USER)
        public abstract Builder proxyUser(String proxyUser);

        @JsonProperty(FIELD_PROXY_PASSWORD)
        public abstract Builder proxyPassword(String proxyPassword);


        public abstract ZoomEventNotificationConfig build();
    }

    @Override
    public EventNotificationConfigEntity toContentPackEntity(EntityDescriptorIds entityDescriptorIds) {
        return ZoomEventNotificationConfigEntity.builder()
        		.webhook(ValueReference.of(webhook()))
        		.token(ValueReference.of(token()))
                .graylogURL(ValueReference.of(graylogURL()))                
                .messageTemplate(ValueReference.of(messageTemplate()))
                .proxyAddress(ValueReference.of(proxyAddress()))
                .proxyUser(ValueReference.of(proxyUser()))
                .proxyPassword(ValueReference.of(proxyPassword()))
                .build();
    }
}