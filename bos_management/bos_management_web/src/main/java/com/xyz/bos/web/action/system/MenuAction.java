package com.xyz.bos.web.action.system;

import java.io.IOException;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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
import com.xyz.bos.domain.system.User;
import com.xyz.bos.service.system.MenuService;
import com.xyz.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;
@Namespace("/") // 访问这个Action路径
@ParentPackage("struts-default") //继承struts属性
@Controller // spring 的注解,控制层代码
@Scope("prototype")//多实现
public class MenuAction extends CommonAction<Menu> {
	public MenuAction() {
		super(Menu.class);
	}
	@Autowired
private MenuService menuService;
	
	@Action(value="menuAction_pageQuery")//分页展示菜单管理
	public String pageQuery() throws IOException{
		Pageable pageable=new PageRequest(Integer.parseInt(getModel().getPage())-1,rows);//
		Page<Menu> page= (Page<Menu>) menuService.findAll(pageable);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[]{"roles","childrenMenus","parentMenu"});
		page2json(page, jsonConfig);
		return NONE;
	}
	@Action(value="menuAction_findLevelOne")//查询出父菜单项
	public String findLevelOne() throws IOException{
	List<Menu>list=menuService.findLevelOne();
	JsonConfig jsonConfig = new JsonConfig();
	jsonConfig.setExcludes(new String[]{"roles","childrenMenus","parentMenu"});
	list2json(list, jsonConfig);
	return NONE;
	}
	@Action(value="menuAction_findbyUser")//根据不同用户角色查询出菜单树
	public String findbyUser() throws IOException{
		Subject subject = SecurityUtils.getSubject();//获取当前用户权限
		User user = (User) subject.getPrincipal();//强转为当前用户
		List<Menu>list= menuService.findbyUser(user);//查出角色菜单
		JsonConfig jsonConfig = new JsonConfig();
		//忽略children不需要标准json创建菜单树 用简单json方式需要pid在user实体加get方法
		jsonConfig.setExcludes(new String[]{"roles", "childrenMenus", "parentMenu","children"});//忽略不要转json字段
		list2json(list, jsonConfig);
		return NONE;
	}
	
	@Action(value="menuAction_save",results={
			@Result(name="success",location="/pages/system/menu.html",type="redirect")
	})//保存
	public String save() throws IOException{
		menuService.save(getModel());
		return SUCCESS;
	}
}
