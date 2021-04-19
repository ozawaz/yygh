package cn.distantstar.yygh.hosp.service;

import cn.distantstar.yygh.model.hosp.Schedule;
import cn.distantstar.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author distantstar
 */
public interface ScheduleService {
    /**
     * 上传排班信息
     * @param paramMap 数据集合
     */
    void save(Map<String, Object> paramMap);

    /**
     * 分页查询
     * @param page 当前页码
     * @param limit 每页记录数
     * @param scheduleQueryVo 查询条件
     * @return 返回查询到的数据
     */
    Page<Schedule> selectPage(Integer page, Integer limit, ScheduleQueryVo scheduleQueryVo);

    /**
     * 删除科室
     * @param hoscode 医院编号
     * @param hosScheduleId 医院排班信息
     */
    void remove(String hoscode, String hosScheduleId);

    /**
     * 根据医院编号 和 科室编号，查询排班规则数据
     * @param page 当前页
     * @param limit 每页记录数
     * @param hoscode 医院编号
     * @param depcode 科室编号
     * @return 返回查询集合
     */
    Map<String, Object> getRuleSchedule(long page, long limit, String hoscode, String depcode);

    /**
     * 根据医院编号 、科室编号和工作日期，查询排班详细信息
     * @param hoscode 医院编号
     * @param depcode 科室编号
     * @param workDate 工作日期
     * @return 返回排班详细信息
     */
    List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate);
}
