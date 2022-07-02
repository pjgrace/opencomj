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

/**
 * API to update the runtime model of the DFD (dataflow diagram) of the 
 * component system.
 * 
 * @author pjgrace
 */
public interface IUpdateMetaDataFlow {
    
    /**
     * The runtime generates an event to notify the runtime model that a new
     * external entity has been added to the system e.g. stakeholder, user
     * or system.
     * @param newEntity The data structure of the new entity.
     * @return 
     */
    public boolean addExternalEntity(ExternalEntity newEntity);
}
