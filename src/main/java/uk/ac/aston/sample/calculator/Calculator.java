/*
 * Calculator.java
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

package uk.ac.lancs.sample.calculator;

import uk.ac.lancs.opencomj.IConnections;
import uk.ac.lancs.opencomj.ILifeCycle;
import uk.ac.lancs.opencomj.IMetaInterface;
import uk.ac.lancs.opencomj.IUnknown;
import uk.ac.lancs.opencomj.OCMSingleReceptacle;
import uk.ac.lancs.opencomj.AbstractOpenCOMComponent;
import uk.ac.lancs.sample.adder.IAdd;
import uk.ac.lancs.sample.subtract.ISubtract;


/**
 * Calculator component supporting arithmetic plug-in components.
 * @author  Paul Grace
 * @version 1.2.3
 */

public class Calculator extends AbstractOpenCOMComponent implements ICalculator, IConnections, ILifeCycle, IUnknown, IMetaInterface {

    /**
     * Requires Interface of type IAdd.
     */
    public final transient OCMSingleReceptacle<IAdd> mpSRIAdd;

    /**
     * Requires Interface of type ISubtract.
     */
    public final transient OCMSingleReceptacle<ISubtract> mpSRISubtract;

    /**
     * Creates a new instance of Calculator.
     * @param binder The opencom kernel reference.
     */
    public Calculator(final IUnknown binder) {
        super(binder);
        mpSRIAdd = new OCMSingleReceptacle();
        mpSRISubtract = new OCMSingleReceptacle();
    }

    //Interface ICalculator
    @Override
    public final int add(final int xOperand, final int yOperand) {
        return mpSRIAdd.getInterface().add(xOperand, yOperand);
    }

    @Override
    public final int subtract(final int xOperand, final int yOperand) {
      return mpSRISubtract.getInterface().subtract(xOperand, yOperand);
    }

    @Override
    public final String display(final String message) {
       return message.concat(":: From Calculator");
    }

    @Override
    public final void compWait(final long seconds) {
        final long time0 = System.currentTimeMillis();
        long time1 = -1;
        while (time1 < (seconds * 1000)) {
            time1 = System.currentTimeMillis() - time0;
        }
    }

    // IConnections Interface
    @Override
    public final boolean connect(final IUnknown pSinkIntf, final String riid, final long provConnID) {
        if (riid.equalsIgnoreCase("IAdd")) {
            return mpSRIAdd.connectToRecp(pSinkIntf, riid, provConnID);
	} else if (riid.equalsIgnoreCase("ISubtract")) {
            return mpSRISubtract.connectToRecp(pSinkIntf, riid, provConnID);
	}
	return false;
    }

    @Override
    public final boolean disconnect(final String riid, final long connID) {
	if (riid.equalsIgnoreCase("IAdd")) {
            return mpSRIAdd.disconnectFromRecp(connID);
	} else if (riid.equalsIgnoreCase("ISubtract")) {
            return mpSRISubtract.disconnectFromRecp(connID);
	}
	return false;
    }

    // ILifeCycle Interface
    @Override
    public final boolean shutdown() {
        return true;
    }

    @Override
    public final boolean startup(final Object pIOCM) {
        return true;
    }

}