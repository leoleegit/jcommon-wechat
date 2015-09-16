package org.jcommon.com.wechat.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.oracle.jrockit.jfr.ContentType;

public class FileTest {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String dir = System.getProperty("user.dir");
		File file  = new File(dir,"type.txt");
//		if(file.exists()){
//			FileInputStream is = new FileInputStream(file);
//			InputStreamReader read = new InputStreamReader(
//					is, "UTF-8");
//			BufferedReader reader = new BufferedReader(read);
//			String line;
//			try {
//				while ((line = reader.readLine()) != null) {
//					String str1 = line.split(":")[0];
//					String str2 = line.split(":")[1];
//					str1 = str1.replace("'", "");
//					str2 = str2.replace("'", "");
//					System.out.println(str1.substring(1) + "(\""+str1+"\",\""+str2+"\"),");
//				}
//				reader.close();
//				read.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		
		System.out.println(org.jcommon.com.wechat.media.ContentType.getContentType(file).type);
	}

}
