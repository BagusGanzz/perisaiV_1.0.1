package com.perisaimobile.model;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.perisaimobile.iface.IProblem;
import com.perisaimobile.util.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Scanner {
    public static Set<GoodPackageResultData> scanForWhiteListedApps(List<PackageInfo> packagesToSearch, Set<PackageData> whiteListPackages, Set<GoodPackageResultData> result) {
        Set<GoodPackageResultData> subResult = new HashSet();
        for (PackageData pd : whiteListPackages) {
            getPackagesByNameFilter(packagesToSearch, pd.getPackageName(), subResult);
            result.addAll(subResult);
        }
        return result;
    }

    public static boolean isAppWhiteListed(String packageName, Set<PackageData> whiteListPackages) {
        for (PackageData pd : whiteListPackages) {
            if (Utils.stringMatchesMask(packageName, pd.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static Set<IProblem> scanForBlackListedActivityApps(List<PackageInfo> packagesToSearch, Set<PackageData> blackListedActivityPackages, Set<IProblem> setToUpdate) {
        List<ActivityInfo> subResult = new ArrayList();
        for (PackageInfo pi : packagesToSearch) {
            for (PackageData pd : blackListedActivityPackages) {
                getActivitiesByNameFilter(pi, pd.getPackageName(), subResult);
                if (subResult.size() > 0) {
                    AppProblem bprd = getBadPackageResultByPackageName(setToUpdate, pi.packageName);
                    if (bprd == null) {
                        bprd = new AppProblem(pi.packageName);
                        setToUpdate.add(bprd);
                    }
                    for (ActivityInfo ai : subResult) {
                        bprd.addActivityData(new ActivityData(ai.name));
                    }
                }
            }
        }
        return setToUpdate;
    }

    public static AppProblem scanForBlackListedActivityApp(PackageInfo pi, AppProblem bprdToFill, Set<PackageData> blackListedActivityPackages, List<ActivityInfo> arrayToRecycle) {
        for (PackageData pd : blackListedActivityPackages) {
            getActivitiesByNameFilter(pi, pd.getPackageName(), arrayToRecycle);
            if (arrayToRecycle.size() > 0) {
                for (ActivityInfo ai : arrayToRecycle) {
                    bprdToFill.addActivityData(new ActivityData(ai.packageName));
                }
            }
        }
        return bprdToFill;
    }

    public static AppProblem scanForBlackListedActivityApp(Context context, String packageName, Set<PackageData> blackListedActivityPackages) {
        PackageInfo pi;
        try {
            pi = Utils.getPackageInfo(context, packageName, 1);
        } catch (NameNotFoundException e) {
            pi = null;
        }
        if (pi == null) {
            return null;
        }
        AppProblem bprd = new AppProblem(pi.packageName);
        scanForBlackListedActivityApp(pi, bprd, blackListedActivityPackages, new ArrayList());
        return bprd;
    }

    public static Set<IProblem> scanForSuspiciousPermissionsApps(List<PackageInfo> packagesToSearch, Set<PermissionData> suspiciousPermissions, Set<IProblem> setToUpdate) {
        for (PackageInfo pi : packagesToSearch) {
            AppProblem bprd = getBadPackageResultByPackageName(setToUpdate, pi.packageName);
            if (bprd == null) {
                bprd = new AppProblem(pi.packageName);
            }
            scanForSuspiciousPermissionsApp(pi, bprd, (Set) suspiciousPermissions);
            if (bprd.getPermissionData().size() > 0) {
                setToUpdate.add(bprd);
            }
        }
        return setToUpdate;
    }

    public static AppProblem scanForSuspiciousPermissionsApp(PackageInfo pi, AppProblem bprdToFill, Set<PermissionData> suspiciousPermissions) {
        for (PermissionData permData : suspiciousPermissions) {
            if (Utils.packageInfoHasPermission(pi, permData.getPermissionName())) {
                bprdToFill.addPermissionData(permData);
            }
        }
        return bprdToFill;
    }

    public static AppProblem scanForSuspiciousPermissionsApp(Context context, String packageName, Set<PermissionData> suspiciousPermissions) {
        PackageInfo pi;
        try {
            pi = Utils.getPackageInfo(context, packageName, 1);
        } catch (NameNotFoundException e) {
            pi = null;
        }
        if (pi == null) {
            return null;
        }
        return scanForSuspiciousPermissionsApp(pi, new AppProblem(pi.packageName), (Set) suspiciousPermissions);
    }

    public static AppProblem getBadPackageResultByPackageName(Set<IProblem> prd, String packageName) {
        for (IProblem p : prd) {
            if (p.getType() == IProblem.ProblemType.AppProblem) {
                AppProblem temp = (AppProblem) p;
                if (temp.getPackageName().equals(packageName)) {
                    return temp;
                }
            }
        }
        return null;
    }

    public static Set<GoodPackageResultData> getPackagesByNameFilter(List<PackageInfo> packages, String filter, Set<GoodPackageResultData> result) {
        boolean wildcard = false;
        result.clear();
        if (filter.charAt(filter.length() - 1) == '*') {
            wildcard = true;
        }
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packInfo = (PackageInfo) packages.get(i);
            if (Utils.stringMatchesMask(packInfo.packageName, filter)) {
                result.add(new GoodPackageResultData(packInfo));
                if (!wildcard) {
                    break;
                }
            }
        }
        return result;
    }

    public static List<ActivityInfo> getActivitiesByNameFilter(PackageInfo pi, String filter, List<ActivityInfo> result) {
        result.clear();
        if (pi.activities != null) {
            if (filter.charAt(filter.length() - 1) == '*') {
                filter = filter.substring(0, filter.length() - 2);
            }
            for (ActivityInfo activityInfo : pi.activities) {
                if (activityInfo.name.startsWith(filter)) {
                    result.add(activityInfo);
                }
            }
        }
        return result;
    }

    public static Set<IProblem> scanInstalledAppsFromGooglePlay(Context context, List<PackageInfo> packagesToSearch, Set<IProblem> setToUpdate) {
        for (PackageInfo pi : packagesToSearch) {
            AppProblem bprd;
            if (Utils.checkIfAppWasInstalledThroughGooglePlay(context, pi.packageName)) {
                bprd = getBadPackageResultByPackageName(setToUpdate, pi.packageName);
                if (bprd != null) {
                    bprd.setInstalledThroughGooglePlay(true);
                }
            } else {
                bprd = getBadPackageResultByPackageName(setToUpdate, pi.packageName);
                if (bprd == null) {
                    bprd = new AppProblem(pi.packageName);
                    setToUpdate.add(bprd);
                }
                bprd.setInstalledThroughGooglePlay(false);
            }
        }
        return setToUpdate;
    }

    public static AppProblem scanInstalledAppFromGooglePlay(Context context, AppProblem bprd) {
        if (Utils.checkIfAppWasInstalledThroughGooglePlay(context, bprd.getPackageName())) {
            bprd.setInstalledThroughGooglePlay(true);
        } else {
            bprd.setInstalledThroughGooglePlay(false);
        }
        return bprd;
    }

    public static Set<IProblem> scanSystemProblems(Context context, UserWhiteList whiteList, Set<IProblem> setToUpdate) {
        if (Utils.checkIfUSBDebugIsEnabled(context) && !whiteList.checkIfSystemPackageInList(DebugUSBEnabledProblem.class)) {
            setToUpdate.add(new DebugUSBEnabledProblem());
        }
        if (Utils.checkIfUnknownAppIsEnabled(context) && !whiteList.checkIfSystemPackageInList(UnknownAppEnabledProblem.class)) {
            setToUpdate.add(new UnknownAppEnabledProblem());
        }
        return setToUpdate;
    }
}
