 /**
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
