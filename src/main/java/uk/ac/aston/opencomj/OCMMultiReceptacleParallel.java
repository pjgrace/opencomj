/*
 * OCM_MultiReceptacle.java
 *
 * Created on 23 July 2004, 13:52
 */

package uk.ac.lancs.opencomj;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OpenCOM defines a Multi Receptacle to contain multiple
 * pointers to interface implementations of the same type. Hence, it can
 * be used to invoke selected connected and/or parallel connections.
 * @param <InterfaceType> The receptacle interface type.
 * @author  Paul Grace
 * @version 1.2.3 (Untested)
 *
 */
public class OCMMultiReceptacleParallel<InterfaceType> implements IReceptacle {

    /**
     * The debug proxy class that is used to create a proxy for the
     * instance.
     */
    final class DebugProxy implements java.lang.reflect.InvocationHandler {

        /**
         * The proxy instance.
         */
        private final transient Object obj;

        /**
         * Creates a proxy from the original object instance.
         * @param nObj The original object
         * @return The proxy
         */
        public Object newInstance(final Object nObj) {
            return java.lang.reflect.Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                obj.getClass().getInterfaces(),
                new DebugProxy(obj));
        }

        /**
         * Create the proxy object.
         * @param oObj The object to create a proxy of.
         */
        private DebugProxy(final Object oObj) {
            this.obj = oObj;
        }

        /**
         * The invoke operation overridden by reflection when invoking the
         * proxy object.
         * @param proxy The proxy object.
         * @param meth The method to call.
         * @param args The method arguments.
         * @return The result of the operation.
         * @throws Throwable General exception thrown.
         */
        @Override
        public Object invoke(final Object proxy, final Method meth, final Object[] args) throws Throwable {
            return meth.invoke(obj, args);
        }
    }

    /**
     * The interface type of the receptacle.
     */
    private transient InterfaceType mpIntf;

    /**
     * List of interface pointers this receptacle is connected to.
     */
    private final transient List<Object> interfaceList;

    /** List of connIDS for each connection of this receptacle. */
    private final transient List<Long> connIDS;

    /** List of components that this receptacle is connected to. */
    private final transient List<IUnknown> components;

    /**
     * Current number of connections to the receptacle.
     */
    private transient int noConnections;

    /**
     * List of meta data attached to receptacle.
     */
    private final transient Map<String, TypedAttribute> metaData;

    /**
     * Constructor creates a new instance of OCM_MultiReceptacle object. Usually called
     * from within OpenCOM component constructors.
     * @param interfaceType The type of interface to initialse this receptacle to
     */
    public OCMMultiReceptacleParallel(final Class<InterfaceType> interfaceType) {
        interfaceList = new ArrayList();
        connIDS = new ArrayList();
        components = new ArrayList();
        noConnections = 0;

        Object object = null;
        ClassLoader cl2 = null;
        metaData = new HashMap();
        try {
            object = interfaceType.newInstance();
            cl2 = interfaceType.getClassLoader();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mpIntf = (InterfaceType) Proxy.newProxyInstance(cl2,
                new Class[] {interfaceType}, new DebugProxy(object));
    }

    //! Implementation of IReceptacle interface
    ////////////////////////////////////////////////////////////////////////////////
    //! This method stores the reference to the component hosting the interface
    //!
    @Override
    public final boolean connectToRecp(final IUnknown pIUnkSink, final String riid, final long provConnID) {
        // Get the reference to the component hosting the interface
        try {
            mpIntf = (InterfaceType) pIUnkSink.queryInterface(riid);
        } catch (ClassCastException e) {
            return false;
        }

        // Add the component, reference and id to the receptacles object stores
        components.add(pIUnkSink);
        interfaceList.add(mpIntf);
        connIDS.add(provConnID);

        noConnections++;
        return true;
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
            }
            if (noConnections == 0) {
                mpIntf = null;
                return true;
            }
            return true;
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
