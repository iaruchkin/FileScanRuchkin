package com.ruchkin.igor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static List<File> filesList = new LinkedList<>();
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public void filesFromFolder(File folder) {
        //осуществляется рекурсивный обход всех вложенных папок
        LOGGER.log(Level.FINE, "Scanning files");
        File[] folderEntries = folder.listFiles();
        if (folderEntries != null) {
            for (File entry : folderEntries){
                if (entry.isDirectory())
                {
                    filesFromFolder(entry);
                } else {
                    filesList.add(entry);   //если попадается файл, он добавляется в список
                }
            }
        }
    }

    public String joinFiles(List<File> fileList){
        //файлы сортируются по имени в списке и записываются в строку
        LOGGER.log(Level.INFO, "Sort files");
        Collections.sort(fileList);
        StringBuilder str = new StringBuilder();
        LOGGER.log(Level.INFO, "Joining files");

        for(File f : fileList) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(f), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "IOException caught", e);

            }
        }
        return str.toString();
    }

    public void writeFile(File rootPath, File newFile, String fileText){
        //создается файл в корневой директории, в него записывается строка
        LOGGER.log(Level.INFO, "Building new file");
        try(FileWriter writer = new FileWriter(rootPath
                + File.separator + newFile, false))
        {
            writer.write(fileText);
            writer.flush();
        }
        catch(IOException e){
            LOGGER.log(Level.SEVERE, "IOException caught", e);
            System.out.println("Directory does not exist");
        }
    }

    public static void main(String[] args) {

        System.out.println("Please type your root folder: ");
        Scanner input = new Scanner(System.in);
        File rootPath =  new File(input.next());
        System.out.println("Please type new file name: ");
        File newFile =  new File(input.next());
//        File rootPath =  new File("/home/iaruchkin/java/workspace/files");
//        File newFile =  new File("newfile.txt");

        new Main().filesFromFolder(rootPath);
        new Main().writeFile(rootPath, newFile, new Main().joinFiles(filesList));
        System.out.println("Your data now in: " + rootPath
                + File.separator + newFile);
    }
}
