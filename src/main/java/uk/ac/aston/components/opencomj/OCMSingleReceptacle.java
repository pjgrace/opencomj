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

import java.util.HashMap;
import java.util.Map;

/**
 * OpenCOM defines a Single Receptacle to be " a single pointer to an
 * interface". For this purpose, the OCM_Single_Receptacle object holds the object
 * reference on which the interface resides. Allowing the connection to be cast
 * across the correct interface type.
 *
 * @param <InterfaceType> The interface type of the receptacle.
 *
 * @author  Paul Grace
 * @version 1.2.3
 *
 */
public class OCMSingleReceptacle<InterfaceType> implements IReceptacle {

    /**
     * The pointer to the reference to the connected interface.
     */
    private transient InterfaceType mpIntf;

    /**
     * Get the interface reference of the receptacle.
     * @return The interface reference.
     */
    public final InterfaceType getInterface() {
        return mpIntf;
    }

    /**
     * The id of the connection of the receptacle to interface.
     */
    private transient long mConnID;

    /**
     * The set of meta data attached to the receptacle.
     */
    private final transient Map<String, TypedAttribute> metaData;

    /**
     * Creates a new instance of the OCM_SingleReceptacle class. Typically this is only
     * performed within the constructor of OpenCOM components.
     */
    public OCMSingleReceptacle() {
        mpIntf = null;
        mConnID = -1;
        metaData = new HashMap();
    }

    //! Implementation of IReceptacle interface
    ////////////////////////////////////////////////////////////////////////////////
    //! This method stores the reference to the component hosting the interface
    //!
    @Override
    public final boolean connectToRecp(final IUnknown pIUnkSink, final String riid, final long provConnID) {

	if (mpIntf == null) {
            try {
                mpIntf = (InterfaceType) pIUnkSink.queryInterface(riid);
            } catch (ClassCastException except) {
                return false;
            }
            mConnID = provConnID;
            return true;
	} else {
            return false;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    //! This method destroys the existing connection data
    //!
    @Override
    public final boolean disconnectFromRecp(final long connID) {
        if (mConnID != connID) {
            return false;
        }

	if (mpIntf == null) {
            return false;
	} else {
            mConnID = -1;
            mpIntf = null;
            return false;
        }

    }

   /**
     * This method adds meta-data name-value pair attributes to the receptacle instance.
     * @param name The attribute name.
     * @param type The attribute name.
     * @param value An Object holding the attribute value.
     * @return A boolean describing if the pair was added or not.
     */
    @Override
    public final boolean putData(final String name, final String type, final Object value) {
        try {
            metaData.put(name, new TypedAttribute(type, value));
        } catch (NullPointerException n) {
            return false;
        }
        return true;
    }

    /**
     * This method gets the value of a named meta-data attribute.
     * @param name The attribute name.
     * @return The TypedAttribute object storing the value.
     */
    @Override
    public final TypedAttribute getValue(final String name) {
        return  (TypedAttribute) metaData.get(name);
    }

    /**
     * This method returns all name-value meta-data pairs on this receptacle instance.
     * @return A Hashtable storing the pairs.
     */
    @Override
    public final Map<String, TypedAttribute> getValues() {
        return metaData;
    }

}


