/*
 * OCMDelegatorInfo.java
 *
 * OpenCOMJ is a flexible component model for reconfigurable reflection developed at Lancaster University.
 * Copyright (C) 2005 Paul Grace
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program; if not,
 * write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package uk.ac.aston.opencomj;
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
