package com.xyz.bos.service.realm;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xyz.bos.dao.system.PermissionRepository;
import com.xyz.bos.dao.system.RoleRepository;
import com.xyz.bos.dao.system.UserRepository;
import com.xyz.bos.domain.system.Permission;
import com.xyz.bos.domain.system.Role;
import com.xyz.bos.domain.system.User;

@Component
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired//注入角色dao接口roleRepository
    private RoleRepository roleRepository;
   @Autowired ////注入权限dao接口roleRepository
   private PermissionRepository permissionRepository;
       
   	@Override// 授权的方法 每一次访问需要权限的资源的时候,都会调用授权的方法 要一次要用到缓存
   	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
   	SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();//获得授权对象
   	//根据当前用户去查询对应的权限和角色 角色是权限的集合 一个角色包含各种不同权限
   	Subject subject = SecurityUtils.getSubject();//当前用户所有的权限和角色对象
   	User user = (User) subject.getPrincipal();//权限和角色对象用这个方法 强转为登陆成功后的user对象
   	if ("admin".equals(user.getUsername())) {//如果是内置管理员 和权限和角色是写死的有全部权限
   		List<Role> roles = roleRepository.findAll();//所有角色
   		for (Role role : roles) {//遍历所有角色
   			info.addRole(role.getKeyword());//所有角色都给了admin Keyword关键字
   		}
   		List<Permission> permissions = permissionRepository.findAll();//所有权限
   		for (Permission permission : permissions) {//遍历所有权限
   			info.addStringPermission(permission.getKeyword());//所有权限都给了管理员 Keyword关键字
   		}
   	}else {//其它普遍用户 动态授权角色和权限跟据 用户id
   		List<Role> roles = roleRepository.findbyUid(user.getId());//根据用户id查有多少角色
   		for (Role role : roles) {//遍历所有角色
   			info.addRole(role.getKeyword());//所有角色都给了当前用户 Keyword关键字
   		}
   		List<Permission> permissions = permissionRepository.findbyUid(user.getId());////根据用户id查有多少权限
   		for (Permission permission : permissions) {//遍历所有权限
   			info.addStringPermission(permission.getKeyword());//所有权限都给了管理员 Keyword关键字
   		}
   	}
   	return info;
   	}
    
    
    @Override// 认证的方法  参数中的token就是subject.login(token)方法中传入的参数
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken =(UsernamePasswordToken) token;//强转为usernamePasswordToken
        String username = usernamePasswordToken.getUsername();//获得用户名
        User user = userRepository.findByUsername(username); // 根据用户名查找用户
        if (user != null) { // 找到 ->比对密码
      AuthenticationInfo info = new SimpleAuthenticationInfo(user,user.getPassword(), getName());//参数 从数据库中查询到的用户,凭证,密码.是从数据库中查询出来的密码
            return info; // 比对成功 -> 执行后续的逻辑
        }
        return null; // 比对失败 -> 抛异常
    }


}
