package cn.distantstar.yygh.hosp.controller;

import cn.distantstar.yygh.common.result.Result;
import cn.distantstar.yygh.common.utils.MD5;
import cn.distantstar.yygh.hosp.service.HospitalSetService;
import cn.distantstar.yygh.model.hosp.HospitalSet;
import cn.distantstar.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

/**
 * @author distantstar
 */
@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
@CrossOrigin
public class HospitalSetController {

    private HospitalSetService hospitalSetService;

    @Autowired
    public void setHospitalSetService(HospitalSetService hospitalSetService) {
        this.hospitalSetService = hospitalSetService;
    }

    /**
     * 查询医院设置表所有信息
     * @return 返回所有的医院设置的json集合
     */
    @ApiOperation(value = "获取所有医院设置")
    @GetMapping("findAll")
    public Result<List<HospitalSet>> findAllHospitalSet() {
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    /**
     * 逻辑删除医院设置
     * @param id 医院设置唯一标识id号
     * @return 返回一个状态码，代表成功失败
     */
    @ApiOperation(value = "逻辑删除医院设置")
    @DeleteMapping("{id}")
    public Result removeHospSet(@PathVariable Long id) {
        boolean flag = hospitalSetService.removeById(id);

        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    /**
     * 条件查询带分页
     * @param current 当前页
     * @param limit 记录数
     * @param hospitalSetQueryVo 条件Vo类
     * @return 返回查询结果并带页数
     */
    @ApiOperation(value = "条件查询带分页")
    @PostMapping("findPageHospSet/{current}/{limit}")
    public Result<Page<HospitalSet>> findPageHospSet(@PathVariable long current,
                                  @PathVariable long limit,
                                  @RequestBody (required = false) HospitalSetQueryVo hospitalSetQueryVo) {
        // 创建Page对象，传递当前页，每页记录数
        Page<HospitalSet> page = new Page<>(current, limit);
        // 构建条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        String hosname = hospitalSetQueryVo.getHosname(); // 医院名称
        String hoscode = hospitalSetQueryVo.getHoscode(); // 医院编号
        if(!StringUtils.isBlank(hosname)) {
            wrapper.like("hosname",hospitalSetQueryVo.getHosname());
        }
        if(!StringUtils.isBlank(hoscode)) {
            wrapper.eq("hoscode",hospitalSetQueryVo.getHoscode());
        }

        // 调用方法实现分页查询
        Page<HospitalSet> pageHospitalSet = hospitalSetService.page(page, wrapper);

        // 返回结果
        return Result.ok(pageHospitalSet);
    }

    /**
     * 添加医院设置
     * @param hospitalSet 添加实体
     * @return 返回状态码
     */
    @ApiOperation("添加医院设置")
    @PostMapping("saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet) {
        // 设置状态 1 使用 0 不能使用
        hospitalSet.setStatus(1);
        //签名秘钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000)));
        //调用service
        boolean save = hospitalSetService.save(hospitalSet);
        if(save) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    /**
     * 根据id获取医院设置
     * @param id 医院设置唯一标识id
     * @return 返回查询结果
     */
    @ApiOperation("根据id获取医院设置")
    @GetMapping("getHospSet/{id}")
    public Result<HospitalSet> getHospSet(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    /**
     * 修改医院设置
     * @param hospitalSet 传入的结果
     * @return 返回状态码
     */
    @ApiOperation("修改医院设置")
    @PostMapping("updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet) {
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if(flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    /**
     * 批量删除医院设置
     * @param idList 想要删除的id集合
     * @return 返回状态码
     */
    @ApiOperation("批量删除医院设置")
    @DeleteMapping("batchRemove")
    public Result batchRemoveHospitalSet(@RequestBody List<Long> idList) {
        hospitalSetService.removeByIds(idList);
        return Result.ok();
    }

    /**
     * 医院设置锁定和解锁
     * @param id 想要锁定的医院设置id值
     * @param status 锁定状态
     * @return 返回状态码
     */
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,
                                  @PathVariable Integer status) {
        //根据id查询医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        //设置状态
        hospitalSet.setStatus(status);
        //调用方法
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }

    /**
     * 发送签名秘钥
     * @param id 要发送的医院设置id
     * @return 返回状态码
     */
    @PutMapping("sendKey/{id}")
    public Result lockHospitalSet(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        //TODO 发送短信
        return Result.ok();
    }


}
