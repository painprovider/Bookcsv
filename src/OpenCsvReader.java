import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OpenCsvReader implements FileManger {
    private File file;
    private List<String> headers = new ArrayList<>();

    public OpenCsvReader(File file) throws IOException {
        this.file = file;
        try {
            BufferedReader buf = getReader();
            headers.addAll(Arrays.asList(buf.readLine().split(",")));
        } catch (IOException ie) {
            throw new IOException();
        }
    }

    private BufferedReader getReader() throws IOException {
        return Files.newBufferedReader(Paths.get(file.getAbsolutePath()));
    }

    @Override
    public List<String> readFile() throws IOException {
        List<String> lines = new ArrayList<>();
        try {
            BufferedReader buf = getReader();
            String str = "";
            while ((str = buf.readLine()) != null) {
                lines.add(str);
            }
        } catch (IOException ie) {
            throw new IOException();
        }
        return lines;
    }

    @Override
    public Map<String, String> read(String searchKey, String searchValue, String... headerKeys) throws IOException {
        Map<String, String> data = new HashMap<>();
        try {
            BufferedReader buf = getReader();
            buf.readLine();
            String line = "";
            int searchIndex = headers.indexOf(searchKey);
            while ((line = buf.readLine()) != null) {
                String[] tempData = parseLineToArray(line);
                if (tempData[searchIndex].equals(searchValue)) {
                    for (String headerKey : headerKeys) {
                        data.put(headerKey, tempData[headers.indexOf(headerKey)]);
                    }
                    break;
                }
            }
        } catch (IOException ie) {
            throw new IOException();
        }
        return data;
    }

    @Override
    public boolean edit(String searchKey, String searchValue, String headerKey, String newData) throws IOException {
        boolean result = false;
        File tempfile = new File("E:\\resources\\temp.csv");
        try (BufferedReader buf = getReader(); FileWriter fileWriter = new FileWriter(tempfile)) {
            String line = "";
            int searchIndex = headers.indexOf(searchKey);
            while ((line = buf.readLine()) != null) {
                String[] tempData = parseLineToArray(line);
                if (tempData[searchIndex].equals(searchValue)) {
                    tempData[headers.indexOf(headerKey)] = newData;
                    String toAppend = String.join(",", tempData);
                    fileWriter.append(toAppend.concat("\n"));
                    result = true;
                } else {
                    fileWriter.append(line.concat("\n"));
                    result = true;
                }
            }
        } catch (IOException ie) {
            throw new IOException();
        }
        System.gc();
        rotateFile(tempfile, this.file);
        return result;
    }

    @Override
    public boolean add(String id, String[] book, File file) {
        boolean result = false;
        try (FileWriter writer = new FileWriter(file, true)) {

            writer.write(id);
            for (String str : book) {
                writer.write(",");
                writer.write(str);
                result = true;
            }
        } catch (IOException ie) {
            System.out.println(ie.getMessage());
        }
        return result;
    }

    @Override
    public boolean delete(String searchKey, String searchValue) throws IOException {
        boolean result = false;
        File tempfile = new File("E:\\resources\\temp.csv");
        try (BufferedReader buf = getReader(); FileWriter fileWriter = new FileWriter(tempfile)) {
            String line = "";
            int searchIndex = headers.indexOf(searchKey);
            if (!tempfile.createNewFile()) {
                while ((line = buf.readLine()) != null) {
                    String[] tempData = parseLineToArray(line);
                    if (tempData[searchIndex].equals(searchValue)) {
                        result = true;
                    } else {
                        fileWriter.append(line.concat("\n"));
                    }
                }
            }
        } catch (IOException ie) {
            ie.printStackTrace();
            throw new IOException();
        }
        System.gc();
        rotateFile(tempfile, this.file);
        return result;
    }

    /*private void rotateFile(File source, File target) throws IOException {
        if (source.exists()) {
            Files.deleteIfExists(target.toPath());
            System.gc();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            FileUtils.moveFile(source, target);
        }
    }*/

    private void rotateFile(File source, File target) throws IOException {

        if (source.exists() && target.exists()) {

            Files.deleteIfExists(target.toPath());

            if (target.createNewFile()) {

                try (BufferedReader reader = Files.newBufferedReader(source.toPath()); BufferedWriter writer = new BufferedWriter(new FileWriter(target))) {

                    String line = "";

                    while ((line = reader.readLine()) != null) {

                        writer.write(line + "");

                        writer.newLine();

                    }

                }

            }

        }

        Files.deleteIfExists(source.toPath());

    }



    private String[] parseLineToArray(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    }
}

