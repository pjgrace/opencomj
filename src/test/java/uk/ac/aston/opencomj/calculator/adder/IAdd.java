/*
 * IAdd.java
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

package uk.ac.aston.opencomj.calculator.adder;

import uk.ac.aston.opencomj.privacy.PrivacyFunction;
import uk.ac.aston.opencomj.privacy.Private;
import uk.ac.aston.opencomj.privacy.Purpose;

/**
 * Interface containing addition operations.
 * @author  Paul Grace
 * @version 1.2.3
 */
@PrivacyFunction
public interface IAdd {
    /**
     * Add two integers together x+y.
     * @param leftOperand Operand X.
     * @param rightOperand Operand Y.
     * @return The added values.
     */
    @Purpose(description = "optimization")
    int add(int leftOperand, @Private(action="read", id="age") int rightOperand);
}
