/*
 * CalculatorFramework.java
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

package uk.ac.aston.opencomj.calculator.framework;

import uk.ac.aston.opencomj.AbstractCFMetaInterface;
import uk.ac.aston.opencomj.ICFMetaInterface;
import uk.ac.aston.opencomj.IConnections;
import uk.ac.aston.opencomj.ILifeCycle;
import uk.ac.aston.opencomj.IMetaInterface;
import uk.ac.aston.opencomj.IUnknown;


/**
 * Example component framework that manages the three calculator components.
 *
 * @author  Paul Grace
 * @version 1.2.3
 */
public class CalculatorFramework extends AbstractCFMetaInterface implements ICFMetaInterface, IConnections, IMetaInterface, IUnknown, ILifeCycle {

    /**
     * Creates a new instance of CalculatorFramework.
     * @param pRuntime Reference to the kernel
     */
    public CalculatorFramework(final IUnknown pRuntime) {
       super(pRuntime);
    }

    @Override
    public final Object queryInterface(final String interfaceName) {
         // Test if its an exposed interface
        return queryInterface(interfaceName, this);
    }

    // ILifeCycle Interface
   @Override
    public final boolean shutdown() {
        return super.shutdown();

    }

   @Override
    public final boolean startup(final Object pIOCM) {
        return super.startup(pIOCM);
    }

}