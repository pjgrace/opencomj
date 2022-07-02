/*
 * PreAndPostMethods.java
 *
 * OpenCOMJ is a flexible component model for reconfigurable reflection developed at Lancaster University.
 * Copyright (C) 2022 Paul Grace
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program; if not,
 * write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package uk.ac.aston.components.privacy;

import uk.ac.aston.components.opencomj.IDelegator;
import uk.ac.aston.components.opencomj.IMetaInterception;
import uk.ac.aston.components.opencomj.IOpenCOM;
import uk.ac.aston.components.opencomj.IUnknown;
import uk.ac.aston.components.opencomj.TypedAttribute;
/**
 * Interceptors used in sample applications.
 * @author  Paul Grace
 * @version 1.2.3
 */
public class PrivacyInterceptors {
    /**
     * Pointer to the OpenCom kernel interface.
     */
    private final transient IOpenCOM pKernel;

    /**
     * Creates a new instance of PreAndPostMethods.
     * @param pIOCM runtime kernel.
     */
    public PrivacyInterceptors(final IOpenCOM pIOCM) {
         pKernel = pIOCM;
    }

    /**
     * Pre interceptor implementing addition mistake correction.
     * @param method The method intercepted.
     * @param args The arguments of the call.
     * @return The corrected value.
     */
    public final int pre0(final String method, final Object[] args) {
        
        
        // Take 8 off the first integer parameter to correct the addition
        final IUnknown pAdder = pKernel.getComponentPIUnknown("Adder");
        final IMetaInterception pMetaIntc = (IMetaInterception) pKernel.queryInterface("IMetaInterception");
        final IDelegator pDel = pMetaIntc.getDelegator(pAdder, "IAdd");

        final TypedAttribute vary = (TypedAttribute) pDel.getAttributeValue("Variation");
        final int value = (Integer) vary.getValue();

        final Integer int1 = (Integer) args[1];
        int val = int1;
        val = val - value;
        args[1] = val;

        pDel.setAttributeValue("Variation", "int", 0);
        return 0;
    }

    /**
     * Post operation to match the variation pre interceptor. Simple empty method
     * check.
     * @param method Method intercepted.
     * @param args The arguments of the call.
     * @return Function response code.
     */
    public final int post0(final String method, final Object[] args) {
        System.out.println(" Post Intercepting Method : " + method);
        return 0;
    }

}
