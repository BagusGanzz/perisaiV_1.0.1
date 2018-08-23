package com.perisaimobile.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.IPackageStatsObserver.Stub;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.RemoteException;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils.SimpleStringSplitter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.perisaimobile.iface.IProblem;
import com.perisaimobile.model.AppLock;
import com.perisaimobile.model.Application;
import com.perisaimobile.model.AppsLocked;
import com.perisaimobile.model.JunkOfApplication;
import com.perisaimobile.service.MyAccessibilityService;
import com.studioninja.locker.BuildConfig;
import com.studioninja.locker.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utils {
    private static final long CACHE_APP = Long.MAX_VALUE;
    public static List<JunkOfApplication> junkOfApplications = new ArrayList();
    public static int numOfPackages;
    public static IProblem selectedProblem;

    public static void notificatePush(Context context, int notificationId, int iconDrawableId, String tickerText, String contentTitle, String contentText, Intent intent) {
        Builder mBuilder = new Builder(context).setSmallIcon(R.drawable.ic_small_notificiation).setLargeIcon(BitmapFactory.decodeResource(context.getResources(), iconDrawableId)).setContentTitle(contentTitle).setContentText(contentText).setTicker(tickerText);
        if (intent != null) {
            mBuilder.setContentIntent(PendingIntent.getActivity(context, notificationId, intent, 134217728));
        }
        mBuilder.setAutoCancel(true);
        mBuilder.setOnlyAlertOnce(true);
        ((NotificationManager) context.getSystemService("notification")).notify(notificationId, mBuilder.build());
    }

    public static void notificateAppLock(Context context, int notificationId, int iconDrawableId, String tickerText, String contentTitle, String contentText, Intent intent) {
        Builder mBuilder = new Builder(context).setSmallIcon(R.drawable.ic_small_notificiation).setLargeIcon(BitmapFactory.decodeResource(context.getResources(), iconDrawableId)).setContentTitle(contentTitle).setContentText(contentText).setTicker(tickerText);
        mBuilder.setContentIntent(PendingIntent.getActivity(context, notificationId, intent, 134217728));
        mBuilder.setAutoCancel(true);
        mBuilder.setOnlyAlertOnce(true);
        ((NotificationManager) context.getSystemService("notification")).notify(notificationId, mBuilder.build());
    }

    public static void notificatePermanentPush(Context context, int notificationId, int iconDrawableId, String tickerText, String contentTitle, String contentText, Intent intent) {
        Builder mBuilder = new Builder(context).setSmallIcon(iconDrawableId).setContentTitle(contentTitle).setContentText(contentText).setCategory("service").setVisibility(1).setTicker(tickerText).setOngoing(true);
        mBuilder.setContentIntent(PendingIntent.getActivity(context, notificationId, intent, 134217728));
        mBuilder.setAutoCancel(true);
        mBuilder.setOnlyAlertOnce(true);
        ((NotificationManager) context.getSystemService("notification")).notify(notificationId, mBuilder.build());
    }

    public static boolean isSDAvailable() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static String getSDPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static String getInternalDataPath(Context c) {
        String path = c.getFilesDir().getPath();
        if (path.length() != 0) {
            return path;
        }
        path = "/data/data/" + c.getPackageName() + "/files";
        new File(path).mkdirs();
        return path;
    }

    public static boolean existsFile(String filePath) {
        File f = new File(filePath);
        if (!f.exists() || f.isDirectory()) {
            return false;
        }
        return true;
    }

    public static boolean existsFolder(String folderPath) {
        File f = new File(folderPath);
        if (f.exists() && f.isDirectory()) {
            return true;
        }
        return false;
    }

    public static boolean existsInternalStorageFile(Context ctx, String internalRelativePath) {
        return existsFile(getInternalDataPath(ctx) + internalRelativePath);
    }

    public static boolean existsSDFile(Context ctx, String sdRelativePath) {
        return existsFile(getSDPath() + sdRelativePath);
    }

    public static boolean deleteFile(String filePath) {
        File f = new File(filePath);
        if (!f.exists() || f.isDirectory()) {
            return false;
        }
        return f.delete();
    }

    public static StatFs getSDData() {
        return new StatFs(getSDPath());
    }

    public static File createTempFile(Context context, String prefix, String extension) {
        File outputFile = null;
        try {
            outputFile = File.createTempFile(prefix, extension, context.getCacheDir());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return outputFile;
    }

    public static void copyAssetFileToCacheFile(Context context, String assetFile, File cacheFile) {
        try {
            _copyAssetFile(new BufferedReader(new InputStreamReader(context.getAssets().open(assetFile))), cacheFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean copyAssetFileToFileSystem(Context context, String assetFile, String dstFile) {
        return copyAssetFileToFileSystem(context, assetFile, new File(dstFile));
    }

    public static boolean copyAssetFileToFileSystem(Context context, String assetFile, File dstFile) {
        try {
            _copyAssetFile(new BufferedReader(new InputStreamReader(context.getAssets().open(assetFile))), dstFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void _copyAssetFile(BufferedReader br, File toFile) throws IOException {
        Throwable th;
        BufferedWriter bw = null;
        try {
            BufferedWriter bw2 = new BufferedWriter(new FileWriter(toFile));
            while (true) {
                try {
                    int in = br.read();
                    if (in == -1) {
                        break;
                    }
                    bw2.write(in);
                } catch (Throwable th2) {
                    th = th2;
                    bw = bw2;
                }
            }
            if (bw2 != null) {
                bw2.close();
            }
            br.close();
        } catch (Throwable th3) {
            th = th3;
            if (bw != null) {
                bw.close();
            }
            br.close();
            //throw th;
        }
    }

    public static String loadJSONFromAsset(Context context, String file) {
        try {
            InputStream is = context.getAssets().open(file);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            return json;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String loadJSONFromFile(Context context, String filePath) {
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            while (true) {
                String line = br.readLine();
                if (line != null) {
                    text.append(line);
                    text.append('\n');
                } else {
                    br.close();
                    return text.toString();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return BuildConfig.FLAVOR;
        }
    }

    public static String loadTextFile(String filePath) throws IOException {
        Throwable th;
        File file = new File(filePath);
        StringBuilder text = new StringBuilder();
        BufferedReader br = null;
        try {
            BufferedReader br2 = new BufferedReader(new FileReader(file));
            while (true) {
                try {
                    String line = br2.readLine();
                    if (line != null) {
                        text.append(line);
                        text.append('\n');
                    } else {
                        br2.close();
                        return text.toString();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    br = br2;
                }
            }
        } catch (Throwable th3) {
            th = th3;
            br.close();
            //throw th;
        }

        return "";
    }

    public static void writeTextFile(String filePath, String text) throws IOException {
        Throwable th;
        BufferedWriter bw = null;
        try {
            BufferedWriter bw2 = new BufferedWriter(new FileWriter(filePath));
            try {
                bw2.write(text);
                if (bw2 != null) {
                    bw2.close();
                }
            } catch (Throwable th2) {
                th = th2;
                bw = bw2;
                if (bw != null) {
                    bw.close();
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            if (bw != null) {
                bw.close();
            }
            //throw th;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    public static boolean isPackageInstalled(Context context, String targetPackage) {
        try {
            context.getPackageManager().getPackageInfo(targetPackage, 128);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public static List<PackageInfo> getSystemApps(Context context, List<PackageInfo> appsToFilter) {
        List<PackageInfo> filteredPackgeInfo = new ArrayList();
        for (int i = 0; i < appsToFilter.size(); i++) {
            PackageInfo packInfo = (PackageInfo) appsToFilter.get(i);
            if ((packInfo.applicationInfo.flags & 129) != 0) {
                filteredPackgeInfo.add(packInfo);
            }
        }
        return filteredPackgeInfo;
    }

    public static List<PackageInfo> getNonSystemApps(Context context, List<PackageInfo> appsToFilter) {
        List<PackageInfo> filteredPackgeInfo = new ArrayList();
        for (int i = 0; i < appsToFilter.size(); i++) {
            PackageInfo packInfo = (PackageInfo) appsToFilter.get(i);
            if ((packInfo.applicationInfo.flags & 129) == 0) {
                filteredPackgeInfo.add(packInfo);
            }
        }
        return filteredPackgeInfo;
    }

    public static void logPackageNames(List<PackageInfo> packages) {
        for (int i = 0; i < packages.size(); i++) {
            Log.d("Package", ((PackageInfo) packages.get(i)).packageName);
        }
    }

    public static List<PackageInfo> getApps(Context context, int packageManagerPermissions) {
        try {
            return context.getPackageManager().getInstalledPackages(packageManagerPermissions);
        } catch (Exception e) {
            return getInstalledPackages(context, packageManagerPermissions);
        }
    }

    public static List<PackageInfo> getInstalledPackages(Context context, int flags) {
        List<PackageInfo> installedPackages;
        Exception e;
        Throwable th;
        PackageManager pm = context.getPackageManager();
        try {
            installedPackages = pm.getInstalledPackages(flags);
        } catch (Exception e2) {
            installedPackages = new ArrayList();
            BufferedReader bufferedReader = null;
            try {
                Process process = Runtime.getRuntime().exec("pm list packages");
                BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(process.getInputStream()));
                while (true) {
                    try {
                        String line = bufferedReader2.readLine();
                        if (line == null) {
                            break;
                        }
                        installedPackages.add(pm.getPackageInfo(line.substring(line.indexOf(58) + 1), flags));
                    } catch (Exception e3) {
                        e = e3;
                        bufferedReader = bufferedReader2;
                    } catch (Throwable th2) {
                        th = th2;
                        bufferedReader = bufferedReader2;
                    }
                }
                process.waitFor();
                if (bufferedReader2 != null) {
                    try {
                        bufferedReader2.close();
                        bufferedReader = bufferedReader2;
                    } catch (IOException e4) {
                        e4.printStackTrace();
                        bufferedReader = bufferedReader2;
                    }
                }
            } catch (Exception e5) {
                e = e5;
                try {
                    e.printStackTrace();
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e42) {
                            e42.printStackTrace();
                        }
                    }
                    return installedPackages;
                } catch (Throwable th3) {
                    th = th3;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e422) {
                            e422.printStackTrace();
                        }
                    }
                    //throw th;
                }
            }
        }
        return installedPackages;
    }

    public static String getAppNameFromPackage(Context context, String packageName) {
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        String appName = BuildConfig.FLAVOR;
        try {
            return (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, 128));
        } catch (NameNotFoundException e) {
            return "Unkown app";
        }
    }

    public static Drawable getIconFromPackage(String packageName, Context context) {
        Drawable icon = null;
        try {
            icon = context.getPackageManager().getApplicationIcon(packageName);
        } catch (NameNotFoundException e) {
        }
        return icon;
    }

    public static boolean checkIfAppWasInstalledThroughGooglePlay(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            if ("com.android.vending".equals(packageManager.getInstallerPackageName(packageManager.getApplicationInfo(packageName, 0).packageName))) {
                return true;
            }
            return false;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkIfUSBDebugIsEnabled(Context context) {
        if (Secure.getInt(context.getContentResolver(), "adb_enabled", 0) == 1) {
            return true;
        }
        return false;
    }

    public static ActivityInfo[] getActivitiesInPackage(Context context, String packageName, int packageManagerPermissions) throws NameNotFoundException {
        return getPackageInfo(context, packageName, packageManagerPermissions).activities;
    }

    public static PackageInfo getPackageInfo(Context context, String packageName, int packageManagerPermissions) throws NameNotFoundException {
        return context.getPackageManager().getPackageInfo(packageName, packageManagerPermissions);
    }

    public static boolean packageInfoHasPermission(PackageInfo packageInfo, String permissionName) {
        if (packageInfo.requestedPermissions == null) {
            return false;
        }
        for (String permInfo : packageInfo.requestedPermissions) {
            if (permInfo.equals(permissionName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkIfUnknownAppIsEnabled(Context context) {
        if (Secure.getInt(context.getContentResolver(), "install_non_market_apps", 0) == 1) {
            return true;
        }
        return false;
    }

    public static void openSecuritySettings(Context context) {
        context.startActivity(new Intent("android.settings.SECURITY_SETTINGS"));
    }

    public static void openDeveloperSettings(Context context) {
        context.startActivity(new Intent("android.settings.APPLICATION_DEVELOPMENT_SETTINGS"));
    }

    public static String fillParams(String data, String paramStr, String... args) {
        for (int i = 0; i < args.length; i++) {
            data = data.replace(paramStr + (i + 1), args[i]);
        }
        return data;
    }

    public static String capitalize(String source) {
        return source.substring(0, 1).toUpperCase() + source.substring(1);
    }

    public static String hexStrToStr(String hex) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            output.append((char) Integer.parseInt(hex.substring(i, i + 2), 16));
        }
        return output.toString();
    }

    public static long getFreeRAM(Context context) {
        MemoryInfo mi = new MemoryInfo();
        ((ActivityManager) context.getSystemService("activity")).getMemoryInfo(mi);
        return mi.availMem;
    }

    public static long getTotalRAM(Context context) {
        RandomAccessFile randomAccessFile;
        IOException ex;
        if (VERSION.SDK_INT >= 16) {
            MemoryInfo mi = new MemoryInfo();
            ((ActivityManager) context.getSystemService("activity")).getMemoryInfo(mi);
            return mi.totalMem;
        }
        long total = 0;
        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/meminfo", "r");
            try {
                total = (long) (Integer.parseInt(reader.readLine().replaceAll("\\D+", BuildConfig.FLAVOR)) / 1024);
                randomAccessFile = reader;
            } catch (IOException e) {
                ex = e;
                randomAccessFile = reader;
                ex.printStackTrace();
                return 1024 * total;
            }
        } catch (IOException e2) {
            ex = e2;
            ex.printStackTrace();
            return 1024 * total;
        }
        return 1024 * total;
    }

    public static String mixUp(String str, int interleaveRange) {
        StringBuffer sb = new StringBuffer(str);
        for (int i = 0; i < str.length() - interleaveRange; i++) {
            char c = sb.charAt(i);
            sb.setCharAt(i, sb.charAt(i + interleaveRange));
            sb.setCharAt(i + interleaveRange, c);
        }
        return sb.toString();
    }

    public static String unMixUp(String str, int interleaveRange) {
        StringBuffer sb = new StringBuffer(str);
        for (int i = str.length() - 1; i >= interleaveRange; i--) {
            char c = sb.charAt(i);
            sb.setCharAt(i, sb.charAt(i - interleaveRange));
            sb.setCharAt(i - interleaveRange, c);
        }
        return sb.toString();
    }

    public static String padRight(String s, int n, char paddingChar) {
        return String.format("%1$-" + n + "s", new Object[]{s}).replace(' ', paddingChar);
    }

    public static String padLeft(String s, int n, char paddingChar) {
        return String.format("%1$" + n + "s", new Object[]{s}).replace(' ', paddingChar);
    }

    public static void convertFileSizeToString(long size, String[] outputParts) {
        if (outputParts.length != 2) {
            throw new IllegalArgumentException("output parts must be an array of length 2");
        }
        String[] units = new String[]{"b", "Kb", "Mb", "Gb", "Tb", "Pb"};
        if (size <= 0) {
            outputParts[0] = "0";
            outputParts[1] = "Kb";
            return;
        }
        int digitGroups = (int) (Math.log10((double) size) / Math.log10(1024.0d));
        if (digitGroups > units.length - 1) {
            outputParts[0] = "Too big";
            outputParts[1] = BuildConfig.FLAVOR;
            return;
        }
        outputParts[0] = new DecimalFormat("#,##0.#").format(((double) size) / Math.pow(1024.0d, (double) digitGroups));
        outputParts[1] = units[digitGroups];
    }

    public static String convertFileSizeToString(long size) {
        String[] units = new String[]{"b", "Kb", "Mb", "Gb", "Tb", "Pb"};
        if (size <= 0) {
            return "0Kb";
        }
        int digitGroups = (int) (Math.log10((double) size) / Math.log10(1024.0d));
        if (digitGroups > units.length - 1) {
            return "Too big";
        }
        return new DecimalFormat("#,##0.#").format(((double) size) / Math.pow(1024.0d, (double) digitGroups)) + units[digitGroups];
    }

    public static boolean stringMatchesMask(String packageName, String mask) {
        boolean wildcard;
        if (mask.charAt(mask.length() - 1) == '*') {
            wildcard = true;
            mask = mask.substring(0, mask.length() - 2);
        } else {
            wildcard = false;
        }
        if (wildcard) {
            if (packageName.startsWith(mask)) {
                return true;
            }
            return false;
        } else if (packageName.equals(mask)) {
            return true;
        } else {
            return false;
        }
    }

    @SuppressLint({"NewApi"})
    public static void setViewBackgroundDrawable(View view, Drawable drawable) {
        if (VERSION.SDK_INT < 16) {
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        for (RunningServiceInfo runningServiceInfo : ((ActivityManager) context.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE)) {
            if (runningServiceInfo.service.getClassName().equals(serviceClass.getName())) {
                return true;
            }
        }
        return false;
    }

    public static void openMarketURL(Context context, String marketUrl, String webUrl) {
        try {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(marketUrl)));
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(webUrl)));
        }
    }

    public static <T> T deserializeFromFile(String fileName) throws FileNotFoundException, IOException {
        T t = null;
        try {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            t = (T) ois.readObject();
            ois.close();
            fis.close();
            return t;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return t;
        }
    }

    public static <T> T deserializeFromDataFolder(Context ctx, String rootRelativePath) {
        T obj = null;
        try {
            String path = getInternalDataPath(ctx) + File.separatorChar + rootRelativePath;
            if (existsFile(path)) {
                obj = deserializeFromFile(path);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    public static void serializeToFile(String fileName, Serializable obj) throws IOException {
        String fileParentFolder = new File(fileName).getParent();
        File parentPath = new File(fileParentFolder);
        if (!(fileParentFolder == null || existsFolder(fileParentFolder))) {
            parentPath.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(obj);
        oos.close();
        fos.close();
    }

    public static void serializeToDataFolder(Context ctx, Serializable obj, String rootRelativePath) throws IOException {
        serializeToFile(getInternalDataPath(ctx) + File.separatorChar + rootRelativePath, obj);
    }

    public static boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static long getAvailableInternalMemorySize() {
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        return ((long) stat.getAvailableBlocks()) * ((long) stat.getBlockSize());
    }

    public static long getTotalInternalMemorySize() {
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        return ((long) stat.getBlockCount()) * ((long) stat.getBlockSize());
    }

    public static long getAvailableExternalMemorySize() {
        if (!externalMemoryAvailable()) {
            return 0;
        }
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return ((long) stat.getAvailableBlocks()) * ((long) stat.getBlockSize());
    }

    public static long getTotalExternalMemorySize() {
        if (!externalMemoryAvailable()) {
            return 0;
        }
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return ((long) stat.getBlockCount()) * ((long) stat.getBlockSize());
    }

    public static String formatSize(long size) {
        return (((double) Math.round((((double) size) / 1.073741824E9d) * 100.0d)) / 100.0d) + "GB";
    }

    public long getAvalableMemory(Context c) {
        MemoryInfo mi = new MemoryInfo();
        ((ActivityManager) c.getSystemService("activity")).getMemoryInfo(mi);
        return mi.availMem / 1048576;
    }

    public static void showAlertDialog(Context c, String message, OnClickListener listener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c, R.style.MyAlertDialogStyle);
        alertDialogBuilder.setTitle(message);
        alertDialogBuilder.setPositiveButton("OK", listener);
        alertDialogBuilder.create().show();
    }

    public static void showConfirmDialog(Context c, String title, OnClickListener positiveListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c, R.style.MyAlertDialogStyle);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton(c.getString(R.string.ok), positiveListener);
        alertDialogBuilder.setNegativeButton(c.getString(R.string.cancel), null);
        alertDialogBuilder.create().show();
    }

    public static long getMemoryOfService(Context context, int pid) {
        ActivityManager manager = (ActivityManager) context.getSystemService("activity");
        manager.getMemoryInfo(new MemoryInfo());
        return (long) manager.getProcessMemoryInfo(new int[]{pid})[0].getTotalPss();
    }

    public static List<Application> getRunningApplications(Context context) {
        List<RunningServiceInfo> runningServiceInfos = ((ActivityManager) context.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE);
        List<Application> applications = new ArrayList();
        for (RunningServiceInfo serviceInfo : runningServiceInfos) {
            String packageName = serviceInfo.service.getPackageName();
            try {
                if ((context.getPackageManager().getApplicationInfo(packageName, 0).flags & 1) == 1) {
                } else {
                    if (!serviceInfo.process.equals(context.getPackageName())) {
                        Application application = new Application(serviceInfo.pid, packageName);
                        if (applications.contains(application)) {
                            Application app = (Application) applications.get(applications.indexOf(application));
                            app.setSize(app.getSize() + getMemoryOfService(context, serviceInfo.pid));
                        } else {
                            application.setName(getAppNameFromPackage(context, packageName));
                            application.setIcon(getIconFromPackage(packageName, context));
                            application.setSize(getMemoryOfService(context, serviceInfo.pid));
                            applications.add(application);
                        }
                    }

                }
            } catch (NameNotFoundException e) {
            }


        }
        return applications;
    }


    public static int getTotalMemoryBoost(Context context) {
        int total = 0;
        for (Application application : getRunningApplications(context)) {
            total = (int) (((long) total) + (application.getSize() / 1024));
        }
        return total;
    }

    public static void killBackgroundProcesses(Context context, String packageName) {
        ((ActivityManager) context.getSystemService("activity")).killBackgroundProcesses(packageName);
    }

    public static void getCacheSize(final Context context) {
        junkOfApplications.clear();
        for (final PackageInfo packageInfo : context.getPackageManager().getInstalledPackages(0)) {
            try {
                Method getPackageSizeInfo = context.getPackageManager().getClass().getMethod("getPackageSizeInfo", new Class[]{String.class, IPackageStatsObserver.class});
                try {
                    getPackageSizeInfo.invoke(context.getPackageManager(), new Object[]{packageInfo.packageName, new Stub() {
                        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
                            if (pStats.cacheSize != 0 && !context.getPackageName().equals(packageInfo.packageName) && pStats.cacheSize >= 102400) {
                                JunkOfApplication junkOfApplication = new JunkOfApplication(packageInfo.packageName, pStats.cacheSize);
                                if (!Utils.junkOfApplications.contains(junkOfApplication)) {
                                    Utils.junkOfApplications.add(junkOfApplication);
                                }
                            }
                        }
                    }});
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e2) {
                    e2.printStackTrace();
                }
            } catch (NoSuchMethodException e3) {
                e3.printStackTrace();
            }
        }
    }

    public static void clearCache(Context context) {
        junkOfApplications.clear();
        CachePackageDataObserver mClearCacheObserver = new CachePackageDataObserver();
        PackageManager mPM = context.getPackageManager();
        Class[] classes = new Class[]{Long.TYPE, IPackageDataObserver.class};
        Long localLong = Long.valueOf(CACHE_APP);
        try {
            try {
                mPM.getClass().getMethod("freeStorageAndNotify", classes).invoke(mPM, new Object[]{localLong, mClearCacheObserver});
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            } catch (InvocationTargetException e3) {
                e3.printStackTrace();
            }
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        }
    }

    public static boolean isRecommendAppLock(String packageName) {
        for (String recommendApp : new String[]{"com.android.settings", "com.android.email", "com.android.chrome", "com.google.android.youtube", "com.facebook.katana", "com.facebook.orca", "com.google.android.gm", "com.instagram.android", "com.twitter.android", "com.snapchat.android", "com.whatsapp", "com.zing.zalo", "com.facebook.lite", "com.viber.voip", "com.skype.raider", "com.dropbox.android", "com.google.android.apps.docs", "com.google.android.apps.photos"}) {
            if (recommendApp.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static List<AppLock> getAppLock(Context context) {
        AppsLocked appsLocked = new AppsLocked(context);
        List<AppLock> apps = new ArrayList();
        PackageManager packageManager = context.getPackageManager();
        Intent mainIntent = new Intent("android.intent.action.MAIN", null);
        mainIntent.addCategory("android.intent.category.LAUNCHER");
        for (ResolveInfo ri : packageManager.queryIntentActivities(mainIntent, 0)) {
            if (!appsLocked.isAppLocked(ri.activityInfo.packageName)) {
                AppLock appLock = new AppLock(getAppNameFromPackage(context, ri.activityInfo.packageName), ri.activityInfo.packageName, false);
                if (isRecommendAppLock(appLock.getPackageName())) {
                    appLock.setRecommend(true);
                }
                apps.add(appLock);
            }
        }
        return apps;
    }

    public static String getDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    @TargetApi(21)
    public static boolean isUsageAccessEnabled(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
            if (((AppOpsManager) context.getSystemService("appops")).checkOpNoThrow("android:get_usage_stats", applicationInfo.uid, applicationInfo.packageName) == 0) {
                return true;
            }
            return false;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    @TargetApi(21)
    public static void openUsageAccessSetings(Context context) {
        if (!isUsageAccessEnabled(context)) {
            Intent intent = new Intent("android.settings.USAGE_ACCESS_SETTINGS");
            intent.setFlags(268435456);
            context.startActivity(intent);
        }
    }

    public static Intent AppDetailsIntent(String packageName) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setFlags(276824064);
        intent.setData(Uri.parse("package:" + packageName));
        return intent;
    }

    public static boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        String appName = mContext.getPackageName();
        String serviceName = MyAccessibilityService.class.getSimpleName();
        try {
            accessibilityEnabled = Secure.getInt(mContext.getApplicationContext().getContentResolver(), "accessibility_enabled");
        } catch (SettingNotFoundException e) {
        }
        SimpleStringSplitter mStringColonSplitter = new SimpleStringSplitter(':');
        if (accessibilityEnabled == 1) {
            String settingValue = Secure.getString(mContext.getApplicationContext().getContentResolver(), "enabled_accessibility_services");
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
                    if (accessibilityService.contains(appName) && accessibilityService.contains(serviceName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static Intent getHomeIntent() {
        Intent startHomescreen = new Intent("android.intent.action.MAIN");
        startHomescreen.addCategory("android.intent.category.HOME");
        startHomescreen.addFlags(268435456);
        return startHomescreen;
    }

    public static void hideKeyBoard(Context context, EditText editText) {
        ((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }
}
