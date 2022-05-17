/*
 * OCM_MultiReceptacle.java
 *
 * Created on 23 July 2004, 13:52
 */

package uk.ac.aston.opencomj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OpenCOM defines a Multi Receptacle to contain multiple
 * pointers to interface implementations of the same type. Hence, it can
 * be used to invoke selected connected and/or parallel connections.
 * @param <InterfaceType> The type of the receptacle.
 * @author  Paul Grace
 * @version 1.2.3 (Untested)
 *
 */
public class OCMMultiReceptacleContext<InterfaceType> implements IReceptacle {

    /**
     * List of interface pointers this receptacle is connected to.
     */
    private final transient List<Object> interfaceList;

    /**
     * List of connIDS for each connection of this receptacle.
     */
    private final transient List<Long> connIDS;

    /**
     * List of components that this receptacle is connected to.
     */
    private final transient List<IUnknown> components;

    /**
     * Interface type (IID in traditional OpenCOM) of this receptacle.
     */
    private final transient String iidType;

    /**
     * Number of connection using this receptacle.
     */
    private transient int noConnections;

    /**
     * The meta data attached to the receptacle.
     */
    private final transient Map<String, TypedAttribute> metaData;         // Meta-data stored on this receptacle

    /**
     * Constructor creates a new instance of OCM_MultiReceptacle object. Usually called
     * from within OpenCOM component constructors.
     * @param interfaceType The type of interface to initialse this receptacle to
     */
    public OCMMultiReceptacleContext(final String interfaceType) {
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
     * This method is unique to multiple receptacles. It finds the index number
     * of a particular connection based upon context information i.e. meta-data
     * in the form of a name value pair. For example... I want the connection
     * with name=foo you would pass name and foo as parameters.
     *
     * @param name The name of the attribute.
     * @param value The required value of the meta-data pair.
     * @return An integer describing the index number in the interface list of the
     * context based connection.
     */
    public final int getInterfaceContextIndex(final String name, final Object value) {
        // Traverse the list of connections
        for (int i = 0; i < noConnections; i++) {
            // Get the IMetaInterface from the component at the other end of connection
            final IMetaInterface pGetAtts = (IMetaInterface) components.get(i);

            // Read the meta-value from the Interface
            final TypedAttribute attrVal =  (TypedAttribute) pGetAtts.getAttributeValue(iidType, "Interface", name);
            // If this matches the given value - then this is the connection index to return
            if (attrVal.getValue().equals(value)) {
                return i;
            }
        }
        return -1;  // The context connection isn't here
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
