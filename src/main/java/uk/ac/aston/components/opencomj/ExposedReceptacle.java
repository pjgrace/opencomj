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
 * Local class definition for Exposed Receptacles that are stored in the MOPs of the framework.
 */
public class ExposedReceptacle {

    /**
     * Reference to the actual component instance that this exposed receptacle exists upon.
     */
    private final transient IUnknown pCompID;

    /**
     * Getter for the component hosting the exposed receptacle.
     * @return The component reference.
     */
    public final IUnknown getComponent() {
        return pCompID;
    }

    /**
     * The interface type of this exposed receptacle.
     */
    private final transient String intf;

    /**
     * Getter for the interface type of the receptacle.
     * @return The receptacle's interface type.
     */
    public final String getInterfaceType() {
        return intf;
    }

    /**
     * The receptacle type (e.g. single, multiple).
     */
    private final transient String recpType;

    /**
     * Getter for the type of the receptacle (single, multiple).
     * @return The receptacle type.
     */
    public final String getReceptacleType() {
        return recpType;
    }

    /**
    * Constructor for the receptacle data.
    * @param comp The component the receptacle is on.
    * @param intface The interface type of the exposed receptacle.
     *@param recType The type of this receptacle (single, multiple, etc.)
    *
    */
    public ExposedReceptacle(final IUnknown comp, final String intface, final String recType) {
        pCompID = comp;
        intf = intface;
        recpType = recType;
    }
}
