/*
 * PreAndPostMethods.java
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

package uk.ac.lancs.sample.Interceptors;

import java.util.Map;
import uk.ac.lancs.opencomj.OpenComConstants;
import uk.ac.lancs.opencomj.IDelegator;
import uk.ac.lancs.opencomj.IMetaInterception;
import uk.ac.lancs.opencomj.IMetaInterface;
import uk.ac.lancs.opencomj.IOpenCOM;
import uk.ac.lancs.opencomj.IUnknown;
import uk.ac.lancs.opencomj.TypedAttribute;
/**
 * Interceptors used in sample applications.
 * @author  Paul Grace
 * @version 1.2.3
 */
public class PreAndPostMethods {
    /**
     * Pointer to the OpenCom kernel interface.
     */
    private final transient IOpenCOM pKernel;

    /**
     * Creates a new instance of PreAndPostMethods.
     * @param pIOCM runtime kernel.
     */
    public PreAndPostMethods(final IOpenCOM pIOCM) {
         pKernel = pIOCM;
    }

    /**
     * Encode interceptor. Takes a string and a key and produces the
     * encrypted text.
     * @param word The string to encrypt.
     * @param key The caesar key.
     * @return The encrypted key.
     */
    public final String encode(final String word, final int key) {
        int index = 0;

        char[] temp = word.toCharArray();
        while (index < word.length()) {
            temp[index] = (char) (temp[index] + (key));
            index++;
        }
        return new String(temp);
    }

    /**
     * Check that the meta rules are followed.
     * @param method The method intercepted.
     * @param args The arguments of the call
     * @return This is an integer object return so we can return value typed.
     */
    public final int checkRules(final String method, final Object[] args) {

        final IUnknown pCalc = pKernel.getComponentPIUnknown("Calculator");
        final IMetaInterface pMeta = (IMetaInterface) pCalc.queryInterface(OpenComConstants.METAINTERFACE);
        final Map ppVals = pMeta.getAllValues("Receptacle", "IAdd");
        if (ppVals != null) {
            final IUnknown pAdder = pKernel.getComponentPIUnknown("Adder");
            final IMetaInterception pMetaIntc = (IMetaInterception) pKernel.queryInterface("IMetaInterception");
            final IDelegator pDel = pMetaIntc.getDelegator(pAdder, "IAdd");
            for (Object entry : ppVals.keySet()) {
                final String ert = (String) entry;
                final TypedAttribute vary = (TypedAttribute) pDel.getAttributeValue(ert);
                final int value = (Integer) vary.getValue();
                final TypedAttribute rule = (TypedAttribute) ppVals.get(ert);
                final int value2 = (Integer) rule.getValue();
                if (value != value2) {
                    return -1;
                }
            }
        }

        return 0;
    }

    /**
     * Decode undoes the encode, by returning down the ascii char set.
     * @param word The parameter being decoded
     * @param key The decoding key.
     * @return The decoded parameter.
     */
    public final String decode(final String word, final int key) {
        int keyN = 0;

        //Change the string to an array of characters. So each character can be encrypted one at a time
        //as in classical caesar method

        char[] temp = word.toCharArray();
        while (keyN < word.length()) {
            temp[keyN] = (char) (temp[keyN ] - (key));
            keyN++;
        }

        return new String(temp);
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
     * Pre interceptor to add the encryptor method.
     * @param method The method being intercepted.
     * @param args The arguments of the method.
     * @return The function code response.
     */
    public final int pre1(final String method, final Object[] args) {

        args[0] = encode((String) args[0].toString(), 12);
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

    /**
     * The decrypt post method of the encrypted parameter.
     * @param method The method being called.
     * @param args The arguments of the call.
     * @return The function response code.
     */
    public final String post1(final String method, final Object[] args) {

        return decode((String) args[0], 12);
    }

}
