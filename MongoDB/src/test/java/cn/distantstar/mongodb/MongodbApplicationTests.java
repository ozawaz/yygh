package cn.distantstar.mongodb;

import cn.distantstar.mongodb.entity.User;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.regex.Pattern;

@SpringBootTest
class MongodbApplicationTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    // 添加
    @Test
    public void addUser() {
        User user = new User();
        user.setAge(20);
        user.setName("Kike");
        user.setEmail("34252346@qq.com");
        User insert = mongoTemplate.insert(user);
        System.out.println(insert);
    }

    // 查询所有
    @Test
    public void findUser() {
        List<User> userList = mongoTemplate.findAll(User.class);
        System.out.println(userList);
    }

    //根据id查询
    @Test
    public void getById() {
        User user = mongoTemplate.findById("5ffbfa2ac290f356edf9b5aa", User.class);
        System.out.println(user);
    }

    // 条件查询
    @Test
    public void findUserList() {
        Query query = new Query(Criteria
                .where("name").is("Mike")
                .and("age").is(20));
        List<User> userList = mongoTemplate.find(query, User.class);
        System.out.println(userList);
    }

    // 模糊查询
    @Test
    public void findUserListLikeName() {
        String name = "ike";

        String regex = String.format("%s%s%s", "^.*", name, ".*$");
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Query query = new Query(Criteria.where("name").regex(pattern));
        List<User> userList = mongoTemplate.find(query, User.class);
        System.out.println(userList);
    }

    // 分页查询
    @Test
    public void findUsersPage() {
        String name = "ike";
        int pageNo = 1; // 当前页
        int pageSize = 10; // 每页记录数

        // 条件构建
        String regex = String.format("%s%s%s", "^.*", name, ".*$");
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Query query = new Query(Criteria.where("name").regex(pattern));

        // 分页构建
        // 查询总数
        long count = mongoTemplate.count(query, User.class);
        // 分页
        List<User> userList = mongoTemplate.find(
                query.skip((pageNo - 1) * pageSize)
                        .limit(pageSize), User.class);

        System.out.println(count);
        System.out.println(userList);
    }

    // 修改数据
    @Test
    public void updateUser() {
        // 根据id查询
        User user = mongoTemplate.findById("6075932a1dae540e8e9bd961", User.class);

        // 设置修改值
        user.setName("Mikejkson");
        user.setAge(24);
        user.setEmail("7801564@qq.com");

        // 调用方法实现修改
        Query query = new Query(Criteria.where("_id").is(user.getId()));
        Update update = new Update();
        update.set("name", user.getName());
        update.set("age", user.getAge());
        update.set("email", user.getEmail());
        UpdateResult upsert = mongoTemplate.upsert(query, update, User.class);

        // 返回修改的行数
        long count = upsert.getModifiedCount();
        System.out.println(count);
    }

    //删除操作
    @Test
    public void delete() {
        Query query =
                new Query(Criteria.where("_id").is("6075932a1dae540e8e9bd961"));
        DeleteResult result = mongoTemplate.remove(query, User.class);
        long count = result.getDeletedCount();
        System.out.println(count);
    }


}
