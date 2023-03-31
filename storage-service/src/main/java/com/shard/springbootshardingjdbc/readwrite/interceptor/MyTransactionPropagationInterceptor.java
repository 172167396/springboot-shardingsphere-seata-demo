package com.shard.springbootshardingjdbc.readwrite.interceptor;

import io.seata.core.context.RootContext;
import io.seata.integration.http.XidResource;
import io.seata.tm.api.GlobalTransactionContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.base.seata.at.SeataTransactionHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class MyTransactionPropagationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String xid = RootContext.getXID();
        String rpcXid = request.getHeader(RootContext.KEY_XID);

        if (log.isDebugEnabled()) {
            log.debug("xid in RootContext[{}] xid in HttpContext[{}]", xid, rpcXid);
        }
        if (xid == null && rpcXid != null) {
            RootContext.bind(rpcXid);
            SeataTransactionHolder.set(GlobalTransactionContext.getCurrentOrCreate());
            if (log.isDebugEnabled()) {
                log.debug("bind[{}] to RootContext", rpcXid);
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) {
        if (RootContext.inGlobalTransaction()) {
            XidResource.cleanXid(request.getHeader(RootContext.KEY_XID));
        }
    }

}