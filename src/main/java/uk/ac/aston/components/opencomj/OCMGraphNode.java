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

import java.util.ArrayList;
import java.util.List;

/**
 * The system graph in OpenCOM is a set of OCMGraphNode objects stored in a list.
 * How they are connected together is stored within the information in the node.
 * Every OpenCOM component has a corresponding node in the system graph i.e. its meta
 * representation.
 *
 * @author  Paul Grace
 * @version 1.2.3
 */
public class OCMGraphNode {

    /** An associated unique name for the component. */
    private final transient String name;

    /** An readable type description for the component. */
    private final transient String type;

    /**
     * Getter for the component name field.
     * @return The string component name.
     */
    public final String getComponentName() {
        return this.name;
    }

    /**
     * Getter for the component type field.
     * @return The string component type.
     */
    public final String getComponentType() {
        return this.type;
    }

    /**
     * The reference to the physical instance of the component.
     */
    private transient Object pIUnknown;

    /**
     * Getter for the component field.
     * @return The reference to the component.
     */
    public final Object getComponent() {
        return this.pIUnknown;
    }

    /**
     * Setter for the component field.
     * @param compRef The reference to the component.
     */
    public final void setComponent(final Object compRef) {
        this.pIUnknown = compRef;
    }

    /**
     * list of OCMGraphRecpInfo nodes describing the list of connections on receptacles for the component.
     */
    private final transient List<OCMGraphRecpInfo> pGRecpInfo;

    /**
     * Getter for the receptacle list field.
     * @return The list of receptacles.
     */
    public final List<OCMGraphRecpInfo> getReceptacles() {
        return this.pGRecpInfo;
    }

    /**
     * list of OCMGraphIntfInfo nodes describing the list of connections on interfaces for this component.
     */
    private final transient List<OCMGraphIntfInfo> pGIntfInfo;

    /**
     * Getter for the interface list field.
     * @return The list of interfaces.
     */
    public final List<OCMGraphIntfInfo> getInterfaces() {
        return this.pGIntfInfo;
    }

    /**
     * list of OCM_DelegatorInfo_t nodes describing the list of delegated interfaces for this component.
     */
    private final transient List<OCMDelegatorInfo> pGDelInfo;

    /**
     * Getter for the delegator list field.
     * @return The list of delegators.
     */
    public final List<OCMDelegatorInfo> getDelegators() {
        return this.pGDelInfo;
    }

    /**
     * Constructor creates a new instance of OCM_GraphNode_t object.
     * @param componentName The name of the component.
     * @param cType The component class type
     * @param compInterface The reference to the component.
     */
    public OCMGraphNode(final String componentName, final String cType, final Object compInterface) {
        pGIntfInfo = new ArrayList();
        pGRecpInfo = new ArrayList();
        pGDelInfo = new ArrayList();
        name = componentName;
        type = cType;
        pIUnknown = compInterface;
    }

}
