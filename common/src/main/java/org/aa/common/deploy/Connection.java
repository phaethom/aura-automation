/**	   Copyright 


**/
package org.aa.common.deploy;

import java.util.Set;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.aa.common.exception.DeployException;
import org.aa.common.helper.ConnectionObjects;
import org.aa.common.helper.SDAdminClientFactory;
import org.aa.common.log.SDLog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import com.ibm.ws.management.configservice.ConfigServiceImpl;
import com.ibm.websphere.management.AdminClient;
import com.ibm.websphere.management.Session;
import com.ibm.websphere.management.configservice.ConfigService;
import com.ibm.websphere.management.exception.AdminException;
import com.ibm.websphere.management.exception.ConfigServiceException;
import com.ibm.websphere.management.exception.ConnectorException;

public class Connection {
    protected AdminClient adminClient;
    protected ObjectName nodeAgent;

    protected Session session ;
    protected String sessionID ;
    protected ConfigService configService ;
	final Log logger  = LogFactory.getLog(Connection.class);	

    protected void createAdminClient(DeployInfo deployInfo)
    	throws DeployException,AdminException, ConnectorException{
    	
    	SDAdminClientFactory sdAdminClientFactory  = new SDAdminClientFactory (); 

    	SDLog.log("Server: " +  deployInfo.getHost() );
		SDLog.log("******************************");

    	ConnectionObjects connectionObjects = sdAdminClientFactory.getConnectionObjects(deployInfo);

		//createAdminClient(deployInfo);
    	configService = connectionObjects.getConfigService();
		adminClient = connectionObjects.getAdminClient();
    	configService = connectionObjects.getConfigService();
    	nodeAgent = connectionObjects.getNodeAgent(); 
    	session = connectionObjects.getSession();
    	sessionID = connectionObjects.getSessionID();
    	String cellName = getCellName();
    	deployInfo.setCell(cellName);
    }
    
    private String getCellName() throws ConfigServiceException, ConnectorException{
		ObjectName[] configIDs = configService.resolve(session, "Cell=");
		if (configIDs.length > 0){
			String cellName = configService.getAttribute(session, (ObjectName)configIDs[0], "name").toString();
			System.out.println(" Cell name is " + cellName);
			return cellName;
		}else{
			return null;
		}

    }

    protected ObjectName getObjectName(String query)
    	throws MalformedObjectNameException,ConnectorException{
	    ObjectName queryName = new ObjectName (query);
	    ObjectName _objectName = null;
	    Set s = adminClient.queryNames(queryName, null);
	    
	    if (!s.isEmpty()){
	    	_objectName   = (ObjectName)s.iterator().next();
	    }
	    return _objectName ;
    }
}
