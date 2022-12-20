package live.baize.server.controller;

import live.baize.server.bean.exception.BusinessException;
import live.baize.server.bean.exception.SystemException;
import live.baize.server.bean.response.Response;
import live.baize.server.bean.response.ResponseEnum;
import live.baize.server.service.utils.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class Handler {

    @Resource
    private MailUtil mailUtil;

    @ExceptionHandler(MissingRequestValueException.class)
    public Response handleMissingRequestValueException(MissingRequestValueException missingRequestValueException) {
        return new Response(ResponseEnum.Param_Missing, missingRequestValueException.getMessage());
    }

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

    @ExceptionHandler(BusinessException.class)
    public Response handleBusinessException(BusinessException businessException) {
        // 直接返回即可
        return new Response(businessException.getCode(), null, businessException.getMessage());
    }

    @ExceptionHandler(SystemException.class)
    public Response handleSystemException(SystemException systemException) {
        StringBuilder stringBuilder = new StringBuilder();
        for (StackTraceElement error : systemException.getStackTrace()) {
            stringBuilder.append(error.toString()).append('\n');
        }
        // 记录日志
        log.error(systemException.getMessage() + stringBuilder);
        // 发送信息给运维
        mailUtil.sendExceptionMail("systemException, " + systemException.getMessage() + stringBuilder);
        // 返回前端信息
        return new Response(systemException.getCode(), null, systemException.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Response handleException(Exception exception) {
        StringBuilder stringBuilder = new StringBuilder();
        for (StackTraceElement error : exception.getStackTrace()) {
            stringBuilder.append(error.toString()).append('\n');
        }
        // 记录日志
        log.error(exception.getMessage() + stringBuilder);
        // 发送信息给运维
        mailUtil.sendExceptionMail("SYSTEM_UNKNOWN, " + exception.getMessage() + stringBuilder);
        // 返回前端信息
        return new Response(ResponseEnum.SYSTEM_UNKNOWN, null);
    }

}
