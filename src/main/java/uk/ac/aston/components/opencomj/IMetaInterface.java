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

package uk.ac.aston.components.opencomj;

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
