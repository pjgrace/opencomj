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
 * The IUnknown interface is a central aspect of OpenCOM. However,
 it is not actually required in the Java version. Standard Interface/Object behaviour
 in Java negates the need for queryInterface. Furthermore, the garbage collector
 means addref and release are not required. However, IUnknown remains in this
 version to present the same (okay similar) programming model to traditional
 OpenCOM.

 Note: QI is an improvement over object casting, like OpenCOM you can detect if the
 interface is available on the component before invoking it. That is, we retain
 one of the programming model benefits of COM.
 *
 * @author  Paul Grace
 * @version 1.2.3
 */
public interface IUnknown {
    /**
     * Obtain a reference to the interface of the type passed as parameter.
     * @param interfaceName a string representing the Java interface type, equivalent to the IID type in COM.
     * @return an Object representing a reference to the component hosting the interface requested.
     **/
    Object queryInterface(String interfaceName);
}

