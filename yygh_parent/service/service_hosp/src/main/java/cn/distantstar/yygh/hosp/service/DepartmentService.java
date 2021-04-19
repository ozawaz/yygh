package cn.distantstar.yygh.hosp.service;

import cn.distantstar.yygh.model.hosp.Department;
import cn.distantstar.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * @Author: distantstar
 */
public interface DepartmentService {
    /**
     * 保存科室信息
     * @param paramMap 数据集合
     */
    void saveDepartment(Map<String, Object> paramMap);

    /**
     * 分页查询
     * @param page 当前页码
     * @param limit 每页记录数
     * @param departmentQueryVo 查询条件
     * @return 返回Page对象
     */
    Page<Department> selectPage(Integer page, Integer limit, DepartmentQueryVo departmentQueryVo);

    /**
     * 删除科室
     * @param hoscode
     * @param depcode
     */
    void remove(String hoscode, String depcode);

}
