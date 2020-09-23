/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.content.pm;

import android.annotation.IntDef;
import android.annotation.NonNull;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.internal.util.DataClass;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * A typed checksum.
 *
 * @see PackageInstaller.Session#addChecksums(String, List)
 */
@DataClass(genConstDefs = false)
public final class Checksum implements Parcelable {
    /**
     * Root SHA256 hash of a 4K Merkle tree computed over all file bytes.
     * <a href="https://source.android.com/security/apksigning/v4">See APK Signature Scheme V4</a>.
     * <a href="https://git.kernel.org/pub/scm/fs/fscrypt/fscrypt.git/tree/Documentation/filesystems/fsverity.rst">See fs-verity</a>.
     *
     * Recommended for all new applications.
     * Can be used by kernel to enforce authenticity and integrity of the APK.
     * <a href="https://git.kernel.org/pub/scm/fs/fscrypt/fscrypt.git/tree/Documentation/filesystems/fsverity.rst#">See fs-verity for details</a>
     *
     * @see PackageManager#getChecksums
     * @see PackageInstaller.Session#addChecksums
     */
    public static final int WHOLE_MERKLE_ROOT_4K_SHA256 = 0x00000001;

    /**
     * MD5 hash computed over all file bytes.
     *
     * @see PackageManager#getChecksums
     * @see PackageInstaller.Session#addChecksums
     * @deprecated Use SHA2 family of hashes (SHA256/SHA512) instead.
     *             MD5 is cryptographically broken and unsuitable for further use.
     *             Provided for completeness' sake and to support legacy usecases.
     */
    @Deprecated
    public static final int WHOLE_MD5 = 0x00000002;

    /**
     * SHA1 hash computed over all file bytes.
     *
     * @see PackageManager#getChecksums
     * @see PackageInstaller.Session#addChecksums
     * @deprecated Use SHA2 family of hashes (SHA256/SHA512) instead.
     *             SHA1 is broken and should not be used.
     *             Provided for completeness' sake and to support legacy usecases.
     */
    @Deprecated
    public static final int WHOLE_SHA1 = 0x00000004;

    /**
     * SHA256 hash computed over all file bytes.
     *
     * @see PackageManager#getChecksums
     * @see PackageInstaller.Session#addChecksums
     */
    public static final int WHOLE_SHA256 = 0x00000008;

    /**
     * SHA512 hash computed over all file bytes.
     *
     * @see PackageManager#getChecksums
     * @see PackageInstaller.Session#addChecksums
     */
    public static final int WHOLE_SHA512 = 0x00000010;

    /**
     * Root SHA256 hash of a 1M Merkle tree computed over protected content.
     * Excludes signing block.
     * <a href="https://source.android.com/security/apksigning/v2">See APK Signature Scheme V2</a>.
     *
     * @see PackageManager#getChecksums
     * @see PackageInstaller.Session#addChecksums
     */
    public static final int PARTIAL_MERKLE_ROOT_1M_SHA256 = 0x00000020;

    /**
     * Root SHA512 hash of a 1M Merkle tree computed over protected content.
     * Excludes signing block.
     * <a href="https://source.android.com/security/apksigning/v2">See APK Signature Scheme V2</a>.
     *
     * @see PackageManager#getChecksums
     * @see PackageInstaller.Session#addChecksums
     */
    public static final int PARTIAL_MERKLE_ROOT_1M_SHA512 = 0x00000040;

    /** @hide */
    @IntDef(flag = true, prefix = {"WHOLE_", "PARTIAL_"}, value = {
            WHOLE_MERKLE_ROOT_4K_SHA256,
            WHOLE_MD5,
            WHOLE_SHA1,
            WHOLE_SHA256,
            WHOLE_SHA512,
            PARTIAL_MERKLE_ROOT_1M_SHA256,
            PARTIAL_MERKLE_ROOT_1M_SHA512,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Kind {}

    /**
     * Checksum kind.
     */
    private final @Checksum.Kind int mKind;
    /**
     * Checksum value.
     */
    private final @NonNull byte[] mValue;




    // Code below generated by codegen v1.0.15.
    //
    // DO NOT MODIFY!
    // CHECKSTYLE:OFF Generated code
    //
    // To regenerate run:
    // $ codegen $ANDROID_BUILD_TOP/frameworks/base/core/java/android/content/pm/Checksum.java
    //
    // To exclude the generated code from IntelliJ auto-formatting enable (one-time):
    //   Settings > Editor > Code Style > Formatter Control
    //@formatter:off


    /**
     * Creates a new Checksum.
     *
     * @param kind
     *   Checksum kind.
     * @param value
     *   Checksum value.
     */
    @DataClass.Generated.Member
    public Checksum(
            @Checksum.Kind int kind,
            @NonNull byte[] value) {
        this.mKind = kind;
        com.android.internal.util.AnnotationValidations.validate(
                Checksum.Kind.class, null, mKind);
        this.mValue = value;
        com.android.internal.util.AnnotationValidations.validate(
                NonNull.class, null, mValue);

        // onConstructed(); // You can define this method to get a callback
    }

    /**
     * Checksum kind.
     */
    @DataClass.Generated.Member
    public @Checksum.Kind int getKind() {
        return mKind;
    }

    /**
     * Checksum value.
     */
    @DataClass.Generated.Member
    public @NonNull byte[] getValue() {
        return mValue;
    }

    @Override
    @DataClass.Generated.Member
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        // You can override field parcelling by defining methods like:
        // void parcelFieldName(Parcel dest, int flags) { ... }

        dest.writeInt(mKind);
        dest.writeByteArray(mValue);
    }

    @Override
    @DataClass.Generated.Member
    public int describeContents() { return 0; }

    /** @hide */
    @SuppressWarnings({"unchecked", "RedundantCast"})
    @DataClass.Generated.Member
    /* package-private */ Checksum(@NonNull Parcel in) {
        // You can override field unparcelling by defining methods like:
        // static FieldType unparcelFieldName(Parcel in) { ... }

        int kind = in.readInt();
        byte[] value = in.createByteArray();

        this.mKind = kind;
        com.android.internal.util.AnnotationValidations.validate(
                Checksum.Kind.class, null, mKind);
        this.mValue = value;
        com.android.internal.util.AnnotationValidations.validate(
                NonNull.class, null, mValue);

        // onConstructed(); // You can define this method to get a callback
    }

    @DataClass.Generated.Member
    public static final @NonNull Parcelable.Creator<Checksum> CREATOR
            = new Parcelable.Creator<Checksum>() {
        @Override
        public Checksum[] newArray(int size) {
            return new Checksum[size];
        }

        @Override
        public Checksum createFromParcel(@NonNull Parcel in) {
            return new Checksum(in);
        }
    };

    @DataClass.Generated(
            time = 1600717052366L,
            codegenVersion = "1.0.15",
            sourceFile = "frameworks/base/core/java/android/content/pm/Checksum.java",
            inputSignatures = "public static final  int WHOLE_MERKLE_ROOT_4K_SHA256\npublic static final @java.lang.Deprecated int WHOLE_MD5\npublic static final @java.lang.Deprecated int WHOLE_SHA1\npublic static final  int WHOLE_SHA256\npublic static final  int WHOLE_SHA512\npublic static final  int PARTIAL_MERKLE_ROOT_1M_SHA256\npublic static final  int PARTIAL_MERKLE_ROOT_1M_SHA512\nprivate final @android.content.pm.Checksum.Kind int mKind\nprivate final @android.annotation.NonNull byte[] mValue\nclass Checksum extends java.lang.Object implements [android.os.Parcelable]\n@com.android.internal.util.DataClass(genConstDefs=false)")
    @Deprecated
    private void __metadata() {}


    //@formatter:on
    // End of generated code

}
