package cn.distantstar.yygh.hosp.repository;

import cn.distantstar.yygh.model.hosp.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

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

    /**
     * 根据医院编号 、科室编号和工作日期，查询排班详细信息
     * @param hoscode 医院编号
     * @param depcode 科室编号
     * @param toDate 工作日期
     * @return 返回排班详细信息
     */
    List<Schedule> findScheduleByHoscodeAndDepcodeAndWorkDate(String hoscode, String depcode, Date toDate);
}
