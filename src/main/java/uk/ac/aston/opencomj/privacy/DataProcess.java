/////////////////////////////////////////////////////////////////////////
//
// © Aston University 2020
//
// Copyright in this library belongs to the University of Southampton
// University Road, Highfield, Southampton, UK, SO17 1BJ
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
// Created By : Paul Grace
//
/////////////////////////////////////////////////////////////////////////
//
//  License : GNU Lesser General Public License, version 3
//
/////////////////////////////////////////////////////////////////////////
package uk.ac.aston.opencomj.privacy;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data process element within the data flow diagram (DFD) runtime model. This
 * will normally map onto a function provided by an individual component (or
 * component framework). Generally, this is a set of methods (API) that implement
 * the function (the API is annotated with the function label to identify this
 * piece of system behaviour).
 * 
 * @author pjgrace
 */
public class DataProcess {
    
    /**
     * The descriptor in the model of the function. This will map onto domain
     * functions e.g. high-level privacy functions (marketing, optimization) or
     * more specific data functions (aggregation, pseudonymization).
     */
    private String FunctionName;
    
    /**
     * Unique identifier of this element in the runtime model.
     */
    private String id;
    
    /**
     * Pointer to the full java class of the function
     */
    private Class<?> API;
    
    public DataProcess(String id, String fName, String className) {
        this.id = id;
        this.FunctionName = fName;
        try {
            API = Class.forName(className);
        } catch (ClassNotFoundException ex) {
            System.err.println("Class create error: " + ex.getMessage());
        }
    }
    
    /**
     * Getter methods
     */
    public String getFunctionName() {
        return this.FunctionName;
    }
    
    public String getIdentity() {
        return this.id;
    }
    
    public Class<?> getFunctionClass() {
        return this.API;
    }
}
