package cn.distantstar.yygh.hosp.service.impl;

import cn.distantstar.yygh.hosp.mapper.HospitalSetMapper;
import cn.distantstar.yygh.hosp.service.HospitalSetService;
import cn.distantstar.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet>
        implements HospitalSetService {
}
