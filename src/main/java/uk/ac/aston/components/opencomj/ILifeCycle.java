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
 * Interface implemented by every OpenCOM components. Manages the startup and shutdown
 * of components.
 * @author  Paul Grace
 * @version 1.2.3
 */

public interface ILifeCycle {

    /**
    * Allows a component to take action whenever an instance
    * is created. A pointer to the IOCM of the runtime is passed to allow
    * the instance to store it as a member variable, thus allowing
    * convenient access from member methods.
    * This method should be called directly by the programmer after an
    * instance is created. This separation between creation and
    * initialisation is needed because startup() typically
    * invokes receptacle-based intarfaces, which must be connected
    * after a component instance has been created by the runtime,
    * i.e. before startup can be invoked.
    * @param data Any startup data to be passed to the component when it is activated.
    * @return A boolean indicating the success of the operation
    * @see OpenCOM.IOpenCOM
    **/
    boolean startup(Object data);

    /**
    * Allows a component to take action while it is being deleted.
    * This method SHOULD NOT be directly called by the programmer
    * but it is called by the OpenCOM runtime when a component
    * it deleted by calling deleteInstance().
    * @return A boolean indicating the success of the operation
    **/
     boolean shutdown();

}
