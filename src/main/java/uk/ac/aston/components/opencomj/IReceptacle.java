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

import java.util.Map;

/**
 * Interface implemented only by receptacles. Provides operations to manipulate the receptacles
 * themselves. That is, connect and disconnect them, add meta-data to them and so on...
 *
 * @author Paul Grace
 * @version 1.2.3
 */

public interface IReceptacle {
    /**
     * This method connects the receptacle to given component on the given interface type.
     * @param pIUnkSink Reference to the sink component who hosts the interface that the receptacle is to be connected to.
     * @param riid A string representing the interface type of the connection.
     * @param provConID The unique connection id.
     * @provConID A long representing the generated unique ID of this particular connection.
     * @return A boolean indicating the success of this operation
     **/
    boolean connectToRecp(IUnknown pIUnkSink, String riid, long provConID);

    /**
     * This method disconnects a given receptacle.
     * @param connID A long representing the generated unqiue ID of this particular connection.
     * @return A boolean indicating the success of this operation
     **/
    boolean disconnectFromRecp(long connID);

    /**
     * This method attaches a name-value pair element of meta-data to the receptacle.
     * @param name A String describing the attribute name.
     * @param type A String describing the attribute name.
     * @param value An object representing the attribute value.
     * @return A boolean indicating the success of this operation
     **/
    boolean putData(String name, String type, Object value);

    /**
     * This method retrieves the value of a name attribute from the receptacle.
     * @param name A String describing the attribute name.
     * @return A TypedAttribute object containing the value of the meta-data attribute.
     **/
    TypedAttribute getValue(String name);

     /**
     * This method retrieves all the meta-data stored on the  receptacle.
     * @return A hashmap containing all of the attribute-value pairs for the receptacle.
     **/
    Map getValues();
}
