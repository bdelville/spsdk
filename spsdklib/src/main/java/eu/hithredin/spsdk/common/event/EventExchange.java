package eu.hithredin.spsdk.common.event;

import de.greenrobot.event.EventBus;

public class EventExchange {

    public int idSender;
    public int idTarget;

    public EventExchange(EventExchange originMsg){
        idTarget = originMsg.idSender;
    }
}
