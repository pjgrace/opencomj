/*
 * NameType.java
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
package uk.ac.aston.opencomj;

/**
 * TypedAttribute stores a Java object representing an attribute value
 * with its explicit type information. It can be reused across name-value
 * pair implementations. However, it is fundamental in the implementation
 * of the OpenCOM IMetaInterface meta-model.
 *
 * @author  Paul Grace
 * @version 1.2.3
 */
public class TypedAttribute {

    /**
     * Java class description as a String describing the type of an attribute.
     */
    private final transient String type;

    /**
     * Getter for the type field.
     * @return The attribute type string.
     */
    public final String getType() {
        return this.type;
    }

    /**
     * Java Object holding the attribute value.
     */
    private final transient Object value;

    /**
     * Getter for the attribute value field.
     * @return The value object.
     */
    public final Object getValue() {
        return this.value;
    }

    /**
     * Constructor creates a new instance of TypedAttribute.
     * @param attrType The String description of the Java class of the attribute object.
     * @param attrVal The Java object representing the value of the attribute.
     */
    public TypedAttribute(final String attrType, final Object attrVal) {
        this.type = attrType;
        this.value = attrVal;
    }
}
