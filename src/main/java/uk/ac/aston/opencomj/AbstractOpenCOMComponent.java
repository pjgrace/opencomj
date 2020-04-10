/*
 * AbstractOpenCOMComponent.java
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * OpenCOM Component is a public abstract class that an OpenCOM developer can use to implement
 * their components and prevent any code bloat. However, it is not a requirement of an OpenCOM
 * component to extend this interface. Remember the only specification of an OpenCOM Component
 * is that it implements IUnknown. Therefore, the developer can produce this anyway they see fit.
 *
 * @author  Paul Grace
 * @version 1.2.3
 * @see OpenCOM.IOpenCOM
 * @see OpenCOM.IMetaInterface
 * @see OpenCOM.IMetaInterception
 * @see OpenCOM.IMetaArchitecture
 */
public abstract class AbstractOpenCOMComponent implements IUnknown, IMetaInterface {

    /**
     * The component's meta interface implementation object.
     */
    private final transient MetaInterface metaIntf;

    /**
     * Binding to the OpenCom runtime.
     */
    protected final transient OCMSingleReceptacle<IOpenCOM> mSRIOpenCOM;

    /**
     * Creates a new instance of OpenCOMComponent.
     * @param mpIOCM The kernel reference.
     */
    public AbstractOpenCOMComponent(final IUnknown mpIOCM) {

        mSRIOpenCOM = new OCMSingleReceptacle();
        mSRIOpenCOM.connectToRecp(mpIOCM, "IOpenCOM", 0);
        metaIntf = new MetaInterface((IOpenCOM) mSRIOpenCOM.getInterface(), this);
    }

    /**
     * Obtain a reference to the interface of the type passed as parameter.
     * @param interfaceName a string representing the Java interface type, equivalent to the IID type in COM.
     * @return an Object representing a reference to the component hosting the interface requested.
     **/
    @Override
    public final Object queryInterface(final String interfaceName) {

        final List<String> query = new ArrayList();
        metaIntf.readInterfaceNames(this.getClass(), query);
        for (String iName : query) {
            iName = iName.substring(iName.lastIndexOf(".") + 1, iName.length());
            if (iName.equalsIgnoreCase(interfaceName)) {
                return this;
            }
        }
        return null;
    }

    // IMetaInterface Interface
    /**
    * Returns a Vector of meta-information. Each elements of the Vector is a String describing
    * that interface's type.
    * @param ppIntf a Vector to be filled with interface meta-information.
    * @return an Integer describing the number of interfaces on the component.
    **/
    @Override
    public final int enumIntfs(final List<Class> ppIntf) {
        return metaIntf.enumIntfs(this, ppIntf);
    }

    /**
    * Returns a Vector of meta-information. Each elements of the Vector is an object of
 type OCMRecpMetaInfo, which describes the attributes of indiviudal receptacles
 including: type (single or multiple) & interface type.
    * @param ppRecpMetaInfo a Vector to be filled with receptacle meta-information.
    * @return an Integer describing the number of receptacles on the component.
    **/
    @Override
    public final int enumRecps(final List<OCMRecpMetaInfo> ppRecpMetaInfo) {
        return metaIntf.enumRecps((IUnknown) this, ppRecpMetaInfo);
    }

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
    @Override
    public final boolean setAttributeValue(final String iid, final String kind,
            final String name, final String type, final Object value) {
         return metaIntf.setAttributeValue(iid, kind, name, type, value);
    }

    /**
     * Meta-data can be retrieved from each interface/receptacle of a component. This method
     * retrieves the value of a name attribute on a receptacle or interface.
     * @param iid the type of the interface or receptacle.
     * @param kind a string saying whether to attach to an interface or a receptacle.
     * @param name A String describing the attribute name.
     * @return A TypedAttribute object containing the value and type of the meta-data attribute.
     **/
    @Override
    public final TypedAttribute getAttributeValue(final String iid, final String kind, final String name) {
         return metaIntf.getAttributeValue(iid, kind, name);
    }

    /**
     * This method retrieves all the meta-data stored on the interface or receptacle.
     * @param iid the type of the interface or receptacle.
     * @param kind a string saying whether to attach to an interface or a receptacle.
     * @return A hashtable containing all of the attribute-value pairs for the receptacle or interface.
     **/
    @Override
    public final Map getAllValues(final String kind, final String iid) {
         return metaIntf.getAllValues(kind, iid);
    }
}
