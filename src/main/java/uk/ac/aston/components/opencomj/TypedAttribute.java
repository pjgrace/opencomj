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
