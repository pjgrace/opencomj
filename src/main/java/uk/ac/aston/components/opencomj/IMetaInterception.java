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
 * This interface is implemented by the OpenCOM runtime. It is used to obtain delegator interfaces
 * for perform meta-interception operations.
 *
 * @author  Paul Grace
 * @version 1.2.3
 */

public interface IMetaInterception {

	/**
         * Returns the delegator of a given interface type on a given instantiated component.
         * @param pIUnkParent an IUnknown reference describing a component instantiation.
         * @param riid a string representing the interface type of the requested delegator.
         * @return the IDelegator interface.
         * @see OpenCOM.IUnknown
         * @see OpenCOM.IDelegator
         */
        IDelegator getDelegator(IUnknown pIUnkParent, String riid);

};