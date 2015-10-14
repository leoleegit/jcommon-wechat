package org.jcommon.com.wechat.jiaoka;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jxl.write.WriteException;

import org.jcommon.com.wechat.jiaoka.db.bean.Case;
import org.jcommon.com.wechat.jiaoka.service.ExportService;
import org.jcommon.com.wechat.jiaoka.service.excel.Excel;
import org.json.JSONException;

public class ExportTest {

	/**
	 * @param args
	 * @throws JSONException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws WriteException 
	 */
	public static void main(String[] args) throws WriteException, FileNotFoundException, IOException, JSONException {
		// TODO Auto-generated method stub
		test2();
	}
	
	public static void test2() throws WriteException, FileNotFoundException, IOException, JSONException{
		File file = new File(System.getProperty("user.dir"),"test.xls");
		ExportService ex = new ExportService();
		
		String name = "testname";
		Case title  = new Case();
		title.setCase_id("CaseID");
		title.setHandle_agent("HandleAgent");
		title.setJiaoka_type("Type");
		
		List<Case> data = new ArrayList<Case>();
		for(int i=0;i<3;i++){
			Case c  = new Case();
			c.setCase_id("CaseID-"+i);
			c.setHandle_agent("HandleAgent-"+i);
			c.setJiaoka_type("Type-"+i);
			data.add(c);
		}
		Excel cel = new Excel(null);
		cel.setName(name);
		cel.setData(org.jcommon.com.wechat.data.JsonObject.list2Json(data));
		cel.setTitle(title.toJson());
		System.out.println(org.jcommon.com.wechat.data.JsonObject.list2Json(data));
		ex.exportExcel(cel, new FileOutputStream(file));
	}
	
	public static void test1(){
		List<String> keys = new ArrayList<String>();
		keys.add("4");
		keys.add("2");
		keys.add("3");
		keys.add("1");
		Collections.sort(keys, new Comparator<String>(){

			@Override
			public int compare(String o1, String o2) {
				// TODO Auto-generated method stub
				Integer i1 = Integer.valueOf(o1);
				Integer i2 = Integer.valueOf(o2);
				return i1.compareTo(i2);
			}
			
		});
		for(int j=0;j<keys.size();j++){
			System.out.println(keys.get(j));
		}
	}

}
