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

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Each OpenCOM component contains this object to implement the interface meta-model.
 * Methods for introspecting interfaces, and receptacles are available. Furthermore,
 * meta-data can be attached and read as name-value pairs from both the interfaces
 * and the receptacles.
 *
 * @author  Paul Grace.
 * @version 1.2.3
 */
public class MetaInterface {

    /**
     * Internal reference to the OpenCom kernel.
     */
    private final transient IOpenCOM mpRTintf;

    /**
     * The component that this meta interface is all about.
     */
    private final transient IUnknown mComp;

    /**
     * Creates a new instance of MetaInterface.
     * @param pRTintf Reference to opencom kernel.
     * @param component Reference to component for meta data.
     */
    public MetaInterface(final IOpenCOM pRTintf, final IUnknown component) {
        mpRTintf = pRTintf;
        mComp = component;
    }

    /**
     * Get the interfaces (as Java classes) of a component and stores them in the given vector.
     * The operation is recursive to find inherited interfaces.
     * @param compClass The class of the component.
     * @param intfList The Vector to be filled with the components interfaces.
     */
    private void getInterfaces(final Class compClass, final List<Class> intfList) {
        final Class[] theInterfaces = compClass.getInterfaces();
        if (theInterfaces.length != 0) {
            for (Class theInterface : theInterfaces) {
                boolean found = false;
                for (Class intfList1 : intfList) {
                    if (intfList1 == theInterface) {
                        found = true;
                    }
                }
                if (!found) {
                    intfList.add(theInterface);
                }
                getInterfaces(theInterface, intfList);
            }
        }
    }

    /**
     * Read the interface names from a component class type into an interface
     * list.
     * @param cls The component type to read.
     * @param intfs The list of interface names to fill.
     */
    public final void readInterfaceNames(final Class cls, final List<String> intfs) {
        final Class[] theInterfaces = cls.getInterfaces();
        if (theInterfaces.length != 0) {
            for (Class theInterface : theInterfaces) {
                boolean found = false;
                for (String intf : intfs) {
                    if (intf.equalsIgnoreCase(theInterface.getName())) {
                        found = true;
                    }
                }
                if (!found) {
                    intfs.add(theInterface.getName());
                }
                readInterfaceNames(theInterface, intfs);
            }
        }
    }

    /**
     * Get the interfaces (as Java classes) of a component and stores them in the given vector.
     * @param compRef The reference to the instance of the component.
     * @param intfList The Vector to be filled with the components interfaces.
     * @return An integer describing the number of interfaces on this component.
     */
    public final int enumIntfs(final Object compRef, final List<Class> intfList) {
        final Class cls = compRef.getClass();
        getInterfaces(cls, intfList);
        return intfList.size();
    }


    /**
     * Get the receptacles of a component and stores them in the given vector.
     * @param compRef The reference to the instance of the component.
     * @param ppRecpMetaInfo The List to be filled with the component's receptacles.
     * @return An integer describing the number of receptacles on this component.
     * @see OpenCOM.OCM_RecpMetaInfo_t
     */
    public final int enumRecps(final IUnknown compRef, final List<OCMRecpMetaInfo> ppRecpMetaInfo) {

        int count = 0;
        // Receptacles are public fields in each component - so we first read the component's fields
        final Class compType = compRef.getClass();
        final Field[] publicFields = compType.getFields();

        String type = null;

        for (Field publicField : publicFields) {
            String fieldType = publicField.getType().getName();
            fieldType = fieldType.substring(fieldType.lastIndexOf('.') + 1, fieldType.length());

            if (fieldType.equalsIgnoreCase("OCMSingleReceptacle")) {
                // add to vector
                type = "OCM_SINGLE_RECEPTACLE";
            }
            if (fieldType.equalsIgnoreCase("OCMMultiReceptacle")) {
                // add to vector
                type = "OCM_MULTI_RECEPTACLE";
            }
            if (type != null) {
                try {
                    final String genericType = publicField.getGenericType().toString();
                    ppRecpMetaInfo.add(new OCMRecpMetaInfo(genericType.substring(genericType.lastIndexOf('.') + 1, genericType.lastIndexOf('>')), type));
                } catch (Exception e) {
                    return count;
                }
                count++;
            }
        }

        return count;
    }

    /**
     * Set the value of a name value pair on either an interface or receptacle.
     * @param iid The type of the interface or receptacle.
     * @param kind A string which is either "Interface" or "Receptacle".
     * @param name A string describing the attribute name.
     * @param type A string describing the attribute type.
     * @param value An object holding the attribute value.
     * @return A boolean indicating if the attribute value was added.
     */
    public final boolean setAttributeValue(final String iid, final String kind, final String name, final String type, final Object value) {
        if (kind.equalsIgnoreCase("Interface")) {
            // Get meta interception interface
            final IMetaInterception pMetaIc = (IMetaInterception) mpRTintf.queryInterface("IMetaInterception");
            final IDelegator pIDel =  pMetaIc.getDelegator(mComp, iid);
            return pIDel.setAttributeValue(name, type, value);
        } else if (kind.equalsIgnoreCase("Receptacle")) {
            final Class cls = mComp.getClass();
            final Field[] publicFields = cls.getFields();

            for (Field publicField : publicFields) {
                String itype = publicField.getType().getName();
                itype = itype.substring(itype.lastIndexOf('.') + 1, itype.length());
                if (itype.equalsIgnoreCase("OCMSingleReceptacle")) {
                    try {
                        final String genericType = publicField.getGenericType().toString();
                        if (genericType.substring(genericType.lastIndexOf('.') + 1, genericType.lastIndexOf('>')).equalsIgnoreCase(iid)) {
                            final IReceptacle recp = (IReceptacle) publicField.get(mComp);
                            recp.putData(name, type, value);
                            return true;
                        }
                    } catch (IllegalAccessException | IllegalArgumentException e) {
                        return false;
                    }
                }
            }
        }
        return false;

    }

    /**
     * Retrieve the value of a name value pair on either an interface or receptacle.
     * @param iid The type of the interface or receptacle.
     * @param kind A string which is either "Interface" or "Receptacle".
     * @param name A string describing the attribute name.
     * @return An object holding the value of the attribute.
     */
    public final TypedAttribute getAttributeValue(final String iid, final String kind, final String name) {
        if (kind.equalsIgnoreCase("Interface")) {
            final IMetaInterception pMetaIc = (IMetaInterception) mpRTintf.queryInterface("OpenCOM.IMetaInterception");
            final IDelegator pIDel =  pMetaIc.getDelegator(mComp, iid);
            return pIDel.getAttributeValue(name);
        } else if (kind.equalsIgnoreCase("Receptacle")) {
            final Class cls = mComp.getClass();
            final Field[] publicFields = cls.getFields();

            for (Field publicField : publicFields) {
                String itype = publicField.getType().getName();
                itype = itype.substring(itype.lastIndexOf('.') + 1, itype.length());
                if (itype.equalsIgnoreCase("OCMSingleReceptacle")) {
                    try {
                        final String genericType = publicField.getGenericType().toString();
                        if (genericType.substring(genericType.lastIndexOf('.') + 1, genericType.lastIndexOf('>')).equalsIgnoreCase(iid)) {
                            final IReceptacle recp = (IReceptacle) publicField.get(mComp);
                            return (TypedAttribute) recp.getValue(name);
                        }
                    } catch (IllegalAccessException | IllegalArgumentException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Retrieve all the  name value pairs on either an interface or receptacle.
     * @param iid The type of the interface or receptacle.
     * @param kind A string which is either "Interface" or "Receptacle".
     * @return An object holding the value of the attribute.
     */
    public final Map getAllValues(final String kind, final String iid) {
        if (kind.equalsIgnoreCase("Interface")) {
            return null;
        } else  if (kind.equalsIgnoreCase("Receptacle")) {
            final Class cls = mComp.getClass();
            final Field[] publicFields = cls.getFields();
            for (Field publicField : publicFields) {
                String type = publicField.getType().getName();
                type = type.substring(type.lastIndexOf('.') + 1, type.length());
                if (type.equalsIgnoreCase("OCMSingleReceptacle")) {
                    try {
                        String genericType = publicField.getGenericType().toString();
                        if (genericType.substring(genericType.lastIndexOf('.') + 1, genericType.lastIndexOf('>')).equalsIgnoreCase(iid)) {
                            return ((IReceptacle) publicField.get(mComp)).getValues();
                        }
                    } catch (IllegalAccessException | IllegalArgumentException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }
}
