package cn.distantstar.yygh.hosp.service.impl;

import cn.distantstar.yygh.hosp.repository.DepartmentRepository;
import cn.distantstar.yygh.hosp.service.DepartmentService;
import cn.distantstar.yygh.model.hosp.Department;
import cn.distantstar.yygh.vo.hosp.DepartmentQueryVo;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

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
        Page<Department> pages = departmentRepository.findAll(example, pageable);
        return pages;
    }

    @Override
    public void remove(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (null != department) {
            departmentRepository.deleteById(department.getId());
        }
    }
}
