package live.baize.server.bean.exception;

import live.baize.server.bean.response.ResponseEnum;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final Integer code;

    public BusinessException(ResponseEnum responseEnum) {
        super(responseEnum.getMsg());
        this.code = responseEnum.getCode();
    }

    public BusinessException(ResponseEnum responseEnum, Throwable cause) {
        super(responseEnum.getMsg(), cause);
        this.code = responseEnum.getCode();
    }
}
