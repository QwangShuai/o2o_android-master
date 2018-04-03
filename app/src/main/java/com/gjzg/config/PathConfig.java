package com.gjzg.config;


import android.os.Environment;

import java.io.File;

public interface PathConfig {

    public static final String sdCardPath = Environment.getExternalStorageDirectory().getPath();
    public static final String cameraPath = sdCardPath + File.separator + "DCIM" + File.separator + "Camera" + File.separator;
}
