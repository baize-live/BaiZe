package live.baize.server.controller;

import live.baize.server.bean.exception.BusinessException;
import live.baize.server.bean.exception.SystemException;
import live.baize.server.bean.response.Response;
import live.baize.server.bean.response.ResponseEnum;
import live.baize.server.service.utils.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class Handler {

    @Resource
    private MailUtil mailUtil;

    // 文件上传异常
    @ExceptionHandler(MultipartException.class)
    public Response handleMultipartException(MultipartException multipartException) {
        log.info(multipartException.getMessage());
        if (multipartException instanceof MaxUploadSizeExceededException) {
            return new Response(ResponseEnum.MaxUploadSizeExceeded, null);
        }
        return new Response(ResponseEnum.Not_IsMultipart, null);
    }

    // 参数异常
    @ExceptionHandler(ValidationException.class)
    public Response handleValidationException(ValidationException validationException) {
        List<String> errorList = new ArrayList<>();
        if (validationException instanceof ConstraintViolationException) {
            ConstraintViolationException exs = (ConstraintViolationException) validationException;
            Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
            for (ConstraintViolation<?> item : violations) {
                errorList.add(item.getMessage());
            }
        } else {
            errorList.add(validationException.getMessage());
        }
        return new Response(ResponseEnum.Param_Check, errorList);
    }

    @ExceptionHandler(ServletException.class)
    public Response handleServletException(ServletException servletException) {
        log.info(servletException.getMessage(), servletException);
        // 请求方法不支持
        if (servletException instanceof HttpRequestMethodNotSupportedException) {
            return new Response(ResponseEnum.MethodNotSupported, null);
        }

        // 参数校验缺失
        if (servletException instanceof MissingRequestValueException) {
            return new Response(ResponseEnum.Param_Missing, servletException.getMessage());
        }

        // 请求体参数缺失
        if (servletException instanceof MissingServletRequestPartException) {
            return new Response(ResponseEnum.Request_Part_Missing, servletException.getMessage());
        }

        mailUtil.sendExceptionMail("SYSTEM_UNKNOWN, " + servletException.getMessage() + Arrays.toString(servletException.getStackTrace()));
        return new Response(ResponseEnum.SYSTEM_UNKNOWN, servletException.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public Response handleBusinessException(BusinessException businessException) {
        log.info(businessException.getMessage());
        // 直接返回即可
        return new Response(businessException.getCode(), null, businessException.getMessage());
    }

    @ExceptionHandler(SystemException.class)
    public Response handleSystemException(SystemException systemException) {
        // 记录日志
        log.error(systemException.getMessage(), systemException);
        // 发送信息给运维
        mailUtil.sendExceptionMail("systemException, " + systemException.getMessage());
        // 返回前端信息
        return new Response(systemException.getCode(), null, systemException.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Response handleException(Exception exception) {
        // 记录日志
        log.error(exception.getMessage(), exception);
        // 发送信息给运维
        mailUtil.sendExceptionMail("SYSTEM_UNKNOWN, " + exception.getMessage());
        // 返回前端信息
        return new Response(ResponseEnum.SYSTEM_UNKNOWN, null);
    }

}
