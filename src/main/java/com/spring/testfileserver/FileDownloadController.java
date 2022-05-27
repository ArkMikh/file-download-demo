package com.spring.testfileserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

@Controller
@PropertySource("classpath:controller.properties")
public class FileDownloadController{

    //Указываем рабочую папку
    @Value("{FileDownloadController.sourcePath}")
    private String folderPath;

    //Сканирует папку на наличие всех файлов и добавляет их в массив файлов
    @RequestMapping("/")
    public String showFiles(Model model) throws IOException {
        Path path=Paths.get(folderPath);
        Files.walkFileTree(path, new MyFileVisitor());

        File folder=new File(folderPath);
        File[] listOfFiles=folder.listFiles();
        //Добавляет массив файлов в модель для передачи в showFiles.jsp
        model.addAttribute("files", listOfFiles);
        return "showFiles";
    }

    //Создает IOStream для скачивания файла, если он попадает под регулярное выражение
    @RequestMapping("/file/{fileName:.+}")
    @ResponseBody
    public void show(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        System.out.println(fileName);
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setHeader("Content-Transfer-Encoding", "binary");

        try {
            BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            FileInputStream inputStream = new FileInputStream(folderPath + fileName);
            int bytesRead;
            byte[] dataBuffer = new byte[1024];
            while ((bytesRead = inputStream.read(dataBuffer)) > 0) {
                outputStream.write(dataBuffer, 0, bytesRead);
            }
            inputStream.close();
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    static class MyFileVisitor implements FileVisitor<Path> {


        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            int pathLength=file.getNameCount();
            System.out.println("visitFile: " + file);
            FileWriter fileWriter=new FileWriter(new File("dir_info.info"));
            fileWriter.write(file.getFileSystem().toString());
            fileWriter.close();
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            return FileVisitResult.TERMINATE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }
    }
}
