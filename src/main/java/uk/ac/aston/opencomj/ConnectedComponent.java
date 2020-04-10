/*
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
 * The framework stores connection information for use during rollback of configurations.
 */

public class ConnectedComponent {
    /**
     * The reference of the connected component.
     */
    private final transient IUnknown pCompID;

    /**
     * The reference ID of the connection.
     */
    private final transient long connection;

    /**
     * Construct the connected component object information.
     * @param comp The component.
     * @param conn The connection id.
     */
    public ConnectedComponent(final IUnknown comp, final long conn) {
       pCompID = comp;
       connection = conn;
    }

    /**
     * Get the connected component reference.
     * @return The IUnknown reference of the component.
     */
    public final IUnknown getComponent() {
        return pCompID;
    }

    /**
     * Get the connection information identifier.
     * @return The connection id.
     */
    public final long getConnection() {
        return connection;
    }
}
