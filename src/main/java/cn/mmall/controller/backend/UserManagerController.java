package cn.mmall.controller.backend;

import cn.mmall.common.Const;
import cn.mmall.common.ServerResponse;
import cn.mmall.pojo.User;
import cn.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 *
 * @Author WJ
 * @Date 2018/8/24 15:29
 */
@Controller
@RequestMapping(value = "/manage/user")
public class UserManagerController {

    @Autowired
    private IUserService iUserService;

    /**
     * 判断登陆的是不是管理员
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = iUserService.login(username, password);

        if (response.isSuccess()) { //如果这个请求是成功的
            //通过泛型getData拿到这个User请求数据
            User user = response.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) { //说明用户登陆的是管理员
                session.setAttribute(Const.CURRENT_USER, user);
                return response;
            } else {
                return ServerResponse.createByErrorMessage("不是管理员");
            }
        }
        return response;
    }
}
