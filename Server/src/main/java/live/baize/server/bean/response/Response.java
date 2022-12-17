package live.baize.server.bean.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Response {
    private String msg;
    private Object data;
    private Integer code;

    public Response(ResponseEnum responseEnum) {
        this.msg = responseEnum.getMsg();
        this.code = responseEnum.getCode();
        this.data = null;
    }

    public Response(ResponseEnum responseEnum, Object data) {
        this.msg = responseEnum.getMsg();
        this.code = responseEnum.getCode();
        this.data = data;
    }

    public Response(Integer code, Object data, String msg) {
        this.msg = msg;
        this.code = code;
        this.data = data;
    }
}
