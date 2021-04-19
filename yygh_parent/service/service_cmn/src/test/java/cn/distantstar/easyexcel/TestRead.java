package cn.distantstar.easyexcel;

import com.alibaba.excel.EasyExcel;

/**
 * @Author: distantstar
 */
public class TestRead {

    public static void main(String[] args) {
        String fileName = new String("D:\\ideaStart\\JavaProject\\shangyitong\\test\\easyExcel\\user.xlsx");
        EasyExcel.read(fileName, UserData.class, new ExcelListener()).sheet()
                .doRead();
    }
}
