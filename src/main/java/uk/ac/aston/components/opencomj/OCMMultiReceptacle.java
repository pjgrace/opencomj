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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OpenCOM defines a Multi Receptacle to contain multiple
 * pointers to interface implementations of the same type. Hence, it can
 * be used to invoke selected connected and/or parallel connections.
 * @param <InterfaceType> The receptacle type.
 * @author  Paul Grace
 * @version 1.2.3 (Untested)
 *
 */
public class OCMMultiReceptacle<InterfaceType> implements IReceptacle {

    /** List of interface pointers this receptacle is connected to. */
    private final transient List<Object> interfaceList;

    /** List of connIDS for each connection of this receptacle. */
    private final transient  List<Long> connIDS;

    /** List of components that this receptacle is connected to. */
    private final transient  List<IUnknown> components;

    /** Interface type (IID in traditional OpenCOM) of this receptacle. */
    private final transient String iidType;

    /**
     * The number of connection on the receptacle.
     */
    private transient int noConnections;

    /**
     * The set of meta data attached to the receptacle.
     */
    private final transient Map<String, TypedAttribute> metaData;         // Meta-data stored on this receptacle

    /**
     * Constructor creates a new instance of OCM_MultiReceptacle object. Usually called
     * from within OpenCOM component constructors.
     * @param interfaceType The type of interface to initialse this receptacle to
     */
    public OCMMultiReceptacle(final String interfaceType) {
        interfaceList = new ArrayList();
        connIDS = new ArrayList();
        components = new ArrayList();
        noConnections = 0;
        iidType = interfaceType;
        metaData = new HashMap();
    }

    //! Implementation of IReceptacle interface
    ////////////////////////////////////////////////////////////////////////////////
    //! This method stores the reference to the component hosting the interface
    //!
    @Override
    public final boolean connectToRecp(final IUnknown pIUnkSink, final String riid, final long provConnID) {
        // Get the reference to the component hosting the interface
        if (riid.equalsIgnoreCase(iidType)) {

            // Add the component, reference and id to the receptacles object stores
            components.add(pIUnkSink);
            interfaceList.add(pIUnkSink.queryInterface(riid));
            connIDS.add(provConnID);

            noConnections++;
            return true;
        } else {
            return false;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    //! This method destroys the existing connection data
    //!
    @Override
    public final boolean disconnectFromRecp(final long connID) {
        // Traverse the receptacle data looking for the required connection ID
        for (int i = 0; i < noConnections; i++) {
            if (connIDS.get(i) == connID) {
                // Found it - now remove all pieces of information about that connection
                noConnections--;
                interfaceList.remove(i);
                connIDS.remove(i);
                return true;
            }
	}

	return false;
    }

    /**
     * This method adds meta-data name-value pair attributes to the receptacle instance.
     * @param name The attribute name.
     * @param type The attribute name.
     * @param value An Object holding the attribute value.
     * @return A boolean describing if the pair was added or not.
     */
    @Override
    public final boolean putData(final String name, final String type, final Object value) {
        try {
            metaData.put(name, new TypedAttribute(type, value));
        } catch (NullPointerException n) {
            return false;
        }
        return true;
    }

    /**
     * This method gets the value of a named meta-data attribute.
     * @param name The attribute name.
     * @return The TypedAttribute object storing the value.
     */
    @Override
    public final TypedAttribute getValue(final String name) {
        return  (TypedAttribute) metaData.get(name);
    }

    /**
    * This method returns all name-value meta-data pairs on this receptacle instance.
    * @return A Hashtable storing the pairs.
    */
    @Override
    public final Map getValues() {
        return metaData;
    }

}
