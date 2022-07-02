/////////////////////////////////////////////////////////////////////////
//
// © Aston University 2022
//
// This software may not be used, sold, licensed, transferred, copied
// or reproduced in whole or in part in any manner or form or in or
// on any media by any person other than in accordance with the terms
// of the Licence Agreement supplied with the software, or otherwise
// without the prior written consent of the copyright owners.
//
// This software is distributed WITHOUT ANY WARRANTY, without even the
// implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
// PURPOSE, except where stated in the Licence Agreement supplied with
// the software.
//
/////////////////////////////////////////////////////////////////////////
//
//  License : GNU Lesser General Public License, version 3
//
/////////////////////////////////////////////////////////////////////////
package uk.ac.aston.components.security.dataflow;

import java.util.LinkedList;
import java.util.List;
import uk.ac.aston.components.opencomj.IUnknown;

/**
 * Data process element within the data flow diagram (DFD) runtime model. 
 * 
 * A component that processes data maps onto the process element. 
 * 
 */
public class DataProcess {
    
    /**
     * The process name.
     */
    private final String processName;
    
    /**
     * Link to component.
     */
    private final String componentPointer;
    
    /**
     * List of data flows
     */
    List<DataFlow> inDataFlows;
    List<DataFlow> outDataFlows;
    
    
    /**
     * Constructor for process entry into the meta model.
     * @param id
     * @param func 
     */
    public DataProcess(String id, String func) {
        this.processName = id;
        this.componentPointer = func;
        inDataFlows = new LinkedList<>();
        outDataFlows = new LinkedList<>();
    }
    
    /**
     * Getter methods
     * @return 
     */
    public String getProcessName() {
        return this.processName;
    }
    
    public String getComponent() {
        return this.componentPointer;
    }

}
