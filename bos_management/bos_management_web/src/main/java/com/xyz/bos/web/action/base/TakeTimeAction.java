package com.xyz.bos.web.action.base;
import java.io.IOException;
import java.util.List;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xyz.bos.domain.base.TakeTime;
import com.xyz.bos.service.base.TakeTimeService;
import com.xyz.bos.web.action.CommonAction;
@Namespace("/") // 等价于struts.xml文件中package节点namespace属性
@ParentPackage("struts-default") // 等价于struts.xml文件中package节点extends属性
@Controller // spring 的注解,控制层代码
@Scope("prototype") // spring 的注解,多例
public class TakeTimeAction extends CommonAction<TakeTime> {
	public TakeTimeAction() {
		super(TakeTime.class);	}
	@Autowired
	private TakeTimeService takeTimeService;
		
    @Action("takeTimeAction_listajax")// 查询所有收派时间
    public String listajax() throws IOException {
    	List<TakeTime> list=takeTimeService.findAll();
    	list2json(list, null);
    	return NONE;
    }
	
	
}
