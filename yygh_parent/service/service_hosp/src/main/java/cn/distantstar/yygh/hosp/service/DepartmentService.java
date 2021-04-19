package cn.distantstar.yygh.hosp.service;

import cn.distantstar.yygh.model.hosp.Department;
import cn.distantstar.yygh.vo.hosp.DepartmentQueryVo;
import cn.distantstar.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
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
     * @param hoscode hoscode
     * @param depcode depcode
     */
    void remove(String hoscode, String depcode);

    /**
     * 根据医院编号，查询医院所有科室列表
     * @param hoscode hoscode
     * @return 返回查询的结果集
     */
    List<DepartmentVo> findDeptTree(String hoscode);

    /**
     * 根据科室编号，和医院编号，查询科室名称
     * @param hoscode 医院编号
     * @param depcode 科室编号
     * @return 返回科室名称
     */
    String getDepName(String hoscode, String depcode);

}
