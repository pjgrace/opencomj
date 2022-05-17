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

import uk.ac.aston.opencomj.IOpenCOM;
import uk.ac.aston.opencomj.IUnknown;
import uk.ac.aston.opencomj.InvalidComponentTypeException;
import uk.ac.aston.opencomj.OpenCOM;
import uk.ac.aston.opencomj.calculator.adder.IAdd;


/**
 * Test program for all the basic operations of the OpenCOM runtime.
 * @author  Paul Grace
 * @version 1.2.3
 */
public final class PrivacyTest {

    /**
     * Creates a new instance of TestProgram.
     */
    private PrivacyTest() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) {

        try {
            // Create the OpenCOM runtime & Get the IOpenCOM interface reference
            final OpenCOM runtime = new OpenCOM();
            final IOpenCOM pIOCM =  (IOpenCOM) runtime.queryInterface("IOpenCOM");
            
            // Create the Adder component
            final IUnknown pAdderIUnk = (IUnknown) pIOCM.createInstance("uk.ac.aston.opencomj.calculator.adder.Adder", "Adder");
            final IAdd pICalc =  (IAdd) pAdderIUnk.queryInterface("IAdd");

            // Lets test the Add and Subtract component
            System.out.println("The value of 18+19 = " + pICalc.add(18, 19));
        } catch (InvalidComponentTypeException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
