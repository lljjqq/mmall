package cn.mmall.dao.user;

import cn.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    /**
     * 根据id查询用户详细信息
     * @param id
     * @return
     */
    User selectByPrimaryKey(Integer id);

    /**
     * 用户名是否存在
     * @param username
     * @return
     */
    int checkUsername(String username);

    /**
     * 验证邮箱
     * @param email
     * @return
     */
    int checkEmail(String email);

    /**
     * 匹配用户名和密码
     * @param username
     * @param password
     * @return
     */
    User selectLogin(@Param("username") String username, @Param("password")  String password);

    /**
     * 注册插入
     * @param user
     * @return
     */
    int insert(User user);

    /**
     * 查询找回密码问题
     * @param username
     * @return
     */
    String selectQuestionByUsername(String username);

    /**
     * 查询问题答案
     * @param username
     * @param question
     * @param answer
     * @return
     */
    int checkAnswer(@Param("username") String username,@Param("question") String question,@Param("answer") String answer);

    /**
     * 忘记密码的重置密码
     * @param username
     * @param passwordNew
     * @return
     */
    int updatePasswordByUsername(@Param("username") String username,@Param("passwordNew") String passwordNew );

    /**
     * 登陆下的重置密码
     * @param password
     * @param userId
     * @return
     */
    int checkPassword(@Param("password")String password,@Param("userId")Integer userId);

    /**
     * 根据主键更新不为空的属性
     * @param user
     * @return
     */
    int updateByPrimaryKeySelective(User user);

    /**
     *
     * @param email
     * @param userId
     * @return
     */
    int checkEmailByUserId(@Param(value="email")String email,@Param(value="userId")Integer userId);
}
