package it.itsociety.logger;

import it.itsociety.logger.ItsEvent;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Exchange;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Properties;
import java.util.Calendar;
import java.util.Date;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


import org.apache.ode.bpel.evt.BpelEvent;
import org.apache.ode.bpel.iapi.BpelEventListener;
import org.apache.ode.bpel.evt.ProcessEvent;
import org.apache.ode.bpel.evt.ActivityExecEndEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import it.itsociety.webserver.sdk.messages.MessagesSocketUtils;
import it.itsociety.webserver.sdk.messages.model.ItsocMessage;
import it.itsociety.webserver.sdk.messages.model.Actor;

import it.itsociety.logger.JSonEventSerializer;

public class RmqBpelEventListener implements BpelEventListener {

    
    private static ApplicationContext context;
    private static AmqpAdmin amqpAdmin;
    private String routing_key=null;
    private MessagesSocketUtils messagesUtils=null;
    private AmqpTemplate amqp=null;
    
    protected Calendar _calendar = Calendar.getInstance();

    public RmqBpelEventListener() {
        context = new ClassPathXmlApplicationContext("rabbitConfiguration.xml");
        amqp = (AmqpTemplate) context.getBean("amqpTemplate");
        //FIXME generate user-dependant routing keys?
        routing_key="service.event";
	messagesUtils = new MessagesSocketUtils("bpelLogger");
    }

    public void onEvent(BpelEvent bpelEvent) {
        if(bpelEvent instanceof ActivityExecEndEvent) {
	    
	    Actor mySelf = new Actor("bpelLogger", "logger");
	    
            String om = serializeEvent((ActivityExecEndEvent)bpelEvent);
	    ItsocMessage message = messagesUtils.createMessageObj(mySelf, null, om);

            amqp.convertAndSend(routing_key,message);
	    
        }
    }
    
    public void shutdown() {
		
    }

    public void startup(Properties arg0) {
    }

    protected String getUserId(BpelEvent evt  ) {
	String[] variables = evt.eventContext.getVariableNames();
	String userIdVar = "userId";
	
	if(Arrays.asList(variables).contains(userIdVar))
	    {
		return evt.eventContext.getVariableData(userIdVar).replaceAll("\\<.*?>","").replaceAll("\n", "");
	    }
	return "null";
    }
    
    protected String serializeEvent(ActivityExecEndEvent event) {
	ItsEvent info = new ItsEvent();
	info.setName(BpelEvent.eventName(event).toString());
	info.setType(event.getType().toString());
	info.setTimestamp(event.getTimestamp().getTime());
	info.setProcessName(((ProcessEvent) event).getProcessName().toString());
	info.setProcessId(((ProcessEvent) event).getProcessId().toString());
	info.setUserId(getUserId(event));
   
	return JSonEventSerializer.toJson(info);	
    }

}
