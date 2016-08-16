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

import java.math.BigInteger;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Branden Smith; Liaison Technologies, Inc.
 * Created 2015.10.13 15:55
 */
public class SemanticVersionComparator implements Comparator<String> {

    private static final int COMPARE_RESULT_GREATER = 1;
    private static final int COMPARE_RESULT_LESSER = -1;

    private static final String REGEX_VERSIONDOT = "[.]";
    private static final String REGEXSTR_NUMERIC = "[0-9]+";
    private static final Pattern REGEX_NUMERIC = Pattern.compile(REGEXSTR_NUMERIC);
    private static final int VERCOMPONENT_MAX_NUMERIC = 100;

    private static int compareVerComponentSubNumber(final Matcher verOneMatcher, final Matcher verTwoMatcher, final int recursionCount) throws IllegalArgumentException {
        String logMsg;
        String verOneNumStr = null;
        String verTwoNumStr = null;
        final BigInteger verOneNum;
        final BigInteger verTwoNum;
        final int subNumCompareResult;

        if (recursionCount > VERCOMPONENT_MAX_NUMERIC) {
            logMsg =
                "Version component had more than maximum allowed ("
                + VERCOMPONENT_MAX_NUMERIC
                + ") subcomponent integers";
            throw new IllegalArgumentException(logMsg);
        }
        if (verOneMatcher.find()) {
            verOneNumStr = Util.simplify(verOneMatcher.group());
        }
        if (verTwoMatcher.find()) {
            verTwoNumStr = Util.simplify(verTwoMatcher.group());
        }

        if ((verOneNumStr == null) && (verTwoNumStr == null)) {
            return 0;
        }
        if (verOneNumStr == null) {
            return COMPARE_RESULT_LESSER;
        }
        if (verTwoNumStr == null) {
            return COMPARE_RESULT_GREATER;
        }

        verOneNum = new BigInteger(verOneNumStr);
        verTwoNum = new BigInteger(verTwoNumStr);
        subNumCompareResult = verOneNum.compareTo(verTwoNum);
        if (subNumCompareResult == 0) {
            return
                compareVerComponentSubNumber(verOneMatcher,
                                             verTwoMatcher,
                                             (recursionCount + 1));
        } else {
            return subNumCompareResult;
        }
    }

    private static int compareVerComponents(String verOneComponent, String verTwoComponent) {
        verOneComponent = Util.simplify(verOneComponent);
        verTwoComponent = Util.simplify(verTwoComponent);
        if ((verOneComponent == null) && (verTwoComponent == null)) {
            return 0;
        }
        if (verOneComponent == null) {
            return COMPARE_RESULT_LESSER;
        }
        if (verTwoComponent == null) {
            return COMPARE_RESULT_GREATER;
        }
        return
            compareVerComponentSubNumber(
                REGEX_NUMERIC.matcher(verOneComponent),
                REGEX_NUMERIC.matcher(verTwoComponent),
                1);
    }

    /**
     * Compare two strings representing version numbers in x.y.z.(...) format. The logic is as
     * follows (let "empty" refer to either null or entirely whitespace):
     * <ol>
     *     <li>If both strings are empty, return 0.</li>
     *     <li>If the first string is empty, return -1.</li>
     *     <li>If the second string is empty, return 1.</li>
     *     <li>Tokenize the each version string by the '.' (dot) character.</li>
     *     <li>For the first dot-separated component of each string:
     *         <ol>
     *             <li>If said component of both strings is empty, return 0.</li>
     *             <li>If said component of the first string is empty, return -1.</li>
     *             <li>If said component of the second string is empty, return 1.</li>
     *             <li>Otherwise, pattern match all of the integer subcomponents of each string,
     *                 and iterate through them, continuing until one of the following conditions
     *                 is met:
     *                 <ul>
     *                     <li>Subcomponent integer from first version string is greater than
     *                         subcomponent integer from second version string (return 1)</li>
     *                     <li>Subcomponent integer from second version string is greater than
     *                         subcomponent integer from first version string (return -1)</li>
     *                     <li>Both lists of subcomponents are exhausted (return 0)</li>
     *                     <li>List of subcomponents from first version string is exhausted, but
     *                         second version string still has numeric subcomponents (return -1)
     *                         </li>
     *                     <li>List of subcomponents from second version string is exhausted, but
     *                         first version string still has numeric subcomponents (return 1)
     *                         </li>
     *                 </ul>
     *             </li>
     *         </ol>
     *     </li>
     * </ol>
     * Note that this method makes no attempt to different version numbers on the basis of
     * non-numeric version string components (e.g. "-SNAPSHOT"), and all such substrings are
     * ignored. Date references are treated as pure integers (e.g. "1.2.3-20140101" is computed to
     * be greater than "1.2.3-201501"). Integer subcomponents are compared as dictated by
     * {@link BigInteger#compareTo(BigInteger)}.
     * @param verOne The first version number to be compared
     * @param verTwo The second version number to be compared
     * @return -1, 0, or 1, as dictated by the logic described above
     */
    @Override
    public int compare(String verOne, String verTwo) {
        final String[] verOneSplit;
        final String[] verTwoSplit;
        int lockStepIter;
        int result;

        result = 0;

        verOne = Util.simplify(verOne);
        verTwo = Util.simplify(verTwo);
        if ((verOne == null) && (verTwo == null)) {
            return 0;
        }
        if (verOne == null) {
            return COMPARE_RESULT_LESSER;
        }
        if (verTwo == null) {
            return COMPARE_RESULT_GREATER;
        }

        verOneSplit = verOne.split(REGEX_VERSIONDOT, -1);
        verTwoSplit = verTwo.split(REGEX_VERSIONDOT, -1);
        lockStepIter = 0;

        while ((result == 0)
               && (lockStepIter < verOneSplit.length)
               && (lockStepIter < verTwoSplit.length)) {
            result = compareVerComponents(verOneSplit[lockStepIter], verTwoSplit[lockStepIter]);
            lockStepIter++;
        }
        if (result == 0) {
            // first version number still has components; second version number is exhausted
            if ((lockStepIter < verOneSplit.length) && (lockStepIter >= verTwoSplit.length)) {
                return COMPARE_RESULT_GREATER;
            }
            // second version number still has components; first version number is exhausted
            if ((lockStepIter >= verOneSplit.length) && (lockStepIter < verTwoSplit.length)) {
                return COMPARE_RESULT_LESSER;
            }
        }
        return result;
    }

    public static void main(final String[] arguments) {
        final SemanticVersionComparator verCompare;
        verCompare = new SemanticVersionComparator();

        System.out.println("0.0.1 == 0.0.1");
        System.out.println(verCompare.compare("0.0.1", "0.0.1"));

        System.out.println("0.0.1 == 0.0.1-SNAPSHOT");
        System.out.println(verCompare.compare("0.0.1", "0.0.1-SNAPSHOT"));

        System.out.println("0.1.1 > 0.0.1");
        System.out.println(verCompare.compare("0.1.1", "0.0.1"));

        System.out.println("0.0.1-20151014 > 0.0.1");
        System.out.println(verCompare.compare("0.0.1-20151014", "0.0.1"));

        System.out.println("0.0.1-20151014 < 0.0.1-20151015");
        System.out.println(verCompare.compare("0.0.1-20151014", "0.0.1-20151015"));

        System.out.println("0.0.2-20151014 > 0.0.1-20151015");
        System.out.println(verCompare.compare("0.0.2-20151014", "0.0.1-20151015"));

        System.out.println("1.0.1-20151014 > 0.0.1-20151015");
        System.out.println(verCompare.compare("1.0.1-20151014", "0.0.1-20151015"));

        System.out.println("1 > 0.0.1");
        System.out.println(verCompare.compare("1", "0.0.1"));

        System.out.println("1.0 > 0.0.1");
        System.out.println(verCompare.compare("1.0", "0.0.1"));

        System.out.println("0.0.1 < 1");
        System.out.println(verCompare.compare("0.0.1", "1"));

        System.out.println("0.0.1 < 1.0");
        System.out.println(verCompare.compare("0.0.1", "1.0"));

        System.out.println("1 < 1.0.1");
        System.out.println(verCompare.compare("1", "1.0.1"));

        System.out.println("1.0 < 1.0.1");
        System.out.println(verCompare.compare("1.0", "1.0.1"));

        System.out.println("1.0.1 > 1");
        System.out.println(verCompare.compare("1.0.1", "1"));

        System.out.println("1.0.1 > 1.0");
        System.out.println(verCompare.compare("1.0.1", "1.0"));

        System.out.println(".0.1 < 0.0.1");
        System.out.println(verCompare.compare(".0.1", "0.0.1"));

        System.out.println("0.1.0 > 0.0.5");
        System.out.println(verCompare.compare("0.1.0", "0.0.5"));

        System.out.println("1.0.0 > 0.9.5");
        System.out.println(verCompare.compare("1.0.0", "0.9.5"));

        System.out.println("0.1-SNAPSHOT.1 == 0.1.1-SNAPSHOT");
        System.out.println(verCompare.compare("0.1-SNAPSHOT.1", "0.1.1-SNAPSHOT"));

        System.out.println("0.1.0 < 0.1.0.0");
        System.out.println(verCompare.compare("0.1.0", "0.1.0.0"));

        System.out.println("0.1.0 < 0.1.0.5");
        System.out.println(verCompare.compare("0.1.0", "0.1.0.5"));

        System.out.println("0.1-2015.1 > 0.1.1");
        System.out.println(verCompare.compare("0.1-2015.1", "0.1.1"));
    }
}
