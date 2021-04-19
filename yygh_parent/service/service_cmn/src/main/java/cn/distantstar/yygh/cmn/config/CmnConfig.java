package cn.distantstar.yygh.cmn.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author distantstar
 */
@Configuration
@MapperScan("cn.distantstar.yygh.cmn.mapper")
public class CmnConfig {

    /**
     * 分页插件
     * @return
     */
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
