package com.qc.language.service.db.user;

import com.qc.language.service.db.data.UserDetails;

/**
 * 用户信息仓储器
 */
public interface UserRepository {

    /**
     * 保存信息
     *
     * @param details
     */
    public void saveUserDetails(UserDetails details);

    /**
     * 获得信息
     @param username
     * @return
     */
    public UserDetails getUserDetailsByUsername(String username);

    /**
     * 清除当前用户信息
     *
     * @param username
     */
    public void removeUserDetailsByUsername(String username);

}
