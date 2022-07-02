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
 * This class stores meta-information about individual interface delegators. Primarily, their
 * interface type and their instance reference.
 *
 * @author Paul Grace
 * @version 1.2.3
 */
public class OCMDelegatorInfo {

    /**
     * pointer to IDelegator interface of the associated delegator component.
     * @see OpenCOM.IDelegator
     **/
    private final transient IDelegator pIDelegator;

    /**
     * Getter for the delegator object field.
     * @return The reference to the delegator.
     */
    public final IDelegator getDelegator() {
        return this.pIDelegator;
    }

    /**
     * The type of the interface that this delegator is associated with.
     **/
    private final transient String iid;

    /**
     * Getter for the interface type field.
     * @return The String on the interface type.
     */
    public final String getInterfaceType() {
        return this.iid;
    }

    /**
     * Constructor that creates a new instance of OCM_DelegatorInfo.
     * @param del Instance of the IDelegator interface
     * @param intf A string describing the interface type of this delegator
     * @see OpenCOM.IDelegator
     **/
    public OCMDelegatorInfo(final IDelegator del, final String intf) {
        pIDelegator = del;
        iid = intf;
    }

}
