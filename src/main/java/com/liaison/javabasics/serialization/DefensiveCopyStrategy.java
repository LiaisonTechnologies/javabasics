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
package com.liaison.javabasics.serialization;

import java.util.EnumSet;

/**
 * Defines the points in program flow when a defensive copy must be made on a mutable field.
 * @author Branden Smith; Liaison Technologies, Inc.
 */
public enum DefensiveCopyStrategy {
    NEVER, GET, SET, ALWAYS;
    
    public static final DefensiveCopyStrategy DEFAULT = ALWAYS;
    
    public static final EnumSet<DefensiveCopyStrategy> COPY_ON_GET = EnumSet.of(GET, ALWAYS);
    public static final EnumSet<DefensiveCopyStrategy> COPY_ON_SET = EnumSet.of(SET, ALWAYS);
}
