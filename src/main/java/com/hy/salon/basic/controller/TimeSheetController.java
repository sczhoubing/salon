package com.hy.salon.basic.controller;

import com.hy.salon.basic.common.StatusUtil;
import com.hy.salon.basic.dao.*;
import com.hy.salon.basic.entity.*;
import com.hy.salon.basic.service.StuffService;
import com.hy.salon.basic.service.TimeSheetService;
import com.hy.salon.basic.vo.MyResult;
import com.hy.salon.basic.vo.Result;
import com.hy.salon.basic.vo.TimeSheetVo;
import com.zhxh.admin.entity.SystemUser;
import com.zhxh.admin.service.AuthenticateService;
import com.zhxh.admin.service.SystemUserService;
import com.zhxh.core.data.BaseDAOWithEntity;
import com.zhxh.core.web.SimpleCRUDController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/hy/basic/timeSheet")
@Api(value = "TimeSheetController| 考勤控制器")
public class TimeSheetController extends SimpleCRUDController<TimeSheet> {
    @Resource(name = "timeSheetDao")
    private TimeSheetDao timeSheetDao;
    @Resource(name = "picturesDao")
    private PicturesDAO picturesDao;
    @Resource(name = "scheduleDao")
    private ScheduleDao scheduleDao;
    @Resource(name = "timeSheetService")
    private TimeSheetService timeSheetService;
    @Resource(name = "authenticateService")
    private AuthenticateService authenticateService;
    @Resource(name = "stuffDao")
    private StuffDao stuffDao;

    @Resource(name = "salonDao")
    private SalonDao salonDao;

    @Override
    protected BaseDAOWithEntity<TimeSheet> getCrudDao() {
        return timeSheetDao;
    }

    /**
     * 重写当月每家门店考勤
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getTimeSheets",method = RequestMethod.GET)
    @ApiOperation(value="获取当天每家门店考勤", notes="获取当天每家门店考勤")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "recordId", value = "美容院id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query", name = "time", value = "当月日期", required = true, dataType = "String")
    })
    public Result getTimeSheets(Long recordId,String time,Integer page,Integer limit){
        if(StringUtils.isEmpty(recordId)){
            SystemUser user = authenticateService.getCurrentLogin();
            Stuff stuff=stuffDao.getStuffForUser(user.getRecordId());
            recordId=stuff.getStoreId();
        }
        Result result=new Result();
        try {
            List<Map> list = timeSheetService.getTimeSheets(recordId, time,page,limit);
            result.setTotal(salonDao.getPageListCount(new HashMap())-1);
            result.setData(list);
            result.setSuccess(true);
            result.setMsgcode(StatusUtil.OK);
        }catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMsgcode(StatusUtil.ERROR);
        }
        return result;
    }
    /**
     * 获取个人一天的考勤
     */
    @ResponseBody
    @RequestMapping(value = "getOneTimeSheetByStuffId",method = RequestMethod.GET)
    @ApiOperation(value="获取月門店考勤", notes="获取当月門店考勤")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "stuffId", value = "员工id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query", name = "time", value = "日期", required = true, dataType = "String")
    })
    public Result getOneTimeSheetByStuffId(HttpServletRequest request){
        String stuffId = request.getParameter("stuffId");
        String time = request.getParameter("time");
        String filterExpr   = request.getParameter("filterExpr");
        long stuffIdL = Long.parseLong(stuffId);
        if(StringUtils.isEmpty(stuffIdL)){
            SystemUser user = authenticateService.getCurrentLogin();
            Stuff stuff=stuffDao.getStuffForUser(user.getRecordId());
            stuffIdL=stuff.getRecordId();
        }
        MyResult result = new MyResult();
        try {
            String cTime = time+"00:00:00";
            String eTime = time+"24:59:59";

            TimeSheet timeSheet=timeSheetDao.getTSheet(stuffIdL,cTime,eTime);
            Schedule schedule = scheduleDao.getPaiByStuffId(stuffIdL, time);

            Pictures pictures = picturesDao.getOnePicturesForCondition(stuffIdL,(byte)1,(byte)0);


            result.setSuccess(true);
            result.setMsgcode(StatusUtil.OK);
        }catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMsgcode(StatusUtil.ERROR);
        }
        return result;
    }


    /**
     * 获取个人当月的考勤
     */
    @ResponseBody
    @RequestMapping(value = "getTimeSheetByStuffId",method = RequestMethod.GET)
    @ApiOperation(value="获取月門店考勤", notes="获取当月門店考勤")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "stuffId", value = "员工id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query", name = "time", value = "当月日期", required = true, dataType = "String")
    })
    public Result getTimeSheetByStuffId(HttpServletRequest request){
        String stuffId = request.getParameter("stuffId");
        String time = request.getParameter("time");
        String filterExpr   = request.getParameter("filterExpr");
        long stuffIdL = Long.parseLong(stuffId);
        if(StringUtils.isEmpty(stuffIdL)){
            SystemUser user = authenticateService.getCurrentLogin();
            Stuff stuff=stuffDao.getStuffForUser(user.getRecordId());
            stuffIdL=stuff.getRecordId();
        }
        MyResult result = new MyResult();
        try {
            Map list = timeSheetService.getTimeSheetByStuffIdNew(result,stuffIdL, time);
            result.setData(list);
            result.setSuccess(true);
            result.setMsgcode(StatusUtil.OK);
        }catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMsgcode(StatusUtil.ERROR);
        }
        return result;
    }

    /**
     * 获取当月的一个门店的考勤
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getTimeSheetBySalonId",method = RequestMethod.GET)
    @ApiOperation(value="获取月門店考勤", notes="获取当月門店考勤")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "salonId", value = "門店id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query", name = "time", value = "当月日期", required = true, dataType = "String")
    })
    public Result getTimeSheetBySalonId(HttpServletRequest request){

        String salonIdS = request.getParameter("salonId");
        String time = request.getParameter("time");
        String filterExpr   = request.getParameter("filterExpr");
        long salonId = Long.parseLong(salonIdS);
        if(StringUtils.isEmpty(salonId)){
            SystemUser user = authenticateService.getCurrentLogin();
            Stuff stuff=stuffDao.getStuffForUser(user.getRecordId());
            salonId=stuff.getStoreId();
        }
        MyResult result = new MyResult();
        try {
            List<Map> list = timeSheetService.getTimeSheetBySalonId(result,salonId, time);
            result.setData(list);
            result.setSuccess(true);
            result.setMsgcode(StatusUtil.OK);
        }catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMsgcode(StatusUtil.ERROR);
        }
        return result;
    }
    /**
     * 考勤3
     * @param time
     * @return
     */
/*    @ResponseBody
    @RequestMapping(value = "getTimeSheetByStuffId",method = RequestMethod.GET)
    @ApiOperation(value="获取员工考勤情况", notes="获取员工考勤情况")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "stuffId", value = "员工id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query", name = "time", value = "当月日期", required = true, dataType = "String")
    })
    public Result getTimeSheetByStuffId(Long stuffId,String time){
        MyResult result=new MyResult();
        try {
            Map map = timeSheetService.getTimeSheetByStuffId(result,stuffId, time);
            result.setData(map);
            result.setSuccess(true);
            result.setMsgcode(StatusUtil.OK);
        }catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMsgcode(StatusUtil.ERROR);
        }
        return result;
    }*/
    /**
     * 查询员工未打卡记录
     */
    @ResponseBody
    @RequestMapping(value = "getTimeId",method = RequestMethod.GET)
    @ApiOperation(value="查询员工未打卡记录", notes="查询员工未打卡记录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "stuffId", value = "员工id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query", name = "time", value = "当月日期", required = true, dataType = "String")
    })
    public Result getTimeId(Long stuffId,String time){
        Result result=new Result();
        try {
            List<Schedule> list = timeSheetService.getTimeId(stuffId, time);
            result.setData(list);
            result.setSuccess(true);
            result.setMsgcode(StatusUtil.OK);
        }catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMsgcode(StatusUtil.ERROR);
        }
        return result;
    }
    /**
     * 查询员工未打卡记录详情
     */
    @ResponseBody
    @RequestMapping(value = "getTimeSheetDetails",method = RequestMethod.GET)
    @ApiOperation(value="查询员工未打卡记录详情", notes="查询员工未打卡记录详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "stuffId", value = "员工id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType="query", name = "time", value = "当天日期", required = true, dataType = "String")
    })
    public Result getTimeSheetDetails(Long stuffId,String time){
        Result result=new Result();
        try {
            Map map = timeSheetService.getTimeSheetDetails(stuffId, time);
            result.setData(map);
            result.setSuccess(true);
            result.setMsgcode(StatusUtil.OK);
        }catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMsgcode(StatusUtil.ERROR);
        }
        return result;
    }
    /**
     * 获取分店
     */
    @ResponseBody
    @RequestMapping(value = "getSalon",method = RequestMethod.GET)
    public Result getSalon(){
        SystemUser user = authenticateService.getCurrentLogin();
        Stuff stuff=stuffDao.getStuffForUser(user.getRecordId());
        Result result=new Result();
        try {
            List<Salon> list = timeSheetService.getSalon(stuff.getStoreId());
            result.setData(list);
            result.setSuccess(true);
            result.setMsgcode(StatusUtil.OK);
        }catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMsgcode(StatusUtil.ERROR);
        }
        return result;
    }
    /**
     * 考勤2pc
     * @param time
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getTimeSheetBySalonIds",method = RequestMethod.GET)
    public Result getTimeSheetBySalonIds(Long salonId,String time,String name){
        if(StringUtils.isEmpty(salonId)){
            SystemUser user = authenticateService.getCurrentLogin();
            Stuff stuff=stuffDao.getStuffForUser(user.getRecordId());
            salonId=stuff.getStoreId();
        }
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
        Result result=new Result();
        try {
            List<Map> list=null;
            String s="";
            if(time!=null&&time!=""){
                String[] str = time.split("-");
                StringBuffer sb = new StringBuffer();
                for(int i = 0; i < str.length;i++){
                    sb.append(str[i]);
                }
                s= sb.toString();
            }else{
                s=sdf.format(new Date());
            }
            if(name!=null){
                list = timeSheetService.getTimeSheetBySalonId(0,0,0,salonId, s,name);
            }else{
                list = timeSheetService.getTimeSheetBySalonId(salonId, s);
            }
            result.setTotal(stuffDao.getPageListCount(new HashMap()));
            result.setData(list);
            result.setSuccess(true);
            result.setMsgcode(StatusUtil.OK);
        }catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMsgcode(StatusUtil.ERROR);
        }
        return result;
    }
    /**
     * 初始化
     * @param time
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getTimeSheetInit",method = RequestMethod.GET)
    public Result getTimeSheetInit(Long salonId,String time){
        if(StringUtils.isEmpty(salonId)){
            SystemUser user = authenticateService.getCurrentLogin();
            Stuff stuff=stuffDao.getStuffForUser(user.getRecordId());
            salonId=stuff.getStoreId();
        }
        Result result=new Result();
        try {
            List<Map> list = timeSheetService.getTimeSheetInit(0,0,0,salonId,time);
            result.setData(list);
            result.setSuccess(true);
            result.setMsgcode(StatusUtil.OK);
        }catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMsgcode(StatusUtil.ERROR);
        }
        return result;
    }
}
