#!/usr/bin/env bash
set -euo pipefail

PKG="com.calculator.maybe"
APP_NAME="SimpleCalculator"
ROOT="$(pwd)/${APP_NAME}"

echo "🩸 إنشاء مشروع ${APP_NAME} في: ${ROOT}"

# ── هيكل المجلدات ──────────────────────────────────────────────
mkdir -p "${ROOT}/app/src/main/java/${PKG//.//}"
mkdir -p "${ROOT}/app/src/main/res/layout"
mkdir -p "${ROOT}/app/src/main/res/values"
mkdir -p "${ROOT}/gradle/wrapper"

# ── settings.gradle ───────────────────────────────────────────
cat > "${ROOT}/settings.gradle" <<'EOF'
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "SimpleCalculator"
include ':app'
EOF

# ── build.gradle (root) ───────────────────────────────────────
cat > "${ROOT}/build.gradle" <<'EOF'
plugins {
    id 'com.android.application' version '8.7.3' apply false
}
EOF

# ── gradle.properties ─────────────────────────────────────────
cat > "${ROOT}/gradle.properties" <<'EOF'
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
android.useAndroidX=true
android.nonTransitiveRClass=true
EOF

# ── app/build.gradle ──────────────────────────────────────────
cat > "${ROOT}/app/build.gradle" <<EOF
plugins {
    id 'com.android.application'
}

android {
    namespace '${PKG}'
    compileSdk 36

    defaultConfig {
        applicationId "${PKG}"
        minSdk 24
        targetSdk 36
        versionCode 1
        versionName "1.0"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }
}

dependencies {
    implementation 'androidx.appcompat:appcompat:1.7.0'
    implementation 'com.google.android.material:material:1.12.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.2.0'
    implementation 'androidx.core:core:1.15.0'
}
EOF

# ── proguard-rules.pro ────────────────────────────────────────
cat > "${ROOT}/app/proguard-rules.pro" <<'EOF'
# Add project specific ProGuard rules here.
EOF

# ── AndroidManifest.xml ───────────────────────────────────────
cat > "${ROOT}/app/src/main/AndroidManifest.xml" <<EOF
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <application
        android:allowBackup="true"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/Theme.Material3.DayNight.NoActionBar">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:windowSoftInputMode="adjustResize">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
EOF

# ── strings.xml ───────────────────────────────────────────────
cat > "${ROOT}/app/src/main/res/values/strings.xml" <<'EOF'
<resources>
    <string name="app_name">VANTA Calc</string>
</resources>
EOF

# ── styles.xml ────────────────────────────────────────────────
cat > "${ROOT}/app/src/main/res/values/styles.xml" <<'EOF'
<resources>
    <style name="CalcButton" parent="Widget.Material3.Button">
        <item name="android:layout_width">0dp</item>
        <item name="android:layout_height">0dp</item>
        <item name="android:layout_columnWeight">1</item>
        <item name="android:layout_rowWeight">1</item>
        <item name="android:layout_margin">4dp</item>
        <item name="android:textSize">24sp</item>
        <item name="android:insetTop">0dp</item>
        <item name="android:insetBottom">0dp</item>
    </style>
</resources>
EOF

# ── activity_main.xml ─────────────────────────────────────────
cat > "${ROOT}/app/src/main/res/layout/activity_main.xml" <<'EOF'
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/rootLayout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="16dp">

    <TextView
        android:id="@+id/tvDisplay"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:gravity="end|bottom"
        android:text="0"
        android:textSize="48sp"
        android:textColor="?android:attr/textColorPrimary"
        android:maxLines="1"
        android:ellipsize="start"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintBottom_toTopOf="@+id/gridButtons"
        app:layout_constraintVertical_weight="1" />

    <GridLayout
        android:id="@+id/gridButtons"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:rowCount="5"
        android:columnCount="4"
        app:layout_constraintTop_toBottomOf="@+id/tvDisplay"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintVertical_weight="3">

        <Button android:id="@+id/btnClear"   android:text="C"  style="@style/CalcButton" />
        <Button android:id="@+id/btnSign"    android:text="±"  style="@style/CalcButton" />
        <Button android:id="@+id/btnPercent" android:text="%"  style="@style/CalcButton" />
        <Button android:id="@+id/btnDiv"     android:text="÷"  style="@style/CalcButton" />

        <Button android:id="@+id/btn7" android:text="7" style="@style/CalcButton" />
        <Button android:id="@+id/btn8" android:text="8" style="@style/CalcButton" />
        <Button android:id="@+id/btn9" android:text="9" style="@style/CalcButton" />
        <Button android:id="@+id/btnMul" android:text="×" style="@style/CalcButton" />

        <Button android:id="@+id/btn4" android:text="4" style="@style/CalcButton" />
        <Button android:id="@+id/btn5" android:text="5" style="@style/CalcButton" />
        <Button android:id="@+id/btn6" android:text="6" style="@style/CalcButton" />
        <Button android:id="@+id/btnSub" android:text="−" style="@style/CalcButton" />

        <Button android:id="@+id/btn1" android:text="1" style="@style/CalcButton" />
        <Button android:id="@+id/btn2" android:text="2" style="@style/CalcButton" />
        <Button android:id="@+id/btn3" android:text="3" style="@style/CalcButton" />
        <Button android:id="@+id/btnAdd" android:text="+" style="@style/CalcButton" />

        <Button android:id="@+id/btn0"
            android:text="0"
            style="@style/CalcButton"
            android:layout_columnSpan="2"
            android:layout_gravity="fill" />
        <Button android:id="@+id/btnDot" android:text="." style="@style/CalcButton" />
        <Button android:id="@+id/btnEquals" android:text="=" style="@style/CalcButton" />

    </GridLayout>

</androidx.constraintlayout.widget.ConstraintLayout>
EOF

# ── MainActivity.java ─────────────────────────────────────────
cat > "${ROOT}/app/src/main/java/${PKG//.//}/MainActivity.java" <<EOF
package ${PKG};

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView tvDisplay;
    private String currentInput = "0";
    private String pendingOperator = null;
    private double firstOperand = 0;
    private boolean isNewInput = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Android 16 (API 36): Edge-to-Edge إلزامي
        View root = findViewById(R.id.rootLayout);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            int top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;
            int bottom = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom;
            v.setPadding(16, 16 + top, 16, 16 + bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        tvDisplay = findViewById(R.id.tvDisplay);
        setupButtons();
        updateDisplay();
    }

    private void setupButtons() {
        int[] numberIds = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};
        for (int id : numberIds) {
            findViewById(id).setOnClickListener(v -> onDigit(((Button) v).getText().toString()));
        }

        findViewById(R.id.btnDot).setOnClickListener(v -> onDot());
        findViewById(R.id.btnClear).setOnClickListener(v -> onClear());
        findViewById(R.id.btnSign).setOnClickListener(v -> onSign());
        findViewById(R.id.btnPercent).setOnClickListener(v -> onPercent());

        findViewById(R.id.btnAdd).setOnClickListener(v -> onOperator("+"));
        findViewById(R.id.btnSub).setOnClickListener(v -> onOperator("-"));
        findViewById(R.id.btnMul).setOnClickListener(v -> onOperator("*"));
        findViewById(R.id.btnDiv).setOnClickListener(v -> onOperator("/"));

        findViewById(R.id.btnEquals).setOnClickListener(v -> onEquals());
    }

    private void onDigit(String digit) {
        if (isNewInput) {
            currentInput = digit;
            isNewInput = false;
        } else {
            if (currentInput.equals("0")) currentInput = digit;
            else currentInput += digit;
        }
        updateDisplay();
    }

    private void onDot() {
        if (isNewInput) {
            currentInput = "0.";
            isNewInput = false;
        } else if (!currentInput.contains(".")) {
            currentInput += ".";
        }
        updateDisplay();
    }

    private void onClear() {
        currentInput = "0";
        pendingOperator = null;
        firstOperand = 0;
        isNewInput = true;
        updateDisplay();
    }

    private void onSign() {
        currentInput = formatNumber(-parseCurrent());
        updateDisplay();
    }

    private void onPercent() {
        currentInput = formatNumber(parseCurrent() / 100.0);
        updateDisplay();
    }

    private void onOperator(String op) {
        if (pendingOperator != null && !isNewInput) {
            double second = parseCurrent();
            double result = compute(firstOperand, second, pendingOperator);
            currentInput = formatNumber(result);
            firstOperand = result;
        } else {
            firstOperand = parseCurrent();
        }
        pendingOperator = op;
        isNewInput = true;
        updateDisplay();
    }

    private void onEquals() {
        if (pendingOperator == null) return;
        double second = parseCurrent();
        double result = compute(firstOperand, second, pendingOperator);
        currentInput = formatNumber(result);
        pendingOperator = null;
        firstOperand = 0;
        isNewInput = true;
        updateDisplay();
    }

    private double compute(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": return (b == 0) ? 0 : a / b;
            default: return b;
        }
    }

    private double parseCurrent() {
        try {
            return Double.parseDouble(currentInput);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String formatNumber(double val) {
        if (val == (long) val) return String.valueOf((long) val);
        return String.valueOf(val);
    }

    private void updateDisplay() {
        tvDisplay.setText(currentInput);
    }
}
EOF

# ── Gradle wrapper ────────────────────────────────────────────
cat > "${ROOT}/gradle/wrapper/gradle-wrapper.properties" <<'EOF'
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-8.9-bin.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
EOF

echo ""
echo "✅ تم إنشاء المشروع بنجاح في: ${ROOT}"
echo ""
echo "الخطوات التالية:"
echo "  1) افتح ${ROOT} في Android Studio"
echo "  2) SDK Manager → ثبّت Android SDK Platform 36"
echo "  3) Sync Gradle ثم Run"