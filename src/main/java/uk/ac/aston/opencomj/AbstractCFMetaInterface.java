/*
 * OpenCOMJ is a flexible component model for reconfigurable reflection developed at Lancaster University.
 * Copyright (C) 2015 Paul Grace
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
import java.util.StringTokenizer;


/**
 * Description of an abstract class defining operations for creating a component framework.
 * An OpenCOM component extends this behaviour to become a component framework.
 * That is if you QI a component and it support ICFMetaInterface then you know it is
 * a component framework rather than a primitive OpenCOM component.
 * @see ICFMetaInterface
 * @see ILifeCycle
 * @author  Paul Grace
 * @version 1.2.3
 **/

public abstract class AbstractCFMetaInterface implements ICFMetaInterface, ILifeCycle, IUnknown, IMetaInterface {

    /**
     * Fixed reference to the OpenCOM runtime API.
     * @see OpenCOM.IOpenCOM
     */
    private final transient MetaInterface meta;

    /**
     * connection to the opencom runtime kernel.
     */
    private final transient OCMSingleReceptacle<IOpenCOM> srIOpenCOM;

    /**
     * Receptacle to plug-in the validation checks for this component framework.
     */
    private final transient OCMSingleReceptacle<IAccept> srIAccept;

    /**
     * Receptacle to connect framework to the meta interface functionality in
     * the kernel.
     */
    private final transient OCMSingleReceptacle<IMetaInterface> srIMetaInterface;

    /**
     * Receptacle to connect framework to the meta interception functionality in
     * the kernel.
     */
    private final transient OCMSingleReceptacle<IMetaInterception> srIMetaIntercept;

    /**
     *  Receptacle to connect framework to the meta architecture functionality in
     *  the kernel.
     */
    private final transient OCMSingleReceptacle<IMetaArchitecture> srIMetaArchitect;

    /**
     * meta-Information store - The list of components in the framework.
     */
    private final transient List<IUnknown>  ppComps;

    /**
     * meta-Information store - Backup of the last good configuration.
     * The list of components in that configuration. Utilised during
     * rollback.
     */
    private final transient List<IUnknown>  ppCompsback;

    /**
     * meta-Information store - The list of exposed interfaces in the framework.
     */
    private final transient List<ExposedInterface> intseq;

    /**
     * meta-Information store - Backup of the last good configuration.
     * The list of exposed interfaces in that configuration. Utilised during
     * rollback.
     */
    private final transient List<ExposedInterface> intseqback;

    /**
     * meta-Information store - The list of exposed receptacles in the framework.
     */
    private final transient List<ExposedReceptacle> recpseq;

    /**
     * meta-Information store - Backup of the last good configuration.
     * The list of exposed receptacles in that configuration. Utilised during
     * rollback.
     */
    private final transient List<ExposedReceptacle> recpseqback;

    /**
     * meta-Information store - Backup of the last good configuration.
     * The list of connections in that configuration. Utilised during
     * rollback.
     */
    private final transient List<OCMConnInfo> pConnInfoBack;

    /**
     * The write access lock to the component framework.
     */
    private final transient Semaphore cFlock;

    /**
     * The semaphore for updating the reader count.
     */
    private final transient Semaphore readersMutex;

    /**
     * Integer describing the current number of operations executing within the framework.
     */
    private transient int readersCount;


    /**
     * The local object of the framework responsible for implementing the
     * operations of the interface meta-model.
     */
    private final transient CFInterceptors interceptors;


    /**
     * Constructor. Creates a new instance of CFMetaInterface
     * @param pRuntime The opencom runtime reference.
     */
    public AbstractCFMetaInterface(final IUnknown pRuntime) {

        srIOpenCOM = new OCMSingleReceptacle();
        srIOpenCOM.connectToRecp(pRuntime, "IOpenCOM", 0);
        meta = new MetaInterface((IOpenCOM) srIOpenCOM.getInterface(), this);
        srIAccept = new OCMSingleReceptacle();

        // Three receptacle connections to the 3 OpenCOM meta-models
        srIMetaInterface = new OCMSingleReceptacle();
        srIMetaIntercept = new OCMSingleReceptacle();
        srIMetaArchitect = new OCMSingleReceptacle();

        srIOpenCOM.getInterface().connect(this, pRuntime, "IMetaInterception");
        srIOpenCOM.getInterface().connect(this, pRuntime, "IMetaArchitecture");

        cFlock = new Semaphore(1);
        readersMutex = new Semaphore(1);
        interceptors = new CFInterceptors(this);
        readersCount = 0;

        ppComps = new ArrayList();
        ppCompsback = new ArrayList();
        intseq = new ArrayList();
        intseqback = new ArrayList();
        recpseq = new ArrayList();
        recpseqback = new ArrayList();
        pConnInfoBack = new ArrayList();
    }

    //! Interface IUnknown.

    /**
    * This method is an extension of queryInterface to allow exposed interfaces to be found
    * and used. Basically the outer component framework's IUnknown QI method will call this.
    * method if it is yet to find the required interface
    * @param intfName A string describing the interface we are looking for
    * @param cfReference A reference to the outer component framework instance
    * @return A Reference of the component hosting the required interface
    */
    public final Object queryInterface(final String intfName, final Object cfReference) {
        final Class cls = cfReference.getClass();
        final Class[] theInterfaces = cls.getInterfaces();
        for (Class theInterface : theInterfaces) {
            String interfaceName = theInterface.toString();
            interfaceName = interfaceName.substring(interfaceName.lastIndexOf('.') + 1, interfaceName.length());
            if (interfaceName.equalsIgnoreCase(intfName)) {
                return cfReference;
            }
        }

	// queryInterface() of the matching exposed interface
	for (int index = 0; index < intseq.size(); index++) {
            if (((ExposedInterface) intseq.get(index)).getIntfType().equalsIgnoreCase(intfName)) {
                final Object proxy = ((ExposedInterface) intseq.get(index)).getComponentID();
                final Delegator del = (Delegator) srIMetaIntercept.getInterface().getDelegator((IUnknown) proxy, intfName);
                if (del == null) {
                    return proxy;
                }
                return del.getHigherObject();
            }
	}
	return null;
    }

    //! Interface ILifeCycle
    @Override
    public boolean shutdown() {
        cFlock.release();
        readersMutex.release();
        return true;
    }

    @Override
    public boolean startup(final Object pIOCM) {
        return true;
    }

    //! Interface ICFMetaInterface

    /**
     * This method creates the component within the framework. The component is created, stored
     * in the runtime, and inserted into this framework's meta-data.
     * @param componentType The type of the component to create.
     * @param componentName The unique name of the component to create.
     * @return A reference to the newly created component instance.
     * @throws uk.ac.lancs.opencomj.InvalidComponentTypeException
     * @see OpenCOM.IUnknown
     */
    @Override
    public final IUnknown createComponent(final String componentType, final String componentName) throws InvalidComponentTypeException {

        //OpenCOM create component call
	final IUnknown pIUnknown = (IUnknown) srIOpenCOM.getInterface().createInstance(componentType, componentName);
	if (pIUnknown == null) {
            // Check if its already in this CF - if it is return the IUnknown
            // If not then we must report an error
            // Check both components are in local graph
            String name;
            for (IUnknown ppComp : ppComps) {
                name = srIOpenCOM.getInterface().getComponentName((IUnknown) ppComp);
                if (name.equalsIgnoreCase(componentName)) {
                    return (IUnknown) ppComp;
                }
            }
            return null;
	}

	// Add component to CF list
	ppComps.add(pIUnknown);

	return pIUnknown;
    }

   /**
     * This method inserts a previously instantiated component from the runtime, into
     * the framework instance.
     * @param componentRef The reference of the component instance.
     * @return A boolean indicating if the insert occured or not.
     * @see OpenCOM.IUnknown
     */
    @Override
    public final boolean insertComponent(final IUnknown componentRef) {

        // Check it isn't already in the local graph
	for (int i = 0; i < ppComps.size(); i++) {
            if (componentRef.hashCode() == (ppComps.get(i).hashCode())) {
                return false;
            }
        }

	// Add component to CF list
	ppComps.add(componentRef);

	return true;
    }

    /**
     * This method deletes the component from the framework. The component is disconnected,
     * deleted from the runtime, and this framework's meta-data is updated.
     * @param pIUnknown The component instance to delete
     * @return A boolean indicating if the component was deleted or not.
     * @see OpenCOM.IUnknown
     */
    @Override
    public final boolean deleteComponent(final IUnknown pIUnknown) {

	// Delete component if its in the CF list
	for (int index = 0; index < ppComps.size(); index++) {
            if ((IUnknown) ppComps.get(index) == pIUnknown) {
                // delete from runtime - this will disconnect it first.
                if (srIOpenCOM.getInterface().deleteInstance((IUnknown) ppComps.get(index))) {
                    ppComps.remove(index);
                    return true;
                }
                return false;
            }
	}
        // Cannot delete it if its not in the list
	return false;
    }

    /**
     * This method binds together two components only if they both reside in the framework.
     * @param pIUnkSource The source component with the receptacle.
     * @param pIUnkSink The sink component with the interface.
     * @param interfaceType The interface type to make the connection on.
     * @return A long describing the unique ID of this new connection. -1 indicates failure to connect.
     * @see OpenCOM.IUnknown
     */
    @Override
    public final long localBind(final IUnknown pIUnkSource, final IUnknown pIUnkSink, final String interfaceType) {
        boolean source = false, sink = false;

	// Check both components are in local graph
	for (int index = 0; index < ppComps.size(); index++) {
            if (ppComps.get(index) == pIUnkSource) {
                source = true;
            }
            if (ppComps.get(index) == pIUnkSink) {
                sink = true;
            }
	}

        // If Source or Sink is outside then fail local bind
	if ((!source) || (!sink)) {
            return -1;
        }
        // Connect the two components through the runtime
	return srIOpenCOM.getInterface().connect(pIUnkSource, pIUnkSink, interfaceType);
    }

    /**
     * This method disconnects two components only if they both reside in the framework
     * and are connected.
     * @param connID The unique ID of the connection to break.
     * @return A boolean indicating if the disconnection was made.
     */
    @Override
    public final boolean breakLocalBind(final long connID) {
        final List<Long> connectionList = new ArrayList();
	boolean inList = false;

	// check the connection exists in the local structure
	final int connsCount = getInternalBindings(connectionList);

	// Check the ID is a connection in list
	for (int index = 0; index < connsCount; index++) {
            if (connectionList.get(index) == connID) {
                inList = true;
                break;
            }
	}

        // connection doesn't exist therefore, cannot break
	if (inList) {
            return srIOpenCOM.getInterface().disconnect(connID);
        }
        return false;
    }

    /**
     * This method takes the interface from one of the framework's internal components
     * and then makes it one of its own functional interfaces.
     * @param rintf The interface type that will be exposed.
     * @param pCompR The internal component hosting the the interface.
     * @return A boolean describing if the interface was exposed.
     * @see OpenCOM.IUnknown
     */
    @Override
    public final boolean exposeInterface(final String rintf, final IUnknown pCompR) {

        IUnknown pCompRef = pCompR;
        // Check Component holding the interface to expose is in the CF
	boolean inCFGraph = false;

	for (int index = 0; index < ppComps.size(); index++) {
            if ((IUnknown) ppComps.get(index) == pCompRef) {
                inCFGraph = true;
                break;
            }
	}

        // Cannot expose interface of component not in the framework
	if (!inCFGraph) {
            return false;
	}

	// Check the interface isn't already exposed
	boolean inCFInts = false;

	for (int index = 0; index < intseq.size(); index++) {
            if (((ExposedInterface) intseq.get(index)).getIntfType().equalsIgnoreCase(rintf)) {
                inCFInts = true;
                break;
            }
	}

        // If its already exposed - fail
	if (inCFInts) {
            return false;
	}

        // Add the readers/writers lock to the exposed interface
        final IDelegator pIDel = srIMetaIntercept.getInterface().getDelegator(pCompRef, rintf);

        if (pIDel == null) {
            // Potential nested Exposed Interface
            for (IUnknown ppComp : ppComps) {
                final IUnknown pCompNested = (IUnknown) ((IUnknown) ppComp).queryInterface(rintf);
                if (pCompNested != null) {
                    pCompRef = pCompNested;
                    break;
                }
            }
        } else {
            pIDel.addPreMethod(interceptors, "Pre0");
            pIDel.addPostMethod(interceptors, "Post0");
        }

        // Create the meta-data about the exposed interface and store meta data about the new exposed interface
	intseq.add(new ExposedInterface(pCompRef, rintf));
	return true;
    }

    /**
     * This method takes the receptacle from one of the framework's internal components
     * and then makes it one of its own receptacles.
     * @param rintf The interface type that will be exposed.
     * @param pComp The internal component hosting the the interface.
     * @param recpType The type of the receptacle.
     * @return A boolean describing if the receptacle was exposed.
     * @see OpenCOM.IUnknown
     */
    @Override
    public final boolean exposeReceptacle(final String rintf, final IUnknown pComp, final String recpType) {

        // Check Component is in the CF
	boolean inCFGraph = false;

	for (int index = 0; index < ppComps.size(); index++) {
            if (ppComps.get(index) == pComp) {
                inCFGraph = true;
                break;
            }
	}

        // Cannot expose receptacle of component not in the framework
	if (!inCFGraph) {
            return false;
	}

	// Check the receptacle isn't already exposed
	boolean inCFInts = false;

	for (int index = 0; index < recpseq.size(); index++) {
            if (((ExposedReceptacle) recpseq.get(index)).getInterfaceType().equalsIgnoreCase(rintf)) {
                inCFInts = true;
                break;
            }
	}

        // Fail if receptacle already exposed
	if (inCFInts) {
            return false;
	}

        // Create and store the meta data about the new exposed receptacle
	recpseq.add(new ExposedReceptacle(pComp, rintf, recpType));
	return true;
    }

    /**
     * This method removes all exposed interfaces.
     * @return A boolean describing if all the interfaces have been removed.
     */
    @Override
    public final boolean unexposeAllInterfaces() {

        final int iter = intseq.size();
        for (int index = 0; index < iter; index++) {
		// For each exposed interface remove its delegator
            final ExposedInterface expIntf = (ExposedInterface) intseq.get(0);
            if (!unexposeInterface(expIntf.getIntfType(), expIntf.getComponentID())) {
                return false;
            }
	}

	return true;

    }

    /**
     * This method removes all exposed receptacles.
     * @return A boolean describing if all the receptacle have been removed.
     */
    @Override
    public final boolean unexposeAllReceptacles() {
        final int iter = recpseq.size();
        for (int index = 0; index < iter; index++) {
            final ExposedReceptacle expRecp = (ExposedReceptacle) recpseq.get(0);
            if (!unexposeReceptacle(expRecp.getReceptacleType(), expRecp.getComponent())) {
                return false;
            }
	}
	return true;

    }

    /**
     * This method removes the exposed interface from the outer component framework.
     * @param rintf The interface type that will be removed.
     * @param pComp The internal component hosting the the interface.
     * @return A boolean describing if the interface has been removed.
     * @see OpenCOM.IUnknown
     */
    @Override
    public final boolean unexposeInterface(final String rintf, final IUnknown pComp) {
        boolean foundIntf = false;

	// Find Interface in the internal components
	for (int index = 0; index < intseq.size(); index++) {
            final ExposedInterface expIntf = (ExposedInterface) intseq.get(index);
            if (expIntf.getIntfType().equalsIgnoreCase(rintf)) {
		foundIntf = true;
                // Remove from the meta-data
                intseq.remove(expIntf);
                break;
            }

	}

	if (!foundIntf) {
            return false;
	}


	return true;
    }

    /**
     * This method removes the exposed receptacle from the outer component framework.
     * @param rintf The interface type that will be removed.
     * @param pComp The internal component hosting the the receptacle.
     * @return A boolean describing if the receptacle has been removed.
     * @see OpenCOM.IUnknown
     */
    @Override
    public final boolean unexposeReceptacle(final String rintf, final IUnknown pComp) {
        boolean foundIntf = false;

	// Find Interface
	for (int index = 0; index < recpseq.size(); index++) {
            final ExposedReceptacle expRecp = (ExposedReceptacle) recpseq.get(index);
            if (expRecp.getInterfaceType().equalsIgnoreCase(rintf)) {
		foundIntf = true;
		recpseq.remove(expRecp);
                break;
            }
	}

	if (!foundIntf) {
            return false;
        }

	return true;
    }


    /**
     * This method produces a list of components that are connected to a
     * particular component within the framework.
     * @param comp Instance of the component we wish to find what is connected to it.
     * @param ppConnections Vector to be filled with the list of components that are connected to this component.
     * @return An integer describing the number of components connected to this component.
     * @see OpenCOM.IUnknown
     */
    @Override
    public final int getBoundComponents(final IUnknown comp, final List<ConnectedComponent> ppConnections) {

        boolean foundComp = false;
	final List<OCMRecpMetaInfo> pRecps = new ArrayList();
	final List<Class> pIntfs = new ArrayList();

	int count;

	final List<Long> pConnsIDS =  new ArrayList();
	OCMConnInfo buffer;
	int recpsConnCount;

        // check component is in the CF graph
        for (IUnknown ppComp : ppComps) {
            if (comp == (IUnknown) ppComp) {
                foundComp = true;
                break;
            }
        }

	if (!foundComp) {
            return 0;	// The component to inspect is not in framework
        }
	final long connID = srIOpenCOM.getInterface().connect(this, comp, OpenComConstants.METAINTERFACE);

	// Enumerate Interfaces on component
	count = srIMetaInterface.getInterface().enumIntfs(pIntfs);

	for (int i = 0; i < count; i++) {
            // Get Components connected to each interface
            String riid = pIntfs.get(i).toString();
            final StringTokenizer sToken = new StringTokenizer(riid);
            final String fname = sToken.nextToken();
            if (fname.equalsIgnoreCase("Interface")) {
                riid = sToken.nextToken();
            }
            pConnsIDS.clear();
            int intfsConnCount = 0;
            if (!riid.equalsIgnoreCase(OpenComConstants.METAINTERFACE)) {
                intfsConnCount = srIMetaArchitect.getInterface().enumConnsToIntf(comp, riid , pConnsIDS);
            }
            if (intfsConnCount != 0) {
                for (int index = 0; index < intfsConnCount; index++) {
                    final long ident = pConnsIDS.get(index);
                    buffer = srIOpenCOM.getInterface().getConnectionInfo(ident);

                    // Allocate memory for output list of connections
                    ppConnections.add(new ConnectedComponent(buffer.getSource(), ident));
                }
            }
	}


	// Enumerate Receptacles on component
	count = srIMetaInterface.getInterface().enumRecps(pRecps);

	for (int i = 0; i < count; i++) {
            // Get Components connected to interfaces
            pConnsIDS.clear();
            recpsConnCount = srIMetaArchitect.getInterface().enumConnsFromRecp(comp, ((OCMRecpMetaInfo) pRecps.get(i)).getInterfaceType(), pConnsIDS);

            if (recpsConnCount != 0) {
                for (int index = 0; index < recpsConnCount; index++) {
                    final long ident = pConnsIDS.get(index);
                    buffer = srIOpenCOM.getInterface().getConnectionInfo(ident);

                    // Allocate memory for output list of connections
                    ppConnections.add(new ConnectedComponent(buffer.getSink(), ident));
                }
            }
	}
	srIOpenCOM.getInterface().disconnect(connID);
	return ppConnections.size();

    }

    /**
     * This method fills the vector passed as a parameter with the list of interfaces
     * exposed by this framework. Its behaviour is similar to that provided by
     * enumIntfs of OpenCOM.
     * @param ppIntfs A vector to be filled with the list of interfaces.
     * @return The number of interfaces exposed by this framework.
     */
    @Override
    public final int getExposedInterfaces(final List<String> ppIntfs) {
	for (int index = 0; index < intseq.size(); index++) {
            ppIntfs.add(((ExposedInterface) intseq.get(index)).getIntfType());
	}
	return intseq.size();

    }

    /**
     * This method fills the vector passed as a parameter with the list of receptacles
     * exposed by this framework. Its behaviour is similar to that provided by
     * enumRecps of OpenCOM.
     * @param ppRecps A vector to be filled with the list of receptacles.
     * @return The number of interfaces exposed by this framework.
     */
    @Override
    public final int getExposedReceptacles(final List<ExposedReceptacle> ppRecps) {

	for (int index = 0; index < recpseq.size(); index++) {
            ppRecps.add(new ExposedReceptacle(recpseq.get(index).getComponent(),
                    recpseq.get(index).getInterfaceType(),
                    recpseq.get(index).getReceptacleType()));
	}
	return recpseq.size();
    }

    /**
     * This method returns all of the internal connections between components that are wholly within
     * the framework.
     * @param pConnIDS A vector to be filled with long values describing the unique id of each connection.
     * @return An integer describing the number of connections within the framework.
     */
    @Override
    public final int getInternalBindings(final List<Long> pConnIDS) {

	int connsCount;
	boolean alreadyInList = false;

        final List<ConnectedComponent> components = new ArrayList();
	// for each component in the Component Framework list its connections
	for (int index = 0; index < ppComps.size(); index++) {
            connsCount = this.getBoundComponents((IUnknown) ppComps.get(index), components);
            // For each of returned connections extract the connection ID
            for (int index2 = 0; index2 < connsCount; index2++) {
                final ConnectedComponent value = (ConnectedComponent) components.get(index2);

                // check that Value isn't already in list
                for (Long pConnIDS1 : pConnIDS) {
                    if (value.getConnection() == pConnIDS1) {
                        alreadyInList = true;
                        break;
                    }
                }

                if (!alreadyInList) {
                    // Copy the ID into the list
                    pConnIDS.add(value.getConnection());
                }
                alreadyInList = false;
            }
	}
	return pConnIDS.size();

    }

    @Override
    public final List<IUnknown> getInternalComponents() {
	return ppComps;
    }

    /**
     * All reconfigurations must be performed as part of a transaction. Therefore,
     * the reconfigure agent must first call this method before subsequent write
     * operations.
     * @return A boolean describing if the transaction can continue.
     */
    @Override
    public final boolean initArchTransaction() {
        // First get the CF lock for write access
        try {
            cFlock.acquire();
        } catch (java.lang.InterruptedException excep) {
            // Interupted before lock received
            return false;
        }

	// Store the current settings

        //Empty the vectors
        ppCompsback.clear();
        intseqback.clear();
        recpseqback.clear();
        pConnInfoBack.clear();

        // Store the current data in the backup structures
	for (int index = 0; index < ppComps.size(); index++) {
            ppCompsback.add(ppComps.get(index));
        }
	for (int index = 0; index < intseq.size(); index++) {
            intseqback.add(intseq.get(index));
        }
	for (int index = 0; index < recpseq.size(); index++) {
            recpseqback.add(recpseq.get(index));
        }

	// Get the backup connection info
	final List<Long> connections = new ArrayList();
	final int count = getInternalBindings(connections);

	for (int i = 0; i < count; i++) {
            pConnInfoBack.add(srIOpenCOM.getInterface().getConnectionInfo(connections.get(i)));
	}

	return true;

    }

    /**
     * This method must be called by the reconfiguration agent at the end of the reconfiguration
     * transaction. It forces a check on the new configuration, which is committed or not based upon the result.
     * @return The boolean describes of the new configuration was committed. A false means that the
     * last good configuration was rolled back to.
     */
    @Override
    public final boolean commitArchTransaction() {
        // There is no validation plug-in. So we allow anything - change to false and rollback
        // if you want stronger architectures.
        if (srIAccept.getInterface() == null) {
            cFlock.release();
            return true;
        }

        // Call the operation to check the new configuration.
        if (((IAccept) srIAccept.getInterface()).isValid(ppComps, intseq)) {
            cFlock.release();
            return true;
        } else {
            // We have created an invalid configuration - force a rollback
            rollbackArchTransaction();
            cFlock.release();
            return false;
        }

    }

    /**
     * Rolls the configuration back to its previous state - ideally should not be called
     * directly; maybe if faults are being detected is a supposedly valid architecture
     * you may wish to try returning to a stable version.
     * @return A boolean describing if the roll back was a success.
     */
    @Override
    public final boolean rollbackArchTransaction() {
        // Check there is a backup configuration to roll back to

        final List<Long> connections = new ArrayList();
        final int count = getInternalBindings(connections);
        for (int i = 0; i < count; i++) {
            breakLocalBind(connections.get(i));
        }
        final int iter = ppComps.size();
        for (int i = 0; i < iter; i++) {
            deleteComponent((IUnknown) ppComps.get(0));
        }

        unexposeAllInterfaces();
        unexposeAllReceptacles();

        for (IUnknown ppCompsback1 : ppCompsback) {
            ppComps.add(ppCompsback1);
        }
        for (ExposedInterface intseqback1 : intseqback) {
            intseq.add(intseqback1);
        }
        for (ExposedReceptacle recpseqback1 : recpseqback) {
            recpseq.add(recpseqback1);
        }
        //reconnect components
        for (OCMConnInfo connInfo : pConnInfoBack) {
            localBind(connInfo.getSource(), connInfo.getSink(), connInfo.getInterfaceType());
        }
        return true;

    }

   /**
    * Each component framework implemnts a lock to prevent reconfiguration
    * during functional operation. This method attempts to get read or write
    * access to the lock based upon the input. The locking mechanism is readers,
    * writers with priority for readers.
    * @param index An integer describing acces type: 0 for read, 1 for write.
    * @return A boolean describing if the lock has processed this request or not.
    **/
    @Override
    public final boolean accessCFgraphLock(final int index) {
        // Write Access
        if (index == 0) {
            try {
               cFlock.acquire();
            } catch (java.lang.InterruptedException e) {
                return false;
            }
	} else if (index == 1) { // Read Access
            try {
               readersMutex.acquire();
            } catch (java.lang.InterruptedException excep) {
                return false;
            }
	} else {
            return false;
        }
	return true;
    }

    /**
    * Releases the lock, previously acquired.
    * @param index An integer describing acces type: 0 for read, 1 for write.
    * @return A boolean describing if the lock has processed this request or not.
    **/
    @Override
    public final boolean releaseCFgraphLock(final int index) {
        if (index == 0) {
           cFlock.release();
	} else if (index == 1) { // Read Access
           readersMutex.release();
	} else {
            return false;
        }
	return true;
    }

    /**
    * Update the CF's locks readers count. Do not use - only used by runtime.
    * @param value increment amount.
    * @return An integer describing the new reader count.
    **/
    @Override
    public final int updateReadersCount(final int value) {
        readersCount = readersCount + value;
	return readersCount;
    }

    // IMetaInterface Interface
    /**
    * Returns a List of meta-information. Each elements of the Vector is a String describing
    * that interface's type.
    * @param ppIntfs a List to be filled with interface meta-information.
    * @return an Integer describing the number of interfaces on the component.
    **/
    @Override
    public final int enumIntfs(final List<Class> ppIntfs) {

	// Get CFs custom receptacles
        final int length = meta.enumIntfs(this, ppIntfs);

	// Get exposed receptacles
        final List<String> ppExpIntfs = new ArrayList();
        final int length2 = this.getExposedInterfaces(ppExpIntfs);

        for (int i = 0; i < length2; i++) {
            try {
                ppIntfs.add(Class.forName(ppExpIntfs.get(i)));
            } catch (Exception e) {
            }
        }
        return length + length2;
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
        // Get CFs custom receptacles
        final int length = meta.enumRecps(this, ppRecpMetaInfo);

	// Get exposed receptacles
        final List<ExposedReceptacle>  pExpRecps = new ArrayList();
        final int length2 = this.getExposedReceptacles(pExpRecps);
        for (int i = 0; i < length2; i++) {
            final ExposedReceptacle pExpRec = pExpRecps.get(i);
            ppRecpMetaInfo.add(new OCMRecpMetaInfo(pExpRec.getInterfaceType(), pExpRec.getReceptacleType()));
        }
	return length + length2;
    }

    /**
     * meta-data can be attached to each interface/receptacle of a component. This method adds a name
     * value pair to a given interface or receptacle instance.
     * @param iid the type of the interface or receptacle.
     * @param kind a string saying whether to attach to an interface or a receptacle.
     * @param name A String describing the attribute name.
     * @param type A String describing the attribute type.
     * @param value An object representing the attribute value.
     * @return A boolean indicating the success of the operation.
     **/
    @Override
    public final boolean setAttributeValue(final String iid, final String kind, final String name, final String type, final Object value) {
         return meta.setAttributeValue(iid, kind, name, type, value);
    }

    /**
     * meta-data can be retrieved from each interface/receptacle of a component. This method
     * retrieves the value of a name attribute on a receptacle or interface.
     * @param iid the type of the interface or receptacle.
     * @param kind a string saying whether to attach to an interface or a receptacle.
     * @param name A String describing the attribute name.
     * @return A TypedAttribute object containing the value and type of the meta-data attribute.
     **/
    @Override
    public final TypedAttribute getAttributeValue(final String iid, final String kind, final String name) {
         return meta.getAttributeValue(iid, kind, name);
    }

    /**
     * This method retrieves all the meta-data stored on the interface or receptacle.
     * @param iid the type of the interface or receptacle.
     * @param kind a string saying whether to attach to an interface or a receptacle.
     * @return A hashtable containing all of the attribute-value pairs for the receptacle or interface.
     **/
    @Override
    public final Map getAllValues(final String kind, final String iid) {
         return meta.getAllValues(kind, iid);
    }

    // IConnections Interface
    /**
     * Connect a component to this framework instance.
     * @param pSinkIntf The component being connected.
     * @param riid The interface type.
     * @param provConnID The unique ID of the connection.
     * @return Indication if the connection was successful.
     */
    public final boolean connect(final IUnknown pSinkIntf, final String riid, final long provConnID) {
        if (riid.equalsIgnoreCase("IAccept")) {
            return srIAccept.connectToRecp(pSinkIntf, riid, provConnID);
	}
        if (riid.equalsIgnoreCase(OpenComConstants.METAINTERFACE)) {
            return srIMetaInterface.connectToRecp(pSinkIntf, riid, provConnID);
	}
        if (riid.equalsIgnoreCase("IMetaInterception")) {
            return srIMetaIntercept.connectToRecp(pSinkIntf, riid, provConnID);
	}
        if (riid.equalsIgnoreCase("IMetaArchitecture")) {
            return srIMetaArchitect.connectToRecp(pSinkIntf, riid, provConnID);
	}

	return false;
    }


    /**
     * Disconnect a component from this framework instance.
     * @param riid The interface type.
     * @param connID The unique ID of the connection.
     * @return Indication if the connection was successful.
     */
    public final boolean disconnect(final String riid, final long connID) {
	if (riid.equalsIgnoreCase("IAccept")) {
            return srIAccept.disconnectFromRecp(connID);
	}
        if (riid.equalsIgnoreCase(OpenComConstants.METAINTERFACE)) {
            return srIMetaInterface.disconnectFromRecp(connID);
	}
        if (riid.equalsIgnoreCase("IMetaInterception")) {
            return srIMetaIntercept.disconnectFromRecp(connID);
	}
        if (riid.equalsIgnoreCase("IMetaArchitecture")) {
            return srIMetaArchitect.disconnectFromRecp(connID);
	}
	return false;
    }

}
