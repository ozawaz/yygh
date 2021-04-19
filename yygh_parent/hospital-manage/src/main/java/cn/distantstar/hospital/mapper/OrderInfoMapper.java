package cn.distantstar.hospital.mapper;

import cn.distantstar.hospital.model.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

}
