package cn.distantstar.yygh.hosp.service.impl;

import cn.distantstar.yygh.hosp.repository.DepartmentRepository;
import cn.distantstar.yygh.hosp.service.DepartmentService;
import cn.distantstar.yygh.model.hosp.Department;
import cn.distantstar.yygh.vo.hosp.DepartmentQueryVo;
import cn.distantstar.yygh.vo.hosp.DepartmentVo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: distantstar
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    private DepartmentRepository departmentRepository;

    @Autowired
    public void setDepartmentRepository(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public void saveDepartment(Map<String, Object> paramMap) {
        Department department = JSONObject.parseObject(JSONObject.toJSONString(paramMap), Department.class);
        Department targetDepartment =
                departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());

        if (null == targetDepartment) {
            department.setCreateTime(new Date());
        }

        department.setUpdateTime(new Date());
        department.setIsDeleted(0);
        departmentRepository.save(department);
    }

    @Override
    public Page<Department> selectPage(Integer page, Integer limit, DepartmentQueryVo departmentQueryVo) {
        // 创建Pageable对象，设置当前页和每页记录数
        Pageable pageable = PageRequest.of(page - 1, limit);

        // 将VO类转换城实体类
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo, department);

        // 创建匹配器
        ExampleMatcher matcher = ExampleMatcher.matching()
                            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                            .withIgnoreCase(true);

        // 创建实例
        Example<Department> example = Example.of(department, matcher);

        // 查找
        return departmentRepository.findAll(example, pageable);
    }

    @Override
    public void remove(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (null != department) {
            departmentRepository.deleteById(department.getId());
        }
    }

    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {
        // 创建list集合，用于最终数据封装
        List<DepartmentVo> result = new ArrayList<>();

        // 根据医院编号，查询医院所有科室信息
        Department department = new Department();
        department.setHoscode(hoscode);
        Example<Department> example = Example.of(department);
        // 所有科室信息
        List<Department> departmentList = departmentRepository.findAll(example);

        // 根据大科室编号 bigcode 分组，获取每个大科室里面下级子科室
        Map<String, List<Department>> departmentMap =
                departmentList.stream().collect(Collectors.groupingBy(Department::getBigcode));
        // 遍历map集合
        for (Map.Entry<String, List<Department>> entry: departmentMap.entrySet()) {
            // 大科室编号
            String bigcode = entry.getKey();

            // 大科室编号对应的全局数据
            List<Department> departmentListValue = entry.getValue();

            // 封装大科室
            DepartmentVo bigDepartmentVo = new DepartmentVo();
            bigDepartmentVo.setDepcode(bigcode);
            bigDepartmentVo.setDepname(departmentListValue.get(0).getBigname());

            // 封装小科室
            List<DepartmentVo> children = new ArrayList<>();
            for (Department department1: departmentListValue) {
                DepartmentVo smallDepartmentVo = new DepartmentVo();
                smallDepartmentVo.setDepcode(department1.getDepcode());
                smallDepartmentVo.setDepname(department1.getDepname());
                // 封装到list集合
                children.add(smallDepartmentVo);
            }

            // 将children放入到大科室的children中
            bigDepartmentVo.setChildren(children);

            // 将大科室添加到result中
            result.add(bigDepartmentVo);
        }

        return result;
    }

    @Override
    public String getDepName(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if(null != department) {
            return department.getDepname();
        }
        return null;

    }
}
