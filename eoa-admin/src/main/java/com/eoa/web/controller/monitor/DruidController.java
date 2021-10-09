package com.eoa.web.controller.monitor;

import com.eoa.common.core.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ChengLong
 * @ClassName: DruidController
 * @Description TODO
 * @Date 2021/10/9 0009 12:54
 * @Version 1.0.0
 */
@Controller
@RequestMapping(value = "/monitor/data")
public class DruidController extends BaseController {
    private String prefix = "/druid";
    
    @RequiresPermissions("monitor:data:view")
    @GetMapping()
    public String index(){
        return redirect(prefix + "/index.html");    
    }
}
