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
 * Exception identifying an error in a pre/post based invocation.
 * @author pjg
 */
public class InvocationException extends Exception {

    /**
     * Exception identifying an error in a pre/post based invocation.
     * @param exceptionMessage The corresponding error message to tag
     */
    public InvocationException(final String exceptionMessage) {
        super(exceptionMessage);
    }

    /**
     * Exception identifying an error in a pre/post based invocation.
     * @param exceptionMessage The corresponding error message to tag
     * @param excep The current stack trace.
     */
    public InvocationException(final String exceptionMessage, final Exception excep) {
        super(exceptionMessage, excep);
    }
}
