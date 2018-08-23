package com.perisaimobile.adapter;


import com.perisaimobile.iface.IProblem;
import com.perisaimobile.model.AppProblem;
import com.perisaimobile.model.SystemProblem;

class ResultsAdapterProblemItem implements IResultsAdapterItem {
    IProblem _problem = null;

    public ResultsAdapterProblemItem(IProblem problem) {
        this._problem = problem;
    }

    public IProblem getProblem() {
        return this._problem;
    }

    public AppProblem getAppProblem() throws ClassCastException {
        if (AppProblem.class.isAssignableFrom(this._problem.getClass())) {
            return (AppProblem) this._problem;
        }
        throw new ClassCastException();
    }

    public SystemProblem getSystemProblem() throws ClassCastException {
        if (SystemProblem.class.isAssignableFrom(this._problem.getClass())) {
            return (SystemProblem) this._problem;
        }
        throw new ClassCastException();
    }

    public ResultsAdapterItemType getType() {
        return this._problem.getType() == IProblem.ProblemType.AppProblem ? ResultsAdapterItemType.AppMenace : ResultsAdapterItemType.SystemMenace;
    }
}
