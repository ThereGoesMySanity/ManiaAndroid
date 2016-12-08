package com.mcpubba.game.util;

/**
 * Copied from StackOverflow on 2016-12-04
 */

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.badlogic.gdx.Gdx;

public class DecompressFast {



    private String _zipFile;
    private String _location;

    public DecompressFast(String zipFile, String location) {
        _zipFile = zipFile;
        _location = location;

        _dirChecker("");
    }

    public void unzip() {
        try  {
            FileInputStream fin = new FileInputStream(_zipFile);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                Gdx.app.log("Decompress", "Unzipping " + ze.getName());

                if(ze.isDirectory()) {
                    _dirChecker(ze.getName());
                } else {
                    FileOutputStream fout = new FileOutputStream(_location + ze.getName());
                    BufferedOutputStream bufout = new BufferedOutputStream(fout);
                    byte[] buffer = new byte[1024];
                    int read = 0;
                    while ((read = zin.read(buffer)) != -1) {
                        bufout.write(buffer, 0, read);
                    }




                    bufout.close();

                    zin.closeEntry();
                    fout.close();
                }

            }
            zin.close();


            Gdx.app.log("Unzip", "Unzipping complete. path :  " +_location );
        } catch(Exception e) {
            Gdx.app.log("Decompress", "unzip", e);

            Gdx.app.log("Unzip", "Unzipping failed");
        }

    }

    private void _dirChecker(String dir) {
        File f = new File(_location + dir);

        if(!f.isDirectory()) {
            f.mkdirs();
        }
    }


}