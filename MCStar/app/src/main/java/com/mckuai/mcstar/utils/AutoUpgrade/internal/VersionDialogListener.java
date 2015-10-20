package com.mckuai.mcstar.utils.AutoUpgrade.internal;

public interface VersionDialogListener {
	void doUpdate(boolean laterOnWifi);
	void doIgnore();
}
