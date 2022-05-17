/*
 * OCMConnInfo.java
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
 * The OCMConnInfo class represents Meta-Information stored in the OpenCOM runtime about every
 connection between components.
 *
 * @author  Paul Grace
 * @version 1.2.3
 **/

public class OCMConnInfo {


    /**
     * A reference to the component instance hosting the receptacle.
     * @see OpenCOM.IUnknown
     **/
    private final transient IUnknown sourceComponent;

    /**
     * Getter for the source component field.
     * @return The source component reference.
     */
    public final IUnknown getSource() {
        return this.sourceComponent;
    }

    /**
     *A reference to the component instance hosting the interface.
     * @see OpenCOM.IUnknown
     **/
    private final transient IUnknown sinkComponent;

     /**
     * Getter for the sink component field.
     * @return The sink component reference.
     */
    public final IUnknown getSink() {
        return this.sinkComponent;
    }

    /** A string describing the interface type of the connection. **/
    private final transient String interfaceType;

     /**
     * Getter for the interface type component field.
     * @return The interface type string.
     */
    public final String getInterfaceType() {
        return this.interfaceType;
    }

    /**
     * Default constructor that allows the source component, sink component & interface type information to be set.
     * @param srcComponent The source comp of the connection.
     * @param skComponent The sink comp of the connection.
     * @param iidType The interface type.
     * @see OpenCOM.IUnknown
     **/
    public OCMConnInfo(final IUnknown srcComponent, final IUnknown skComponent, final String iidType) {
        sourceComponent = srcComponent;
        sinkComponent = skComponent;
        interfaceType = iidType;
    }

}
