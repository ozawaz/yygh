package cn.distantstar.yygh.cmn.listener;

import cn.distantstar.yygh.cmn.mapper.DictMapper;
import cn.distantstar.yygh.model.cmn.Dict;
import cn.distantstar.yygh.vo.cmn.DictEeVo;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.springframework.beans.BeanUtils;

/**
 * @Author: distantstar
 */
public class DictListener extends AnalysisEventListener<DictEeVo> {

    private final DictMapper dictMapper;

    public DictListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        // 调用方法添加数据库
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo,dict);
        dictMapper.insert(dict);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

}
