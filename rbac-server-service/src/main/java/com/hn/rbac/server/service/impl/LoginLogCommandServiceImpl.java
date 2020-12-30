package com.hn.rbac.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hn.rbac.server.common.utils.HttpContextUtil;
import com.hn.rbac.server.common.utils.IPUtil;
import com.hn.rbac.server.service.LoginLogCommandService;
import com.hn.rbac.server.service.dao.LoginLogMapper;
import com.hn.rbac.server.service.entity.LoginLogDO;
import com.hn.rbac.server.service.entity.converter.LoginLogConverter;
import com.hn.rbac.server.share.model.LoginLog;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class LoginLogCommandServiceImpl extends ServiceImpl<LoginLogMapper, LoginLogDO> implements LoginLogCommandService {

    @Override
    public void saveLoginLog(LoginLog loginLog) {
        Date now = new Date();
        loginLog.setGmtCreate(now);
        loginLog.setGmtModified(now);
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String ip = IPUtil.getIpAddr(request);
        loginLog.setIp(ip);
//        loginLog.setLocation(AddressUtil.getCityInfo(ip));
        baseMapper.insert(LoginLogConverter.INSTANCE.to(loginLog));
    }

}
