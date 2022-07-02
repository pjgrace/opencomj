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

import uk.ac.aston.components.privacy.PrivacyDelegator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import uk.ac.aston.components.security.dataflow.DataProcess;
import uk.ac.aston.components.security.dataflow.MetaDataFlow;
import uk.ac.aston.components.security.dataflow.PrivacyFunction;
import uk.ac.aston.components.security.dataflow.Private;


/**
 * OpenCOM is the implementation of the component runtime kernel. It should be instantiated once only - n.b.
 * future versions may implement this class using the singleton pattern. OpenCOM provides a set of runtime
 * services that support the creation, deletetion and binding of software components. Notably, it maintains
 * information about the running component architectures in a system wide graph data structure. Reflective operations
 * can then be performed on this meta-representation i.e. inspection and dynamic reconfiguration. The aim is
 * to support the capabilities of the three meta-models proposed by the Open ORB philosopy i.e. interface,
 * architecture and interception meta-models. Not that this is a prototype version of OpenCOM version 1 as
 * described in Clarke,01. There is a C++ implementation offering identical operations.
 *
 * @author  Paul Grace
 * @version 1.2.3
 * @see OpenCOM.IOpenCOM
 * @see OpenCOM.IMetaInterface
 * @see OpenCOM.IMetaInterception
 * @see OpenCOM.IMetaArchitecture
 */

public class OpenCOM implements IOpenCOM, IMetaArchitecture, IUnknown, IMetaInterception {

    /**
     * The complete kernel system graph.
     */
    private final transient List<OCMGraphNode> mGraph;

    /**
     * The unique id generator for the system graph.
     */
    private transient long mcConnID;

    /**
     * The internal meta interface object for the kernel.
     */
    private final transient MetaInterface metaObject;
    
    /**
     * The DFD runtime model
     */
    private final transient MetaDataFlow metaDFD;

    /**
     * Constructor that creates a new instance of the OpenCOM runtime kernel.
     */
    public OpenCOM() {
        mGraph = new ArrayList();
        mcConnID = 1;
        metaObject = new MetaInterface((IOpenCOM) this, this);
        metaDFD = new MetaDataFlow();
    }

    // Implementation for the IMetaArchitecture interface of the OpenCOM runtime
    //////////////////////////////////////////////////////////////////////
    /**
    * This method introspects connections on existing components. It tells you how many components
    * are connected to this particular receptacle. For single receptacles this will be 0 or 1; however,
    * multiple receptacles can have 0 to N connections.
    * @param pIUnknown The component hosting the receptacle we wish to introspect.
    * @param riid The interface type of the receptacle.
    * @param ppConnsFromRecp A vector to be filled with unique connection ids of all the connections to this receptacle.
    * @return An integer describing the number of connections to this receptacle.
    */
    @Override
    public final int enumConnsFromRecp(final IUnknown pIUnknown, final String riid, final List<Long> ppConnsFromRecp) {
        // Traverse the system graph, we are looking for pIUnknown component first
        for (OCMGraphNode mGraph1 : mGraph) {
            if (mGraph1.getComponent().hashCode() == pIUnknown.hashCode()) {
                // Found the component, now we will traverse its list of connections on its riid receptacle
                final List<OCMGraphRecpInfo> recpIterator = mGraph1.getReceptacles();
                OCMGraphRecpInfo tempRecpInfo;
                for (OCMGraphRecpInfo recpIterator1 : recpIterator) {
                    tempRecpInfo = recpIterator1;
                    if (tempRecpInfo.getInterfaceType().equalsIgnoreCase(riid)) {
                        // For matching receptacle-interface type we add the connection ID to the output parameter
                        ppConnsFromRecp.add(tempRecpInfo.getConnectionID());
                    }
                }
            }
        }
        return ppConnsFromRecp.size();
    }

    /**
    * This method introspects connections on existing components. It tells you how many components
    * are connected to this particular INTERFACE.
    * @param pIUnknown The component hosting the interface we wish to introspect.
    * @param riid The interface type of the interface.
    * @param ppConnsToIntf A vector to be filled with unique connection ids of all the connections to this interface.
    * @return An integer describing the number of connections to this interface.
    */
    @Override
    public final int enumConnsToIntf(final IUnknown pIUnknown, final String riid, final List<Long> ppConnsToIntf) {
        // Traverse the system graph, we are looking for pIUnknown component first
        for (OCMGraphNode mGraph1 : mGraph) {
            if (mGraph1.getComponent().hashCode() == pIUnknown.hashCode()) {
                // Found the component, now we will traverse its list of interfaces
                final List<OCMGraphIntfInfo> interfaceIterator = mGraph1.getInterfaces();

                for (OCMGraphIntfInfo interfaceIterator1 : interfaceIterator) {
                    if (interfaceIterator1.getInterfaceType().equalsIgnoreCase(riid)) {
                        // Found the interface type, add the connection ID to the output parameter
                        ppConnsToIntf.add(interfaceIterator1.getConnID());
                    }
                }
            }
        }
        return ppConnsToIntf.size();
    }

    //! Implementation for the IUnknown interface
    //////////////////////////////////////////////////////////////////////

    /**
    * This method returns a reference point to the runtime, based upon the
    * requested interface type. If the interface isn't hosted then the
    * null indicates that those operations are not available on the runtime.
    * @param iName A string describing the interface requested.
    * @return A reference to the runtime if the interface is available, otherwise null is returned.
    */
    @Override
    public final Object queryInterface(final String iName) {
        final List<String> query = new ArrayList();
        metaObject.readInterfaceNames(this.getClass(), query);
        String interfaceName;
        for (String query1 : query) {
            interfaceName = query1;
            interfaceName = interfaceName.substring(interfaceName.lastIndexOf('.') + 1, interfaceName.length());
            if (interfaceName.equalsIgnoreCase(iName)) {
                return this;
            }
        }
        return null;
    }


    //! Implementation for the IOpenCOM interface
    //////////////////////////////////////////////////////////////////////

    /**
    * Connects the source component hosting the receptacle to the
    * sink component hosting the interface on the given interface type.
    * @param pIUnkSource The source component of the connection (hosts receptacle).
    * @param pIUnkSink The sink component of the connection (provides interface).
    * @param iid The interface type to make the connection on.
    * @return A long representing the unique ID of this connection.
    */
    @Override
    public final long connect(final IUnknown pIUnkSource, final IUnknown pIUnkSink, final String iid) {

        // Get pIConnections interface from the source component
        final IConnections pIConnections = (IConnections) pIUnkSource.queryInterface(OpenComConstants.CONNECTINTERFACE);

        // Register the information about the new connection to the system graph
        boolean success = registerConnection(pIUnkSource, pIUnkSink, iid, mcConnID);
        if (!success) {
            return -1;
        }
        // Make the connection between the two components
        success = pIConnections.connect(pIUnkSink, iid, mcConnID);

        // If the connection fails we must remove the meta-data
        if (!success) {
            deRegisterConnection(mcConnID);
            return -1;
        }

        final long pConnID = mcConnID;
        //Increment unique ConnID as connection succeeded
        mcConnID++;
        return pConnID;		// Return the ID of the created connection
    }

    /**
    * Private method of the runtime, which is used to add meta-data information abot connections
    * to the run-time graph. This is only ever invoked by the connect method of OpenCOM.
    * @param pIUnkSource Source component of the connection.
    * @param pIUnkSink Sink component of the connection.
    * @param riid The interface type.
    * @param connID The id of the connection.
    * @return Success indicator.
    */
    private boolean registerConnection(final IUnknown pIUnkSource, final IUnknown pIUnkSink, final String riid, final long connID) {

        // Add receptacle info to front of pGRecpInfo on source
        final OCMGraphRecpInfo pGRecpInfo = new OCMGraphRecpInfo(connID, pIUnkSink, riid);

        // Add interface info to front of pGIntfInfo on sink
        final OCMGraphIntfInfo pGIntfInfo = new OCMGraphIntfInfo(connID, pIUnkSource, riid);

        for (int i = 0; i < mGraph.size(); i++) {
            // Copy IntfInfo onto list
            if (mGraph.get(i).getComponent().hashCode() == pIUnkSink.hashCode()) {
                final OCMGraphNode tempP =  mGraph.get(i);
                tempP.getInterfaces().add(pGIntfInfo);
                mGraph.set(i, tempP);
                break;
            }
        }
        for (int i = 0; i < mGraph.size(); i++) {
            // Copy RecpInfo onto list
            if (mGraph.get(i).getComponent().hashCode() == pIUnkSource.hashCode()) {
                final OCMGraphNode tempP = mGraph.get(i);
                tempP.getReceptacles().add(pGRecpInfo);
                mGraph.set(i, tempP);
                break;
            }
        }

        return true;
    }

    
    
    @Override
    public final IUnknown createInstance(final String componentType, final String componentName)
            throws InvalidComponentTypeException{

        Object pIUnknown = null;                                        // Original version of component before delegators added
        Class componentClass = null;                                    // Java Class type corresponding to componentType string
        final List<String> interfaceList = new ArrayList();             // List of interface types provided by the new component
        Object delComponent = null;                                     // Version of component after delegators are added
        boolean validComponent = false;

        //Has an optional name been supplied ?
        if (componentName != null) {
            //Make sure name is unique
            for (OCMGraphNode mGraph1 : mGraph) {
                final OCMGraphNode listElement = (OCMGraphNode) mGraph1;
                if (listElement.getComponentName().equalsIgnoreCase(componentName)) {
                    throw new InvalidComponentTypeException(componentName + " is not unique");
                }
            }
        }
        //Now Create component
        try {
            componentClass = Class.forName(componentType);
        } catch (ClassNotFoundException ex) {
            throw new InvalidComponentTypeException("Unknown component type" + componentType + "; check class name");
        }

        final Class[] intArgsClass = new Class[] {IUnknown.class};
        final Object[] intArgs = new Object[] {(IUnknown) this};

        // Use java reflection the instantiate an instance of this component type
        try {
            Constructor intArgsConst;
            intArgsConst = componentClass.getConstructor(intArgsClass);
            pIUnknown = (IUnknown) intArgsConst.newInstance(intArgs);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException excep) {
           throw new InvalidComponentTypeException("Invalid opencom component constructor in "+ componentType + "; check implementation");
        }

        //pIUnknown = componentClass.newInstance();
        // Find the interfaces of the component type, we need to make sure this is a valid
        // OpenCOM component i.e. it implements the IUnknown interface
        addInterfaces(componentClass, interfaceList);
        String traverseString;
        for (String interfaceList1 : interfaceList) {
            traverseString = interfaceList1;
            traverseString = traverseString.substring(traverseString.lastIndexOf('.') + 1, traverseString.length());
            if (traverseString.equalsIgnoreCase("IUnknown")) {
                validComponent = true;
                break;
            }
        }

        // If it's a valid component then we can add delegators and place it in the graph
        if (validComponent) {
            //Record it at next free position on graph
            final OCMGraphNode newVectorElement = new OCMGraphNode(componentName, componentType, pIUnknown);
            mGraph.add(newVectorElement);
            final int index = mGraph.indexOf(newVectorElement);
            for (String interfaceList1 : interfaceList) {
                String intfName = interfaceList1;
                intfName = intfName.substring(intfName.lastIndexOf('.') + 1, intfName.length());
                if (!(intfName.equalsIgnoreCase(OpenComConstants.CONNECTINTERFACE))
                        || (intfName.equalsIgnoreCase("IMetaInterface"))
                        || (intfName.equalsIgnoreCase("ILifeCycle"))) {
                    // do not attach delegators to standard OpenCOM interfaces
                    
                    boolean isPrivate = MetaDataFlow.isPrivateMethod(interfaceList1);
                    IDelegator pDel = null;
                    if(isPrivate){
                        final PrivacyDelegator del = new PrivacyDelegator(pIUnknown, (IMetaInterception) this);
                        pDel = (IDelegator) del;
                        final Object tempDel = del.newInstance(pIUnknown);
                        del.setHigherObject(tempDel);
                        if (intfName.equalsIgnoreCase("IUnknown")) {
                            // This is the special case - we need to replace the Component reference
                            // in the graph with the delegated component reference
                            delComponent = tempDel;
                            mGraph.get(index).setComponent(delComponent);
                        }
                    }
                    else {
                        // We now need to attach delegators to each of the "non-component" interfaces
                        // Create a new delegator to be attached
                        final Delegator del = new Delegator(pIUnknown, (IMetaInterception) this);
                        pDel = (IDelegator) del;
                        final Object tempDel = del.newInstance(pIUnknown);
                        del.setHigherObject(tempDel);
                        if (intfName.equalsIgnoreCase("IUnknown")) {
                            // This is the special case - we need to replace the Component reference
                            // in the graph with the delegated component reference
                            delComponent = tempDel;
                            mGraph.get(index).setComponent(delComponent);
                        }
                    }
                    
                    //
                    boolean isPrivateInterface = MetaDataFlow.isPrivateInterface(interfaceList1);
                    if (isPrivateInterface) {
                        DataProcess dProcess = MetaDataFlow.getProcessFromInterface(interfaceList1);
                        metaDFD.addDataFunction(dProcess);
                    }
                    
                    //Create OCMDelegatorInfo structure for new list entry
                    //Add it to the list
                    mGraph.get(index).getDelegators().add(new OCMDelegatorInfo(pDel, intfName));
                }
            }
        } else {
            throw new InvalidComponentTypeException("Component " + componentType +" does not implement IUnknown interface");
        }
        return (IUnknown) delComponent;
    }

    /**
    * private method that is used only by the createInstance method of OpenCOM. It allows
    * the list of interfaces of a particular component to be stored in the list passed
    * as a parameter.
    * @param component The component type.
    * @param interfaceList The list of interfaces to be filled.
    */
    private void addInterfaces(final Class component, final List<String> interfaceList) {
        final Class[] theInterfaces = component.getInterfaces();
        if (theInterfaces.length != 0) {
            for (Class theInterface : theInterfaces) {
                interfaceList.add(theInterface.getName());
                addInterfaces(theInterface, interfaceList);
            }
        }
    }

    /**
    * This method deletes the information about a component from the system graph. It does not attempt
    * to delete the instance of the component itself. At present this is left to Garbage Collection. In
    * future versions, a reference counter a la COM may provide more control of memory management; at present
    * it is very easy to leave references to components in place after they are no longer needed.
    * @param pCompToDelete Reference to the component instance to be deleted.
    * @return A boolean describing whether the information about the component was removed from the graph or not.
    */
    @Override
    public final boolean deleteInstance(final IUnknown pCompToDelete) {
        for (int i = 0; i < mGraph.size(); i++) {
            // Find the component in the system graph
            if (((OCMGraphNode) mGraph.get(i)).getComponent().hashCode() == pCompToDelete.hashCode()) {
                // Once found, first Call its shutdown() method
                final ILifeCycle pILifeCycle = (ILifeCycle)   pCompToDelete.queryInterface("ILifeCycle");
                pILifeCycle.shutdown();
                // Delete all Connections made to its Interfaces
                final IMetaInterface pIMetaI = (IMetaInterface)   pCompToDelete.queryInterface(OpenComConstants.METAINTERFACE);
                // First enumerate the interfaces
                final List<Class> ppIntf = new ArrayList();
                final int length = pIMetaI.enumIntfs(ppIntf);
                // For each interface find if its connected
                Class interfaceClass;
                String interfaceName;
                for (int y = 0; y < length; y++) {
                    interfaceClass = (Class) ppIntf.get(y);
                    interfaceName = interfaceClass.getName();
                    final List<Long> list = new ArrayList();
                    final int connections = enumConnsToIntf(pCompToDelete, interfaceName, list);
                    // connections tells us how many connections to delete for this interface
                    for (int z = 0; z < connections; z++) {
                        disconnect(list.get(z));
                    }
                }
                //Disconnect all of connections on the receptacles of this component
                final IConnections pIConnections = (IConnections) pCompToDelete.queryInterface(OpenComConstants.CONNECTINTERFACE);
                if (pIConnections == null) {
                        // Can safely remove component node from graph
                    mGraph.remove(i);
                    return true;
                }
                // If we are here, the IConnections interface IS implemented by the component
                final List<OCMGraphRecpInfo> recpListVector = mGraph.get(i).getReceptacles();
                // Travese the list, disconnecting connections directly and removing the meta-data
                for (OCMGraphRecpInfo recpListVector1 : recpListVector) {
                    pIConnections.disconnect(recpListVector1.getInterfaceType(), recpListVector1.getConnectionID());
                    deRegisterConnection(recpListVector1.getConnectionID());
                }
                // Disconnect all receptacles bound to this components interfaces.
                final List<OCMGraphIntfInfo> intfListVector = mGraph.get(i).getInterfaces();
                IUnknown pParent;
                IConnections pIntfConnections;
                for (OCMGraphIntfInfo intfListVector1 : intfListVector) {
                    pParent = intfListVector1.getSource();
                    pIntfConnections = (IConnections) pParent.queryInterface("OpenCOM.IConnections");
                    if (pIntfConnections.disconnect(intfListVector1.getInterfaceType(), intfListVector1.getConnID())) {
                        deRegisterConnection(intfListVector1.getConnID());
                    }
                }
                // Remove component node from graph
                mGraph.remove(i);
                return true;
            }
        }
        return false; // Component not in the graph
    }

    /**
    * This operation disconnects two previously connected components based upon the unqiue ID of this connection.
    * @param connID A long representing the unqie identifier of the connection to destroy.
    * @return boolean A boolean describing whether the disconnect operation succeeded.
    */
    @Override
    public final boolean disconnect(final long connID) {

            // Obtain meta-information about the connection (source, sink, type) using the id
            final OCMConnInfo pConnInfo = getConnectionInfo(connID);

            // Get the connections interface of receptacle component so we can call its disconnect operation
            final IConnections pIConnections = (IConnections) pConnInfo.getSource().queryInterface(OpenComConstants.CONNECTINTERFACE);

            // Make the physical disconnection
            pIConnections.disconnect(pConnInfo.getInterfaceType(), connID);

            //If we were able to disconnect then the connection must exist, i.e. deregister cannot fail
            deRegisterConnection(connID); // Remove information from the graph about the connection

            return true;
    }

    /**
    * This is a private method of the OpenCOM runtime that removes information about connections from the graph.
    * It is only called by the OpenCOM disconnect operation.
    * @param connID The id of the connection to remove.
    * @return The connection id, -1 if failed.
    */
    private long deRegisterConnection(final long connID) {
        OCMConnInfo pConnInfo;

        pConnInfo = getConnectionInfo(connID);
        List<OCMGraphIntfInfo> kernelIntfs = new ArrayList();
        // Find the sink component (hosting the interface)
        // and remove its meta data for this connection ID
        for (OCMGraphNode mGraph1 : mGraph) {
            if (mGraph1.getComponent().hashCode() == pConnInfo.getSink().hashCode()) {
                // Extract the list of interface connection information stored for the sink component
                kernelIntfs = mGraph1.getInterfaces();
                break;
            }
        }
        // Find the Interface information element within the list and remove it
        for (int i = 0; i < kernelIntfs.size(); i++) {
            if (kernelIntfs.get(i).getConnID() == connID) {
                kernelIntfs.remove(i);
                break;
            }
        }

        // Find the source component (hosting the receptacle) of this connection ID
        // and remove its meta data for this connection ID
        List<OCMGraphRecpInfo> kernelRecps = new ArrayList();
        for (OCMGraphNode mGraph1 : mGraph) {
            if (mGraph1.getComponent().hashCode() == pConnInfo.getSource().hashCode()) {
                kernelRecps = mGraph1.getReceptacles();
                break;
            }
        }
        for (int i = 0; i < kernelRecps.size(); i++) {
            if (kernelRecps.get(i).getConnectionID() == connID) {
                kernelRecps.remove(i);
                break;
            }
        }
        return 0;
    }

    @Override
    public final List<IUnknown> enumComponents() {
        List<IUnknown> ppComps = new ArrayList();
        for (OCMGraphNode mGraph1 : mGraph) {
            ppComps.add((IUnknown) mGraph1.getComponent());
        }
        return ppComps;
    }

    /**
    * This method returns the unqiue component name  for a given instance of a component.
    * @param pIUnknown The reference of the component instance.
    * @return A string describing the component name.
    */
    @Override
    public final String getComponentName(final IUnknown pIUnknown) {
        if (pIUnknown == null) {
            return null;
        }
        for (OCMGraphNode mGraph1 : mGraph) {
            if (mGraph1.getComponent().hashCode() == pIUnknown.hashCode()) {
                return mGraph1.getComponentName();
            }
        }
        return null;
    }

    /**
    * This method returns the component reference for a given unique component name.
    * @param compName The name of the component instance.
    * @return A reference to the component instance.
    */
    @Override
    public final IUnknown getComponentPIUnknown(final String compName) {
        if (compName == null) {
            return null;
        }

        for (OCMGraphNode mGraph1 : mGraph) {
            if (mGraph1.getComponentName().equalsIgnoreCase(compName)) {
                return (IUnknown) mGraph1.getComponent();
            }
        }
        return null;
    }

    @Override
    public final String getComponentType(final IUnknown pIUnknown) {
        for (OCMGraphNode mGraph1 : mGraph) {
            if (mGraph1.getComponent().hashCode() == pIUnknown.hashCode()) {
                return mGraph1.getComponentType();
            }
        }
        return null;
    }

    /**
    * Returns meta-information about a connection (i.e. the source and sink components, and the
    * type of interface these components are connected by.
    * @param connID The unique connection identifier of the connection to inspect.
    * @return An object holding the meta data about the connection.
    * @see OCMConnInfo
    */
    @Override
    public final OCMConnInfo getConnectionInfo(final long connID) {

        List<OCMGraphRecpInfo> recpInfos;
        for (OCMGraphNode mGraph1 : mGraph) {
            recpInfos = mGraph1.getReceptacles();
            for (OCMGraphRecpInfo recpInfo : recpInfos) {
                if (recpInfo.getConnectionID() == connID) {
                    final IUnknown pSinkID = recpInfo.getSinkComponent();
                    return new OCMConnInfo((IUnknown) mGraph1.getComponent(), pSinkID, recpInfo.getInterfaceType());
                }
            }
        }

        return null;
    }


    //! Implementation of the IMetaInterception interface for OpenCOM runtime
    //////////////////////////////////////////////////////////////////////////

    /**
    * This method get the delegator for a particular component interface. This can then
    * be used to attache pre and post methods. Each component has a set of delegators - one for
    * each functional interface and one for the IUnknown interface. The developer uses this
    * method to pinpoint the correct one.
    * @param pIUnkParent The component reference we want to get a delegator from.
    * @param riid The interface type we want to get the delegator for.
    * @return The IDelegator interface of the selected delegator instance.
    * @see OpenCOM.IDelegator
    */
    @Override
    public final IDelegator getDelegator(final IUnknown pIUnkParent, final String riid) {
        //Search components
        for (OCMGraphNode mGraph1 : mGraph) {
            //Look for specified component
            if (mGraph1.getComponent().hashCode() == pIUnkParent.hashCode()) {
                //Component found!
                //Search the component's pGDelInfo list
                final List<OCMDelegatorInfo> pGDelInfo = mGraph1.getDelegators();
                for (OCMDelegatorInfo delinfo : pGDelInfo) {
                    if (delinfo.getInterfaceType().equalsIgnoreCase(riid)) {
                        //There is already a delegator associated to the
                        //specified interface of this component

                        //Return existing delegator component interface
                        return delinfo.getDelegator();
                    }
                }
            }
        }
        return null;
    }
    
    
    // IUpdateMetaDataFlow
    
}
