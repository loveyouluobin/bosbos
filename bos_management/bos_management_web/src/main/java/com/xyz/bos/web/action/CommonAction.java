package com.xyz.bos.web.action;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class CommonAction<T> extends ActionSupport implements ModelDriven<T> {
    private T model;//声明T类型model实体对象
    private Class<T> clazz;
    public CommonAction(Class<T> clazz) {//用构造函数 初始化new model
        this.clazz = clazz;
        try {
            model = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }    }
    @Override//模型驱动返回model实体
    public T getModel() {
        return model;    }
	    
    protected int page;// 第几页 使用属性驱动获取数据
    protected int rows;// 每一页显示多少条数据
    public void setPage(int page) {
        this.page = page;    }
    public void setRows(int rows) { 
	this.rows = rows;    }
//把分页的map转json
public void page2json(Page<T> page, JsonConfig jsonConfig)throws IOException {
	long total = page.getTotalElements();// 查出总数据条数
	List<T> list = page.getContent();// 当前页要实现的内容
	Map<String, Object> map = new HashMap<>();// 声明封装数据的map
	map.put("total", total);//增加到map
	map.put("rows", list);
	String json;//声明json
	if (jsonConfig != null) {//有排除对象/不是空 就排除再转成json
	    json = JSONObject.fromObject(map, jsonConfig).toString();
	} else {//没有排除的对象就直接转
	    json = JSONObject.fromObject(map).toString();        }
	HttpServletResponse response = ServletActionContext.getResponse();//响应流对象
	response.setContentType("application/json;charset=UTF-8");//设置编码
	response.getWriter().write(json);//写到流
	}
//list转json
    public void list2json(List list, JsonConfig jsonConfig) throws IOException {
        String json;//声明json
        if (jsonConfig != null) {//有排除对象/不是空 就排除再转成json
            json = JSONArray.fromObject(list, jsonConfig).toString();
        } else {//没有排除的对象就直接转
            json = JSONArray.fromObject(list).toString();        }
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);//写到流
    }

}
