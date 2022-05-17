/*
 * TestProgram.java
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

package uk.ac.aston.opencomj.sample;

import uk.ac.aston.opencomj.IDelegator;
import uk.ac.aston.opencomj.ILifeCycle;
import uk.ac.aston.opencomj.IMetaInterception;
import uk.ac.aston.opencomj.IMetaInterface;
import uk.ac.aston.opencomj.IOpenCOM;
import uk.ac.aston.opencomj.IUnknown;
import uk.ac.aston.opencomj.InvalidComponentTypeException;
import uk.ac.aston.opencomj.OpenCOM;
import uk.ac.aston.opencomj.OpenComConstants;
import uk.ac.aston.opencomj.calculator.Interceptors.PreAndPostMethods;
import uk.ac.aston.opencomj.calculator.calculator.ICalculator;


/**
 * Demonstrates how to program and check interface contracts.
 * @author  Paul Grace
 * @version 1.2.3
 */
public final class ContractTest {

    /**
     * Creates a new instance of TestProgram.
     */
    private ContractTest() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) {

        // Create the OpenCOM runtime & Get the IOpenCOM interface reference
        final OpenCOM runtime = new OpenCOM();
        final IOpenCOM pIOCM =  (IOpenCOM) runtime.queryInterface("IOpenCOM");

        // Create the Adder component
        IUnknown pAdderIUnk = null;
        try {
            pAdderIUnk = (IUnknown) pIOCM.createInstance("uk.ac.aston.opencomj.calculator.adder.Adder", "Adder");
            ILifeCycle pILife =  (ILifeCycle) pAdderIUnk.queryInterface("ILifeCycle");
            pILife.startup(pIOCM);
        } catch (InvalidComponentTypeException ex) {
            System.err.println("Cannot create component - check class location " + ex.getMessage());
        }

        // Create the Subtract component
        IUnknown pSubIUnk = null;
        try {
            pSubIUnk  = (IUnknown) pIOCM.createInstance("uk.ac.aston.opencomj.calculator.subtract.Subtract", "Subtract");
            ILifeCycle pILife =  (ILifeCycle) pSubIUnk.queryInterface("ILifeCycle");
            pILife.startup(pIOCM);
        } catch (InvalidComponentTypeException ex) {
            System.err.println("Cannot create component - check class location " + ex.getMessage());
        }

        // Create the Calculator component
        IUnknown pCalcIUnk = null;
        try {
            pCalcIUnk  = (IUnknown) pIOCM.createInstance("uk.ac.aston.opencomj.calculator.calculator.Calculator", "Calculator");
            ILifeCycle pILife =  (ILifeCycle) pCalcIUnk.queryInterface("ILifeCycle");
            pILife.startup(pIOCM);
        } catch (InvalidComponentTypeException ex) {
            System.err.println("Cannot create component - check class location " + ex.getMessage());
        }

        // Get the Calculator Interface
        final ICalculator pICalc =  (ICalculator) pCalcIUnk.queryInterface("ICalculator");

        runtime.connect(pCalcIUnk, pAdderIUnk, "IAdd");
        runtime.connect(pCalcIUnk, pSubIUnk, "ISubtract");

        // Lets test the Add and Subtract component
        System.out.println("The value of 18+19 = " + pICalc.add(18, 19));
        System.out.println("The value of 63-16 = " + pICalc.subtract(63, 16));

        final IMetaInterface pMeta2 =  (IMetaInterface) pAdderIUnk.queryInterface(OpenComConstants.METAINTERFACE);
        // Test the insertion of meta data // Add values to IADD
        pMeta2.setAttributeValue("IAdd", "Interface", "Variation", "int", 8);

        final IMetaInterface pMeta3 =  (IMetaInterface) pCalcIUnk.queryInterface(OpenComConstants.METAINTERFACE);

        // Test the insertion of meta data // Add values to IADD
        pMeta3.setAttributeValue("IAdd", "Receptacle", "Variation", "int", 0);

        // However, the addition is wrong by 8, so lets use interception to correct it
        final IMetaInterception pIMeta = (IMetaInterception) runtime.queryInterface("IMetaInterception");
        final IDelegator pIAdderDel = pIMeta.getDelegator(pAdderIUnk, "IAdd");

        // Add the new pre-method
        final PreAndPostMethods interceptors = new PreAndPostMethods(pIOCM);
        pIAdderDel.addPreMethod(interceptors, "checkRules");
        // Lets test the Add component again
        try {
            System.out.println("The \"Intercepted value\" of 18+19 = " + pICalc.add(18, 19));
        } catch (Exception e) {
            System.out.println("Contract breached");
        }

        pIAdderDel.delPreMethod("checkRules");
        pIAdderDel.addPreMethod(interceptors, "pre0");
        pIAdderDel.addPreMethod(interceptors, "checkRules");
        try {
            System.out.println("The \"Intercepted value\" of 18+19 = " + pICalc.add(18, 19));
        } catch (Exception e) {
            System.out.println("Contract breached");
        }

    }

}
