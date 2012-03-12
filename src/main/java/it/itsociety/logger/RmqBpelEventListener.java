package it.itsociety.logger;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Properties;
import java.util.Calendar;
import java.util.Date;

import org.apache.ode.bpel.evt.BpelEvent;
import org.apache.ode.bpel.iapi.BpelEventListener;
import org.apache.ode.bpel.evt.ActivityEvent;
import org.apache.ode.bpel.evt.CorrelationEvent;
import org.apache.ode.bpel.evt.CorrelationMatchEvent;
import org.apache.ode.bpel.evt.CorrelationSetEvent;
import org.apache.ode.bpel.evt.CorrelationSetWriteEvent;
import org.apache.ode.bpel.evt.ExpressionEvaluationEvent;
import org.apache.ode.bpel.evt.ExpressionEvaluationFailedEvent;
import org.apache.ode.bpel.evt.NewProcessInstanceEvent;
import org.apache.ode.bpel.evt.PartnerLinkEvent;
import org.apache.ode.bpel.evt.ProcessCompletionEvent;
import org.apache.ode.bpel.evt.ProcessEvent;
import org.apache.ode.bpel.evt.ProcessInstanceEvent;
import org.apache.ode.bpel.evt.ProcessInstanceStartedEvent;
import org.apache.ode.bpel.evt.ProcessInstanceStateChangeEvent;
import org.apache.ode.bpel.evt.ProcessMessageExchangeEvent;
import org.apache.ode.bpel.evt.ScopeCompletionEvent;
import org.apache.ode.bpel.evt.ScopeEvent;
import org.apache.ode.bpel.evt.ScopeFaultEvent;
import org.apache.ode.bpel.evt.VariableEvent;
import org.apache.ode.bpel.pmapi.TEventInfo;
import org.apache.ode.bpel.evt.ActivityExecEndEvent;
import it.itsociety.logger.JSonEventSerializer;


public class RmqBpelEventListener implements BpelEventListener {

    
    private static ApplicationContext context;
    private static AmqpAdmin amqpAdmin;
    private static Queue mainQueue;
    private static AmqpTemplate template;
    protected Calendar _calendar = Calendar.getInstance(); 


    public void onEvent(BpelEvent bpelEvent) {

        if(bpelEvent instanceof ActivityExecEndEvent) {
            String om = serializeEvent((ActivityExecEndEvent)bpelEvent);
            template.convertAndSend("itsociety.logger", "itsociety.main.queue", om);
        }
    }
    
    public void shutdown() {
        // TODO Auto-generated method stub
		
    }

    public void startup(Properties arg0) {
        context = new ClassPathXmlApplicationContext("rabbitConfiguration.xml");
        
        amqpAdmin = context.getBean(AmqpAdmin.class);
        //mainQueue = new Queue("itsociety.main.queue");
        //amqpAdmin.declareQueue(mainQueue);
        template = context.getBean(AmqpTemplate.class);
    }

    protected String serializeEvent(ActivityExecEndEvent evt) {
        TEventInfo ei = TEventInfo.Factory.newInstance();
        //fillEventInfo(ei, evt);
        return JSonEventSerializer.toJson(evt);
    }

    // Actually unused JSonEventSerializer can't serialize TEventeInfo
    private void fillEventInfo(TEventInfo info, BpelEvent event) {
        info.setName(BpelEvent.eventName(event));
        info.setType(event.getType().toString());
        info.setLineNumber(event.getLineNo());
        info.setTimestamp(toCalendar(event.getTimestamp()));
        if (event instanceof ActivityEvent) {
            info.setActivityName(((ActivityEvent) event).getActivityName());
            info.setActivityId(((ActivityEvent) event).getActivityId());
            info.setActivityType(((ActivityEvent) event).getActivityType());
            info.setActivityDefinitionId(((ActivityEvent) event).getActivityDeclarationId());
        }
        if (event instanceof CorrelationEvent) {
            info.setPortType(((CorrelationEvent) event).getPortType());
            info.setOperation(((CorrelationEvent) event).getOperation());
            info.setMexId(((CorrelationEvent) event).getMessageExchangeId());
        }
        if (event instanceof CorrelationMatchEvent) {
            info.setPortType(((CorrelationMatchEvent) event).getPortType());
        }
        if (event instanceof CorrelationSetEvent) {
            info.setCorrelationSet(((CorrelationSetEvent) event).getCorrelationSetName());
        }
        if (event instanceof CorrelationSetWriteEvent) {
            info.setCorrelationKey(((CorrelationSetWriteEvent) event).getCorrelationSetName());
        }
        if (event instanceof ExpressionEvaluationEvent) {
            info.setExpression(((ExpressionEvaluationEvent) event).getExpression());
        }
        if (event instanceof ExpressionEvaluationFailedEvent) {
            info.setFault(((ExpressionEvaluationFailedEvent) event).getFault());
        }
        if (event instanceof NewProcessInstanceEvent) {
            if ((((NewProcessInstanceEvent) event).getRootScopeId()) != null)
                info.setRootScopeId(((NewProcessInstanceEvent) event).getRootScopeId());
            info.setScopeDefinitionId(((NewProcessInstanceEvent) event).getScopeDeclarationId());
        }
        if (event instanceof PartnerLinkEvent) {
            info.setPartnerLinkName(((PartnerLinkEvent) event).getpLinkName());
        }
        if (event instanceof ProcessCompletionEvent) {
            info.setFault(((ProcessCompletionEvent) event).getFault());
        }
        if (event instanceof ProcessEvent) {
            info.setProcessId(((ProcessEvent) event).getProcessId());
            info.setProcessType(((ProcessEvent) event).getProcessName());
        }
        if (event instanceof ProcessInstanceEvent) {
            info.setInstanceId(((ProcessInstanceEvent) event).getProcessInstanceId());
        }
        if (event instanceof ProcessInstanceStartedEvent) {
            info.setRootScopeId(((ProcessInstanceStartedEvent) event).getRootScopeId());
            info.setRootScopeDeclarationId(((ProcessInstanceStartedEvent) event).getScopeDeclarationId());
        }
        if (event instanceof ProcessInstanceStateChangeEvent) {
            info.setOldState(((ProcessInstanceStateChangeEvent) event).getOldState());
            info.setNewState(((ProcessInstanceStateChangeEvent) event).getNewState());
        }
        if (event instanceof ProcessMessageExchangeEvent) {
            info.setPortType(((ProcessMessageExchangeEvent) event).getPortType());
            info.setOperation(((ProcessMessageExchangeEvent) event).getOperation());
            info.setMexId(((ProcessMessageExchangeEvent) event).getMessageExchangeId());
        }
        if (event instanceof ScopeCompletionEvent) {
            //info.setSuccess(((ScopeCompletionEvent) event).isSuccess());
            //info.setFault(((ScopeCompletionEvent) event).getFault());
        }
        if (event instanceof ScopeEvent) {
            info.setScopeId(((ScopeEvent) event).getScopeId());
            if (((ScopeEvent) event).getParentScopeId() != null)
                info.setParentScopeId(((ScopeEvent) event).getParentScopeId());
            if (((ScopeEvent) event).getScopeName() != null)
                info.setScopeName(((ScopeEvent) event).getScopeName());
            info.setScopeDefinitionId(((ScopeEvent) event).getScopeDeclarationId());
        }
        if (event instanceof ScopeFaultEvent) {
            info.setFault(((ScopeFaultEvent) event).getFaultType());
            info.setFaultLineNumber(((ScopeFaultEvent) event).getFaultLineNo());
            info.setExplanation(((ScopeFaultEvent) event).getExplanation());
        }
        if (event instanceof VariableEvent) {
            info.setVariableName(((VariableEvent) event).getVarName());
        }
    }
    
    /**
     * Convert a {@link Date} to a {@link Calendar}.
     * 
     * @param dtime
     *            a {@link Date}
     * @return a {@link Calendar}
     */
    private Calendar toCalendar(Date dtime) {
        if (dtime == null)
            return null;
        
        Calendar c = (Calendar) _calendar.clone();
        c.setTime(dtime);
        return c;
    }
}
