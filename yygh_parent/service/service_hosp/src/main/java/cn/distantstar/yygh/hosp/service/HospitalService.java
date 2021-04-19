package cn.distantstar.yygh.hosp.service;

import cn.distantstar.yygh.model.hosp.Hospital;
import cn.distantstar.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * @author distantstar
 */
public interface HospitalService {

    /**
     * 上传医院接口
     * @param paramMap 需要保存的数据
     */
    void save(Map<String, Object> paramMap);

    /**
     * 根据医院编码查询
     * @param hoscode 医院编码
     * @return 返回查询结果
     */
    Hospital getHospitalByHoscode(String hoscode);

    /**
     * 获取医院信息
     * @param page 当前页
     * @param limit 每页记录数
     * @param hospitalQueryVo 医院管理查询类
     * @return 返回查询到的数据列表
     */
    Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    /**
     * 更新上线状态
     * @param id 需要更改的id
     * @param status 状态1：上线 0：未上线
     */
    void updateStatus(String id, Integer status);

    /**
     * 医院详情
     * @param id 想要显示的id
     * @return 返回值
     */
    Map<String, Object> show(String id);

    /**
     * 根据医院编号获取医院名称接口
     * @param hoscode 医院编号
     * @return 返回医院名称
     */
    String getName(String hoscode);

}
