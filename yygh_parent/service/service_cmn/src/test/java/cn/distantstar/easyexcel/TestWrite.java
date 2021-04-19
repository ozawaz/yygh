package cn.distantstar.easyexcel;

import cn.distantstar.yygh.model.acl.User;
import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;

/**
 * @Author: distantstar
 */
public class TestWrite {

    public static void main(String[] args) {
        ArrayList<UserData> users = new ArrayList<UserData>();
        for (int i = 0; i < 10; i++) {
            UserData userData = new UserData();
            userData.setId(i);
            userData.setName("Mike" + i);
            users.add(userData);
        }


        String fileName = new String("D:\\ideaStart\\JavaProject\\shangyitong\\test\\easyExcel\\user.xlsx");
        EasyExcel.write(fileName, UserData.class).sheet("用户信息")
                .doWrite(users);
    }

}
