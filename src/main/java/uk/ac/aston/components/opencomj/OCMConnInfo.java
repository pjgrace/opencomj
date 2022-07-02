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
