package cn.distantstar.yygh.hosp.service;

import cn.distantstar.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author distantstar
 */
public interface HospitalSetService extends IService<HospitalSet> {

    /**
     * 根据编码查询相应的签名
     * @param hoscode 编码
     * @return 返回签名
     */
    String getSignKey(String hoscode);
}
