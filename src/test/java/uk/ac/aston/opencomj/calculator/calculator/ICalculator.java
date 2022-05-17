/*
 * ICalculator.java
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

package uk.ac.aston.opencomj.calculator.calculator;

import uk.ac.aston.opencomj.IUnknown;

/**
 * General calculator interface.
 * @author  Paul Grace
 * @version 1.2.3
 */
public interface ICalculator extends IUnknown {
    /**
     * Add two integers together.
     * @param xOperand Operand X.
     * @param yOperand Operand Y.
     * @return The added values.
     */
    int add(int xOperand, int yOperand);

    /**
     * Subtract operand y from x.
     * @param xOperand Operand X.
     * @param yOperand Operand Y.
     * @return The result of the subtraction.
     */
    int subtract(int xOperand, int yOperand);

    /**
     * Concatenate a display message to the passed message.
     * @param message The message to attach to.
     * @return The concatenated string.
     */
    String display(String message);

    /**
     * Time passing function. Simply operates in the component
     * doing nothing for the specified time period then
     * returns.
     * @param seconds The time to compWait for.
     */
    void compWait(long seconds);
}