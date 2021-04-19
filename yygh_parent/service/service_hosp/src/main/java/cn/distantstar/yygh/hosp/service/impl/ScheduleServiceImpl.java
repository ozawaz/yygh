package cn.distantstar.yygh.hosp.service.impl;

import cn.distantstar.yygh.hosp.repository.ScheduleRepository;
import cn.distantstar.yygh.hosp.service.DepartmentService;
import cn.distantstar.yygh.hosp.service.HospitalService;
import cn.distantstar.yygh.hosp.service.ScheduleService;
import cn.distantstar.yygh.model.hosp.Schedule;
import cn.distantstar.yygh.vo.hosp.BookingScheduleRuleVo;
import cn.distantstar.yygh.vo.hosp.ScheduleQueryVo;
import com.alibaba.fastjson.JSONObject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: distantstar
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private ScheduleRepository scheduleRepository;
    private MongoTemplate mongoTemplate;
    private HospitalService hospitalService;
    private DepartmentService departmentService;


    @Autowired
    public void setScheduleRepository(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    public void setHospitalService(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @Autowired
    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Override
    public void save(Map<String, Object> paramMap) {
        // 把参数集合转换成需要的对象 Hospital
        String mapString = JSONObject.toJSONString(paramMap);
        Schedule schedule = JSONObject.parseObject(mapString, Schedule.class);

        // 判断数据库中是否存在数据
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
        return scheduleRepository.findAll(example, pageable);
    }

    @Override
    public void remove(String hoscode, String hosScheduleId) {
        Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if (null != schedule) {
            scheduleRepository.deleteById(schedule.getId());
        }
    }

    @Override
    public Map<String, Object> getRuleSchedule(long page, long limit, String hoscode, String depcode) {
        // 1 根据医院编号和科室编号 查询
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);

        // 2 根据工作日期workDate字段进行分组
        Aggregation aggregation = Aggregation.newAggregation(
            // 匹配条件
            Aggregation.match(criteria),
            // 分组字段
            Aggregation.group("workDate")
                    .first("workDate").as("workDate")
                    //3 统计号源数量
                    .count().as("docCount")
                    .sum("reservedNumber").as("reservedNumber")
                    .sum("availableNumber").as("availableNumber"),
            //排序
            Aggregation.sort(Sort.Direction.DESC,"workDate"),
            //4 实现分页
            Aggregation.skip((page-1)*limit),
            Aggregation.limit(limit)
        );

        // 调用方法，最终执行
        AggregationResults<BookingScheduleRuleVo> aggregateResults = 
                mongoTemplate.aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> bookingScheduleRuleVoList  = aggregateResults.getMappedResults();

        // 分组查询的总记录数
        Aggregation totalAgg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
        );
        AggregationResults<BookingScheduleRuleVo> totalAggResults =
                mongoTemplate.aggregate(totalAgg,
                        Schedule.class, BookingScheduleRuleVo.class);
        int total = totalAggResults.getMappedResults().size();

        // 把日期对应星期获取
        for(BookingScheduleRuleVo bookingScheduleRuleVo:bookingScheduleRuleVoList) {
            Date workDate = bookingScheduleRuleVo.getWorkDate();
            String dayOfWeek = this.getDayOfWeek(new DateTime(workDate));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);
        }

        // 设置最终数据，进行返回
        Map<String, Object> result = new HashMap<>();
        result.put("bookingScheduleRuleList",bookingScheduleRuleVoList);
        result.put("total",total);

        // 获取医院名称
        String hosName = hospitalService.getName(hoscode);
        // 其他基础数据
        Map<String, String> baseMap = new HashMap<>();
        baseMap.put("hosname",hosName);
        result.put("baseMap",baseMap);

        return result;

    }

    @Override
    public List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate) {
        //根据参数查询mongodb
        List<Schedule> scheduleList =
                scheduleRepository.findScheduleByHoscodeAndDepcodeAndWorkDate(
                        hoscode,depcode,new DateTime(workDate).toDate());

        //把得到list集合遍历，向设置其他值：医院名称、科室名称、日期对应星期
        scheduleList.forEach(this::packageSchedule);
        return scheduleList;
    }

    /**
     * 封装排班详情其他值 医院名称、科室名称、日期对应星期
     * @param schedule 排班信息
     */
    private void packageSchedule(Schedule schedule) {
        //设置医院名称
        schedule.getParam().put("hosname",hospitalService.getName(schedule.getHoscode()));
        //设置科室名称
        schedule.getParam().put("depname",departmentService.getDepName(schedule.getHoscode(),schedule.getDepcode()));
        //设置日期对应星期
        schedule.getParam().put("dayOfWeek",this.getDayOfWeek(new DateTime(schedule.getWorkDate())));

    }


    /**
     * 根据日期获取周几数据
     * @param dateTime 日期
     * @return 根据日期返回星期
     */
    private String getDayOfWeek(DateTime dateTime) {
        String dayOfWeek = "";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "周日";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "周一";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "周二";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "周四";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "周五";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "周六";
            default:
                break;
        }
        return dayOfWeek;
    }

}
