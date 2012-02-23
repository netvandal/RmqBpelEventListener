package it.itsociety.logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.ode.bpel.pmapi.TEventInfo;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JSonEventSerializer {

	public static String toJson(TEventInfo ei) {
		String result =null;
        ObjectMapper mapper = new ObjectMapper();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
			mapper.writeValue(out, ei);
	        result= new String(out.toByteArray(), "UTF-8");
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return result;


        /*    String name=getName();
        String type=getType();
        int line_number=getLineNumber();
        java.util.Calendar timestamp= getTimestamp();
        javax.xml.namespace.QName process_id=getProcessId();
        javax.xml.namespace.QName process_typegetProcessType();
        long instance_id=getInstanceId();
        long scope_id=getScopeId();
        long parent_scope_id=getParentScopeId();
        String scope_name=getScopeName();
        int scope_definition_id=getScopeDefinitionId();
        long activity_id=getActivityId();
        String activity_name=getActivityName();
        String activity_type=getActivityType();
        int activity_definition_id=getActivityDefinitionId();
        String activity_failure_reason=getActivityFailureReason();
        String activity_recovery_action=getActivityRecoveryAction();
        String variable_name=getVariableName();
        String new_value=getNewValue();
        javax.xml.namespace.QName port_type=getPortType();
        String operation=getOperation();
        String correlation_set=getCorrelationSet();
        String mex_id=getMexId();
        String correlation_key=getCorrelationKey();
        String expression=getExpression();
        javax.xml.namespace.QName fault=getFault();
        int fault_line_number=getFaultLineNumber();
        String explanation=getExplanation();
        String result=getResult();
        long root_scope_id=getRootScopeId();
        int root_scope_declaration_id=getRootScopeDeclarationId();
        String partner_link_name=getPartnerLinkName();
        int old_state=getOldState();
        int new_state=getNewState();
        boolean success=getSuccess();   
        */


    }
}
