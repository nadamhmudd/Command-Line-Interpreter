package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.Vector;
import java.lang.*;


public class terminal {
    //current is the current directory
    static String current="D:\\";
    static String file="";

    private void redirect(String filename, String data, String symbol)throws IOException {
        redirect(filename, data, symbol, 0);
    }

    private void redirect(String filename, String data, String symbol, int i)throws IOException{
        try{
            File file= new File(pathHandle(filename));

            //not exist->create new file
            if(!(file.exists())){
                System.out.println("create new file");
                Path filepath = file.toPath(); //convert String to Path
                Files.createFile(filepath);
            }
            //file exist
            FileWriter fr = new FileWriter(file, true); //true -> to append
            if(i==0 && symbol.equals(">")){ //overwrite at begbeginning with symbol(>)
                fr = new FileWriter(file, false);
            }
            fr.write(data+"\n");
            fr.close();

        }catch (IOException e) {
            System.out.println("error!");
        }

    }

    public String pathHandle(String path){//handling short and long path Relative to the current directory
        if(path.indexOf(':')==-1) {
            path=current+path;
            //System.out.println("pathhandle test");
        }

        return path;
    }

    public void cd (String path) {
        if(path.equals("0")) {
            current="D:\\";
            return;
        }

        current = path;
    }

    public void pwd() {
        System.out.println("Current Directory is " + current);
    }

    public void clear() {
        for (int i = 0; i < 50; ++i) System.out.println();
    }

    public void cp(String sourcePath, String destinationPath){
        sourcePath =pathHandle(sourcePath);
        destinationPath=pathHandle(destinationPath);
        try {
            Files.copy(new File(sourcePath).toPath(), new File(destinationPath).toPath());
        }
        catch (IOException e) {
            System.out.println("error");
        }
    }

    public void mv(String sourcePath, String destinationPath){
        sourcePath =pathHandle(sourcePath);
        destinationPath=pathHandle(destinationPath);
        try {
            Files.move(new File(sourcePath).toPath(), new File(destinationPath).toPath());
        }
        catch (IOException e) {
            System.out.println("error");
        }
    }

    public void rmdir(String sourcePath) {
        File f = new File(pathHandle(sourcePath));
        f.delete();
    }

    public void rm(String sourcePath){
        File f = new File(pathHandle(sourcePath));
        if(f.delete())
            System.out.println("Deletion is done");
        else
            System.out.println("No such Directory");
    }

    public void mkdir(String sourcePath) {
        File f = new File(pathHandle(sourcePath));
        f.mkdirs();
    }

    public void date() {
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date dateobj = new Date();
        System.out.println(df.format(dateobj));
    }

    //commands with redirect to files

    public void ls(String arg){  //redirect
        try {
            File f = new File(current);
            File[] files = f.listFiles();

            for(int i = 0; i < files.length; i++){
                //case 1: print on consle
                if(String.valueOf(arg.charAt(0)).equals("0")){
                    if(i==0){System.out.println("Files are:");}
                    System.out.println(files[i].getName());
                }
                //case 2: redirect to file with append(>>)
                else if((arg.substring(0,2)).equals(">>")){ //overwrite
                    String filename= arg.substring(3);//skip space
                    redirect(filename,files[i].getName(),">>");
                }
                //case 3: redirect to file with overwrite(>)
                else if((arg.substring(0,1)).equals(">")){ //overwrite
                    String filename= arg.substring(2);//skip space
                    redirect(filename,files[i].getName(),">",i);
                }
            }
        }
        catch(Exception e){
            System.out.println("error ops");
        }
    }

    public void help(String arg) { //redirect
        try{
            Vector output= new Vector();
            output.add(String.format("%s %s & %15s", "clear:", "Display Information about built in commands", "Takes no Arguments"));
            output.add(String.format("%s %s & %15s", "\ncd:", "Changes the current Directory to new dic", "one Argument:The new Directory Path"));
            output.add(String.format("%s %s & %15s", "\nls:", "List each given file or directory name", "Takes no Arguments"));
            output.add(String.format("%s %s & %15s", "\ncp:", "If the last argument names an existing directory, cp copies each other given file into a file with " +
                    "the same name in that directory", "Two Arguments:(The Source path,The Destination Path)"));
            output.add(String.format("%s %s & %15s", "\nmv:", "If the last argument names an existing directory, mv moves each other given file into a file with the" +
                    " same name in that directory.", "1st Argument: The Source path / 2nd Argument: THe Destination Path"));
            output.add(String.format("%s %s & %15s", "\nrm", "removes each specified File or Directory", "one Argument: The File or Directory path to be removed"));
            output.add(String.format("%s %s & %15s", "\nmkdir:", "Creates a directory with the given name", "one Argument: The New Directory Path even if parent doesn't exist"));
            output.add(String.format("%s %s & %15s", "\nrmdir:", "Removes each given empty directory", "one Argument: The Path to be removed"));
            output.add(String.format("%s %s & %15s", "\ncat:", "Concatenate files and print on the standard output", "Arguments: The Files to be concatenated"));
            output.add(String.format("%s %s & %15s", "\nmore:", "display and scroll down the output in one direction only", "Takes no Arguments"));
            output.add(String.format("%s %s & %15s", "\npwd:", "Display current user directory", "Takes no Arguments"));
            output.add(String.format("%s %s & %15s", "\nargs:", "Display The arguments of the following cmd", "one Argument: cmd-The command instruction"));
            output.add(String.format("%s %s & %15s", "\ndate:", "Display The Current Date and Time", "Takes no Arguments"));
            output.add(String.format("%s %s & %15s", "\nhelp:", "Display Information about built in commands", "Takes no Arguments"));
            output.add(String.format("%s %s & %15s", "\nexit:", "Exit the Shell", "Takes no Arguments"));

            //case 1: print on consle
            if(String.valueOf(arg.charAt(0)).equals("0")){
                System.out.println(output);
            }
            else{
                for(int i=0; i<output.size(); i++){
                    //case 2: redirect to file with append(>>)
                    if((arg.substring(0,2)).equals(">>")){ //overwrite
                        String filename= arg.substring(3);//skip space
                        redirect(filename,String.valueOf(output.get(i)),">>");
                    }
                    //case 3: redirect to file with overwrite(>)
                    else if((arg.substring(0,1)).equals(">")){ //overwrite
                        String filename= arg.substring(2);//skip space
                        redirect(filename,String.valueOf(output.get(i)),">",i);
                    }
                }
            }
        }
        catch(Exception e){
            System.out.println("error");
        }
    }

    public void args(String path){ //redirect
        String [] arg= path.split(" ",path.length());

        //check redirect to file
        String symbol="", filename="";
        if(arg.length>1)
            symbol= arg[arg.length-2];

        int j=0; // pass to redirect func
        if(symbol.equals(">>") || symbol.equals(">"))
            filename=arg[arg.length-1];

        try{
            int idx=0;
            String output="";
            idx = parser.availcmd.valueOf(arg[0]).ordinal();
            switch (idx){
                case 0:
                    output="Number of args is 1: The new Directory Path";
                    break;
                case 2: case 7:
                    output="Number of args is 2: Source Path, Destination Path";
                    break;
                case 3:
                    output="Many Number of args: The File Directories";
                    break;
                case 5:
                    output="Number of args is 1: The new Directory Path";
                    break;
                case 6:
                    output="Number of args is 1: The Directory to be removed";
                    break;
                case 8:
                    output="Number of args is 1: The Directory to be removed";
                    break;
                case 9:
                    output="Number of args is 1: Command Instruction";
                    break;
                case 1: case 4: case 10 : case 13 : case 14 : case 11: case 12:
                    output="No Arguments for this command";
                    break;
            }
            //case 1: redirect to file with append(>>)
            if(symbol.equals(">>")){ //overwrite
                redirect(filename,output,symbol); //always append
            }
            //case 2: redirect to file with overwrite(>)
            else if(symbol.equals(">")){ //overwrite
                redirect(filename,output,">",j++);
            }
            //case 3: print on consle
            else
                System.out.println(output);
        }
        catch (Exception e){
            System.out.println("Error");
        }
    }

    public void cat(String paths) { //redirect
        String [] arg= paths.split(" ",paths.length());
        int size= arg.length;

        //check redirect to file
        String symbol="", filename="";
        if(arg.length>1)
            symbol= arg[arg.length-2];

        int j=0; // pass to redirect func
        if(symbol.equals(">>") || symbol.equals(">")){
            filename=filename= arg[arg.length-1];
            size=arg.length-2;
        }
        for(int i=0;i<size;i++){
            try{
                String line = null;
                FileReader fileReader = new FileReader(arg[i]);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                while((line = bufferedReader.readLine()) != null){

                    //case 1: redirect to file with append(>>)
                    if(symbol.equals(">>")){ //overwrite
                        redirect(filename,line,">>"); //always append
                    }
                    //case 2: redirect to file with overwrite(>)
                    else if(symbol.equals(">")){ //overwrite
                        redirect(filename,line,">",j++);
                    }
                    //case 3: print on consle
                    else
                        System.out.println(line);
                }
                bufferedReader.close();
            }
            catch (Exception e){
                System.out.println("No Such a file name : error");
            }
        }
    }

    public void more(String path){ //redirect

        String [] arg= path.split(" ",path.length());
        int size=arg.length;

        //check redirect to file
        String symbol="", filename="";
        if(arg.length>1)
            symbol= arg[arg.length-2];


        int j=0; // pass to redirect func
        if(symbol.equals(">>") || symbol.equals(">"))
        {
            filename=arg[arg.length-1];
            size=arg.length-2;
        }
        try{
            long max_c=0;
            for(int i=0; i<size;i++){
                max_c += Files.lines(Paths.get(arg[i])).count();
            }
            int count=0,reset=0 ;String line="";
            while(true){
                for (int k=0;k<10;k++){
                    if(count==max_c){
                        System.out.println("No MOre Lines To Show");
                        return;
                    }
                    //still read firstfile
                    else if(count<Files.lines(Paths.get(arg[0])).count()){
                        line = Files.readAllLines(Paths.get(arg[0])).get(count);
                    }
                    //start read secondfile
                    else if(count==Files.lines(Paths.get(arg[0])).count()){
                        System.out.println("Start Reading second file");
                        reset=count;
                        line = Files.readAllLines(Paths.get(arg[1])).get(count-reset);
                    }
                    else{
                        line = Files.readAllLines(Paths.get(arg[1])).get(count-reset);
                    }

                    //case 1: redirect to file with append(>>)
                    if(symbol.equals(">>")){ //overwrite
                        redirect(filename,line,symbol); //always append
                    }
                    //case 2: redirect to file with overwrite(>)
                    else if(symbol.equals(">")){ //overwrite
                        redirect(filename,line,">",j++);
                    }
                    //case 3: print on consle
                    else{
                        System.out.println(line);
                    }
                    count ++;
                }

                Scanner x = new Scanner(System.in);
                System. out. println("DO You Want To Show More Lines?");
                System. out. println("1- to show more ");
                System. out. println("2- to stop ");

                int num = x.nextInt();
                if(num==2)
                    return;
                else if(num==1 && symbol==">")
                    symbol=">>"; //always append
            }
        }
        catch(Exception e){
            System.out.println("No Such a file name : error");
        }
    }

}

