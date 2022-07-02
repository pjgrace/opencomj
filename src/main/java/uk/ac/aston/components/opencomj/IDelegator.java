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
 * Description: Interface describing operation available on individual
 * delegators. Its operations are identical to the original pre and post
 * interception of OpenCOM.
 *
 * Note: I have not implemented hooking as I do not believe it offers
 * anything beneficial. However, if anyone wants to add it - it would
 * be relatively straightforward, just follow the same dynamic proxy
 * approach.
 * @author  Paul Grace
 * @version 1.2.3
 */
public interface IDelegator {
    /**
     * Inserts a pre-method on this delegator. All subsequent invocation of interface operations
     * first pass through this method. Multiple pre-methods can be inserted, they are traversed in
     * the order they were inserted.
     * @param methodHost A Java object containing the pre-method to insert.
     * @param methodName A String describing the name of the pre-method.
     * @return A boolean indicating the success of the operation.
     **/
    boolean addPreMethod(Object methodHost, String methodName);

    /**
     * Deletes a specified pre-method from this delegator.
     * @param methodName A String describing the name of the pre-method.
     * @return A boolean indicating the success of the operation.
     **/
    boolean delPreMethod(String methodName);

    /**
     * Inserts a post-method on this delegator. All subsequent invocation of interface operations
     * pass through this method after invocation. Multiple post-methods can be inserted, they are traversed in
     * the order they were inserted.
     * @param methodHost A Java object containing the pre-method to insert.
     * @param methodName A String describing the name of the pre-method.
     * @return A boolean indicating the success of the operation.
     **/
    boolean addPostMethod(Object methodHost, String methodName);

    /**
     * Deletes a specified post-method from this delegator.
     * @param methodName A String describing the name of the pre-method.
     * @return A boolean indicating the success of the operation.
     **/
    boolean delPostMethod(String methodName);

    /**
     * A Meta-Inspection operation. Returns a list of pre-methods attached to the delegator.
     * @return A list of the method names.
     **/
    List<String> viewPreMethods();

    /**
     * A Meta-Inspection operation. Returns a list of post-methods attached to the delegator.
     * @return A list of the method names.
     **/
    List<String> viewPostMethods();

    /**
     * For simplicity this version of OpenCOM attaches interface meta-data to the delagator.
     * For a cleaner separation see the Java implementation of OpenCOM v2.
     * This method sets the name-value pair.
     * @param name A string describing the meta-data attribute.
     * @param type The type of the attribute.
     * @param value An Object holding the value of the attribute.
     * @return A boolean indicating if the attribute was added/updated or not.
     **/
    boolean setAttributeValue(String name, String type, Object value);

    /**
     * This method retrieves the value of a name-value pair.
     * @param name A string describing the meta-data attribute.
     * @return A TypedAttribute Object holding the value and type of the attribute.
     **/
    TypedAttribute getAttributeValue(String name);

}
