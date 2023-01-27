package com.google.android.lint.aidl

import com.android.tools.lint.checks.infrastructure.LintDetectorTest.java
import com.android.tools.lint.checks.infrastructure.TestFile

val aidlStub: TestFile = java(
    """
    package android.test;
    public interface ITest extends android.os.IInterface {
        public static abstract class Stub extends android.os.Binder implements android.test.ITest {
            protected void test_enforcePermission() throws SecurityException {}
        }
        public void test() throws android.os.RemoteException;
    }
    """
).indented()

val contextStub: TestFile = java(
    """
    package android.content;
    public class Context {
        @android.annotation.PermissionMethod(orSelf = true)
        public void enforceCallingOrSelfPermission(@android.annotation.PermissionName String permission, String message) {}
        @android.annotation.PermissionMethod
        public void enforceCallingPermission(@android.annotation.PermissionName String permission, String message) {}
        @android.annotation.PermissionMethod(orSelf = true)
        public int checkCallingOrSelfPermission(@android.annotation.PermissionName String permission, String message) {}
        @android.annotation.PermissionMethod
        public int checkCallingPermission(@android.annotation.PermissionName String permission, String message) {}
    }
    """
).indented()

val binderStub: TestFile = java(
    """
    package android.os;
    public class Binder {
        public static int getCallingUid() {}
    }
    """
).indented()

val permissionMethodStub: TestFile = java(
    """
    package android.annotation;

    import static java.lang.annotation.ElementType.METHOD;
    import static java.lang.annotation.RetentionPolicy.CLASS;

    import java.lang.annotation.Retention;
    import java.lang.annotation.Target;

    @Retention(CLASS)
    @Target({METHOD})
    public @interface PermissionMethod {}
    """
).indented()

val permissionNameStub: TestFile = java(
    """
    package android.annotation;

    import static java.lang.annotation.ElementType.FIELD;
    import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
    import static java.lang.annotation.ElementType.METHOD;
    import static java.lang.annotation.ElementType.PARAMETER;
    import static java.lang.annotation.RetentionPolicy.CLASS;

    import java.lang.annotation.Retention;
    import java.lang.annotation.Target;

    @Retention(CLASS)
    @Target({PARAMETER, METHOD, LOCAL_VARIABLE, FIELD})
    public @interface PermissionName {}
    """
).indented()

val manifestStub: TestFile = java(
    """
    package android;

    public final class Manifest {
        public static final class permission {
            public static final String READ_CONTACTS="android.permission.READ_CONTACTS";
        }
    }
    """.trimIndent()
)