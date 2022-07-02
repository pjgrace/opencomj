/////////////////////////////////////////////////////////////////////////
//
// © Aston University 2022
//
// This software may not be used, sold, licensed, transferred, copied
// or reproduced in whole or in part in any manner or form or in or
// on any media by any person other than in accordance with the terms
// of the Licence Agreement supplied with the software, or otherwise
// without the prior written consent of the copyright owners.
//
// This software is distributed WITHOUT ANY WARRANTY, without even the
// implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
// PURPOSE, except where stated in the Licence Agreement supplied with
// the software.
//
/////////////////////////////////////////////////////////////////////////
//
//  License : GNU Lesser General Public License, version 3
//
/////////////////////////////////////////////////////////////////////////
package uk.ac.aston.components.opencomj;

/**
 * Implementation of a semaphore object or use in frameworks readers/writers lock.
 * @author pjg
 */

public  class Semaphore {

    /**
     * Counter of number of read accesses on the semaphore.
     */
    private transient int counter;

    /**
     * Construct the semaphore.
     */
    public Semaphore() {
        this(0);
    }

    /**
     * Construct the semaphore with a number of readers given.
     * @param iCount The number of readers
     */
    public Semaphore(final int iCount) {
        if (iCount < 0) {
            throw new IllegalArgumentException(iCount + " < 0");
        }
        counter = iCount;
    }

    /**
     * Increments internal counter, possibly awakening a thread
     * wait()ing in acquire().
     */
    public final void release() {
        synchronized (this) {
            if (counter == 0) {
                this.notify();
            }
            counter++;
        }
    }

    /**
     * Decrements internal counter, blocking if the counter is already
     * zero.
     *
     * @exception InterruptedException passed from this.wait().
     */
    public final void acquire() throws InterruptedException {
        synchronized (this) {
            while (counter == 0) {
                this.wait();
            }
            counter--;
        }
    }
}
