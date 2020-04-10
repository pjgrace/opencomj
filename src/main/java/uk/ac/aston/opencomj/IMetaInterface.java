/*
 * IMetaInterface.java
 *
 * OpenCOMJ is a flexible component model for reconfigurable reflection developed at Lancaster University.
 * Copyright (C) 2005 Paul Grace
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program; if not,
 * write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package uk.ac.lancs.opencomj;

import java.util.List;
import java.util.Map;


/**
 * This interface is implemented by every OpenCOM component. It support the interface meta-model
 * of OpenCOM. Allow introspection and manipulation of interfaces and receptacles on the component
 * plus the corresponding meta-data attachments.
 *
 * @author  Paul Grace
 * @version 1.2.3
 *
 */
public interface IMetaInterface {
    /**
    * Returns a Vector of meta-information. Each elements of the Vector is an object of
 type OCMRecpMetaInfo, which describes the attributes of individual receptacles
 including: type (single or multiple) & interface type.
    * @param ppRecpMetaInfo a Vector to be filled with receptacle meta-information.
    * @return an Integer describing the number of receptacles on the component.
    **/
    int enumRecps(List<OCMRecpMetaInfo> ppRecpMetaInfo);

    /**
    * Returns a Vector of meta-information. Each elements of the Vector is a String describing
    * that interface's type.
    * @param ppIntf a Vector to be filled with interface meta-information.
    * @return an Integer describing the number of interfaces on the component.
    **/
    int enumIntfs(List<Class> ppIntf);

    /**
     * Meta-data can be attached to each interface/receptacle of a component. This method adds a name
     * value pair to a given interface or receptacle instance.
     * @param iid the type of the interface or receptacle.
     * @param kind a string saying whether to attach to an interface or a receptacle.
     * @param name A String describing the attribute name.
     * @param type A String describing the attribute type.
     * @param value An object representing the attribute value.
     * @return A boolean indicating the success of the operation.
     **/
    boolean setAttributeValue(String iid, String kind, String name, String type, Object value);

    /**
     * Meta-data can be retrieved from each interface/receptacle of a component. This method
     * retrieves the value of a name attribute on a receptacle or interface.
     * @param iid the type of the interface or receptacle.
     * @param kind a string saying whether to attach to an interface or a receptacle.
     * @param name A String describing the attribute name.
     * @return A TypedAttribute object containing the value and type of the meta-data attribute.
     **/
    TypedAttribute getAttributeValue(String iid, String kind, String name);

    /**
     * This method retrieves all the meta-data stored on the interface or receptacle.
     * @param iid the type of the interface or receptacle.
     * @param kind a string saying whether to attach to an interface or a receptacle.
     * @return A hash map containing all of the attribute-value pairs for the receptacle or interface.
     **/
    Map<String, TypedAttribute> getAllValues(String kind, String iid);

}
