package com.perisaimobile.util;

import android.content.Context;

import com.perisaimobile.iface.IDataSet;
import com.perisaimobile.iface.IProblem;
import com.perisaimobile.iface.IProblem.ProblemType;

import com.perisaimobile.model.AppProblem;
import com.perisaimobile.model.PackageData;
import com.perisaimobile.model.SystemProblem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProblemsDataSetTools {
    public static Set<IProblem> getAppProblemsSet(IDataSet<? extends IProblem> problems) {
        Set<IProblem> hashSet = new HashSet();
        getAppProblems(problems.getSet(), hashSet);
        return hashSet;
    }

    public static List<IProblem> getAppProblemsList(IDataSet<? extends IProblem> problems) {
        List<IProblem> list = new ArrayList();
        getAppProblems(problems.getSet(), list);
        return list;
    }

    public static Set<IProblem> getSystemProblemsSet(IDataSet<? extends IProblem> problems) {
        Set<IProblem> hashSet = new HashSet();
        getSystemProblems(problems.getSet(), hashSet);
        return hashSet;
    }

    public static List<IProblem> getSystemProblemsList(IDataSet<? extends IProblem> problems) {
        List<IProblem> list = new ArrayList();
        getSystemProblems(problems.getSet(), list);
        return list;
    }

    public static Collection<? extends IProblem> getAppProblems(Collection<? extends IProblem> problems, Collection<IProblem> target) {
        for (IProblem p : problems) {
            if (p.getType() == ProblemType.AppProblem) {
                target.add(p);
            }
        }
        return target;
    }

    public static Collection<? extends IProblem> getSystemProblems(Collection<? extends IProblem> problems, Collection<IProblem> target) {
        for (IProblem p : problems) {
            if (p.getType() == ProblemType.SystemProblem) {
                target.add(p);
            }
        }
        return target;
    }

    public static Set<PackageData> getAppProblemsAsPackageDataList(IDataSet<? extends IProblem> problems) {
        Set<PackageData> pd = new HashSet();
        for (IProblem p : problems.getSet()) {
            if (p.getType() == ProblemType.AppProblem) {
                pd.add((AppProblem) p);
            }
        }
        return pd;
    }

    public static boolean checkIfPackageInCollection(String packageName, Collection<IProblem> problems) {
        for (IProblem p : problems) {
            if (p.getType() == ProblemType.AppProblem && ((AppProblem) p).getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean removeNotExistingProblems(Context context, IDataSet<IProblem> dataSet) {
        boolean dirty = false;
        ArrayList<IProblem> toRemove = new ArrayList();
        Set<IProblem> problems = dataSet.getSet();
        for (IProblem p : problems) {
            if (!p.problemExists(context)) {
                toRemove.add(p);
                dirty = true;
            }
        }
        problems.removeAll(toRemove);
        return dirty;
    }

    public static boolean removeAppProblemByPackage(IDataSet<IProblem> dataSet, String packageName) {
        for (IProblem p : dataSet.getSet()) {
            if (p.getType() == ProblemType.AppProblem && packageName.equals(((AppProblem) p).getPackageName())) {
                return dataSet.removeItem(p);
            }
        }
        return false;
    }

    public static void printProblems(IDataSet<IProblem> dataSet) {
        for (IProblem p : dataSet.getSet()) {
            if (p.getType() == ProblemType.AppProblem) {
                AppProblem p2 = (AppProblem) p;
            } else {
                SystemProblem p3 = (SystemProblem) p;
            }
        }
    }
}
