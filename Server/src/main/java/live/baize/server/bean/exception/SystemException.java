package live.baize.server.bean.exception;

import live.baize.server.bean.response.ResponseEnum;
import lombok.Getter;

@Getter
public class SystemException extends RuntimeException {
    private final Integer code;

    public SystemException(ResponseEnum responseEnum) {
        super(responseEnum.getMsg());
        this.code = responseEnum.getCode();
    }

    public SystemException(ResponseEnum responseEnum, Throwable cause) {
        super(responseEnum.getMsg(), cause);
        this.code = responseEnum.getCode();
    }
}
