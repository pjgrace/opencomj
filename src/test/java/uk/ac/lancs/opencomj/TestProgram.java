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

package uk.ac.lancs.opencomj;

import java.util.ArrayList;
import java.util.List;
import uk.ac.lancs.opencomj.IDelegator;
import uk.ac.lancs.opencomj.IMetaArchitecture;
import uk.ac.lancs.opencomj.IMetaInterception;
import uk.ac.lancs.opencomj.IMetaInterface;
import uk.ac.lancs.opencomj.IOpenCOM;
import uk.ac.lancs.opencomj.IUnknown;
import uk.ac.lancs.opencomj.InvalidComponentTypeException;
import uk.ac.lancs.opencomj.OCMConnInfo;
import uk.ac.lancs.opencomj.OCMRecpMetaInfo;
import uk.ac.lancs.opencomj.OpenCOM;
import uk.ac.lancs.opencomj.OpenComConstants;
import uk.ac.lancs.sample.Interceptors.PreAndPostMethods;
import uk.ac.lancs.sample.calculator.ICalculator;

/**
 * Test program for all the basic operations of the OpenCOM runtime.
 * @author  Paul Grace
 * @version 1.2.3
 */
public final class TestProgram {

    /**
     * Creates a new instance of TestProgram.
     */
    private TestProgram() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) {

        // Create the OpenCOM runtime & Get the IOpenCOM interface reference
        final OpenCOM runtime = new OpenCOM();
        final IOpenCOM pIOCM =  (IOpenCOM) runtime.queryInterface("IOpenCOM");

        // Create the Adder component
        try {
            final IUnknown pAdderIUnk = (IUnknown) pIOCM.createInstance("uk.ac.lancs.sample.adder.Adder", "Adder");
            final IUnknown pSubIUnk = (IUnknown) pIOCM.createInstance("uk.ac.lancs.sample.subtract.Subtract", "Subtract");
            final IUnknown pCalcIUnk = (IUnknown) pIOCM.createInstance("uk.ac.lancs.sample.calculator.Calculator", "Calculator");
            final ICalculator pICalc =  (ICalculator) pCalcIUnk.queryInterface("ICalculator");

            final long connID1 = runtime.connect(pCalcIUnk, pAdderIUnk, "IAdd");
            runtime.connect(pCalcIUnk, pSubIUnk, "ISubtract");

            // Lets test the Add and Subtract component
            System.out.println("The value of 18+19 = " + pICalc.add(18, 19));
            System.out.println("The value of 63-16 = " + pICalc.subtract(63, 16));

            final IMetaInterface pMeta2 =  (IMetaInterface) pAdderIUnk.queryInterface(OpenComConstants.METAINTERFACE);
            // Test the insertion of meta data // Add values to IADD
            pMeta2.setAttributeValue("IAdd", "Interface", "Variation", "int", 8);


            // However, the addition is wrong by 8, so lets use interception to correct it
            final IMetaInterception pIMeta = (IMetaInterception) runtime.queryInterface("IMetaInterception");
            final IDelegator pIAdderDel = pIMeta.getDelegator(pAdderIUnk, "IAdd");

            // Add the new pre-method
            final PreAndPostMethods interceptors = new PreAndPostMethods(pIOCM);
            pIAdderDel.addPreMethod(interceptors, "pre0");
            pIAdderDel.addPreMethod(interceptors, "checkAdd");

            // Lets test the Add component again
            System.out.println("The \"Intercepted value\" of 18+19 = " + pICalc.add(18, 19));

            // Lets do some more interception - Calculator has a display routine which we'll encrypt
            System.out.println(pICalc.display("I am the calculator"));

            final IDelegator pICalculatorDel = pIMeta.getDelegator(pCalcIUnk, "ICalculator");
            pICalculatorDel.addPreMethod(interceptors, "pre1");

            // Encrypted version
            System.out.println("Encrypted: " + pICalc.display("I am the calculator"));
            System.out.println();

            // Now decrypt using Post Interception
            pICalculatorDel.addPostMethod(interceptors, "post1");

            // Decrypted version
            System.out.println("Decrypted: " + pICalc.display("I am the calculator"));
            System.out.println();

            // Test the IMetaArchitecture interface
            final IMetaArchitecture pIMetaArch = (IMetaArchitecture) runtime.queryInterface("IMetaArchitecture");
            final List<Long> list = new ArrayList();
            final int noConns = pIMetaArch.enumConnsToIntf(pAdderIUnk, "IAdd", list);
            for (int index = 0; index < noConns; index++) {
                final OCMConnInfo tempConnInfo = pIOCM.getConnectionInfo(list.get(index));
                System.out.println("Component " + pIOCM.getComponentName(tempConnInfo.getSink()) + " is connected to "
                        + pIOCM.getComponentName(tempConnInfo.getSource()) + " on interface " + tempConnInfo.getInterfaceType());
            }
            System.out.println();

            final List<Long> recplist = new ArrayList();
            final int noConns2 = pIMetaArch.enumConnsFromRecp(pCalcIUnk, "IAdd", recplist);
            for (int index = 0; index < noConns2; index++) {
                final OCMConnInfo tempConnInfo = pIOCM.getConnectionInfo(recplist.get(index));
                System.out.println("Component " + pIOCM.getComponentName(tempConnInfo.getSource()) + " is connected to "
                        + pIOCM.getComponentName(tempConnInfo.getSink()) + " by receptacle of interface " + tempConnInfo.getInterfaceType());
            }
            System.out.println();

            // Test IMetaInterface
            IMetaInterface pMeta =  (IMetaInterface) pAdderIUnk.queryInterface(OpenComConstants.METAINTERFACE);
            final List<Class> ppIntf = new ArrayList();
            final int length = pMeta.enumIntfs(ppIntf);
            System.out.println("The number of Interfaces on Adder component is " + length);
            for (int y = 0; y < length; y++) {
                System.out.println(ppIntf.get(y).toString());
            }
            System.out.println();


            final List<OCMRecpMetaInfo> ppRecps = new ArrayList();
            pMeta =  (IMetaInterface) pCalcIUnk.queryInterface(OpenComConstants.METAINTERFACE);
            final int length2 = pMeta.enumRecps(ppRecps);
            System.out.println("The number of receptacles on Calculator component is " + length2);
            for (int y = 0; y < length2; y++) {
                final OCMRecpMetaInfo temp = ppRecps.get(y);
                System.out.println("Receptacle interface is : " + temp.getInterfaceType());
                System.out.println("Receptacle type is: " + temp.getReceptacleType());
            }
            System.out.println();

            // Test Connection info
            final OCMConnInfo connInfo = pIOCM.getConnectionInfo(connID1);
            System.out.println("Component " + pIOCM.getComponentName(connInfo.getSource()) + " is connected to "
                + pIOCM.getComponentName(connInfo.getSink()) + " by receptacle of interface " + connInfo.getInterfaceType());

            // Enumerate components
            List<IUnknown> ppComps = pIOCM.enumComponents();
            System.out.println("The number of components is : " + ppComps.size());

            // Test GetComponentName & GetComponentCLSID by listing the enumeration
            for (int index = 0; index < ppComps.size(); index++) {
                System.out.println("Component Name is: " + pIOCM.getComponentName(ppComps.get(index)) + " Class ID is: " + pIOCM.getComponentType(ppComps.get(index)));
            }

            // Test component deletion

            runtime.deleteInstance(pSubIUnk);
            ppComps = pIOCM.enumComponents();
            System.out.println("The number of components is : " + ppComps);

            final List<String> delList = pIAdderDel.viewPreMethods();

            for (String delList1 : delList) {
                System.out.println(delList1);
            }

            // Remove the del off ICalc - otherwise add will fail
            pICalculatorDel.delPreMethod("Pre1");

            pICalculatorDel.delPostMethod("Post1");

            // Check add still operates
            System.out.println("The value of 17+45 = " + pICalc.add(17, 45));
        } catch (InvalidComponentTypeException ex) {
            System.err.println(ex.getMessage());
        }

    }

}
