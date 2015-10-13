package org.jcommon.com.wechat.jiaoka.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.jcommon.com.wechat.jiaoka.db.bean.ServiceResponse;
import org.jcommon.com.wechat.jiaoka.service.excel.Excel;
import org.jcommon.com.wechat.media.ContentType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Path("export")
public class ExportService extends Service{
	
	@POST 
	@Path("excel")
	@Produces("text/plain;charset=UTF-8")  
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String exportExcel(InputStream is, @Context HttpServletResponse response){
		try {
			response.setCharacterEncoding("UTF-8");
		    StringBuilder xml = new StringBuilder();
		    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		    String line;
		    while ((line = reader.readLine()) != null) {
		        xml.append(line);
		    }
		    reader.close();
		    String post_data = xml.toString();
		    Excel excel      = new Excel(post_data);
		    response.reset();  
			response.setContentType( ContentType.xls.type ); 
            response.addHeader( "Content-Disposition" ,  "attachment;filename=\""   +   excel.getName()+".xls\"");
            exportExcel(excel,response.getOutputStream());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);
			return new ServiceResponse("system error.").toJson();
		}
       
		return new ServiceResponse(ServiceResponse.SUCCESS,"export excel file done.").toJson();
	}
	
	public void exportExcel(Excel excel, OutputStream os) throws IOException, WriteException, JSONException{
		WritableWorkbook workbook = Workbook.createWorkbook(os);
		WritableSheet sheet = workbook.createSheet(excel.getName(),0);
		JSONObject title = excel.getJsonTitle();
		JSONArray  data  = excel.getJsonData();
		int i=0,j;
		if(title!=null){
			@SuppressWarnings("unchecked")
			Iterator<Object> it = title.keys();
			for(i=0,j=0;it.hasNext();j++){
				String key = (String) it.next();
				String value = title.getString(key);
				Label lable  = new Label(j,i,value);
		        sheet.addCell(lable);
			}
			++i;
		}
		if(data!=null){
			for(int n=0;n<data.length();n++){
				JSONObject json = data.getJSONObject(n);
				@SuppressWarnings("unchecked")
				Iterator<Object> it = json.keys();
				for(j=0;it.hasNext();j++){
					String key = (String) it.next();
					String value = json.getString(key);
					Label lable  = new Label(j,i,value);
			        sheet.addCell(lable);
				}
				++i;
			}
		}
		
		workbook.write();
        workbook.close();
        os.close();
        os.flush();
	}
}






