package com.tars.mcwa.utils.AutoUpgrade.internal;

public interface VersionDialogListener {
	void doUpdate(boolean laterOnWifi);
	void doIgnore();
}
