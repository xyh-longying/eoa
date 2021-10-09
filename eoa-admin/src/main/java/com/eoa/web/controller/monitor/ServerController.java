package com.eoa.web.controller.monitor;

import com.eoa.common.core.controller.BaseController;
import com.eoa.framework.web.domain.Server;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ChengLong
 * @ClassName: ServerController
 * @Description TODO
 * @Date 2021/10/9 0009 13:05
 * @Version 1.0.0
 */
@Controller
@RequestMapping(value = "/monitor/server")
public class ServerController extends BaseController {
    
    private String prefix = "monitor/server";
    
    @RequiresPermissions("monitor:server:view")
    @GetMapping()
    public String server(ModelMap mmap) throws Exception {
        Server server = new Server();
        server.copyTo();
        mmap.put("server", server);
        return prefix + "/server";
    }
}
