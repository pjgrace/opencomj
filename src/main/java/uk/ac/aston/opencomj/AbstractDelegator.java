
/*
 * Delegator.java
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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class of individual delegator objects that are attached to
 * each interface for the purpose of pre and post method interception.
 * Note: The implementation mimics OpenCOM's dummy QI redirection using
 * Java dynamic proxies instead.
 * @see OpenCOM.IDelegator
 * @see java.lang.reflect.InvocationHandler
 * @author  Paul Grace
 * @version 1.2.3
 */
public abstract class AbstractDelegator implements InvocationHandler, IDelegator {

    /**
    * The original Component that we are delegating from.
    */
    protected transient Object obj;

    /**
     * The Outer Proxy of this delegator.
     */
    protected transient Object higherObject;

    /**
     * Getter for the higher object field.
     * @return The higher delegated object.
     */
    public final Object getHigherObject() {
        return higherObject;
    }

    /**
     * Setter for the higher object field.
     * @param nHObj The new higher delegated object.
     */
    public final void setHigherObject(final Object nHObj) {
        higherObject = nHObj;
    }

     /**
     * List of pre methods stored on this delegator.
     */
    protected transient List<MethodList> preMethods;

    /**
     * List of post methods stored on this delegator.
     */
    protected transient List<MethodList> postMethods;

    /**
     * Meta data attached to this receptacle.
     */
    protected transient Map<String, TypedAttribute> metaData;
    
    /**
     * Pointer to MetaInterception runtime.
     * @see OpenCOM.IMetaInterception
     */
    protected transient IMetaInterception pImInterception;

    @Override
    public Object invoke(Object o, Method method, Object[] os) throws Throwable {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

     //! Implements IDelegator interface of OpenCOM

    @Override
    public final boolean addPreMethod(final Object interceptorObject, final String methodName) {
        // Extract the method off the Interceptor object
        final Class cls = interceptorObject.getClass();
        Class[] parameterTypes = new Class[2];
        parameterTypes[0] = String.class;
        parameterTypes[1] = Object[].class;
        try {
            final Method methodPre = cls.getMethod(methodName, parameterTypes);
            final MethodList val = new MethodList(methodPre, interceptorObject, methodName);
            preMethods.add(val);
        } catch (NoSuchMethodException ex) {
            return false;
        } catch (SecurityException ex) {
            return false;
        }
        return true;
    }

    @Override
    public final boolean addPostMethod(final Object interceptorObject, final String methodName) {

        final Class cls = interceptorObject.getClass();
        Class[] parameterTypes = new Class[2];
        parameterTypes[0] = String.class;
        parameterTypes[1] = Object[].class;
        try {
            final Method methodPost = cls.getMethod(methodName, parameterTypes);
            final MethodList val = new MethodList(methodPost, interceptorObject, methodName);

            postMethods.add(val);
        } catch (NoSuchMethodException ex) {
            return false;
        } catch (SecurityException ex) {
            return false;
        }
        return true;
    }


    @Override
    public final boolean delPostMethod(final String methodName) {
        for (int i = 0; i < postMethods.size(); i++) {
            if (((MethodList) postMethods.get(i)).getName().equalsIgnoreCase(methodName)) {
                postMethods.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public final boolean delPreMethod(final String methodName) {
        for (int i = 0; i < preMethods.size(); i++) {
            if (((MethodList) preMethods.get(i)).getName().equalsIgnoreCase(methodName)) {
                preMethods.remove(i);
                return true;
            }
        }
        return false;
    }


    @Override
    public final List<String> viewPostMethods() {
        final List<String> methodNames = new ArrayList();
        for (MethodList postMethod : postMethods) {
            methodNames.add(postMethod.getName());
        }
        return methodNames;
    }

    @Override
    public final List<String> viewPreMethods() {
        final List<String> methodNames = new ArrayList();
        for (MethodList preMethod : preMethods) {
            methodNames.add(preMethod.getName());
        }
        return methodNames;
    }

    @Override
    public final boolean setAttributeValue(final String name, final String type, final Object value) {
        metaData.put(name, new TypedAttribute(type, value));
        return true;
    }

    @Override
    public final TypedAttribute getAttributeValue(final String name) {
        return (TypedAttribute) metaData.get(name);
    }

   
}
