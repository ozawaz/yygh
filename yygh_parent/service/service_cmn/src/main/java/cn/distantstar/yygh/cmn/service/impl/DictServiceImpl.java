package cn.distantstar.yygh.cmn.service.impl;

import cn.distantstar.yygh.cmn.listener.DictListener;
import cn.distantstar.yygh.cmn.mapper.DictMapper;
import cn.distantstar.yygh.cmn.service.DictService;
import cn.distantstar.yygh.model.cmn.Dict;
import cn.distantstar.yygh.vo.cmn.DictEeVo;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author distantstar
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict>
            implements DictService{

    @Override
    public void exportData(HttpServletResponse response){
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyExcel没有关系
            String fileName = URLEncoder.encode("数据字典", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");

            List<Dict> dictList = baseMapper.selectList(null);
            List<DictEeVo> dictVoList = new ArrayList<>(dictList.size());
            for(Dict dict : dictList) {
                DictEeVo dictVo = new DictEeVo();
                BeanUtils.copyProperties(dict, dictVo, DictEeVo.class);
                dictVoList.add(dictVo);
            }

            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("数据字典")
                    .doWrite(dictVoList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void importData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), DictEeVo.class, new DictListener(baseMapper)).sheet()
                    .doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getNameByParentDictCodeAndValue(String parentDictCode, String value) {
        // 如果dict_code为空，就直接用value值查询
        if (StringUtils.isBlank(parentDictCode)) {
            Dict dict = baseMapper.selectOne(new QueryWrapper<Dict>()
            .eq("value", value));

            if (null != dict) {
                return dict.getName();
            }
        } else { // 如果不为空，就先根据dict_code查询到其父的id，然后在根据id和value进行查询
            Dict parentDict = this.getByDictCode(parentDictCode);
            if (null == parentDict) {
                return "";
            }
            Dict dict = baseMapper.selectOne(new QueryWrapper<Dict>()
                    .eq("parent_id", parentDict.getId()).eq("value", value));
            if (null != dict) {
                return dict.getName();
            }
        }

        return "";
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {
        // 根据dictCode获取节点
        Dict codeDict = this.getByDictCode(dictCode);
        if (null == codeDict) {
            return null;
        }
        // 根据id查询子节点
        return this.findChildData(codeDict.getId());
    }

    private Dict getByDictCode(String parentDictCode) {
        return baseMapper.selectOne(new QueryWrapper<Dict>()
                .eq("dict_code", parentDictCode));
    }

    /**
     * 根据数据id查询子数据列表
     * @param id 数据id
     * @return 返回查询到的列表
     */
    @Override
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        List<Dict> dicts = baseMapper.selectList(wrapper);

        for (Dict dict: dicts) {
            Long dictId = dict.getId();
            boolean isChild = this.hasChild(dictId);
            dict.setHasChildren(isChild);
        }

        return dicts;
    }

    /**
     * 判断id下面是否有子节点
     * @param id 数据id
     * @return 根据统计的数量返回true或false
     */
    private boolean hasChild(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(wrapper);
        return count > 0;
    }
}
