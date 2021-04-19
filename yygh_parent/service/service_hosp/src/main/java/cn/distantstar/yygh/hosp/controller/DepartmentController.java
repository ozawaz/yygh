package cn.distantstar.yygh.hosp.controller;

import cn.distantstar.yygh.common.result.Result;
import cn.distantstar.yygh.hosp.service.DepartmentService;
import cn.distantstar.yygh.vo.hosp.DepartmentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: distantstar
 */
@Api(tags = "科室信息管理")
@RestController
@RequestMapping("/admin/hosp/department")
public class DepartmentController {

    private DepartmentService departmentService;

    @Autowired
    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * 据医院编号，查询医院所有科室列表
     * @param hoscode hoscode
     * @return 返回查询的结果集
     */
    @ApiOperation(value = "查询医院所有科室列表")
    @GetMapping("getDeptList/{hoscode}")
    public Result<List<DepartmentVo>> getDeptList(@PathVariable String hoscode) {
        List<DepartmentVo> list = departmentService.findDeptTree(hoscode);
        return Result.ok(list);
    }

}
