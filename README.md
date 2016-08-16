# javabasics: Basic Utilities for Java Projects

## Summary

**javabasics** provides some basic utilities to handle common patterns which emerge in Java projects. The following is a quick summary; see Javadocs for further details:

- **commons**
    - **SemanticVersionComparator:** Comparator which compares standard semantic version numbers (in the major.minor.patch format).
    - **Uninstantiable:** Write your class to extend this when you absolutely, positively, need it not to be instantiable (i.e. for static-utility classes).
    - **Util:** Utility methods for various common, repeatable Java tasks; see Javadocs.
- **logging**
    - **JitLog:** "Just-in-Time" Logger which sits atop SLF4J, and is designed to defer computation of entries to be written to logs (including string concatenation) until just in time to write the log, avoiding performance-robbing computation of log message strings **unless** the corresponding log level is enabled. The mechanism consists of replacing the strings usually passed to `Logger`s with Java 8 lambda functions (or method references) which *produce* said strings, and which are only executed in the event that the log level is enabled.
- **serialization**
    - **BytesUtil:** Utility methods for handling byte arrays; see Javadocs.
    - **Constants:** Common constants used in serialization and for handling byte arrays; see Javadocs.
    - **DefensiveCopyStrategy:** Enum which can serve as a flag to the application layer to indicate when to perform defensive copies of byte arrays being processed.

## TEST BUILDING

Test CI config.

## License and Trademarks

Copyright © 2016 Liaison Technologies, Inc.

Licensed under the Apache License, Version 2.0 (the "License"). You may not use the project encapsulated by this Git repository or any files contained therein, except in compliance with the License. You may obtain a copy of the License at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

Liaison and Liaison Technologies are trademarks of Liaison Technologies, Inc. All rights reserved.

Apache, Apache HBase, and HBase are trademarks of The Apache Software Foundation. No endorsement by The Apache Software Foundation is implied by the use of these marks.

MapR is a trademark of MapR Technologies, Inc. No endorsement by MapR Technologies is implied by the use of these marks.

All other trademarks are the property of their respective owners, and no endorsement by said entities is implied by the use of these marks.
