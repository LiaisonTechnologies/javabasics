/**
 * Copyright 2015 Liaison Technologies, Inc.
 * This software is the confidential and proprietary information of
 * Liaison Technologies, Inc. ("Confidential Information").  You shall
 * not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Liaison Technologies.
 */
package com.liaison.javabasics.commons;

/**
 * Utility class which enforces non-instantiability for classes which are intended only to exist
 * as static entities.
 * @author Branden Smith; Liaison Technologies, Inc.
 */
public abstract class Uninstantiable {
    protected Uninstantiable() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not instantiable: " + getClass().getName());
    }
}
