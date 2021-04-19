package cn.distantstar.yygh.cmn.controller;

import cn.distantstar.yygh.cmn.service.DictService;
import cn.distantstar.yygh.common.result.Result;
import cn.distantstar.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author distantstar
 */
@Api(tags = "数据字典接口")
@RestController
@RequestMapping("/admin/cmn/dict")
public class DictController {

    private DictService dictService;

    @Autowired
    public void setDictService(DictService dictService) {
        this.dictService = dictService;
    }

    /**
     * 导入数据字典
     * @param file Excel文件
     * @return 返回状态码
     */
    @ApiOperation(value = "导入数据字典")
    @PutMapping("/importData")
    public Result importData(MultipartFile file) {
        dictService.importData(file);
        return Result.ok();
    }

    /**
     * 导出数据字典
     * @param response 响应
     */
    @ApiOperation("导出数据字典")
    @GetMapping("/exportData")
    public void exportData(HttpServletResponse response) {
        try {
            dictService.exportData(response);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据id查询子数据列表
     * @param id 数据id
     * @return 返回子数据列表
     */
    @ApiOperation("根据id查询子数据列表")
    @GetMapping("findChildData/{id}")
    public Result<List<Dict>> findChildData(@PathVariable Long id) {
        List<Dict> list = dictService.findChildData(id);
        return Result.ok(list);
    }

    @ApiOperation(value = "获取数据字典名称")
    @GetMapping(value = "/getName/{parentDictCode}/{value}")
    public String getName(@PathVariable String parentDictCode,
                          @PathVariable String value) {
        return dictService.getNameByParentDictCodeAndValue(parentDictCode, value);
    }

    @ApiOperation(value = "获取数据字典名称")
    @GetMapping(value = "/getName/{value}")
    public String getName(@PathVariable String value) {
        return dictService.getNameByParentDictCodeAndValue("", value);
    }

    @ApiOperation(value = "根据dictCode获取下级节点")
    @GetMapping(value = "/findByDictCode/{dictCode}")
    public Result<List<Dict>> findByDictCode(
            @ApiParam(name = "dictCode", value = "节点编码", required = true)
            @PathVariable String dictCode) {
        List<Dict> list = dictService.findByDictCode(dictCode);
        return Result.ok(list);
    }

}
