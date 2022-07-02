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

import java.util.List;

/**
 * Interface implemented by the OpenCOM runtime. Provides operations to determine which
 * components are connected to a particular component's interfaces and receptacles. i.e.
 * it provides introspection operations.
 *
 * @author  Paul Grace
 * @version 1.2.3
 */
public interface IMetaArchitecture {

    /**
    * Returns an array of connection identifiers detailing all the
    * connections established on the specified interface of the target
    * component instance.
    * @param pIUnknown A reference of the component whose connections are to be inspected.
    * @param riid A String representing the interface type to be inspected.
    * @param ppConnsToIntf a list of unique connection identifiers of connections to this interface.
    * @return An integer describing the number of connections to the interface.
    **/
    int enumConnsToIntf(IUnknown pIUnknown, String riid, List<Long> ppConnsToIntf);

    /**
    * Returns an array of connection identifiers detailing all the
    * connections established by the  receptacle of the specified
    * interface type on the target component instance.
    * @param pIUnknown A reference of the component whose connections are to be inspected.
    * @param riid A String representing the interface type to be inspected.
    * @param ppConnsFromRecp a list of unique connection identifiers of connections from this receptacle.
    * @return An integer describing the number of connections from the receptacle.
    **/
    int enumConnsFromRecp(IUnknown pIUnknown, String riid,  List<Long> ppConnsFromRecp);
}
