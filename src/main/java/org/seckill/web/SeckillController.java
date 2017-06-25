package org.seckill.web;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Controller
 * Created by fyy on 6/25/17.
 */
@Controller
@RequestMapping("/seckill") //url:/模块/资源/{id}/细分

public class SeckillController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SeckillService seckillService;

    @Autowired
    public SeckillController(SeckillService seckillService) {
        this.seckillService = seckillService;
    }

//    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
//    public String list(Model model) {
////        获取列表页
//        List<Seckill> list = seckillService.getSeckillList();
//        model.addAttribute("list", list);
//        return "list"; // /WEB-INF/jsp/"list.jsp"
//
//
//    }

    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public String list(Model model , HttpServletResponse response) {
        //list.jsp+mode=ModelAndView
        //获取列表页
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list", list);
//        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");
        return "list";
    }

    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET ,produces = {"text/html; charset=utf-8"})
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {

        if (seckillId == null) {
            return "redirect:/seckill/list";//id不存在直接重定向到列表页
        }
        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null) {
            return "forward:/seckill/list";//不存在seckill请求转发到列表页面
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }


    //返回json
    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST, produces = {"application/json;charset=utf-8"})
    @ResponseBody //表示返回json类型
    public SeckillResult<Exposer>  exposer(@PathVariable("seckillId") Long seckillId) {

        SeckillResult<Exposer> result;
        try {
            System.out.println(seckillId);
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            System.out.println(exposer.toString());
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }
        return result;

    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId, @PathVariable("md5") String md5,
                                                   /*从浏览器cookie中拿phone*/ @CookieValue(value = "killPhone", required = false /*phone不是必须的*/) Long phone) {
        if (phone == null) {
            return new SeckillResult<SeckillExecution>(false, "未注册手机");
        }
//        SeckillResult<SeckillExecution> result;
        try {
            SeckillExecution execution = seckillService.excuteSeckill(seckillId, phone, md5);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (SeckillCloseException e1) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.END);
            return new SeckillResult<SeckillExecution>(false, execution);
        } catch (RepeatKillException e2) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(false, execution);
        } catch (Exception e) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(false, execution);
        }
    }

    //获取系统时间
    @RequestMapping(value = "/time/now", method = RequestMethod.GET)

    public @ResponseBody SeckillResult<Long> time() {
        Date now = new Date();
        System.out.println(now+" -----");
        return new SeckillResult<Long>(true, now.getTime());
    }


}
