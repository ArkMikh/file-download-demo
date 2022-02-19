package com.arkmikhjava.spring.test.fileserver;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class FileDownloadController {
    //Указываем рабочую папку
    String folderPath="C:\\Users\\fruit\\Desktop\\Java\\";
    String newPath="/";

//    //Сканирует папку на наличие всех файлов и добавляет их в массив файлов
    @RequestMapping("/**")
    public String showFiles(Model model){
        File folder=new File(folderPath);
        File[] listOfFiles=folder.listFiles();
        //Добавляет массив файлов в модель для передачи в showFiles.jsp
        model.addAttribute("files", listOfFiles);
        return "showFiles";
    }


    //Создает IOStream для скачивания файла
    @RequestMapping("/file/{fileName:.+}")
    @ResponseBody
    public void show(@PathVariable("fileName") String fileName, HttpServletResponse response){
        System.out.println(fileName);
        response.setHeader("Content-Disposition", "attachment; filename="+fileName);
        response.setHeader("Content-Transfer-Encoding", "binary");

        try {
            BufferedOutputStream outputStream=new BufferedOutputStream(response.getOutputStream());
            FileInputStream inputStream=new FileInputStream(folderPath+fileName);
            int bytesRead;
            byte[] dataBuffer=new byte[1024];
            while ((bytesRead=inputStream.read(dataBuffer))>0){
                outputStream.write(dataBuffer,0,bytesRead);
            }
            inputStream.close();
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
