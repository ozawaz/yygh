package cn.distantstar.yygh.hosp.repository;

import cn.distantstar.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: distantstar
 */
@Repository
public interface HospitalRepository extends MongoRepository<Hospital, String> {

    /**
     * 判断数据库中是否已经存在数据
     * @param hoscode 实体类的唯一标识编码
     * @return 返回查询的结果
     */
    Hospital getHospitalByHoscode(String hoscode);
}
