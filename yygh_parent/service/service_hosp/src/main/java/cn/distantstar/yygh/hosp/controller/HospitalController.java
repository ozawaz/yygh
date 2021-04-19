package cn.distantstar.yygh.hosp.controller;

import cn.distantstar.yygh.common.result.Result;
import cn.distantstar.yygh.hosp.service.HospitalService;
import cn.distantstar.yygh.model.hosp.Hospital;
import cn.distantstar.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: distantstar
 */
@Api(tags = "医院管理接口")
@RestController
@RequestMapping("admin/hosp/hospital")
@CrossOrigin
public class HospitalController {

    private HospitalService hospitalService;

    @Autowired
    public void setHospitalService(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @ApiOperation(value = "获取分页列表")
    @GetMapping("/list/{page}/{limit}")
    public Result listHospital(@PathVariable Integer page,
                               @PathVariable Integer limit,
                               HospitalQueryVo hospitalQueryVo){
        return Result.ok(hospitalService.selectPage(page, limit, hospitalQueryVo));
    }

    @ApiOperation(value = "更新上线状态")
    @GetMapping("updateStatus/{id}/{status}")
    public Result lock(
            @ApiParam(name = "id", value = "医院id", required = true)
            @PathVariable("id") String id,
            @ApiParam(name = "status", value = "状态（0：未上线 1：已上线）", required = true)
            @PathVariable("status") Integer status){
        hospitalService.updateStatus(id, status);
        return Result.ok();
    }

    @ApiOperation(value = "获取医院详情")
    @GetMapping("show/{id}")
    public Result show(
            @ApiParam(name = "id", value = "医院id", required = true)
            @PathVariable String id) {
        return Result.ok(hospitalService.show(id));
    }

}
