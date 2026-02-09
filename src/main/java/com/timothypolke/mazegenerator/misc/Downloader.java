package com.timothypolke.mazegenerator.misc;

import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.lang.ClassNotFoundException;

public class Downloader {
	public byte[] serialize(ArrayList<String> files, String version, String type) {
		ByteArrayOutputStream byteArrayOutputStream = null;
		ZipOutputStream zipOut = null;
		ZipEntry zipEntry = null;
		byte[] bytes = null;
		try{
			byteArrayOutputStream = new ByteArrayOutputStream();
			zipOut = new ZipOutputStream(byteArrayOutputStream);
			byte[] decodedData = null;
			for (int i = 0; i < files.size(); i++) {
				decodedData = null;
				if (type.equals("png")){
					decodedData = Base64.getDecoder().decode(files.get(i));
				}
				else if (type.equals("obj")){
					decodedData = files.get(i).getBytes();
				}
				zipOut.putNextEntry(new ZipEntry(version+"_"+i+"."+type));
				zipOut.write(decodedData);
				zipOut.closeEntry();
			}
			zipOut.finish();
			zipOut.close();
			bytes = byteArrayOutputStream.toByteArray();
			byteArrayOutputStream.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		return bytes;
	}
}