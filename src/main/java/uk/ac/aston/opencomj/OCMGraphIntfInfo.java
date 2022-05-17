/*
 * OCMGraphIntfInfo.java
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
