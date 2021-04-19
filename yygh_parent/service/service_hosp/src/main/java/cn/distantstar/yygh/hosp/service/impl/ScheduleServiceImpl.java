package cn.distantstar.yygh.hosp.service.impl;

import cn.distantstar.yygh.hosp.repository.ScheduleRepository;
import cn.distantstar.yygh.hosp.service.ScheduleService;
import cn.distantstar.yygh.model.hosp.Schedule;
import cn.distantstar.yygh.vo.hosp.ScheduleQueryVo;
import com.alibaba.fastjson.JSONObject;
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
public class ScheduleServiceImpl implements ScheduleService {

    private ScheduleRepository scheduleRepository;

    @Autowired
    public void setScheduleRepository(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public void save(Map<String, Object> paramMap) {
        // 把参数集合转换成需要的对象 Hospital
        String mapString = JSONObject.toJSONString(paramMap);
        Schedule schedule = JSONObject.parseObject(mapString, Schedule.class);

        // 判断数据库中是否存在数据
        String hoscode = schedule.getHoscode();
        Schedule scheduleExist = scheduleRepository.
                getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(), schedule.getHosScheduleId());

        // 如果不存在，直接添加
        if (scheduleExist == null) {
            schedule.setCreateTime(new Date());
        } else {// 如果存在，进行修改
            schedule.setCreateTime(scheduleExist.getCreateTime());
        }
        schedule.setStatus(1);
        schedule.setUpdateTime(new Date());
        schedule.setIsDeleted(0);
        scheduleRepository.save(schedule);
    }

    @Override
    public Page<Schedule> selectPage(Integer page, Integer limit, ScheduleQueryVo scheduleQueryVo) {
        // 创建Pageable对象，设置当前页和每页记录数
        Pageable pageable = PageRequest.of(page - 1, limit);

        // 将VO类转换城实体类
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo, schedule);

        // 创建匹配器
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);

        // 创建实例
        Example<Schedule> example = Example.of(schedule, matcher);

        // 查找
        Page<Schedule> pages = scheduleRepository.findAll(example, pageable);
        return pages;
    }

    @Override
    public void remove(String hoscode, String hosScheduleId) {
        Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if (null != schedule) {
            scheduleRepository.deleteById(schedule.getId());
        }
    }
}
