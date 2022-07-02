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
 * Set of fixed interface types used in the code.
 * To reduce name changing effects.
 *
 * @author pjg
 */
public final class OpenComConstants {

    /**
     * Private constructor. Never called.
     */
    private OpenComConstants() {

    }

    /**
     * The Interface type: IMetaInterface.
     */
    public static final String METAINTERFACE = "IMetaInterface";

    /**
     * The Interface type: IConnections.
     */
    public static final String CONNECTINTERFACE = "IConnections";

    /**
     * The Interface type: ILifeCycle.
     */
    public static final String LIFEINTERFACE = "ILifeCycle";

     /**
     * The method : QI.
     */
    public static final String QUERYI = "QueryInterface";

}
