/*
 * Copyright © 2016 Liaison Technologies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
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
     * @param ref TODO
     * @return TODO
     */
    public static int hashCode(final Object ref) {
        return (ref == null)?0:ref.hashCode();
    }

    /**
     * TODO
     * @param ref1 TODO
     * @param ref2 TODO
     * @param equals TODO
     * @param <T> TODO
     * @return TODO
     */
    public static <T> boolean refEquals(final T ref1, final T ref2, final BiPredicate<? super T, ? super T> equals) {
        return (((ref1 == null) && (ref2 == null))
                ||
                ((ref1 != null) && (equals.test(ref1, ref2))));
    }
    
    /**
     * TODO
     * @param ref1 TODO
     * @param ref2 TODO
     * @return TODO
     */
    public static boolean refEquals(final Object ref1, final Object ref2) {
        return refEquals(ref1,
                         ref2,
                         ((Object inner1, Object inner2) ->
                             {return inner1.equals(inner2);}));
    }
    
    /**
     * TODO
     * @param ref1 TODO
     * @param ref2 TODO
     * @return TODO
     */
    public static boolean refEquals(final byte[] ref1, final byte[] ref2) {     
        return refEquals(ref1,
                         ref2,
                         ((byte[] inner1, byte[] inner2) ->
                             {return Arrays.equals(inner1, inner2);}));
    }
    
    /**
     * TODO
     * @param str TODO
     * @return TODO
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
     * @param ref TODO
     * @param closureName TODO
     * @param varName TODO
     * @param varType TODO
     * @throws IllegalArgumentException TODO
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
     * @param ref TODO
     * @param enclosingType TODO
     * @param varName TODO
     * @param varType TODO
     * @throws IllegalArgumentException TODO
     */
    public static void ensureNotNull(final Object ref, final Class<?> enclosingType, final String varName, final Class<?> varType) throws IllegalArgumentException {
        ensureNotNull(ref, enclosingType.getSimpleName(), varName, varType);
    }
    
    /**
     * TODO
     * @param ref TODO
     * @param enclosingType TODO
     * @param varName TODO
     * @throws IllegalArgumentException TODO
     */
    public static void ensureNotNull(final Object ref, final Class<?> enclosingType, final String varName) throws IllegalArgumentException {
        ensureNotNull(ref, enclosingType, varName, null);
    }
    
    /**
     * TODO
     * @param ref TODO
     * @param enclosingInstance TODO
     * @param varName TODO
     * @param varType TODO
     * @throws IllegalArgumentException TODO
     */
    public static void ensureNotNull(final Object ref, final Object enclosingInstance, final String varName, final Class<?> varType) throws IllegalArgumentException {
        ensureNotNull(ref, enclosingInstance.getClass(), varName, varType);
    }
    
    /**
     * TODO
     * @param ref TODO
     * @param enclosingInstance TODO
     * @param varName TODO
     * @throws IllegalArgumentException TODO
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

    /**
     * Equivalent to {@link #verifyState(Enum, Enum)}, except that the comparison is wrapped in a
     * <code>synchronized</code> block which locks on the given object.
     * @param expectedState Expected value (nullable) for the given state
     * @param currentState Actual/current value (nullable) for the given state
     * @param stateLock Object (non-null) on which to synchronize/lock for the duration of the
     *                  comparison
     * @param <E> TODO
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
     * <br><br>
     * If it is necessary to lock on a given object during the state comparison, use
     * {@link #verifyState(Enum, Enum, Object)} instead.
     * @param expectedState Expected value (nullable) for the given state
     * @param currentState Actual/current value (nullable) for the given state
     * @param <E> TODO
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
     * @param log TODO
     * @param logMethodName TODO
     * @param logMsg TODO
     * @param exc TODO
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
     * @param log TODO
     * @param logMethodName TODO
     * @param logMsg TODO
     */
    public static void traceLog(final Logger log, final String logMethodName, String logMsg) {
        traceLog(log, logMethodName, logMsg, null);
    }

    /**
     * TODO
     * @param param TODO
     * @param enclosingInstance TODO
     * @param paramName TODO
     * @param paramClass TODO
     * @param valueSetTarget TODO
     * @param <X> TODO
     * @return TODO
     * @throws IllegalArgumentException TODO
     * @throws IllegalStateException TODO
     */
    public static <X> X validateExactlyOnceParam(final X param, final Object enclosingInstance, final String paramName, final Class<X> paramClass, final Object valueSetTarget) throws IllegalArgumentException, IllegalStateException {
        ensureNotNull(param, enclosingInstance, paramName, paramClass);
        validateExactlyOnce(paramName, paramClass, valueSetTarget);
        return param;
    }
    
    /**
     * TODO
     * @param entityName TODO
     * @param entityClass TODO
     * @param valueSetTarget TODO
     * @throws IllegalStateException TODO
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
     * @param strGen TODO
     * @param indentCount TODO
     * @throws IllegalStateException TODO
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
     * @param strGen TODO
     * @param indentCount TODO
     * @param objListForLine TODO
     * @throws IllegalStateException TODO
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
     * @param strGen TODO
     * @param objListForLine TODO
     * @throws IllegalStateException TODO
     */
    public static void append(final Appendable strGen, final Object... objListForLine) throws IllegalStateException {
        appendIndented(strGen, -1, objListForLine);
    }

    /**
     *
     * @param map TODO
     * @param key TODO
     * @param addendum TODO
     * @param updater TODO
     * @param maker TODO
     * @param <K> TODO
     * @param <A> TODO
     * @param <V> TODO
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
     * @param key TODO
     * @param value TODO
     * @param map TODO
     * @param <K> TODO
     * @param <V> TODO
     */
    public static <K, V> void putToLinkedListInMap(final Map<K, List<V>> map, final K key, final V value) {
        appendToValueInMap(map, key, value, List::add, LinkedList::new);
    }

    private Util() {}
}
