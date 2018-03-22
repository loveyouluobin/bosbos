package com.xyz.bos.web.action.base;

import java.io.IOException;

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

import com.xyz.bos.domain.base.FixedArea;
import com.xyz.bos.service.base.FixedAreaService;
import com.xyz.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class FixedAreaAction extends CommonAction<FixedArea> {
	public FixedAreaAction() {
		super(FixedArea.class);
	}
	//增加定区
	@Autowired
private FixedAreaService fixedAreaService;
	@Action(value="fixedAreaAction_save",results={
			@Result(location="/pages/base/fixed_area.html",name="success",type="redirect")
	})
	public String save(){
		fixedAreaService.save(getModel());
		return SUCCESS;
	}
	//分页展示
	@Action(value="fixedAreaAction_pageQuery")
	public String pageQuery() throws IOException{
		Pageable pageable = new PageRequest(page-1, rows);
		Page<FixedArea>page=fixedAreaService.findAll(pageable);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[]{"subareas","couriers"});
		page2json(page, jsonConfig);
		return NONE;
	}

}
