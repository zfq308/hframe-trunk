package com.hframework.interceptor;

import com.hframework.base.bean.BusinessHandlerFactory;
import com.hframework.common.annotation.extension.*;
import com.hframework.common.frame.ServiceFactory;
import com.hframework.common.util.ReflectUtils;
import com.hframework.common.util.StringUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 数据集连带规则拦截器
 * Created by zhangquanhong on 2016/8/28.
 */
@Component
@Aspect
public class BusinessHandlerInterceptor {

    private static final long ENUM_CLASS_DEFAULT_HOLDER = 2;

    @Pointcut("execution(* com.hframe.service.impl.*.create(..))")
    private void createMethod(){ }

    @Pointcut("execution(int com.hframe.service.impl.*.update*(..))")
    private void updateMethod(){ }

    @Pointcut("execution(int com.hframe.service.impl.*.batchOperate*(..))")
    private void batchOperateMethod(){ }

    @Pointcut("execution(int com.hframe.service.impl.*.delete*(..))")
    private void deleteMethod(){ }

    @Before(value = "createMethod()")
    public void createBefore(JoinPoint joinPoint) throws Exception {
        Object targetObject = joinPoint.getArgs()[0];
        Map<BeforeCreateHandler, Method> handlers = BusinessHandlerFactory.getHandler(targetObject.getClass(), BeforeCreateHandler.class);
        for (BeforeCreateHandler annotation : handlers.keySet()) {
            Method method = handlers.get(annotation);
            checkAndInvokeHandler(targetObject, annotation.attr(), null, annotation.target(), method, null);
        }
    }

    @AfterReturning(pointcut = "createMethod()", returning = "retVal")
    public void createAfter(JoinPoint joinPoint, int retVal) throws Exception {
        if(retVal > 0) {
            Object targetObject = joinPoint.getArgs()[0];
            Map<AfterCreateHandler, Method> handlers = BusinessHandlerFactory.getHandler(targetObject.getClass(), AfterCreateHandler.class);
            for (AfterCreateHandler annotation : handlers.keySet()) {
                Method method = handlers.get(annotation);
                checkAndInvokeHandler(targetObject, annotation.attr(), null, annotation.target(), method, null);
            }
        }
    }


    @Before(value = "updateMethod()")
    public void updateBefore(JoinPoint joinPoint) throws Exception {
        if(joinPoint.getArgs().length == 1) {//update方法
            Object targetObject = joinPoint.getArgs()[0];
            Class curServiceClass = joinPoint.getSourceLocation().getWithinType();
            Map<BeforeUpdateHandler, Method> handlers = BusinessHandlerFactory.getHandler(targetObject.getClass(), BeforeUpdateHandler.class);
            for (BeforeUpdateHandler annotation : handlers.keySet()) {
                Method method = handlers.get(annotation);
                checkAndInvokeHandler(targetObject, annotation.attr(), annotation.orig(), annotation.target(), method, curServiceClass);
            }

        }/*else {//updateByExample方法
            Object targetObject = joinPoint.getArgs()[0];
        }*/
    }

    @AfterReturning(value = "updateMethod()")
    public void updateAfter(JoinPoint joinPoint) throws Exception {
        if(joinPoint.getArgs().length == 1) {//update方法
            Object targetObject = joinPoint.getArgs()[0];
            Class curServiceClass = joinPoint.getSourceLocation().getWithinType();
            Map<AfterUpdateHandler, Method> handlers = BusinessHandlerFactory.getHandler(targetObject.getClass(), AfterUpdateHandler.class);
            for (AfterUpdateHandler annotation : handlers.keySet()) {
                Method method = handlers.get(annotation);
                checkAndInvokeHandler(targetObject, annotation.attr(), annotation.orig(), annotation.target(), method, curServiceClass);
            }

        }/*else {//updateByExample方法
            Object targetObject = joinPoint.getArgs()[0];
        }*/
    }

    @Before(value = "deleteMethod()")
    public void deleteBefore(JoinPoint joinPoint) throws Exception {
        Object targetObject = joinPoint.getArgs()[0];
        if (targetObject instanceof Long) {
        }else {
            Map<BeforeDeleteHandler, Method> handlers = BusinessHandlerFactory.getHandler(targetObject.getClass(), BeforeDeleteHandler.class);
            for (BeforeDeleteHandler annotation : handlers.keySet()) {
                Method method = handlers.get(annotation);
                checkAndInvokeHandler(targetObject, annotation.attr(), null, annotation.orig(), method, null);
            }
        }
    }

    @AfterReturning(pointcut = "deleteMethod()", returning = "retVal")
    public void deleteAfter(JoinPoint joinPoint, int retVal) throws Exception {
        Object targetObject = joinPoint.getArgs()[0];
        if (targetObject instanceof Long) {
        }else {
            Map<AfterDeleteHandler, Method> handlers = BusinessHandlerFactory.getHandler(targetObject.getClass(), AfterDeleteHandler.class);
            for (AfterDeleteHandler annotation : handlers.keySet()) {
                Method method = handlers.get(annotation);
                checkAndInvokeHandler(targetObject, annotation.attr(), null, annotation.orig(), method, null);
            }
        }
    }

    private void checkAndInvokeHandler(Object targetObject, String attr, String orig, String target,
                                       Method method, Class curServiceClass) throws Exception {
        if(StringUtils.isNotBlank(attr)) {
            String propertyName = attr.trim();
            String targetPropertyValue = BeanUtils.getProperty(targetObject, propertyName);
            if(StringUtils.isNotBlank(target) && !checkValuePass(target, targetPropertyValue)) {
                return;
            }
            if(StringUtils.isNotBlank(orig)) {
                Object originObject = getOriginObject(targetObject, curServiceClass);
                String originPropertyValue = BeanUtils.getProperty(originObject, propertyName);
                if(!checkValuePass(orig, originPropertyValue)) {
                    return;
                }else {
                    invokeHandler(method, targetObject, originObject);
                }
            }else {
                invokeHandler(method, targetObject);
            }
        }else {
            invokeHandler(method, targetObject);
        }
    }

    private boolean checkValuePass(String request, String value) {
        request = request.trim();
        if(request.contains(",")) {
            request = "," + request + ",";
            return request.contains("," + value + ",");
        }else if(request.startsWith("!")) {
            return !request.equals(value);
        }else {
            return request.equals(value);
        }



    }


    private void invokeHandler(Method method, Object... targetObject) throws Exception {
        Object handler = ServiceFactory.getService(method.getDeclaringClass());
        if(targetObject == null) targetObject = new Object[0];

        try {
            Object[] args = new Object[method.getParameterTypes().length];
            for (int i = 0; i < targetObject.length; i++) {
                args[i] = targetObject[i];
            }
            method.invoke(handler, args);
        } catch (Exception e) {
            try{
                Object[] args = new Object[method.getParameterTypes().length +1];
                for (int i = 0; i < targetObject.length; i++) {
                    args[i] = targetObject[i];
                }
                args[method.getParameterTypes().length] = null;
                method.invoke(handler, args);
            }catch (Exception e1){
                e1.printStackTrace();
            }
        }
    }

    private Object getOriginObject(Object targetObject, Class curServiceClass) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        Object service = ServiceFactory.getService(curServiceClass);
        String keyPropertyGetMethod = "get" + targetObject.getClass().getSimpleName() + "Id";
        String keyPropertyValue = String.valueOf(ReflectUtils.invokeMethod(targetObject, keyPropertyGetMethod, new Class[0], new Object[0]));

        String methodName = "get" + targetObject.getClass().getSimpleName() + "ByPK";

        return ReflectUtils.invokeMethod(service, methodName, new Class[]{long.class}, new Object[]{Long.valueOf(keyPropertyValue)});
    }
}