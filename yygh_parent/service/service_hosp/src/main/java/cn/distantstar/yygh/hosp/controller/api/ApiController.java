package cn.distantstar.yygh.hosp.controller.api;

import cn.distantstar.yygh.common.exception.YyghException;
import cn.distantstar.yygh.common.helper.HttpRequestHelper;
import cn.distantstar.yygh.common.result.Result;
import cn.distantstar.yygh.common.result.ResultCodeEnum;
import cn.distantstar.yygh.common.utils.MD5;
import cn.distantstar.yygh.hosp.service.DepartmentService;
import cn.distantstar.yygh.hosp.service.HospitalService;
import cn.distantstar.yygh.hosp.service.HospitalSetService;
import cn.distantstar.yygh.hosp.service.ScheduleService;
import cn.distantstar.yygh.model.hosp.Department;
import cn.distantstar.yygh.model.hosp.Hospital;
import cn.distantstar.yygh.model.hosp.Schedule;
import cn.distantstar.yygh.vo.hosp.DepartmentQueryVo;
import cn.distantstar.yygh.vo.hosp.ScheduleQueryVo;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: distantstar
 */
@Api(tags = "医院管理API接口")
@RestController
@RequestMapping("/api/hosp")
public class ApiController {

    private HospitalService hospitalService;
    private HospitalSetService hospitalSetService;
    private DepartmentService departmentService;
    private ScheduleService scheduleService;

    @Autowired
    public void setHospitalService(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @Autowired
    public void setHospitalSetService(HospitalSetService hospitalSetService) {
        this.hospitalSetService = hospitalSetService;
    }

    @Autowired
    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Autowired
    public void setScheduleService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @ApiOperation(value = "删除科室")
    @PostMapping("schedule/remove")
    public Result removeSchedule(HttpServletRequest request) {
        // 获取传过来的map集合
        Map<String, String[]> requestMap = request.getParameterMap();
        // 将map集合转换成合适的格式
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        // 获取医院编码
        String hoscode = (String)paramMap.get("hoscode");
        // 获取科室编号
        String hosScheduleId = (String)paramMap.get("hosScheduleId");

        // 1 获取map集合中的签名
        String hospSign = (String)paramMap.get("sign");

        // 2 根据hoscode从数据库中获取对应的签名
        String signKey = hospitalSetService.getSignKey(hoscode);

        // 3 将获取到的签名进行MD5加密处理
        String signKeyMd5 = MD5.encrypt(signKey);

        // 4 进行签名比对
        if (!hospSign.equals(signKeyMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        scheduleService.remove(hoscode, hosScheduleId);
        return Result.ok();
    }

    @ApiOperation(value = "获取排班分页列表")
    @PostMapping("/schedule/list")
    public Result findSchedule(HttpServletRequest request) {
        // 获取传过来的map集合
        Map<String, String[]> requestMap = request.getParameterMap();
        // 将map集合转换成合适的格式
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        // 获取医院编码
        String hoscode = (String)paramMap.get("hoscode");
        // 获取科室编号
        String depcode = (String)paramMap.get("depcode");
        // 获取当前页与每页记录数
        int page = StringUtils.isBlank((CharSequence) paramMap.get("page")) ?
                1 : Integer.parseInt((String)paramMap.get("page"));
        int limit = StringUtils.isBlank((CharSequence) paramMap.get("limit")) ?
                10 : Integer.parseInt((String)paramMap.get("limit"));

        // 1 获取map集合中的签名
        String hospSign = (String)paramMap.get("sign");

        // 2 根据hoscode从数据库中获取对应的签名
        String signKey = hospitalSetService.getSignKey(hoscode);

        // 3 将获取到的签名进行MD5加密处理
        String signKeyMd5 = MD5.encrypt(signKey);

        // 4 进行签名比对
        if (!hospSign.equals(signKeyMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        // 获取VO对象
        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);

        Page<Schedule> pageModel = scheduleService.selectPage(page, limit, scheduleQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation("保存排班时间")
    @PostMapping("/saveSchedule")
    public Result saveSchedule(HttpServletRequest request) {
        // 获取传过来的map集合
        Map<String, String[]> requestMap = request.getParameterMap();
        // 将map集合转换成合适的格式
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        // 获取医院编码
        String hoscode = (String)paramMap.get("hoscode");

        // 1 获取map集合中的签名
        String hospSign = (String)paramMap.get("sign");

        // 2 根据hoscode从数据库中获取对应的签名
        String signKey = hospitalSetService.getSignKey(hoscode);

        // 3 将获取到的签名进行MD5加密处理
        String signKeyMd5 = MD5.encrypt(signKey);

        // 4 进行签名比对
        if (!hospSign.equals(signKeyMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        scheduleService.save(paramMap);
        return Result.ok();
    }

    @ApiOperation("删除科室")
    @PostMapping("/department/remove")
    public Result removeDepartment(HttpServletRequest request) {
        // 获取传过来的map集合
        Map<String, String[]> requestMap = request.getParameterMap();
        // 将map集合转换成合适的格式
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        // 获取医院编码
        String hoscode = (String)paramMap.get("hoscode");
        // 获取科室编码
        String depcode = (String)paramMap.get("depcode");

        // 1 获取map集合中的签名
        String hospSign = (String)paramMap.get("sign");

        // 2 根据hoscode从数据库中获取对应的签名
        String signKey = hospitalSetService.getSignKey(hoscode);

        // 3 将获取到的签名进行MD5加密处理
        String signKeyMd5 = MD5.encrypt(signKey);

        // 4 进行签名比对
        if (!hospSign.equals(signKeyMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        departmentService.remove(hoscode, depcode);
        return Result.ok();
    }

    @ApiOperation("获取科室信息分页列表")
    @PostMapping("/department/list")
    public Result<Page<Department>> findDepartment(HttpServletRequest request) {
        // 获取传过来的map集合
        Map<String, String[]> requestMap = request.getParameterMap();
        // 将map集合转换成合适的格式
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        // 获取医院编码
        String hoscode = (String)paramMap.get("hoscode");
        // 获取当前页与每页记录数
        int page = StringUtils.isBlank((CharSequence) paramMap.get("page")) ?
                        1 : Integer.parseInt((String)paramMap.get("page"));
        int limit = StringUtils.isBlank((CharSequence) paramMap.get("limit")) ?
                        10 : Integer.parseInt((String)paramMap.get("limit"));

        // 1 获取map集合中的签名
        String hospSign = (String)paramMap.get("sign");

        // 2 根据hoscode从数据库中获取对应的签名
        String signKey = hospitalSetService.getSignKey(hoscode);

        // 3 将获取到的签名进行MD5加密处理
        String signKeyMd5 = MD5.encrypt(signKey);

        // 4 进行签名比对
        if (!hospSign.equals(signKeyMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        // 获取VO对象
        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);

        Page<Department> pageModel = departmentService.selectPage(page, limit, departmentQueryVo);
        return Result.ok(pageModel);
    }

    @PostMapping("/saveDepartment")
    public Result saveDepartment(HttpServletRequest request){
        // 获取传过来的map集合
        Map<String, String[]> requestMap = request.getParameterMap();
        // 将map集合转换成合适的格式
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        // 获取医院编码
        String hoscode = (String)paramMap.get("hoscode");

        // 1 获取map集合中的签名
        String hospSign = (String)paramMap.get("sign");

        // 2 根据hoscode从数据库中获取对应的签名
        String signKey = hospitalSetService.getSignKey(hoscode);

        // 3 将获取到的签名进行MD5加密处理
        String signKeyMd5 = MD5.encrypt(signKey);

        // 4 进行签名比对
        if (!hospSign.equals(signKeyMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        departmentService.saveDepartment(paramMap);
        return Result.ok();
    }

    @ApiOperation("分页显示医院")
    @PostMapping("/hospital/show")
    public Result<Hospital> getHosptial(HttpServletRequest request) {
        // 获取传过来的map集合
        Map<String, String[]> requestMap = request.getParameterMap();
        // 将map集合转换成合适的格式
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        // 获取医院编码
        String hoscode = (String)paramMap.get("hoscode");

        // 1 获取map集合中的签名
        String hospSign = (String)paramMap.get("sign");

        // 2 根据hoscode从数据库中获取对应的签名
        String signKey = hospitalSetService.getSignKey(hoscode);

        // 3 将获取到的签名进行MD5加密处理
        String signKeyMd5 = MD5.encrypt(signKey);

        // 4 进行签名比对
        if (!hospSign.equals(signKeyMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        // 调用方法，查询医院
        Hospital hospital = hospitalService.getHospitalByHoscode(hoscode);

        return Result.ok(hospital);
    }

    /**
     * 上传医院接口
     * @param request 上传的请求
     * @return 返回状态码
     */
    @ApiOperation("保存医院信息")
    @PostMapping("/saveHospital")
    public Result saveHospital(HttpServletRequest request) {
        // 获取传过来的map集合
        Map<String, String[]> requestMap = request.getParameterMap();
        // 将map集合转换成合适的格式
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        // 1 获取map集合中的签名
        String hospSign = (String)paramMap.get("sign");

        // 2 根据hoscode从数据库中获取对应的签名
        String hoscode = (String)paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);

        // 3 将获取到的签名进行MD5加密处理
        String signKeyMd5 = MD5.encrypt(signKey);

        // 4 进行签名比对
        if (!hospSign.equals(signKeyMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        // 5 传输过程中“+”转换为了“ ”，因此我们要转换回来
        String logoDataString = (String)paramMap.get("logoData");
        if(!StringUtils.isBlank(logoDataString)) {
            String logoData = logoDataString.replaceAll(" ", "+");
            paramMap.put("logoData", logoData);
        }


        hospitalService.save(paramMap);
        return Result.ok();
    }

}

