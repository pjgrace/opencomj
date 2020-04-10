/////////////////////////////////////////////////////////////////////////
//
// © University of Southampton IT Innovation Centre, 2015
//
// Copyright in this library belongs to the University of Southampton
// University Road, Highfield, Southampton, UK, SO17 1BJ
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
// Created By : Paul Grace
// Created for Project : XIFI (http://www.fi-xifi.eu)
//
/////////////////////////////////////////////////////////////////////////
//
//  License : GNU Lesser General Public License, version 3
//
/////////////////////////////////////////////////////////////////////////

package uk.ac.lancs.opencomj;

/**
 * Exception thrown only during the call to create a component. Either
 * during a kernel call, or a framework call. There can be a number of reasons
 * for the error:
 * 1) The class given by the component doesn't exist on the current classpath.
 * 2) The class is not a valid opencom component.
 * @author pjg
 */
public class InvalidComponentTypeException extends Exception {

    /**
     * Exception identifying an error in a pre/post based invocation.
     * @param exceptionMessage The corresponding error message to tag
     */
    public InvalidComponentTypeException(final String exceptionMessage) {
        super(exceptionMessage);
    }

    /**
     * Exception identifying an error in a pre/post based invocation.
     * @param exceptionMessage The corresponding error message to tag
     * @param excep The current stack trace.
     */
    public InvalidComponentTypeException(final String exceptionMessage, final Exception excep) {
        super(exceptionMessage, excep);
    }
}
