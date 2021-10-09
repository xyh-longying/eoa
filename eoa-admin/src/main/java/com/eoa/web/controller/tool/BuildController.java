package com.eoa.web.controller.tool;

import com.eoa.common.core.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ChengLong
 * @ClassName: BuildController
 * @Description build 表单构建
 * @Date 2021/9/8 0008 22:15
 * @Version 1.0.0
 */
@Controller
@RequestMapping("/tool/build")
public class BuildController extends BaseController {
    private String prefix = "tool/build";

    @RequiresPermissions("tool:build:view")
    @GetMapping()
    public String build()
    {
        return prefix + "/build";
    }
}
