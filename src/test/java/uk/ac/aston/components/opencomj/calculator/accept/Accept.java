/*
 * Accept.java
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
package uk.ac.aston.components.opencomj.calculator.accept;

import java.util.ArrayList;
import java.util.List;
import uk.ac.aston.components.opencomj.ExposedInterface;
import uk.ac.aston.components.opencomj.IAccept;
import uk.ac.aston.components.opencomj.IConnections;
import uk.ac.aston.components.opencomj.ILifeCycle;
import uk.ac.aston.components.opencomj.IMetaArchitecture;
import uk.ac.aston.components.opencomj.IMetaInterface;
import uk.ac.aston.components.opencomj.IUnknown;
import uk.ac.aston.components.opencomj.OCMRecpMetaInfo;
import uk.ac.aston.components.opencomj.OCMSingleReceptacle;
import uk.ac.aston.components.opencomj.AbstractOpenCOMComponent;
import uk.ac.aston.components.opencomj.OpenComConstants;

/**
 * This component implements a simple checking mechanism for the calculator framework.
 * i.e. Both receptacles on the internal calculator must be connected.
 * @see OpenCOM.IAccept
 * @author  Paul Grace
 * @version 1.2.3
 */
public class Accept extends AbstractOpenCOMComponent implements IUnknown, IAccept, IMetaInterface, ILifeCycle, IConnections {

    /**
     * The connection to the calculator framework IMetaArchitecture.
     */
    private final transient OCMSingleReceptacle<IMetaArchitecture> mSRIMetaArc;

    /**
     * Connection to the IMetaInterface of the calculator framework.
     */
    private final transient OCMSingleReceptacle<IMetaInterface> mSRIMetaInterface;

    /**
     * Creates a new instance of Accept component.
     * @param pRuntime Reference to the opencom kernel.
     */
    public Accept(final IUnknown pRuntime) {
        super(pRuntime);
        mSRIMetaArc = new OCMSingleReceptacle();
        mSRIMetaInterface = new OCMSingleReceptacle();
        mSRIOpenCOM.getInterface().connect(this, pRuntime, "OpenCOM.IMetaArchitecture");
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

    // Programmatic check
    /**
     * All validation components implement the OpenCOM.IAccept interface which
     * has the following method. The current CF graph information comes in the
     * parameters, and hence you can use it to check validity.
     * @param intfs The set of exposed interfaces of the framework.
     * @param graph The framework graph structure.
     * @return indication if the framework implementation is valid.
     */
    @Override
    public final boolean isValid(final List<IUnknown> graph, final List<ExposedInterface> intfs) {
        final List<Long> pConnsIDS =  new ArrayList();
        if (graph.size() == 3) {
            for (IUnknown graph1 : graph) {
                final IUnknown component = (IUnknown) graph1;
                final String clsid =  mSRIOpenCOM.getInterface().getComponentType(component);
                if (clsid.equalsIgnoreCase("CalculatorComponent.Calculator")) {

                    final long connID = mSRIOpenCOM.getInterface().connect(this, component, OpenComConstants.METAINTERFACE);
                    final List<OCMRecpMetaInfo> ppRecpMetaInfo = new ArrayList();
                    final int recps = mSRIMetaInterface.getInterface().enumRecps(ppRecpMetaInfo);
                    mSRIOpenCOM.getInterface().disconnect(connID);

                    // Check there are two connections - one from each receptacle
                    for (int index2 = 0; index2 < recps; index2++) {
                        final int recpsConnCount = mSRIMetaArc.getInterface().enumConnsFromRecp(component, ppRecpMetaInfo.get(index2).getInterfaceType(), pConnsIDS);
                        if (recpsConnCount < 1) {
                            return false;
                        }
                    }
                }
            }
            return true;
        } else if (graph.isEmpty()) {
            return intfs.isEmpty();
        }
        return false;
    }

    // IConnections Interface
    @Override
    public final boolean connect(final IUnknown pSinkIntf, final String riid, final long provConnID) {
        if (riid.equalsIgnoreCase("OpenCOM.IMetaArchitecture")) {
		return mSRIMetaArc.connectToRecp(pSinkIntf, riid, provConnID);
	} else if (riid.equalsIgnoreCase(OpenComConstants.METAINTERFACE)) {
		return mSRIMetaInterface.connectToRecp(pSinkIntf, riid, provConnID);
	}
	return false;
    }

    @Override
    public final boolean disconnect(final String riid, final long connID) {

	if (riid.equalsIgnoreCase("OpenCOM.IMetaArchitecture")) {
		return mSRIMetaArc.disconnectFromRecp(connID);
	} else if (riid.equalsIgnoreCase(OpenComConstants.METAINTERFACE)) {
		return mSRIMetaInterface.disconnectFromRecp(connID);
	}

	return false;
    }

}