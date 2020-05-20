package top.cyixlq.msgalarm.bean;

public class Msg {
    private String pusher; // 发送者
    private String content; // 内容

    public String getPusher() {
        return pusher;
    }

    public void setPusher(String pusher) {
        this.pusher = pusher;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
