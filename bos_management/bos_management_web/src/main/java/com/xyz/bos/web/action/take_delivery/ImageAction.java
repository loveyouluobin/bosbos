package com.xyz.bos.web.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.junit.Test;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionSupport;

@Namespace("/") // 访问这个Action目录
@ParentPackage("struts-default") //继承struts属性
@Controller // spring 的注解,控制层代码
@Scope("prototype")//多实现
public class ImageAction extends ActionSupport {
	
private File imgFile; // 使用属性驱动获取用户上传的文件
public void setImgFile(File imgFile) {
	this.imgFile = imgFile;}

private String imgFileFileName;//使用属性驱动获取用户上传的文件名
public void setImgFileFileName(String imgFileFileName) {
    this.imgFileFileName = imgFileFileName;}

@Action(value="imageAction_upload")
public String upload() throws IOException{
	HashMap<String ,Object> map = new HashMap<>();
	try {
		String dirPath="/upload";// 指定保存图片的文件夹
		ServletContext servletContext = ServletActionContext.getServletContext();//项目路径
		String dirRealPath = servletContext.getRealPath(dirPath);//获取要保存图片的项目文件夹绝对路径
		String suffix = imgFileFileName.substring(imgFileFileName.lastIndexOf("."));//获取文件名的后缀
		String fileName=UUID.randomUUID().toString().replace("-", "")+suffix;//uuid生成要保存的文件名
		File destFile = new File(dirRealPath+"/"+fileName);//路径+/+文件名+后缀的文件对象
		FileUtils.copyFile(imgFile, destFile);//保存文件
		String contextPath = servletContext.getContextPath();//本项目路径
		map.put("error", 0);//加入到map 错误信息
		map.put("url", contextPath + "/upload/" + fileName);//加入到map
	} catch (Exception e) {
		e.printStackTrace();
		map.put("error", 1);//上传失败时
        map.put("message", e.getMessage());
	}
	String json = net.sf.json.JSONObject.fromObject(map).toString();//转json
	HttpServletResponse response = ServletActionContext.getResponse();//流
    response.setContentType("application/json;charset=UTF-8");
    response.getWriter().write(json);//写到流 回显
	return NONE;
	
}
//属性驱动获取文件类型
private String imgFileContentType;

public void setImgFileContentType(String imgFileContentType) {
    this.imgFileContentType = imgFileContentType;
    
}



}
