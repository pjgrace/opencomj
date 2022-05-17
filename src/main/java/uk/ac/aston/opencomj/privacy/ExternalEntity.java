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

/**
 * Data structure to describe a DFD (data flow diagram) external entity.
 * 
 * @author pjgrace
 */
public class ExternalEntity {
    
    /**
     * The id is the unique identifying reference to the external entity object
     * in the diagram. This is used for querying and update of the model.
     */
    private final String id;
    
    /**
     * The name of the entity. This corresponds the domain of activity e.g.
     * role-based access control, the name would be a role.
     */
    private final String name;
    
    public ExternalEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    /**
     * Getter Methods
     */
    
    public String getIdentity() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
}
