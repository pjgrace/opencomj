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
