package com.eoa.web.controller.tool;

import com.eoa.common.core.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ChengLong
 * @ClassName: SwaggerController
 * @Description swagger 接口
 * @Date 2021/9/8 0008 22:14
 * @Version 1.0.0
 */
@Controller
@RequestMapping("/tool/swagger")
public class SwaggerController extends BaseController {
    @RequiresPermissions("tool:swagger:view")
    @GetMapping()
    public String index()
    {
        return redirect("/swagger-ui/index.html");
    }
}
