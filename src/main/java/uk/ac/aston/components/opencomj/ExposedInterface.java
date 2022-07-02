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
* Local class definition for Exposed Interfaces that are stored in the MOPs of the framework.
*/
public class ExposedInterface {
    /**
     * Reference to the actual component instance that this exposed interface exists upon.
     */
    private final transient IUnknown pCompID;

    /**
     * The interface type of this exposed interface.
     */
    private final transient String intfType;

    /**
     * Constructor for exposed interface.
     * @param component The source component of the interface
     * @param intf The string type of the interface.
     */
    public ExposedInterface(final IUnknown component, final String intf) {
        pCompID = component;
        intfType = intf;
    }

    /**
     * Get the component id of the source of the interface.
     * @return The IUnknown reference of the component.
     */
    public final IUnknown getComponentID() {
        return pCompID;
    }
    /**
     * Get the component id of the source of the interface.
     * @return The type of the interface as a string.
     */
    public final String getIntfType() {
        return intfType;
    }
}
