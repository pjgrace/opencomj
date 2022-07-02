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
 * This class stores Meta-Information about component receptacles. It is used by Interface meta-model
 * to describe a component's receptacles. Application developers can use this meta-data directly.
 *
 * @author  Paul Grace
 * @version 1.2.3
 */

public class OCMRecpMetaInfo {
    //! Information stored about a Receptacle

    /**
     * The interface type of the receptacle.
     */
    private final transient String iid;

    /**
     * Getter for the interface type field.
     * @return The interface type field.
     */
    public final String getInterfaceType() {
        return this.iid;
    }

    /**
     * The receptacle type, either "Single" or "Multiple".
     */
    private final transient String recpType;

    /**
     * Getter for the receptacle type field.
     * @return The receptacle type field.
     */
    public final String getReceptacleType() {
        return this.recpType;
    }

    /**
     * Constructor creates a new instance of OCM_RecpMetaInfo_t object.
     * @param interfaceType The interface type of the receptacle.
     * @param type The receptacle type i.e. either single or multiple.
     */
    public OCMRecpMetaInfo(final String interfaceType, final String type) {
        iid = interfaceType;
        recpType = type;
    }

}
