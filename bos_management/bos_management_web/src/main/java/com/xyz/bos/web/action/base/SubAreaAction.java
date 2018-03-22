package com.xyz.bos.web.action.base;

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

import com.xyz.bos.domain.base.Standard;
import com.xyz.bos.domain.base.SubArea;
import com.xyz.bos.service.base.SubAreaService;
import com.xyz.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;

/**
 * ClassName:SubAreaAction <br/>
 * Function: <br/>
 * Date: 2018年3月16日 上午9:41:27 <br/>
 */
@Namespace("/") // 等价于struts.xml文件中package节点namespace属性
@ParentPackage("struts-default") // 等价于struts.xml文件中package节点extends属性
@Controller // spring 的注解,控制层代码
@Scope("prototype") // spring 的注解,多例
public class SubAreaAction extends CommonAction<SubArea> {
    public SubAreaAction() {
        super(SubArea.class);
    }

    @Autowired
    private SubAreaService subAreaService;
    @Action(value = "subAreaAction_save", results = {@Result(name = "success",
            location = "/pages/base/sub_area.html", type = "redirect")})
    public String save() {
        subAreaService.save(getModel());
        return SUCCESS;
    }

    @Action(value = "subAction_pageQuery")
    public String pageQuery() throws IOException {
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<SubArea> page = subAreaService.findAll(pageable);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"subareas"});
        page2json(page, jsonConfig);
        return NONE;
    }
}