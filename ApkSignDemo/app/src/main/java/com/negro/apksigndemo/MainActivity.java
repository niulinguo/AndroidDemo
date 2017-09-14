package com.negro.apksigndemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.joker.annotation.PermissionsCustomRationale;
import com.joker.annotation.PermissionsDenied;
import com.joker.annotation.PermissionsGranted;
import com.joker.annotation.PermissionsNonRationale;
import com.joker.annotation.PermissionsRationale;
import com.joker.api.Permissions4M;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class MainActivity extends BaseActivity {

    private static final int REQUEST_CODE_READ_APK_FILE = 1;

    private TextView mMessageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMessageView = (TextView) findViewById(R.id.tv_text);

    }

    public void getAPKSign(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Permissions4M.get(MainActivity.this)
                    .requestPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .requestCodes(REQUEST_CODE_READ_APK_FILE)
                    .requestPageType(Permissions4M.PageType.MANAGER_PAGE)
                    .request();
        }

    }

    @PermissionsGranted(REQUEST_CODE_READ_APK_FILE)
    public void grantedReadApkFile() {

        Signature[] signatures;
        StringBuilder stringBuilder = new StringBuilder();
        signatures = showUninstallAPKSignatures21(new File(Environment.getExternalStorageDirectory(), "signDemo/app-release.apk").getAbsolutePath());
        parserSignature(signatures[0].toByteArray(), stringBuilder);
        getSignInfo("com.negro.apksigndemo", stringBuilder);

        mMessageView.setText(stringBuilder);

    }

    @PermissionsDenied(REQUEST_CODE_READ_APK_FILE)
    public void deniedReadApkFile() {
        Toast.makeText(this, "没有权限读取文件", Toast.LENGTH_SHORT).show();
    }

    @PermissionsRationale(REQUEST_CODE_READ_APK_FILE)
    public void rationaleReadApkFile() {
        Toast.makeText(this, "开启权限才能读取文件", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @PermissionsCustomRationale(REQUEST_CODE_READ_APK_FILE)
    public void cusRationaleReadApkFile() {
        new AlertDialog.Builder(this)
                .setMessage("打开权限啊，不然读取不到文件啊")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Permissions4M.get(MainActivity.this)
                                .requestOnRationale()
                                .requestPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                                .requestCodes(REQUEST_CODE_READ_APK_FILE)
                                .request();
                    }
                })
                .show();
    }

    @PermissionsNonRationale(REQUEST_CODE_READ_APK_FILE)
    public void nonRationaleReadApkFile(Intent intent) {
        startActivity(intent);
    }

    public Signature[] showUninstallAPKSignatures21(String apkPath) {

        String packageParserPath = "android.content.pm.PackageParser";

        try {

            Class<?> pkgParserClass = Class.forName(packageParserPath);
            Constructor<?> constructor = pkgParserClass.getConstructor();
            Object pkgParser = constructor.newInstance();

            DisplayMetrics displayMetrics = new DisplayMetrics();
            displayMetrics.setToDefaults();

            Method parsePackageMethod = pkgParserClass.getDeclaredMethod("parsePackage", File.class, int.class);

            Object pkgParserPkg = parsePackageMethod.invoke(pkgParser, new File(apkPath), PackageManager.GET_SIGNATURES);

            Method collectCertificatesMethod = pkgParserClass.getDeclaredMethod("collectCertificates", pkgParserPkg.getClass(), Integer.TYPE);

            collectCertificatesMethod.invoke(pkgParser, pkgParserPkg, PackageManager.GET_SIGNATURES);

            Field signaturesField = pkgParserPkg.getClass().getDeclaredField("mSignatures");

            return (Signature[]) signaturesField.get(pkgParserPkg);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("解析PackageParser有误");
        }
    }

    public void getSignInfo(String pkgName, StringBuilder stringBuilder) {
        try {
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo packageInfo = getPackageManager().getPackageInfo(pkgName, PackageManager.GET_SIGNATURES);
            Signature[] signatures = packageInfo.signatures;
            Signature signature = signatures[0];
            parserSignature(signature.toByteArray(), stringBuilder);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void parserSignature(byte[] signature, StringBuilder stringBuilder) {
        try {
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(signature));
            stringBuilder
                    .append("----------------------")
                    .append('\n')
                    .append("Version=")
                    .append(cert.getVersion())
                    .append('\n')
                    .append("SerialNumber=")
                    .append(cert.getSerialNumber())
                    .append('\n')
                    .append("IssuerDN=")
                    .append(cert.getIssuerDN().toString())
                    .append('\n')
                    .append("IssuerX500Principal=")
                    .append(cert.getIssuerX500Principal().toString())
                    .append('\n')
                    .append("SubjectDN=")
                    .append(cert.getSubjectDN().toString())
                    .append('\n')
                    .append("SubjectX500Principal=")
                    .append(cert.getSubjectX500Principal().toString())
                    .append('\n')
                    .append("NotBefore=")
                    .append(cert.getNotBefore().toString())
                    .append('\n')
                    .append("NotAfter=")
                    .append(cert.getNotAfter().toString())
                    .append('\n')
                    .append("SigAlgName=")
                    .append(cert.getSigAlgName())
                    .append('\n')
                    .append("SigAlgOID=")
                    .append(cert.getSigAlgOID())
                    .append('\n')
                    .append("BasicConstraints=")
                    .append(cert.getBasicConstraints())
                    .append('\n')
                    .append("pubKey=")
                    .append(cert.getPublicKey().toString())
                    .append('\n')
                    .append("----------------------");

        } catch (CertificateException e) {
            e.printStackTrace();
        }
    }
}
