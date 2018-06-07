package com.pase.pasecredit.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pase.pasecredit.model.CreditCardBill;
import com.pase.pasecredit.service.ProductPaseCreditService;
import com.pase.pasecredit.service.database.CreditCardBillService;
import com.pase.pasecredit.utils.ResultJson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.DoubleToIntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: chenpeng
 * @Description:
 * @Date: Created in 11:00 2018/5/29
 */
@RestController
@RequestMapping("/pasecredit")
public class ProductPaseCreditController {
    Logger logger = LoggerFactory.getLogger(ProductPaseCreditController.class);

    @Autowired
    private ProductPaseCreditService productPaseCreditService;

    @Autowired
    private CreditCardBillService creditCardBillService;

    @RequestMapping(value = "/pase.html", method = RequestMethod.POST)
    public String checkPaseAllMail(@RequestParam MultipartFile file, @RequestParam String param) {
        logger.info("请求参数为:" + "param :" + param);
        JSONObject jsonObject = new JSONObject();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");//设置日期格式
        String date = df.format(new Date());
        String savePath = "d:/zhjf/mail/";
        String pathName = savePath + date + file.getOriginalFilename() + ".txt";
        try {
            File file1 = new File(savePath);
            if (!file1.exists()) {
                file1.mkdirs();
            }
            File file2 = new File(pathName);
            file.transferTo(file2);
            Document document = Jsoup.parse(file2, "utf-8");
            String acout = document.toString().replaceAll("&nbsp;", "").replaceAll("&amp;", "")
                    .replaceAll("&gt;", ">").replaceAll("&lt;", "<");
            jsonObject = productPaseCreditService.checkPaseAllMail(acout, param, pathName);
            logger.info("返回参数为:" + jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return jsonObject.toString();
        }
        return jsonObject.toString();
    }

    @RequestMapping("/getAllBank.html")
    public String getAllBank(@RequestParam String idcard) {
        logger.info("请求参数为:" + "idcard :" + idcard);
        JSONObject data = new JSONObject();
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "paymentsDate");
            List<CreditCardBill> list = creditCardBillService.findByIdCard(idcard, sort);
            data.put("list", list);
            logger.info("返回参数为：" + ResultJson.getSuccessResult(data));
            return ResultJson.getSuccessResult(data).toString();
        } catch (Exception e) {
            data.put("list", new ArrayList());
            return ResultJson.getFailResult(data).toString();
        }
    }

    public static void main(String[] args) {
        File file = new File("C:\\Users\\Administrator\\Documents\\HBuilderProjects\\HelloHBuilder\\myTest\\jt.html");
        try {
            String acout = "";
            Document document = Jsoup.parse(file, "utf-8");
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");//设置日期格式
            String date = df.format(new Date());
            System.out.println(Calendar.getInstance().getTimeInMillis());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
