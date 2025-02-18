import java.util.HashMap;
public class Data {
    HashMap<String, String> file;
    Data() {
        file = new HashMap<>();
    }
    public void setMap(String key, String value) {
        file.put(key,value);
    }
    public void removeData(String key) {
        file.remove(key);
    }
    public void printMap() {
        for(String s: file.keySet()) {
            System.out.println(s);
        }
    }
}
