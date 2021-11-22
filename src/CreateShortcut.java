import mslinks.ShellLink;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class CreateShortcut {

    DatabaseCon databaseCon;

    //File archive = new File("M:/tempTest/Archive.txt");
    FileWriter myWriter;

    String[] dir;
    String[] subDir;

    boolean cancel = false;

    String thumbnailPath;
    String linkPath;
    String linkName;
    String mainDirectory;
    File mainDir;
    Gui gui;
    int progressIndex;
    Main.Task task;

    boolean progressCheck = false;
    //final String galleryDirectory = "M:/tempTest/Gallery/";

    //constructor
    public CreateShortcut(String dir, Gui gui, Main.Task task){

        this.mainDir = new File((this.mainDirectory = dir));
        this.gui = gui;
        this.gui.console.setText("");
        this.progressIndex = 0;
        //this.task = task;
        //listDirectories();
        //this.databaseCon = new DatabaseCon();
        //this.databaseCon.printTable();

    }

    public void listDirectories() {

        try {

            //this.myWriter = new FileWriter(archive, true);
            dir = listDir();

            for ( String dirName : dir ){

                System.out.println(dirName + ":");
                subDir = listSubDir(dirName);

                for( String subDirName : subDir ){

                    String thumbnailName = getIconName(dirName + "/"+ subDirName);
                    this.linkName = subDirName;
                    this.linkPath = getMainDirectory() + dirName + "/" + this.linkName;
                    this.thumbnailPath = getMainDirectory() + dirName + "/" + this.linkName + "/"+ thumbnailName;
                    String[] imgSet = getSortedImgSet(dirName + "/"+ subDirName);

                    /*
                    System.out.println("linkName : " + this.linkName);
                    System.out.println("linkPath : " + this.linkPath);
                    System.out.println("iconPath : " + this.thumbnailPath);
                     */

                    this.databaseCon = new DatabaseCon(this.linkPath);

                    //!linkCreatedCheck(this.linkName)
                    System.out.println(this.linkName);
                    if(!this.databaseCon.alreadyInDb(this.linkName)) {
                        if(this.databaseCon.addEntry(this.linkName, true, this.thumbnailPath, dirName)){

                            this.gui.console.append("--> Added " + dirName + " -> " + this.linkName + " to db");

                        }
                        //createLink(this.thumbnailPath, this.linkPath, this.linkName);
                        //this.databaseCon.createNewEntryTable(imgSet, this.linkName);
                        //this.myWriter.write(this.linkName + "\n");
                    } else {
                        System.out.println("Link already created");
                        this.gui.console.append("--> Item " + dirName + " -> " + this.linkName + " already in db");
                    }

                    this.gui.console.append("\n");
                    this.gui.scrollPane.getVerticalScrollBar().setValue(this.gui.scrollPane.getVerticalScrollBar().getMaximum());
                    this.progressIndex++;

                    if(cancel){

                        throw new IOException();

                    }
                }

                System.out.print("\n");

            }

            myWriter.close();
            progressCheck = true;

        } catch (IOException e){

            e.printStackTrace();
            Thread.currentThread().interrupt();

        }

    }

    public int getProgressIndex(){

        return this.progressIndex;

    }

    /*
    //method no longer used, swapped to checking if entry is already in mysql db
    private boolean linkCreatedCheck(String fileName){

        try {
            Scanner scanner = new Scanner(archive);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if(line.equals(fileName)){

                    return true;

                }

            }
        } catch(FileNotFoundException e) {
            //handle this

            e.printStackTrace();

        }

        return false;

    }

    // method no longer used, no need to create links instead add entries to db
    private void createLink(String thumbNailFp, String imgSetFp, String newLinkFp){

        try {
            // createLink( file path to original img )
            // setWorkingDir( file path to dir with all imgs )
            // saveTo( file path to save link )
            ShellLink sl = ShellLink.createLink(thumbNailFp)
                    .setWorkingDir(imgSetFp);

            sl.saveTo(getGalleryDirectory() + newLinkFp + ".lnk");
            System.out.println(sl.getWorkingDir());
            System.out.println(sl.resolveTarget());

            //updateArchive(myWriter, newLinkFp);


        } catch (IOException e) {

            e.printStackTrace();

        }

    }
     */


    private String[] listDir(){

        return this.mainDir.list(filter());

    }

    private String[] getSortedImgSet(String pathName){

        File subDir = new File(getMainDirectory()+pathName);
        File readMe = new File(getMainDirectory() + pathName + "/Readme.txt");
        readMe.delete();
        String[] sortedImgSet = subDir.list();
        try {
            Arrays.sort(sortedImgSet, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    if (toInt(o1) < toInt(o2)) {
                        return -1;
                    } else if (toInt(o1) > toInt(o2)) {
                        return 1;
                    }
                    return 0;
                }
            });
        } catch (NumberFormatException e){



        }

        return sortedImgSet;

    }

    private int toInt(String str){

        return Integer.parseInt(str.split("\\.")[0]);

    }

    private String getIconName(String pathName){

        File subDir = new File(getMainDirectory()+pathName);
        String[] sortedImgSet = subDir.list();
        Arrays.sort(sortedImgSet);

        return sortedImgSet[0];

    }

    private String[] listSubDir(String dirName){

        File subDir = new File(getMainDirectory()+dirName);

        return subDir.list(filter());

    }

    /*
    private String getGalleryDirectory(){

        return galleryDirectory;

    }
     */

    private String getMainDirectory(){

        return this.mainDirectory;

    }

    private FilenameFilter filter(){

        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File f, String name) {
                return new File(f, name).isDirectory();
            }
        };

        return filter;

    }

}
