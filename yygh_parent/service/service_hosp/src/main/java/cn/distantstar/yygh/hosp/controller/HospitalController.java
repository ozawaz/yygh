package cn.distantstar.yygh.hosp.controller;

import cn.distantstar.yygh.common.result.Result;
import cn.distantstar.yygh.hosp.service.HospitalService;
import cn.distantstar.yygh.hosp.service.ScheduleService;
import cn.distantstar.yygh.model.hosp.Hospital;
import cn.distantstar.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: distantstar
 */
@Api(tags = "医院管理接口")
@RestController
@RequestMapping("admin/hosp/hospital")
public class HospitalController {

    private HospitalService hospitalService;
    private ScheduleService scheduleService;

    @Autowired
    public void setHospitalService(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @Autowired
    public void setScheduleService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @ApiOperation(value = "获取分页列表")
    @GetMapping("/list/{page}/{limit}")
    public Result<Page<Hospital>> listHospital(@PathVariable Integer page,
                                               @PathVariable Integer limit,
                                               HospitalQueryVo hospitalQueryVo){
        return Result.ok(hospitalService.selectPage(page, limit, hospitalQueryVo));
    }

    @ApiOperation(value = "更新上线状态")
    @GetMapping("updateStatus/{id}/{status}")
    public Result<Result<T>> lock(
            @ApiParam(name = "id", value = "医院id", required = true)
            @PathVariable("id") String id,
            @ApiParam(name = "status", value = "状态（0：未上线 1：已上线）", required = true)
            @PathVariable("status") Integer status){
        hospitalService.updateStatus(id, status);
        return Result.ok();
    }

    @ApiOperation(value = "获取医院详情")
    @GetMapping("show/{id}")
    public Result<Map<String, Object>> show(
            @ApiParam(name = "id", value = "医院id", required = true)
            @PathVariable String id) {
        return Result.ok(hospitalService.show(id));
    }

    /**
     * 根据医院编号 和 科室编号，查询排班规则数据
     * @param page 当前页
     * @param limit 每页记录数
     * @param hoscode 医院编号
     * @param depcode 科室编号
     * @return 返回查询集合
     */
    @ApiOperation(value ="查询排班规则数据")
    @GetMapping("getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result<Map<String,Object>> getScheduleRule(@PathVariable long page,
                                  @PathVariable long limit,
                                  @PathVariable String hoscode,
                                  @PathVariable String depcode) {
        Map<String,Object> map
                = scheduleService.getRuleSchedule(page,limit,hoscode,depcode);
        return Result.ok(map);
    }


}
