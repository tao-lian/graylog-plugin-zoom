package taolian.graylog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.floreysoft.jmte.Engine;
import com.google.common.collect.ImmutableList;

import taolian.graylog.models.MessageModelData;
import taolian.graylog.models.StreamModelData;

import org.apache.commons.lang3.StringUtils;
import org.graylog.events.notifications.*;
import org.graylog.events.processor.EventDefinitionDto;
import org.graylog.events.processor.aggregation.AggregationEventProcessorConfig;
import org.graylog.scheduler.JobTriggerDto;
import org.graylog2.jackson.TypeReferences;
import org.graylog2.notifications.Notification;
import org.graylog2.notifications.NotificationService;
import org.graylog2.plugin.MessageSummary;
import org.graylog2.plugin.streams.Stream;
import org.graylog2.plugin.system.NodeId;
import org.graylog2.streams.StreamService;
import javax.inject.Inject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class ZoomEventNotification implements EventNotification {
    public interface Factory extends EventNotification.Factory<ZoomEventNotification> {
        @Override
        ZoomEventNotification create();
    }

    private static final String UNKNOWN = "<unknown>";

    private final EventNotificationService notificationCallbackService;
    private final StreamService streamService;
    private final NotificationService notificationService;
    private final NodeId nodeId;
    private final ObjectMapper objectMapper;
    private final Engine templateEngine;

    @Inject
    public ZoomEventNotification(EventNotificationService notificationCallbackService,
                                     StreamService streamService,
                                     NotificationService notificationService,
                                     NodeId nodeId,
                                     ObjectMapper objectMapper) {
        this.notificationCallbackService = notificationCallbackService;
        this.streamService = streamService;
        this.notificationService = requireNonNull(notificationService, "notificationService");
        this.nodeId = requireNonNull(nodeId, "nodeId");
        this.objectMapper = requireNonNull(objectMapper, "objectMapper");
        this.templateEngine = new Engine();
        //templateEngine.registerNamedRenderer(new RawNoopRenderer());
        //templateEngine.setEncoder(new TelegramHTMLEncoder());
    }

    @Override
    public void execute(EventNotificationContext ctx) throws TemporaryEventNotificationException, PermanentEventNotificationException {
        final ZoomEventNotificationConfig config = (ZoomEventNotificationConfig) ctx.notificationConfig();

        ImmutableList<MessageSummary> backlog = notificationCallbackService.getBacklogForEvent(ctx);
        Map<String, Object> model = getModel(ctx, backlog, config, false);
        String message = templateEngine.transform(config.messageTemplate(), model);


        ZoomClient client = new ZoomClient(config.webhook(), config.token());
        client.SetProxyAddress(config.proxyAddress());
        client.SetProxyUser(config.proxyUser());
        client.SetProxyPassword(config.proxyPassword());

        try {
        	client.Send(message);
        }catch (Exception e) {
			String exceptionDetail = e.toString();
			if (e.getCause() != null) {
				exceptionDetail += " (" + e.getCause() + ")";
			}

			final Notification systemNotification = notificationService.buildNow()
					.addNode(nodeId.toString())
					.addType(Notification.Type.GENERIC)
					.addSeverity(Notification.Severity.NORMAL)
					.addDetail("exception", exceptionDetail);
			notificationService.publishIfFirst(systemNotification);

			throw new PermanentEventNotificationException("Zoom notification is triggered, but sending failed. " + e.getMessage(), e);
		}       	
    }

    private Map<String, Object> getModel(EventNotificationContext ctx, ImmutableList<MessageSummary> backlog, ZoomEventNotificationConfig config, boolean messageTooLong) {
        final Optional<EventDefinitionDto> definitionDto = ctx.eventDefinition();
        final Optional<JobTriggerDto> jobTriggerDto = ctx.jobTrigger();

        List<StreamModelData> streams = streamService.loadByIds(ctx.event().sourceStreams())
                .stream()
                .map(stream -> buildStreamWithUrl(stream, ctx, config.graylogURL()))
                .collect(Collectors.toList());

        final MessageModelData modelData = MessageModelData.builder()
                .eventDefinition(definitionDto)
                .eventDefinitionId(definitionDto.map(EventDefinitionDto::id).orElse(UNKNOWN))
                .eventDefinitionType(definitionDto.map(d -> d.config().type()).orElse(UNKNOWN))
                .eventDefinitionTitle(definitionDto.map(EventDefinitionDto::title).orElse(UNKNOWN))
                .eventDefinitionDescription(definitionDto.map(EventDefinitionDto::description).orElse(UNKNOWN))
                .jobDefinitionId(jobTriggerDto.map(JobTriggerDto::jobDefinitionId).orElse(UNKNOWN))
                .jobTriggerId(jobTriggerDto.map(JobTriggerDto::id).orElse(UNKNOWN))
                .event(ctx.event())
                .backlog(backlog)
                .backlogSize(backlog.size())
                .messageTooLong(messageTooLong)
                .graylogUrl(config.graylogURL())
                .streams(streams)
                .build();

        return objectMapper.convertValue(modelData, TypeReferences.MAP_STRING_OBJECT);
    }

    private StreamModelData buildStreamWithUrl(Stream stream, EventNotificationContext ctx, String graylogURL) {
        String streamUrl = null;
        if(StringUtils.isNotBlank(graylogURL)) {
            streamUrl = StringUtils.appendIfMissing(graylogURL, "/") + "streams/" + stream.getId() + "/search";

            if(ctx.eventDefinition().isPresent()) {
                EventDefinitionDto eventDefinitionDto = ctx.eventDefinition().get();
                if(eventDefinitionDto.config() instanceof AggregationEventProcessorConfig) {
                    String query = ((AggregationEventProcessorConfig) eventDefinitionDto.config()).query();
                    try {
                        streamUrl += "?q=" + URLEncoder.encode(query, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        // url without query as fallback
                    }
                }
            }
        }

        return StreamModelData.builder()
                .id(stream.getId())
                .title(stream.getTitle())
                .description(stream.getDescription())
                .url(Optional.ofNullable(streamUrl).orElse(UNKNOWN))
                .build();
    }
}
