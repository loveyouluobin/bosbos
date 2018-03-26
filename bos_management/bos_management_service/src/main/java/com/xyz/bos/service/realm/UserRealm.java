package com.xyz.bos.service.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xyz.bos.dao.system.UserRepository;
import com.xyz.bos.domain.system.User;

/**
 * ClassName:UserRealm <br/>
 * Function: <br/>
 * Date: 2018年3月26日 上午10:31:08 <br/>
 */

@Component
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private UserRepository userRepository;

    // 授权的方法
    // 每一次访问需要权限的资源的时候,都会调用授权的方法
   

    // 认证的方法

    // 参数中的token就是subject.login(token)方法中传入的参数
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) throws AuthenticationException {

        UsernamePasswordToken usernamePasswordToken =
                (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();
        // 根据用户名查找用户
        User user = userRepository.findByUsername(username);
        if (user != null) {
            // 找到 ->比对密码

            /**
             * @param principal 当事人,主体.通常是从数据库中查询到的用户
             * @param credentials 凭证,密码.是从数据库中查询出来的密码
             * @param realmName
             */
            AuthenticationInfo info = new SimpleAuthenticationInfo(user,
                    user.getPassword(), getName());
            // 比对成功 -> 执行后续的逻辑
            // 比对失败 -> 抛异常
            return info;
        }

        // 找不到 -> 抛异常

        return null;
    }

	@Override// 授权的方法 每一次访问需要权限的资源的时候,都会调用授权的方法 要一次要用到缓存
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
	SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
	info.addStringPermission("courierAction_pageQuery");//授权客户 拦截规则 定义的名字
	info.addRole("admin");//授予给 admin角色 给jsp页面
		return info;
	}

}
