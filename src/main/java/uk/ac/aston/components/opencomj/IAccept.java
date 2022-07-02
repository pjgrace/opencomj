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

import java.util.List;

/**
 * The IAccept interface is implemented by components who perform check operations on Component Frameworks.
 * This interface and its syntax is heavily influenced by the C++ CF model proposed by
 * ReMMoC (and hence why it includes what seem like unnecessary parameters).
 *
 * @author Paul Grace
 * @version 1.2.3
 **/

public interface IAccept {
    /**
     * This method performs validation checks on CF graphs.
     * @param graph A Vector containing the internal graph of the composite component framework to check
     * @param intfs A Vector describing the list of interfaces exposed by the component framework
     * @return A boolean indicating whether the CF contains a valid or invalid configuration
     **/
    boolean isValid(List<IUnknown> graph, List<ExposedInterface> intfs);
}

