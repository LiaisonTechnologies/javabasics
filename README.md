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