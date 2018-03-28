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
import com.xyz.bos.domain.system.Role;
import com.xyz.bos.service.system.RoleService;
import com.xyz.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;

@Namespace("/") // 访问这个Action路径
@ParentPackage("struts-default") //继承struts属性
@Controller // spring 的注解,控制层代码
@Scope("prototype")//多实现
public class RoleAction extends CommonAction<Role> {
	public RoleAction() {
		super(Role.class);
	}
@Autowired
private RoleService roleService;

@Action(value="roleAction_findAll")
public String findAll() throws IOException{
	Page<Role> page=  roleService.findAll(null);
	JsonConfig jsonConfig = new JsonConfig();
	jsonConfig.setExcludes(new String[] {"users", "permissions", "menus"});
	List<Role> list = page.getContent();
	list2json(list, jsonConfig);
	return NONE;
}
	@Action(value="roleAction_pageQuery")//分页展示
	public String pageQuery() throws IOException{
		Pageable pageable=new PageRequest(page-1,rows);//
		Page<Role> page=  roleService.findAll(pageable);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[]{"users", "permissions", "menus"});
		page2json(page, jsonConfig);
		return NONE;
	}

	private String menuTds; //属性驱动获得菜单
	public void setMenuTds(String menuTds) {
		this.menuTds = menuTds;	}
	private Long[] permissionIds;//属性驱动获得权限id数组
	public void setPermissionIds(Long[] permissionIds) {
		this.permissionIds = permissionIds;	}
	

	@Action(value="roleAction_save",results={
			@Result(name="success",location="/pages/system/role.html",type="redirect")
	})//保存
	public String save() throws IOException{
		roleService.save(getModel(),menuTds,permissionIds);
		return SUCCESS;
	}

}
