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
    public transient Object higherObject;

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
        } catch (NoSuchMethodException | SecurityException ex) {
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
        } catch (NoSuchMethodException | SecurityException ex) {
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
