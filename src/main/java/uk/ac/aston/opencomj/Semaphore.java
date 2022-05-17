 /**
 * OpenCOMJ is a flexible component model for reconfigurable reflection developed at Lancaster University.
 * Copyright (C) 2015 Paul Grace
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program; if not,
 * write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package uk.ac.aston.opencomj;

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
