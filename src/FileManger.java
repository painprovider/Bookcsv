
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FileManger {
     List<String> readFile() throws IOException;
     Map<String ,String> read(String searchKey, String searchValue, String[] strings) throws IOException;
     boolean add(String id, String[] book, File file);
     boolean edit(String searchKey, String searchValue, String headerKey, String newData) throws IOException;
     boolean delete(String searchKey, String searchValue) throws IOException;
}
