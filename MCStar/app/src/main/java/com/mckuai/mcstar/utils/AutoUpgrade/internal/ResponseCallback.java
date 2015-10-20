package com.mckuai.mcstar.utils.AutoUpgrade.internal;


import com.mckuai.mcstar.utils.AutoUpgrade.Version;

public interface ResponseCallback {
	void onFoundLatestVersion(Version version);
	void onCurrentIsLatest();
}
