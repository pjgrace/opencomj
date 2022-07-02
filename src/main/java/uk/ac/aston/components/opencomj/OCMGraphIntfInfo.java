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
 * This class is used only within the system graph of OpenCOM. Basically, each object stores information
 * about component's interfaces i.e. their type and if they are connected to a particular receptacle.
 * Generally, a component node in the graph will contain a list of these objects.
 *
 * @author  Paul Grace
 * @version 1.2.3
 */
public class OCMGraphIntfInfo {

    /** Connection id of the connection this interface is part of. */
    private final transient long connID;

    /**
     * Getter for the connection id field.
     * @return The connection id long.
     */
    public final long getConnID() {
        return this.connID;
    }

    /**
     * Source component (receptacle host) that this interface is connected to.
     */
    private final transient IUnknown sourceIndex;

    /**
     * Getter for the source component field.
     * @return The source component reference.
     */
    public final IUnknown getSource() {
        return this.sourceIndex;
    }

    /**
     * The interface type of this interface.
     */
    private final transient String iidName;

    /**
     * Getter for the interface type field.
     * @return The interface type string.
     */
    public final String getInterfaceType() {
        return this.iidName;
    }

    /**
     * Constructor creates a new instance of OCM_GraphIntfInfo_t node.
     * @param connectionID The connection id.
     * @param indexinGraph The source component.
     * @param name The interface type.
     */
    public OCMGraphIntfInfo(final long connectionID, final IUnknown indexinGraph, final String name) {
        connID = connectionID;
        sourceIndex = indexinGraph;
        iidName = name;
    }

}
