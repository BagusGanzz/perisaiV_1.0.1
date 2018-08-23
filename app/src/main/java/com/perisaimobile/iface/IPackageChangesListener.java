package com.perisaimobile.iface;

import android.content.Intent;

public interface IPackageChangesListener {
    void OnPackageAdded(Intent intent);

    void OnPackageRemoved(Intent intent);
}
