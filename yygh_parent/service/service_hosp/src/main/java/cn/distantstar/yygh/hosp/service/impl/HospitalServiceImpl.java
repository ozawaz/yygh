package cn.distantstar.yygh.hosp.service.impl;

import cn.distantstar.yygh.cmn.DictFeignClient;
import cn.distantstar.yygh.enums.DictEnum;
import cn.distantstar.yygh.hosp.repository.HospitalRepository;
import cn.distantstar.yygh.hosp.service.HospitalService;
import cn.distantstar.yygh.model.hosp.Hospital;
import cn.distantstar.yygh.vo.hosp.HospitalQueryVo;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: distantstar
 */
@Service
public class HospitalServiceImpl implements HospitalService {

    private HospitalRepository hospitalRepository;
    private DictFeignClient dictFeignClient;

    @Autowired
    public void setHospitalRepository(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    @Autowired
    public void setDictFeignClient(DictFeignClient dictFeignClient) {
        this.dictFeignClient = dictFeignClient;
    }

    /**
     * 上传医院接口
     * @param paramMap 需要保存的数据
     */
    @Override
    public void save(Map<String, Object> paramMap) {
        // 把参数集合转换成需要的对象 Hospital
        String mapString = JSONObject.toJSONString(paramMap);
        Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);

        // 判断数据库中是否存在数据
        String hoscode = hospital.getHoscode();
        Hospital hospitalExist = hospitalRepository.getHospitalByHoscode(hoscode);

        // 如果不存在，直接添加
        if (hospitalExist == null) {
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
        } else {// 如果存在，进行修改
            hospital.setStatus(hospitalExist.getStatus());
            hospital.setCreateTime(hospitalExist.getCreateTime());
        }
        hospital.setUpdateTime(new Date());
        hospital.setIsDeleted(0);
        hospitalRepository.save(hospital);


    }

    /**
     * 根据医院编码查询
     * @param hoscode 医院编码
     * @return 返回查询结果
     */
    @Override
    public Hospital getHospitalByHoscode(String hoscode) {
        return hospitalRepository.getHospitalByHoscode(hoscode);
    }

    @Override
    public Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        // 创建Pageable对象
        Pageable pageable = PageRequest.of(page - 1, limit);
        // 创建ExampleMatcher匹配器
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        // 转换对象
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);
        // 创建Example实例
        Example<Hospital> example = Example.of(hospital, matcher);
        // 调用MongoDB Repository中的方法
        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);

        // 获取查询list集合，遍历进行医院等级封装
        pages.getContent().stream().forEach(item -> {
            this.packHospital(item);
        });
        return pages;
    }

    @Override
    public void updateStatus(String id, Integer status) {
        if(status.intValue() == 0 || status.intValue() == 1) {
            // 根据id查询医院信息
            Hospital hospital = hospitalRepository.findById(id).get();
            // 设置修改的值
            hospital.setStatus(status);
            hospital.setUpdateTime(new Date());
            hospitalRepository.save(hospital);
        }
    }

    @Override
    public Map<String, Object> show(String id) {
        Map<String, Object> result = new HashMap<>();

        Hospital hospital = this.packHospital(hospitalRepository.findById(id).get());
        result.put("hospital", hospital);

        // 单独处理更直观
        result.put("bookingRule", hospital.getBookingRule());
        // 不需要重复返回
        hospital.setBookingRule(null);
        return result;
    }


    /**
     * 封装数据
     * @param hospital
     * @return
     */
    private Hospital packHospital(Hospital hospital) {
        // 根据dictCode和value获取医院等级名称
        String hostypeString = dictFeignClient.getName(DictEnum.HOSTYPE.getDictCode(),hospital.getHostype());
        // 查询省、市、区
        String provinceString = dictFeignClient.getName(hospital.getProvinceCode());
        String cityString = dictFeignClient.getName(hospital.getCityCode());
        String districtString = dictFeignClient.getName(hospital.getDistrictCode());

        hospital.getParam().put("hostypeString", hostypeString);
        hospital.getParam().put("fullAddress", provinceString + cityString + districtString + hospital.getAddress());
        return hospital;
    }

}
