package com.digcoin.snapx.core.advice;

import cn.dev33.satoken.exception.SaTokenException;
import com.digcoin.snapx.core.common.error.CommonError;
import com.digcoin.snapx.core.error.ErrorCodeDefinition;
import com.digcoin.snapx.core.error.enums.EnumErrorCodeFactory;
import com.digcoin.snapx.core.web.ApiBody;
import com.digcoin.snapx.core.error.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StreamUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String CR = System.lineSeparator();

    /**
     * 处理所有不可知的异常
     */
    @ResponseBody
    @ExceptionHandler({Throwable.class, Exception.class, RuntimeException.class})
    public ResponseEntity<ApiBody<Void>> handleThrowableException(Throwable e, HttpServletRequest request) {
        ErrorCodeDefinition errorCode = CommonError.UNEXPECT_ERROR.getErrorCode();
        return this.getResponseEntity(request, e, errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 处理所有不可知的异常
     */
    @ResponseBody
    @ExceptionHandler({SaTokenException.class})
    public ResponseEntity<ApiBody<Void>> handleSaTokenException(SaTokenException e, HttpServletRequest request) {
        return this.getResponseEntity(request, e, String.valueOf(e.getCode()), e.getMessage(), null);
    }

    /**
     * BusinessException 业务异常
     */
    @ResponseBody
    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<ApiBody<Object>> handleBusinessException(BusinessException e, HttpServletRequest request) {
        return this.getResponseEntity(request, e, e.getCode(), e.getMessage(), null);
    }

    /**
     * 请求类型不匹配
     */
    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiBody<Void>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e,
                                                                                      HttpServletRequest request) {
        ErrorCodeDefinition errorCode = HttpError.METHOD_NOT_ALLOWED.getErrorCode();
        return this.getResponseEntity(request, e, errorCode.getCode(), errorCode.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiBody<Void>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e,
                                                                                       HttpServletRequest request) {
        ErrorCodeDefinition errorCode = HttpError.BAD_REQUEST.getErrorCode();
        return this.getResponseEntity(request, e, errorCode.getCode(), "缺少参数 : " + e.getParameterName());
    }

    /**
     * 参数异常
     */
    @ResponseBody
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ApiBody<Void>> handlerValidationException(ConstraintViolationException e, HttpServletRequest request) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        StringBuilder sb = new StringBuilder();
        constraintViolations.forEach(constraintViolation -> sb.append(constraintViolation.getPropertyPath())
                .append(" ")
                .append(constraintViolation.getMessage())
                .append(" "));
        log.warn("请求参数异常 {}", sb);
        String message = constraintViolations.stream().map(constraintViolation -> constraintViolation.getMessage()).findFirst().orElse(null);
        ErrorCodeDefinition errorCode = HttpError.BAD_REQUEST.getErrorCode();
        return this.getResponseEntity(request, e, errorCode.getCode(), message, null);
    }

    /**
     * 参数异常
     */
    @ResponseBody
    @ExceptionHandler(value = {IllegalStateException.class})
    public ResponseEntity<ApiBody<Void>> handlerIllegalStateException(IllegalStateException e, HttpServletRequest request) {
        log.warn("请求参数异常 {} ", e.getLocalizedMessage());
        ErrorCodeDefinition errorCode = HttpError.BAD_REQUEST.getErrorCode();
        return this.getResponseEntity(request, e, errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 参数异常
     */
    @ResponseBody
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ApiBody<Void>> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder();
        bindingResult.getAllErrors().stream().filter(allError -> allError instanceof FieldError).map(allError -> (FieldError) allError)
                .forEach(fieldError -> {
                    String field = fieldError.getField();
                    String message = fieldError.getDefaultMessage();
                    sb.append(String.format("参数：%s 值：%s 错误信息:%s ", field, fieldError.getRejectedValue(), message));
                });
        log.warn("请求参数异常 {}", sb);
        ErrorCodeDefinition errorCode = HttpError.BAD_REQUEST.getErrorCode();
        String message = bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage()).findFirst().orElse(null);
        return this.getResponseEntity(request, e, errorCode.getCode(), message, null);
    }

    /**
     * 参数类型不匹配
     */
    @ResponseBody
    @ExceptionHandler({TypeMismatchException.class})
    public ResponseEntity<ApiBody<Void>> requestTypeMismatch(TypeMismatchException e, HttpServletRequest request) {
        log.warn("参数类型不匹配 参数:{} 类型应该为{}", e.getPropertyName(), e.getRequiredType());
        ErrorCodeDefinition errorCode = HttpError.BAD_REQUEST.getErrorCode();
        return this.getResponseEntity(request, e, errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 参数类型不匹配
     */
    @ResponseBody
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ApiBody<Void>> requestTypeMismatch(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.warn("参数类型不匹配: {}", e.getMessage());
        ErrorCodeDefinition errorCode = HttpError.BAD_REQUEST.getErrorCode();
        return this.getResponseEntity(request, e, errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 参数异常
     */
    @ResponseBody
    @ExceptionHandler(value = {BindException.class})
    public ResponseEntity<ApiBody<Void>> handlerBindException(BindException e, HttpServletRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder();
        bindingResult.getAllErrors().stream().filter(allError -> allError instanceof FieldError).map(allError -> (FieldError) allError)
                .forEach(fieldError -> {
                    String field = fieldError.getField();
                    String message = fieldError.getDefaultMessage();
                    sb.append(String.format("参数：%s 值：%s 错误信息:%s ", field, fieldError.getRejectedValue(), message));
                });
        log.warn("请求参数异常 {}", sb);
        ErrorCodeDefinition errorCode = HttpError.BAD_REQUEST.getErrorCode();
        String message = bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage()).findFirst().orElse(null);
        return this.getResponseEntity(request, e, errorCode.getCode(), message, null);
    }

    protected <T> ResponseEntity<ApiBody<T>> getResponseEntity(HttpServletRequest request, Throwable e, String code, String msg) {
        return this.getResponseEntity(request, e, code, msg, null);
    }

    protected <T> ResponseEntity<ApiBody<T>> getResponseEntity(HttpServletRequest request, Throwable e, String code, String msg, T data) {

        if (!(e instanceof BusinessException)) {
            if (log.isDebugEnabled()) {
                log.debug("请求详情:\n{}", this.getHttpRequestInfo(request));
            }
            log.error(e.getMessage(), e);
        }

        ApiBody<T> voidResponseVO = new ApiBody<>(code, msg, data);
        return new ResponseEntity<>(voidResponseVO, HttpStatus.OK);
    }

    protected String getHttpRequestInfo(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        //请求URL
        String url = request.getRequestURL().toString();
        sb.append("Request URL: ").append(url).append(CR);
        //请求方式
        String method = request.getMethod();
        sb.append("Request Method: ").append(method).append(CR);
        //header请求参数
        Map<String, List<String>> headerMaps = this.getHeaderParameter(request);
        sb.append("Request Headers: ").append(CR);
        for (Map.Entry<String, List<String>> entry : headerMaps.entrySet()) {
            sb.append("    ").append(entry.getKey())
                    .append(": ").append(String.join(",", entry.getValue()))
                    .append(CR);
        }

        //请求参数
        Map<String, List<String>> parameterMaps = this.getParameter(request);
        sb.append("Request Parameter: ").append(CR);
        for (Map.Entry<String, List<String>> entry : parameterMaps.entrySet()) {
            sb.append("    ").append(entry.getKey())
                    .append(": ").append(String.join(",", entry.getValue()))
                    .append(CR);
        }

        try (ServletInputStream inputStream = request.getInputStream()) {
            //如果使用了@RequestBody，那么这里的流其实已经关闭了，在读取就会报错了！！！！
            if (!inputStream.isFinished()) {
                sb.append("Request Body: ").append(CR);
                sb.append("    ").append(StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8)).append(CR);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return sb.toString();
    }

    /**
     * 获取请求参数
     */
    private Map<String, List<String>> getParameter(HttpServletRequest request) {
        Enumeration<String> parameterNames = request.getParameterNames();
        Map<String, List<String>> parameterMaps = new HashMap<>();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String[] parameterValues = request.getParameterValues(paramName);
            if (parameterValues != null && parameterValues.length > 0) {
                parameterMaps.put(paramName, Arrays.asList(parameterValues));
            }
        }
        return parameterMaps;
    }

    /**
     * 获取Header
     */
    private Map<String, List<String>> getHeaderParameter(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, List<String>> parameterMaps = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String paramName = headerNames.nextElement();
            List<String> paramValue = new LinkedList<>();
            Enumeration<String> headers = request.getHeaders(paramName);
            while (headers.hasMoreElements()) {
                paramValue.add(headers.nextElement());
            }
            parameterMaps.put(paramName, paramValue);
        }
        return parameterMaps;
    }

    enum HttpError implements EnumErrorCodeFactory {

        METHOD_NOT_ALLOWED("405", "HTTP请求方法错误"),
        BAD_REQUEST("400", "HTTP请求参数错误");

        HttpError(String code, String message) {
            this.update(code, message);
        }
    }
}
