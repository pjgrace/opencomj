/*
 * OCMGraphRecpInfo.java
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

package uk.ac.lancs.opencomj;

/**
 * This class is used only within the inner working of the OpenCOM graph and its corresponding
 * meta-operations. Generally, these object are attached to the component nodes in the system
 * graph. Each component will have a number of these objects dependent upon the number of
 * receptacles that they host.
 *
 * @author  Paul Grace
 * @version 1.2.3
 */

public class OCMGraphRecpInfo {
    /**
     * Connection id of the connection this receptacle is part of.
     */
    private final transient long connID;

    /**
     * Getter for the connection id field.
     * @return The connection id long.
     */
    public final long getConnectionID() {
        return this.connID;
    }

    /**
     * The index in the system graph of the sink component. That is, which component the
     *  component is connected; its pointing to a OCM_GraphNode_t object.
     */
    private final transient IUnknown sinkIndex;

    /**
     * Getter for the sink component field.
     * @return The sink component reference.
     */
    public final IUnknown getSinkComponent() {
        return this.sinkIndex;
    }

    /**
     * The interface type of the receptacle.
     */
    private final transient String iid;

    /**
     * Getter for the interface type field.
     * @return The interface type string.
     */
    public final String getInterfaceType() {
        return this.iid;
    }

    /**
     * Constructor creates a new instance of OCM_GraphRecpInfo_t object to store in system graph.
     * @param connectionID The connection id.
     * @param indexinGraph The sink component reference.
     * @param name The interface type.
     */
    public OCMGraphRecpInfo(final long connectionID, final IUnknown indexinGraph, final String name) {
        connID = connectionID;
        sinkIndex = indexinGraph;
        iid = name;
    }

}
