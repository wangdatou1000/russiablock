package tools.net.Server;
/*
 * @author uv
 * @date 2018/10/13 18:10
 * 传输请求对象
 */

import java.io.Serializable;

public class RpcRequest implements Serializable{

    private static final long serialVersionUID = -2577707401136472809L;

    private String id;
    private Object data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RpcRequest{" + "id='" + id + '\'' + ", data=" + data + '}';
    }
}
