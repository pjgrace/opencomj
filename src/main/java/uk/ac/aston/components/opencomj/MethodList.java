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

import java.lang.reflect.Method;

/**
* Class describes how pre and post methods are stored on the delegator.
*/
public class MethodList {
    /**
     * Java Reflect method corresponding to the physical method implementation.
     */
    private final transient Method method;

    /**
     * Object instance hosting the pre/post method.
     */
    private final transient Object object;

    /**
     *  string name of the pre/post method.
     */
    private final transient String name;

    /**
     * Constructor.
     * @param newMethod The reflection method of the corresponding physical method.
     * @param interceptorObject Reference to instance hosting the interceptor method.
     * @param methodName Name of the method as a string.
     */
    public MethodList(final Method newMethod, final Object interceptorObject, final String methodName) {
       method = newMethod;
       object = interceptorObject;
       name = methodName;
    }

    /**
     * Get the method object reference.
     * @return The java reflect method.
     */
    public final Method getMethod() {
        return method;
    }

    /**
     * Get the object reference of the method interceptor.
     * @return The java object of the interceptor.
     */
    public final Object getObject() {
        return object;
    }

    /**
     * Get the string name of the method.
     * @return The java string with the method name.
     */
    public final String getName() {
        return name;
    }
}
