package cn.distantstar.yygh.hosp.repository;

import cn.distantstar.yygh.model.hosp.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author distantstar
 */
@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, String> {
    /**
     * 通过医院编码和排班号查询相应的排班信息
     * @param hoscode 医院编码
     * @param hosScheduleId 排班号
     * @return 返回排班信息
     */
    Schedule getScheduleByHoscodeAndHosScheduleId(String hoscode, String hosScheduleId);
}
