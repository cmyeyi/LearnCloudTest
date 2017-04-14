package com.aile.www.basesdk.net.file;


import com.aile.www.basesdk.ProguardKeeper;

public final class UploadFile extends ProguardKeeper {
	private String filePath;

	public UploadFile(String filePath) {
		this.filePath = filePath;
	}

	public String getFilePath() {
		return filePath;
	}

	
	
}
