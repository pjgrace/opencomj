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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;


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
public class Delegator extends AbstractDelegator {

    /**
    * The dynamic proxy creation operation - takes the original component and wraps the
    * dynamic invocation handler around it.
    * @param origObject The object to delegate
     * @return The interceptor object.
    */
    public final Object newInstance(final Object origObject) {
        return java.lang.reflect.Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
             origObject.getClass().getInterfaces(), (java.lang.reflect.InvocationHandler) this);
    }

    /**
     * Constructor for new delegator instance.
     * @param origObj The original object of the component.
     * @param pIOCM The meta interception interface of the component
     * @see OpenCOM.IMetaInterception
     */
    public Delegator(final Object origObj, final IMetaInterception pIOCM) {
          this.obj = origObj;
          preMethods = new ArrayList();
          postMethods = new ArrayList();
          pImInterception = pIOCM;
          metaData = new HashMap();
    }

    /**
     * invoke is called on this dynamic proxy whenever a method of the "inner" component
     * is invoked. Therefore, it will ensure that the pre methods are called before the actual
     * operation and the post methods afterwards.
     * @param proxy the proxy component.
     * @param method The method to be invoked.
     * @param argsList An object array with all the arguments of the original invocation.
     * @return An object holding the result of the invocation.
     * @throws java.lang.Throwable Error.
     */
    @Override
    public final Object invoke(final Object proxy, final Method method, Object[] argsList) throws Throwable {
        Object result = -1;

        if (argsList == null) {
            argsList = new Object[0];
        }

        try {
            // QI is a special case not to intercept
            if (method.getName().equalsIgnoreCase(OpenComConstants.QUERYI)) {
                result = method.invoke(obj, argsList);
                final String intfName = (String) argsList[0].toString();
                if ((!(intfName.equalsIgnoreCase(OpenComConstants.CONNECTINTERFACE))
                         || (intfName.equalsIgnoreCase(OpenComConstants.METAINTERFACE))
                         || (intfName.equalsIgnoreCase(OpenComConstants.LIFEINTERFACE))) && (result != null)) {

                        // Ensure the QI passes back the proxy object not the original component
                        final AbstractDelegator del = (AbstractDelegator) pImInterception.getDelegator((IUnknown) proxy, argsList[0].toString());
                        if (del != null) {
                            result = del.higherObject;
                        }
                }
            } else {
                // Invoke each of the pre-methods in order (list traversal)
                Object[] params = new Object[2];
                params[0] = method.getName();
                params[1] = argsList;
                for (MethodList preMethod : preMethods) {
                    final Integer res = (Integer) preMethod.getMethod().invoke(preMethod.getObject(), params);
                    if (res != 0) {
                        throw new InvocationException("PreMethod halted invocation");
                    }
                }
                // Invoke the actual method
                result = method.invoke(obj, argsList);

                // Invoke each of the post-methods in order (list traversal)
                Object[] arguments = new Object[argsList.length + 1];
                for (MethodList postMethod : postMethods) {
                    arguments[0] = result;
                    System.arraycopy(argsList, 0, arguments, 1, argsList.length);
                    params[1] = arguments;
                    final Object tempResult = postMethod.getMethod().invoke(postMethod.getObject(), params);
                    argsList = (Object[]) params[1];
                    try {
                        if (((Integer) tempResult) != 0) {
                            result = tempResult;
                        }
                    } catch (ClassCastException e) {
                        result = tempResult;
                    }
                }
            }
        } catch (InvocationTargetException e) {
             throw e.getTargetException();
        }
         return result;
    }

   
}
