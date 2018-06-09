package Node;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class MapController {
    private final MapService mapService;
    private final SimpMessagingTemplate template;
    private final ThisNodeInfo thisNodeInfo;

    @Autowired
    public MapController(
            MapService mapService,
            SimpMessagingTemplate template,
            @Qualifier("Node/NodeConfigurator/thisNodeInfo")
            ThisNodeInfo thisNodeInfo
            ){
        this.mapService = mapService;
        this.template = template;
        this.thisNodeInfo = thisNodeInfo;
    }

    @MessageMapping("/mapMessage")
    public void mapMessage(MapMessage message) {
        if(thisNodeInfo.getUid() != message.getTarget()) {
            if (log.isTraceEnabled()) {
               log.trace("<---received  map message {}", message);
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("<---received map message {}", message);
            }
            mapService.doActiveThings();
        }
    }

    public void sendMapMessage(int targetUid) throws MessagingException {
        MapMessage message = new MapMessage(
                thisNodeInfo.getUid(),
                targetUid
                );
        if(log.isDebugEnabled()){
            log.debug("--->sending map message: {}", message);
        }
        template.convertAndSend("/topic/mapMessage", message);
        log.trace("MapMessage message sent");
    }
}