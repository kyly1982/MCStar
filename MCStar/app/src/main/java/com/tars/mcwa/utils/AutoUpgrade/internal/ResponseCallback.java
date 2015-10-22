package com.tars.mcwa.utils.AutoUpgrade.internal;


import com.tars.mcwa.utils.AutoUpgrade.Version;

public interface ResponseCallback {
	void onFoundLatestVersion(Version version);
	void onCurrentIsLatest();
}
