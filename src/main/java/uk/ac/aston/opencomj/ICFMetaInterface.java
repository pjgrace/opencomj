/*
 * ICFMetaInterface.java
 *
 * Created on 11 August 2004, 14:58
 */
package uk.ac.aston.opencomj;

import java.util.List;

/**
 * The ICFMetaInterface interface is implemented by all composite components. It supports operations to inspect
 * and make changes to a composite component framework's internal meta-representation of component
 * architecture. Hence, it supports the architecture meta-model for invidual component frameworks.
 *
 * @author Paul Grace
 * @version 1.2.3
 */

public interface ICFMetaInterface {

    //! Operations for Inspection

    /**
     * Returns a list of all of the
     * the components that reside locally within this framework.
     * @return The component list.
     */
    List<IUnknown> getInternalComponents();

    /**
     * This method produces a list of components that are connected to a
     * particular component within the framework.
     * @param comp Instance of the component we wish to find what is connected to it.
     * @param ppConnections Vector to be filled with the list of components that are connected to this component.
     * @return An integer describing the number of components connected to this component.
     */
    int getBoundComponents(IUnknown comp, List<ConnectedComponent> ppConnections);

    /**
     * This method returns all of the internal connections between components that are wholly within
     * the framework.
     * @param pConnIDS A vector to be filled with long values describing the unique id of each connection.
     * @return An integer describing the number of connections within the framework.
     */
    int getInternalBindings(List<Long> pConnIDS);

    /**
     * This method fills the vector passed as a parameter with the list of interfaces
     * exposed by this framework. Its behaviour is similar to that provided by
     * enumIntfs of OpenCOM.
     * @param ppIntfs A vector to be filled with the list of interfaces.
     * @return The number of interfaces exposed by this framework.
     */
    int getExposedInterfaces(List<String> ppIntfs);

    /**
     * This method fills the vector passed as a parameter with the list of receptacles
     * exposed by this framework. Its behaviour is similar to that provided by
     * enumRecps of OpenCOM.
     * @param ppComps A vector to be filled with the list of receptacles.
     * @return The number of interfaces exposed by this framework.
     */
    int getExposedReceptacles(List<ExposedReceptacle> ppComps);

    //! Operations for reconfiguration

    /**
     * This method binds together two components only if they both reside in the framework.
     * @param pIUnkSource The source component with the receptacle.
     * @param pIUnkSink The sink component with the interface.
     * @param interfaceType The interface type to make the connection on.
     * @return A long describing the unique ID of this new connection. -1 indicates failure to connect.
     * @see OpenCOM.IUnknown
     */
    long localBind(IUnknown pIUnkSource, IUnknown pIUnkSink, String interfaceType);

    /**
     * This method disconnects two components only if they both reside in the framework
     * and are connected.
     * @param connID The unique ID of the connection to break.
     * @return A boolean indicating if the disconnection was made.
     */
    boolean breakLocalBind(long connID);

    /**
     * This method creates the component within the framework. The component is created, stored
     * in the runtime, and inserted into this framework's meta-data.
     * @param className The type of the component to create.
     * @param componentName The unique name of the component to create.
     * @return A reference to the newly created component instance.
     * @throws InvalidComponentTypeException If the component is not a valid component.
     * @see OpenCOM.IUnknown
     */
    IUnknown createComponent(String className, String componentName) throws InvalidComponentTypeException;

    /**
     * This method inserts a previously instantiated component from the runtime, into
     * the framework instance.
     * @param pCompReference The reference of the component instance.
     * @return A boolean indicating if the insert occurred or not.
     * @see OpenCOM.IUnknown
     */
    boolean insertComponent(IUnknown pCompReference);

     /**
     * This method deletes the component from the framework. The component is disconnected,
     * deleted from the runtime, and this framework's meta-data is updated.
     * @param pIUnknown The component instance to delete
     * @return A boolean indicating if the component was deleted or not.
     * @see OpenCOM.IUnknown
     */
    boolean deleteComponent(IUnknown pIUnknown);

    /**
     * This method takes the interface from one of the framework's internal components
     * and then makes it one of its own functional interfaces.
     * @param rintf The interface type that will be exposed.
     * @param pComp The internal component hosting the the interface.
     * @return A boolean describing if the interface was exposed.
     * @see OpenCOM.IUnknown
     */
    boolean exposeInterface(String rintf, IUnknown pComp);

    /**
     * This method removes the exposed interface from the outer component framework.
     * @param rintf The interface type that will be removed.
     * @param pComp The internal component hosting the the interface.
     * @return A boolean describing if the interface has been removed.
     */
    boolean unexposeInterface(String rintf,  IUnknown pComp);

    /**
     * This method removes all exposed interfaces.
     * @return A boolean describing if all the interfaces have been removed.
     */
    boolean unexposeAllInterfaces();

    /**
     * This method takes the receptacle from one of the framework's internal components
     * and then makes it one of its own receptacles.
     * @param rintf The interface type that will be exposed.
     * @param pComp The internal component hosting the the interface.
     * @param recpType The type of the receptacle.
     * @return A boolean describing if the receptacle was exposed.
     * @see OpenCOM.IUnknown
     */
    boolean exposeReceptacle(String rintf,  IUnknown pComp, String recpType);

    /**
     * This method removes the exposed receptacle from the outer component framework.
     * @param rintf The interface type that will be removed.
     * @param pComp The internal component hosting the the receptacle.
     * @return A boolean describing if the receptacle has been removed.
     */
    boolean unexposeReceptacle(String rintf,  IUnknown pComp);

    /**
     * This method removes all exposed receptacles.
     * @return A boolean describing if all the receptacle have been removed.
     */
    boolean unexposeAllReceptacles();

    /**
     * All reconfigurations must be performed as part of a transaction. Therefore,
     * the reconfigure agent must first call this method before subsequent write
     * operations.
     * @return A boolean describing if the transaction can continue.
     */
    boolean initArchTransaction();

    /**
     * This method must be called by the reconfiguration agent at the end of the reconfiguration
     * transaction. It forces a check on the new configuration, which is committed or not based upon the result.
     * @return The boolean describes of the new configuration was committed. A false means that the
     * last good configuration was rolled back to.
     */
    boolean commitArchTransaction();

    /**
     * Rolls the configuration back to its previous state - ideally should not be called
     * directly; maybe if faults are being detected is a supposedly valid architecture
     * you may wish to try returning to a stable version.
     * @return A boolean describing if the roll back was a success.
     */
    boolean rollbackArchTransaction();

    /**
    * Each component framework implements a lock to prevent reconfiguration
    * during functional operation. This method attempts to get read or write
    * access to the lock based upon the input. The locking mechanism is readers,
    * writers with priority for readers.
    * @param accessType An integer describing access type: 0 for read, 1 for write.
    * @return A boolean describing if the lock has processed this request or not.
    **/
    boolean accessCFgraphLock(int accessType);

    /**
    * Releases the lock, previously acquired.
    * @param accessType An integer describing access type: 0 for read, 1 for write.
    * @return A boolean describing if the lock has processed this request or not.
    **/
    boolean releaseCFgraphLock(int accessType);

    /**
    * Update the CF's locks readers count. Do not use - only used by runtime.
    * @param value increment amount.
    * @return An integer describing the new reader count.
    **/
    int updateReadersCount(int value);
}
