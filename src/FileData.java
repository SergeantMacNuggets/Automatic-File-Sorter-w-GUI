import java.util.List;
public class FileData {
    private String file;
    private String dest;
    private String src;
    private List<Integer> date;
    public FileData() {}

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public List<Integer> getDate() {
        return date;
    }

    public void setDate(List<Integer> date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "FileData{" +
                "file='" + file + '\'' +
                ", dest='" + dest + '\'' +
                ", src='" + src + '\'' +
                ", date=" + date +
                '}';
    }
}
