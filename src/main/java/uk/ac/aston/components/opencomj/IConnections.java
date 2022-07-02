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
/**
 * The IConnection interface is implemented by every component with a
 * receptacle. This interface should never be called by the application, and
 * should only be utilised by the runtime kernel.
 * @author  Paul Grace
 * @version 1.2.3
 */
public interface IConnections {

    /**
     * Connects the specified sink interface to a receptacle of the
     * specified type in the target component instance.
     *  @param pSinkIntf a component instance to connect to.
     *  @param riid a string representing the Interface type of the connection.
     *  @param provConnID a long representing the unique identifier of the connection.
     *  @return a boolean indicating the success of the method.
     *  @see OpenCOM.IUnknown
     */
    boolean connect(IUnknown pSinkIntf, String riid, long provConnID);

    /**
     * Takes a previously established connection's connection identifier
     * and disconnects the corresponding receptacle and interface by
     * resetting the receptacle's internal interface pointer.
     *  @param riid a string representing the Interface type of the connection.
     *  @param connID a long representing the unique identifier of the connection.
     *  @return a boolean indicating the success of the method.
     */
    boolean disconnect(String riid, long connID);

}