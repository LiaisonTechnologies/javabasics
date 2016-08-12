/**
 * Copyright 2015 Liaison Technologies, Inc.
 * This software is the confidential and proprietary information of
 * Liaison Technologies, Inc. ("Confidential Information").  You shall
 * not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Liaison Technologies.
 */
package com.liaison.javabasics.commons;

import org.slf4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

/**
 * TODO
 * @author Branden Smith; Liaison Technologies, Inc.
 */
public final class Util extends Uninstantiable {
    
    /**
     * TODO
     */
    public static final String INDENT = "    ";
    
    /**
     * TODO
     * @param ref
     * @return
     */
    public static int hashCode(final Object ref) {
        return (ref == null)?0:ref.hashCode();
    }
    
    /**
     * TODO
     * @param ref1
     * @param ref2
     * @param equals
     * @return
     */
    public static <T> boolean refEquals(final T ref1, final T ref2, final BiPredicate<? super T, ? super T> equals) {
        return (((ref1 == null) && (ref2 == null))
                ||
                ((ref1 != null) && (equals.test(ref1, ref2))));
    }
    
    /**
     * TODO
     * @param ref1
     * @param ref2
     * @return
     */
    public static boolean refEquals(final Object ref1, final Object ref2) {
        return refEquals(ref1,
                         ref2,
                         ((Object inner1, Object inner2) ->
                             {return inner1.equals(inner2);}));
    }
    
    /**
     * TODO
     * @param ref1
     * @param ref2
     * @return
     */
    public static boolean refEquals(final byte[] ref1, final byte[] ref2) {     
        return refEquals(ref1,
                         ref2,
                         ((byte[] inner1, byte[] inner2) ->
                             {return Arrays.equals(inner1, inner2);}));
    }
    
    /**
     * TODO
     * @param str
     * @return
     */
    public static String simplify(String str) {
        if (str != null) {
            str = str.trim();
            if (str.length() <= 0) {
                str = null;
            }
        }
        return str;
    }
    
    /**
     * TODO
     * @param ref
     * @param closureName
     * @param varName
     * @param varType
     * @throws IllegalArgumentException
     */
    public static void ensureNotNull(final Object ref, final String closureName, final String varName, final Class<?> varType) throws IllegalArgumentException {
        if (ref == null) {
            throw new IllegalArgumentException(closureName
                                               + " requires non-null "
                                               + ((varType != null)?(varType.getSimpleName() + " "):"")
                                               + varName);
        }
    }
    
    /**
     * TODO
     * @param ref
     * @param enclosingType
     * @param varName
     * @param varType
     * @throws IllegalArgumentException
     */
    public static void ensureNotNull(final Object ref, final Class<?> enclosingType, final String varName, final Class<?> varType) throws IllegalArgumentException {
        ensureNotNull(ref, enclosingType.getSimpleName(), varName, varType);
    }
    
    /**
     * TODO
     * @param ref
     * @param enclosingType
     * @param varName
     * @throws IllegalArgumentException
     */
    public static void ensureNotNull(final Object ref, final Class<?> enclosingType, final String varName) throws IllegalArgumentException {
        ensureNotNull(ref, enclosingType, varName, null);
    }
    
    /**
     * TODO
     * @param ref
     * @param enclosingInstance
     * @param varName
     * @param varType
     * @throws IllegalArgumentException
     */
    public static void ensureNotNull(final Object ref, final Object enclosingInstance, final String varName, final Class<?> varType) throws IllegalArgumentException {
        ensureNotNull(ref, enclosingInstance.getClass(), varName, varType);
    }
    
    /**
     * TODO
     * @param ref
     * @param enclosingInstance
     * @param varName
     * @throws IllegalArgumentException
     */
    public static void ensureNotNull(final Object ref, final Object enclosingInstance, final String varName) throws IllegalArgumentException {
        ensureNotNull(ref, enclosingInstance.getClass(), varName);
    }
    
    /**
     * Equivalent to {@link #verifyState(Enum, Enum)}, except that the comparison is wrapped in a
     * <code>synchronized</code> block which locks on the given object.
     * @param expectedState Expected value (nullable) for the given state
     * @param currentState Actual/current value (nullable) for the given state
     * @param stateLock Object (non-null) on which to synchronize/lock for the duration of the
     *                  comparison
     * @throws IllegalStateException if <code>expectedState != currentState</code>
     * @throws NullPointerException if <code>stateLock</code> is <code>null</code>
     */
    public static <E extends Enum<E>> void verifyState(final E expectedState, final E currentState, final Object stateLock) throws IllegalStateException, NullPointerException {
        synchronized(stateLock) {
            verifyState(expectedState, currentState);
        }
    }
    
    /**
     * Convenience method which ensures that a given object representing some kind of state
     * (implemented as an object of a particular Enum type) matches a defined, "expected" state. If
     * the current state does not match the expected value, an {@link IllegalStateException} is
     * thrown with a message indicating the expected and actual values of the state.
     * <br /><br />
     * If it is necessary to lock on a given object during the state comparison, use
     * {@link #verifyState(Enum, Enum, Object)} instead.
     * @param expectedState Expected value (nullable) for the given state
     * @param currentState Actual/current value (nullable) for the given state
     * @throws IllegalStateException if <code>expectedState != currentState</code>
     */
    public static <E extends Enum<E>> void verifyState(final E expectedState, final E currentState) throws IllegalStateException {
        if (expectedState != currentState) {
            throw new IllegalStateException("Expected state: " + expectedState
                                            + "; current state: " + currentState);
        }
    }
    
    /**
     * TODO
     * @param log
     * @param logMethodName
     * @param logMsg
     * @param exc
     */
    public static void traceLog(final Logger log, final String logMethodName, String logMsg, final Throwable exc) {
        // >>>>> LOG >>>>>
        if (log.isTraceEnabled()) {
            logMsg = "[" + logMethodName + "] " + logMsg;
            if (exc != null) {
                log.trace(logMsg, exc);
            } else {
                log.trace(logMsg);
            }
        } else if (log.isDebugEnabled()) {
            if (exc != null) {
                log.debug(logMsg, exc);
            } else {
                log.debug(logMsg);
            }
        }
        // <<<<< log <<<<<
    }
    
    /**
     * TODO
     * @param log
     * @param logMethodName
     * @param logMsg
     */
    public static void traceLog(final Logger log, final String logMethodName, String logMsg) {
        traceLog(log, logMethodName, logMsg, null);
    }
    
    /**
     * TODO
     * @param param
     * @param enclosingInstance
     * @param paramName
     * @param paramClass
     * @param valueSetTarget
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     */
    public static <X> X validateExactlyOnceParam(final X param, final Object enclosingInstance, final String paramName, final Class<X> paramClass, final Object valueSetTarget) throws IllegalArgumentException, IllegalStateException {
        ensureNotNull(param, enclosingInstance, paramName, paramClass);
        validateExactlyOnce(paramName, paramClass, valueSetTarget);
        return param;
    }
    
    /**
     * TODO
     * @param entityName
     * @param entityClass
     * @param valueSetTarget
     * @throws IllegalStateException
     */
    public static void validateExactlyOnce(final String entityName, final Class<?> entityClass, final Object valueSetTarget) throws IllegalStateException {
        if (valueSetTarget != null) {
            throw new IllegalStateException(entityClass.getSimpleName()
                                            + " reference for "
                                            + entityName
                                            + " may only be set once, and is already initialized: "
                                            + valueSetTarget);
        }
    }
    
    /**
     * TODO
     * @param strGen
     * @param indentCount
     * @throws IllegalStateException
     */
    public static void indent(final Appendable strGen, final int indentCount) throws IllegalStateException {
        for (int counter = 0; counter < indentCount; counter++) {
            try {
                strGen.append(INDENT);
            } catch (IOException ioExc) {
                throw new IllegalStateException("Failed adding indentation ("
                                                + (counter + 1)
                                                + " of "
                                                + indentCount
                                                + ")");
            }
        }
    }
    
    /**
     * TODO
     * @param strGen
     * @param indentCount
     * @param objListForLine
     * @throws IllegalStateException
     */
    public static void appendIndented(final Appendable strGen, final int indentCount, final Object... objListForLine) throws IllegalStateException {
        if (objListForLine != null) {
            if (indentCount > 0) {
                indent(strGen, indentCount);
            }
            for (Object obj : objListForLine) {
                if (obj != null) {
                    try {
                        strGen.append(obj.toString());
                    } catch (IOException ioExc) {
                        throw new IllegalStateException("Failed to append (" + obj + ")");
                    }
                }
            }
        }
    }
    
    /**
     * TODO
     * @param strGen
     * @param objListForLine
     * @throws IllegalStateException
     */
    public static void append(final Appendable strGen, final Object... objListForLine) throws IllegalStateException {
        appendIndented(strGen, -1, objListForLine);
    }

    /**
     *
     * @param map
     * @param key
     * @param addendum
     * @param updater
     * @param maker
     * @param <K>
     * @param <A>
     * @param <V>
     */
    public static <K, A, V> void appendToValueInMap(final Map<K, V> map, final K key, final A addendum, final BiConsumer<V, A> updater, final Supplier<V> maker) {
        V valueContainer;
        final V existingValueContainer;

        valueContainer = map.get(key);
        if (valueContainer == null) {
            valueContainer = maker.get();
            existingValueContainer = map.putIfAbsent(key, valueContainer);
            if (existingValueContainer != null) {
                valueContainer = existingValueContainer;
            }
        }
        updater.accept(valueContainer, addendum);
    }

    /**
     * TODO
     * @param key
     * @param value
     * @param map
     * @param <K>
     * @param <V>
     */
    public static <K, V> void putToLinkedListInMap(final Map<K, List<V>> map, final K key, final V value) {
        appendToValueInMap(map, key, value, List::add, LinkedList::new);
    }

    private Util() {}
}
