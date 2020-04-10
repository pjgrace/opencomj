/*
 * Adder.java
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

package uk.ac.lancs.sample.adder;

import uk.ac.lancs.opencomj.ILifeCycle;
import uk.ac.lancs.opencomj.IMetaInterface;
import uk.ac.lancs.opencomj.IUnknown;
import uk.ac.lancs.opencomj.AbstractOpenCOMComponent;

/**
 * Simple component offering methods to add integers and receive messages.
 * @author  Paul Grace
 * @version 1.2.3
 */
public class Adder extends AbstractOpenCOMComponent implements IUnknown, IAdd, IMetaInterface, ILifeCycle {


    /**
     * Creates a new instance of Adder.
     * @param pRuntime Reference to the opencom kernel.
     */
    public Adder(final IUnknown pRuntime) {
        super(pRuntime);
    }

    // IAdd interface
    @Override
    public final int add(final int xOperand, final int yOperand) {
        return xOperand + yOperand + 8;
    }

    // ILifeCycle Interface
    @Override
    public final boolean startup(final Object pIOCM) {
        return true;
    }

    @Override
    public final boolean shutdown() {
        return true;
    }

}
