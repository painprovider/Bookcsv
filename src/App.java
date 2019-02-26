import java.io.File;
import java.io.IOException;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        try {
            OpenCsvReader open = new OpenCsvReader(new File("resources/books.csv"));
             System.out.println("-----------the Function read !!--------------");
            Map<String,String> data =open.read("id", "5","title","authors");
            data.forEach((k,v) -> System.out.println(k + ": " + v));
            System.out.println("---------the Function add !!!!-------------");
            String[] buch= {"5107","5107","3036731","360","316769177","9.78031676917e+12","J.DSalinger","1951.0","The Catcher in the Rye","The Catcher in the Rye","eng","3.79","2044241","2120637","44920","109383","185520","455042","661516","709176","https://images.gr-assets.com/books/1398034300m/5107.jpg,https://images.gr-assets.com/books/1398034300s/5107.jpg"};
            System.out.println(open.add("10001",buch,new File("resources/books.csv")));
            System.out.println("---------the Function edit !!!!-------------");
            System.out.println(open.edit("id", "10001","authors","David Pitschmann"));
            System.out.println("---------the Function delete !!!!-------------");
            System.out.println(open.delete("id", "10001"));
        }catch (IOException ie){
            ie.printStackTrace();
        }

    }
}
