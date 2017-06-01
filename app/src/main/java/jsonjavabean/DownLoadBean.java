package jsonjavabean;

/**
 * Created by 啊丁 on 2017/5/20.
 */

public class DownLoadBean {
    private String name;
    private String progress;
    private String max;
    private String author;
    private String source;
    private byte[] pic;

    public byte[] getPic() {
        return pic;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }
}
