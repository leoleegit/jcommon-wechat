package org.jcommon.com.wechat.jiaoka.service;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.jcommon.com.wechat.MediaManager;
import org.jcommon.com.wechat.data.Media;
import org.jcommon.com.wechat.jiaoka.db.bean.ServiceResponse;
import org.jcommon.com.wechat.jiaoka.service.excel.Excel;
import org.jcommon.com.wechat.jiaoka.service.resp.Url;
import org.jcommon.com.wechat.jiaoka.utils.JiaoKaUtils;
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
	public String exportExcel(InputStream is){
		try {
		    StringBuilder xml = new StringBuilder();
		    
		    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		    char[] cbuf = new char[1024];
		    int nRead;
		    while ((nRead=reader.read(cbuf))!=-1) {
		        xml.append(cbuf, 0, nRead);
		    }
		    reader.close();
		    String post_data = xml.toString();
		    logger.info(post_data);
		    Excel excel          = new Excel(post_data);
			String file_id       = org.jcommon.com.util.BufferUtils.generateRandom(6);
			String file_name     = excel.getName()+"-"+file_id+".xls";
			String content_type  = ContentType.xls.type;
		    
			Media media = new Media();
			media.setContent_type(content_type);
			media.setMedia_id(file_id);
			media.setMedia_name(file_name);
			java.io.File file  = MediaManager.getMedia_factory().createEmptyFile(media);
			media.setMedia(file);
			exportExcel(excel,new FileOutputStream(file));
			Url url = new Url(MediaManager.getMedia_factory().createUrl(media).getUrl());
			return new ServiceResponse(url).toJson();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);
			return new ServiceResponse("system error.").toJson();
		}
	}
	
	private List<String> sort(Iterator<Object> it){
		List<String> keys = new ArrayList<String>();
		for(;it.hasNext();){
			String key = (String) it.next();
			keys.add(key);
		}
		Collections.sort(keys, new Comparator<String>(){

			@Override
			public int compare(String o1, String o2) {
				// TODO Auto-generated method stub
				if(o1==null || o2==null)
					return 0;
				if(JiaoKaUtils.isInteger(o1) && JiaoKaUtils.isInteger(o1)){
					Integer i1 = Integer.valueOf(o1);
					Integer i2 = Integer.valueOf(o2);
					return i1.compareTo(i2);
				}
				return o1.compareTo(o2);
			}
			
		});
		return keys;
	}
	
	public void exportExcel(Excel excel, OutputStream os) throws IOException, WriteException, JSONException{
		WritableWorkbook workbook = Workbook.createWorkbook(os);
		WritableSheet sheet = workbook.createSheet(excel.getName(),0);
		JSONObject title = excel.getJsonTitle();
		JSONArray  data  = excel.getJsonData();
		int i=0,j;
		if(title!=null){
			@SuppressWarnings("unchecked")
			Iterator<Object> it = title.sortedKeys();
			List<String> keys   = sort(it);
			
			for(j=0;j<keys.size();j++){
				String key = keys.get(j);
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
				Iterator<Object> it = json.sortedKeys();
				List<String> keys   = sort(it);
				
				for(j=0;j<keys.size();j++){
					String key = keys.get(j);
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






