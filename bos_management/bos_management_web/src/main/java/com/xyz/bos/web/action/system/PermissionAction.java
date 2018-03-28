package com.xyz.bos.web.action.system;

import java.io.IOException;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.xyz.bos.domain.system.Menu;
import com.xyz.bos.domain.system.Permission;
import com.xyz.bos.service.system.PermissionService;
import com.xyz.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;

@Namespace("/") // 访问这个Action路径
@ParentPackage("struts-default") //继承struts属性
@Controller // spring 的注解,控制层代码
@Scope("prototype")//多实现
public class PermissionAction extends CommonAction<Permission> {
	public PermissionAction() {
		super(Permission.class);
	}
@Autowired
private PermissionService permissionService;

	@Action(value="permissionAction_pageQuery")//分页展示
	public String pageQuery() throws IOException{
		Pageable pageable=new PageRequest(page-1,rows);//
		Page<Permission> page=  permissionService.findAll(pageable);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[]{"roles"});
		page2json(page, jsonConfig);
		return NONE;
	}

	@Action(value="permissionAction_save",results={
			@Result(name="success",location="/pages/system/permission.html",type="redirect")
	})//保存
	public String save() throws IOException{
		permissionService.save(getModel());
		return SUCCESS;
	}
	
	
	@Action(value="permissionAction_findAll")
	public String findAll() throws IOException{
		Page<Permission>page=permissionService.findAll(null);//传null过去就不分页
		List<Permission> list = page.getContent();//得到list集合
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[]{"roles"});
		list2json(list, jsonConfig);
		return NONE;
	}
}
