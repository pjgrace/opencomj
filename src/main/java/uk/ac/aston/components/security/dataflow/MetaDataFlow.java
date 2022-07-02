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
package uk.ac.aston.components.security.dataflow;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Implementation of the data flow runtime model. There is a set of methods
 * that react to runtime events from a component based system; these combine
 * to curate an accurate DFD (data flow diagram) of the running system. There
 * is also a set of methods to query the runtime model. This can be used to
 * assess a system for security and privacy risks.
 * 
 * @author pjgrace
 */
public class MetaDataFlow implements IUpdateMetaDataFlow, IQueryMetaDataFlow{
    
    private final HashMap<String, ExternalEntity> entities = new HashMap<>();
    private final HashMap<String, DataFlow> flows = new HashMap<>();
    private final HashMap<String, Warehouse> stores = new HashMap<>();
    private final HashMap<String, DataProcess> functions = new HashMap<>();
    
    public MetaDataFlow() {
        
    }
    
    /**
     * The runtime generates an event to notify the runtime model that a new
     * external entity has been added to the system e.g. stakeholder, user
     * or system.
     * @param newEntity The data structure of the new entity.
     * @return 
     */
    @Override
    public boolean addExternalEntity(ExternalEntity newEntity) {
        return entities.put(newEntity.getIdentity(), newEntity) != null;
    }
    
    /**
     * Remove the external entity from the runtime model 
     * @param id The unique identifier of the entity to remove.
     * @return 
     */
    public boolean removeExternalEntity(String id) {
        return entities.remove(id) != null;
    }
    
    /**
     * 
     * @param newProcess The data process to add
     * @return 
     */
    public boolean addDataFunction(DataProcess newProcess) {
        return functions.put(newProcess.getProcessName(), newProcess) != null;
    }
    
    
    public static DataProcess getProcessFromInterface(String interfaceClass){
        try {
            Class<?> cls = Class.forName(interfaceClass);
            Annotation[] annotations = cls.getAnnotations();

            for(Annotation annotation : annotations){
                if(annotation instanceof PrivacyFunction){
                    String function = ((PrivacyFunction) annotation).function();
                    String id = ((PrivacyFunction) annotation).id();
                    return new DataProcess(id, function);
                }
            }
            
            return null;
        }
        catch(ClassNotFoundException e){
            System.err.println("Error reading Class interface from string - " + e.getMessage());
            return null;
        }
    }
    
    public static boolean isPrivateMethod(String interfaceClass){
        
        try {
            Class<?> cls = Class.forName(interfaceClass);
            Method[] methods = cls.getDeclaredMethods();
            for (Method method: methods) {
                Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                Class[] parameterTypes = method.getParameterTypes();
                
                int i=0;
                for(Annotation[] annotations : parameterAnnotations){
                    for(Annotation annotation : annotations){
                        if(annotation instanceof Private){
                            return true;
                        }
                    }
                }
            }  
            return false;
        }
        catch(ClassNotFoundException | SecurityException e){
            return false;
        }
    }
    
    public static boolean isPrivateInterface(String interfaceClass){
        
        try {
            Class<?> cls = Class.forName(interfaceClass);
            Annotation[] annotations = cls.getAnnotations();

            for(Annotation annotation : annotations){
                if(annotation instanceof PrivacyFunction){
                    return true;
                }
            }
            
            return false;
        }
        catch(ClassNotFoundException | SecurityException e){
            return false;
        }
    }
}
