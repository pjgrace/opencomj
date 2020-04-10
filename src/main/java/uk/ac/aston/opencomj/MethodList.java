/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.lancs.opencomj;

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
