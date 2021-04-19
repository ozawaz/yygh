package cn.distantstar.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author: distantstar
 */
@Data
public class UserData {
    @ExcelProperty(value = "用户id", index = 0)
    private int id;
    @ExcelProperty(value = "用户名称", index = 1)
    private String name;
}
