package cn.distantstar.yygh.hosp.service;

import cn.distantstar.yygh.model.hosp.Schedule;
import cn.distantstar.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

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
     * @param hoscode
     * @param hosScheduleId
     */
    void remove(String hoscode, String hosScheduleId);

}
