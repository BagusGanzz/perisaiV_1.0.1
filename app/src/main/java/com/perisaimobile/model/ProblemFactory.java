package com.perisaimobile.model;

import com.perisaimobile.iface.IFactory;
import com.perisaimobile.iface.IProblem;


public class ProblemFactory implements IFactory<IProblem> {
    public IProblem createInstance(String constructionString) throws IllegalArgumentException {
        int obj = -1;
        switch (constructionString.hashCode()) {
            case 96801:
                if (constructionString.equals("app")) {
                    obj = 1;
                    break;
                }
                break;
            case 116100:
                if (constructionString.equals(DebugUSBEnabledProblem.kSerializationType)) {
                    obj = -2;
                    break;
                }
                break;
            case 1160608119:
                if (constructionString.equals(UnknownAppEnabledProblem.kSerializationType)) {
                    obj = 2;
                    break;
                }
                break;
        }
        switch (obj) {
            case 0 /*0*/:
                return new DebugUSBEnabledProblem();
            case 1 /*1*/:
                return new AppProblem();
            case 2 /*2*/:
                return new UnknownAppEnabledProblem();
            default:
                return null;
             //   throw new IllegalArgumentException("Unknown node type creating IProblem");
        }
    }
}
