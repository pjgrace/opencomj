/*
 * TestProgram.java
 *
 * Created on 26 July 2004, 11:46
 */

package uk.ac.aston.opencomj.sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.aston.opencomj.ConnectedComponent;
import uk.ac.aston.opencomj.ICFMetaInterface;
import uk.ac.aston.opencomj.ILifeCycle;
import uk.ac.aston.opencomj.IOpenCOM;
import uk.ac.aston.opencomj.IUnknown;
import uk.ac.aston.opencomj.InvalidComponentTypeException;
import uk.ac.aston.opencomj.OCMConnInfo;
import uk.ac.aston.opencomj.OpenCOM;
import uk.ac.aston.opencomj.calculator.calculator.ICalculator;

/**
 * Performs some simple framework tests including valid configuration checks and locking.
 * @author  Paul Grace
 * @version 1.2.3
 */
public final class FrameworkTestProgram {

    /**
     * The internal reference to the framework's exposed interface.
     */
    private static ICalculator pICalc = null;

    /**
     * Creates a new instance of TestProgram.
     */
    private FrameworkTestProgram() {
    }

    /**
     * Testing thread for waiting call within a framework.
     */
    static class NewThread extends Thread {

        /**
         * The blocking time.
         */
        private final transient long time;

        /**
         * Thread constructor.
         * @param waitTime Time to wait for blocking.
         */
        public NewThread(final long waitTime) {
            super();
            time = waitTime;
        }

        @Override
        public void run() {
            try {
                System.out.println("Started blocked execution inside Framework (Cannot reconfigure until end)");
                pICalc.compWait(time);
                System.out.println("Ended blocked execution inside Framework");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) {

        // Create the OpenCOM runtime & Get the IOpenCOM interface reference
        final OpenCOM runtime = new OpenCOM();
        final IOpenCOM pIOCM =  (IOpenCOM) runtime.queryInterface("IOpenCOM");

        // Create the CF component
        IUnknown pCFIUnk = null;
        try {
            pCFIUnk = (IUnknown) pIOCM.createInstance("uk.ac.aston.opencomj.calculator.framework.CalculatorFramework", "Framework");
            ILifeCycle pILife =  (ILifeCycle) pCFIUnk.queryInterface("ILifeCycle");
            pILife.startup(pIOCM);
        } catch (InvalidComponentTypeException ex) {
            System.err.println("Cannot create component - check class location " + ex.getMessage());
        }

        IUnknown pAcceptIUnk = null;
        try {
            pAcceptIUnk = (IUnknown) pIOCM.createInstance("uk.ac.aston.opencomj.calculator.accept.Accept", "Accept");
            ILifeCycle pILife =  (ILifeCycle) pAcceptIUnk.queryInterface("ILifeCycle");
            pILife.startup(pIOCM);
        } catch (InvalidComponentTypeException ex) {
            System.err.println("Cannot create component - check class location " + ex.getMessage());
        }

        pIOCM.connect(pCFIUnk, pAcceptIUnk, "IAccept");

        final ICFMetaInterface pCF = (ICFMetaInterface) pCFIUnk.queryInterface("ICFMetaInterface");

        // Try an invalid configuration
        pCF.initArchTransaction();
        IUnknown pAdder = null;
        try {
            pAdder = pCF.createComponent("uk.ac.aston.opencomj.calculator.adder.Adder", "Adder");
        } catch (InvalidComponentTypeException ex) {
            System.err.println("Cannot create component - check class location " + ex.getMessage());
        }
        IUnknown pCal = null;
        try {
            pCal = pCF.createComponent("uk.ac.aston.opencomj.calculator.calculator.Calculator", "Calculator");
        } catch (InvalidComponentTypeException ex) {
            System.err.println("Cannot create component - check class location " + ex.getMessage());
        }

        // Connect the local components
        pCF.localBind(pCal, pAdder, "IAdd");
        pCF.exposeInterface("ICalculator", pCal);

        if (!pCF.commitArchTransaction()) {
            System.out.println("First confiugration is an Invalid configuration");
            System.out.println();
        }

        pCF.initArchTransaction();
        try {
            pAdder = pCF.createComponent("uk.ac.aston.opencomj.calculator.adder.Adder", "Adder");
        } catch (InvalidComponentTypeException ex) {
            Logger.getLogger(FrameworkTestProgram.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            pCal = pCF.createComponent("uk.ac.aston.opencomj.calculator.calculator.Calculator", "Calculator");
        } catch (InvalidComponentTypeException ex) {
            Logger.getLogger(FrameworkTestProgram.class.getName()).log(Level.SEVERE, null, ex);
        }
        IUnknown pSub = null;
        try {
            pSub = pCF.createComponent("uk.ac.aston.opencomj.calculator.subtract.Subtract", "Subtract");
        } catch (InvalidComponentTypeException ex) {
            Logger.getLogger(FrameworkTestProgram.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Connect the local components
        pCF.localBind(pCal, pAdder, "IAdd");
        pCF.localBind(pCal, pSub, "ISubtract");
        pCF.exposeInterface("ICalculator", pCal);

        if (!pCF.commitArchTransaction()) {
            System.out.println("Invalid configuration");
        }

        final List<IUnknown> ppComps = pCF.getInternalComponents();
        System.out.println("The number of components is : " + ppComps.size());
        for (int i = 0; i < ppComps.size(); i++) {
             System.out.println("Component " + i + " is " + pIOCM.getComponentName((IUnknown) ppComps.get(i)));
        }
        System.out.println();

        pICalc = (ICalculator) pCFIUnk.queryInterface("ICalculator");
         // Lets test the Add and Subtract component
        System.out.println("The value of 18+19 = " + pICalc.add(18, 19));
        System.out.println("The value of 63-16 = " + pICalc.subtract(63, 16));
        System.out.println();

        final List<ConnectedComponent> ppConnections = new ArrayList();
        final int val = pCF.getBoundComponents(pCal, ppConnections);
        System.out.println("There are " + val + " components bound to the calculator");
        for (int i = 0; i < val; i++) {
            final OCMConnInfo tempConnInfo = pIOCM.getConnectionInfo(ppConnections.get(i).getConnection());
                System.out.println("Component " + pIOCM.getComponentName(tempConnInfo.getSink()) + " is connected to "
                        + pIOCM.getComponentName(tempConnInfo.getSource()) + " on interface " + tempConnInfo.getInterfaceType());
        }
        System.out.println();
	/////////////////////////////////////////////////////////////////////////////
	// Method - getInternalBindings
	// Description - Returns a list with the ids of all bindings that are
	// part of the base-level composition
	/////////////////////////////////////////////////////////////////////////////
        final List<Long> ppConnIDs = new ArrayList();
	final int val2 = pCF.getInternalBindings(ppConnIDs);
        System.out.println("There are " + val2 + " internal connection in the calculator framework");
	for (int index = 0; index < val2; index++) {
                final OCMConnInfo tempConnInfo = pIOCM.getConnectionInfo(ppConnIDs.get(index).longValue());
                System.out.println("Component " + pIOCM.getComponentName(tempConnInfo.getSink()) + " is connected to "
                        + pIOCM.getComponentName(tempConnInfo.getSource()) + " on interface " + tempConnInfo.getInterfaceType());
        }
        System.out.println();

	/////////////////////////////////////////////////////////////////////////////
	// Method - getExposedInterfaces
	// Description - Returns a list with the interface ids of all exposed
	// interfaces
	/////////////////////////////////////////////////////////////////////////////
        final List<String> ppIntfs = new ArrayList();
	final int val3 = pCF.getExposedInterfaces(ppIntfs);
        System.out.println("There are " + val3 + " exposed Interfaces:");
        for (int i = 0; i < val3; i++) {
            System.out.println(ppIntfs.get(i) + " is exposed");
        }
        System.out.println();

        // Test the locking mechanism
        final NewThread calcWait = new NewThread(10);
        calcWait.start();

        System.out.println("Testing the framework lock....Press the enter key to continue");
        final InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        final BufferedReader stdin = new BufferedReader(inputStreamReader);
        try {
            stdin.readLine();
        } catch (Exception e) {
        }
        pCF.initArchTransaction();
        System.out.println("Got lock");
        pCF.unexposeAllInterfaces();
        pCF.deleteComponent(pAdder);
        pCF.deleteComponent(pSub);
        pCF.deleteComponent(pCal);
        pCF.commitArchTransaction();

    }
}
