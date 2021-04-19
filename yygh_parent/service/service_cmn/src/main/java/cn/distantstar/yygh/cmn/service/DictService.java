package cn.distantstar.yygh.cmn.service;

import cn.distantstar.yygh.model.cmn.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author distantstar
 */
public interface DictService extends IService<Dict> {

    // 根据id查询子数据列表
    List<Dict> findChildData(Long id);

    // 导出数据字典
    void exportData(HttpServletResponse response) throws UnsupportedEncodingException;

    // 导入数据字典
    void importData(MultipartFile file);

    /**
     * 根据上级编码与值获取数据字典名称
     * @param parentDictCode
     * @param value
     * @return
     */
    String getNameByParentDictCodeAndValue(String parentDictCode, String value);

    /**
     * 根据dictCode获取下级节点
     * @param dictCode
     * @return
     */
    List<Dict> findByDictCode(String dictCode);
}
