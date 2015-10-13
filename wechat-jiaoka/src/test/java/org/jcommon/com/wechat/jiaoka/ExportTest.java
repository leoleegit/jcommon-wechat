package org.jcommon.com.wechat.jiaoka;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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

}
