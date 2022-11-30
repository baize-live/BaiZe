package Bean;

import java.io.*;
import java.util.ArrayList;

//一个操作Word的封装类
public interface WordWork {
    //写入Txt文件
    static void writeTxt(String filePath, ArrayList<String> content, boolean append) {
        File file = new File(filePath);
        if (!file.exists()) file.getParentFile().mkdirs();
        try {
            FileWriter fileWriter;
            fileWriter = new FileWriter(file, append);
            //
            BufferedWriter bw = new BufferedWriter(fileWriter);
            for (String str : content) {
                bw.write(str);
                bw.newLine();
            }
            bw.flush();
            bw.close();
            fileWriter.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    //读取Txt文件
    static ArrayList<String> readTxt(String filePath) {
        File file = new File(filePath);
        ArrayList<String> content = new ArrayList<>();
        if (!file.exists()) return content;
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                content.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}

