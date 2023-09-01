package com.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;

import com.entity.ChukujiluEntity;
import com.entity.view.ChukujiluView;

import com.service.ChukujiluService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.CommonUtil;


/**
 * 出库记录
 * 后端接口
 * @author 
 * @email 
 * @date 2020-11-09 14:52:21
 */
@RestController
@RequestMapping("/chukujilu")
public class ChukujiluController {
    @Autowired
    private ChukujiluService chukujiluService;
    


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,ChukujiluEntity chukujilu, HttpServletRequest request){

        EntityWrapper<ChukujiluEntity> ew = new EntityWrapper<ChukujiluEntity>();
    	PageUtils page = chukujiluService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, chukujilu), params), params));
		request.setAttribute("data", page);
        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,ChukujiluEntity chukujilu, HttpServletRequest request){
        EntityWrapper<ChukujiluEntity> ew = new EntityWrapper<ChukujiluEntity>();
    	PageUtils page = chukujiluService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, chukujilu), params), params));
		request.setAttribute("data", page);
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( ChukujiluEntity chukujilu){
       	EntityWrapper<ChukujiluEntity> ew = new EntityWrapper<ChukujiluEntity>();
      	ew.allEq(MPUtil.allEQMapPre( chukujilu, "chukujilu")); 
        return R.ok().put("data", chukujiluService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(ChukujiluEntity chukujilu){
        EntityWrapper< ChukujiluEntity> ew = new EntityWrapper< ChukujiluEntity>();
 		ew.allEq(MPUtil.allEQMapPre( chukujilu, "chukujilu")); 
		ChukujiluView chukujiluView =  chukujiluService.selectView(ew);
		return R.ok("查询出库记录成功").put("data", chukujiluView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") String id){
        ChukujiluEntity chukujilu = chukujiluService.selectById(id);
        return R.ok().put("data", chukujilu);
    }

    /**
     * 前端详情
     */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") String id){
        ChukujiluEntity chukujilu = chukujiluService.selectById(id);
        return R.ok().put("data", chukujilu);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ChukujiluEntity chukujilu, HttpServletRequest request){
    	chukujilu.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(chukujilu);

        chukujiluService.insert(chukujilu);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody ChukujiluEntity chukujilu, HttpServletRequest request){
    	chukujilu.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(chukujilu);

        chukujiluService.insert(chukujilu);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody ChukujiluEntity chukujilu, HttpServletRequest request){
        //ValidatorUtils.validateEntity(chukujilu);
        chukujiluService.updateById(chukujilu);//全部更新
        return R.ok();
    }
    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        chukujiluService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
    /**
     * 提醒接口
     */
	@RequestMapping("/remind/{columnName}/{type}")
	public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request, 
						 @PathVariable("type") String type,@RequestParam Map<String, Object> map) {
		map.put("column", columnName);
		map.put("type", type);
		
		if(type.equals("2")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			Date remindStartDate = null;
			Date remindEndDate = null;
			if(map.get("remindstart")!=null) {
				Integer remindStart = Integer.parseInt(map.get("remindstart").toString());
				c.setTime(new Date()); 
				c.add(Calendar.DAY_OF_MONTH,remindStart);
				remindStartDate = c.getTime();
				map.put("remindstart", sdf.format(remindStartDate));
			}
			if(map.get("remindend")!=null) {
				Integer remindEnd = Integer.parseInt(map.get("remindend").toString());
				c.setTime(new Date());
				c.add(Calendar.DAY_OF_MONTH,remindEnd);
				remindEndDate = c.getTime();
				map.put("remindend", sdf.format(remindEndDate));
			}
		}
		
		Wrapper<ChukujiluEntity> wrapper = new EntityWrapper<ChukujiluEntity>();
		if(map.get("remindstart")!=null) {
			wrapper.ge(columnName, map.get("remindstart"));
		}
		if(map.get("remindend")!=null) {
			wrapper.le(columnName, map.get("remindend"));
		}


		int count = chukujiluService.selectCount(wrapper);
		return R.ok().put("count", count);
	}
	
	


}
