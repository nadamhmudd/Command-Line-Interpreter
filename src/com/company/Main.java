package com.company;

import java.util.Scanner;
import java.util.Vector;

public class Main {

    public static void main(String[] args) {
        //b1.parser("cd args|clear argss");
        // >> C:\\Users\\kc\\Desktop\\test.txt
        //System.out.println(b1.parser("\"help > mufile.txt|cat mufile.txt >> myfile.txt\""));
        //System.out.println(b1.parser("\"more file.txt new.txt > newfile.txt\""));
        //System.out.println(b1.getCmd());
        //System.out.println(b1.getArguments());
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome");
        while (true) {
            System.out.println("\n Enter your command line");
            String input = in.nextLine();

            if (input.equals("exit")) break;

            else {

                String test = "\"" + input + "\"";

                parser ob = new parser();
                terminal ob1 = new terminal();

                if (ob.parser(test)) {
                    Vector<String> cmd = ob.getCmd();
                    Vector<String> path = ob.getArguments();

                    for (int i = 0; i < cmd.size(); i++) {
                        if (cmd.get(i).equals("cd")) {
                            ob1.cd(path.get(i));
                        } else if (cmd.get(i).equals("clear")) {
                            ob1.clear();
                        } else if (cmd.get(i).equals("ls")) { //redirect
                            ob1.ls(path.get(i));
                        } else if (cmd.get(i).equals("pwd")) {
                            ob1.pwd();
                        } else if (cmd.get(i).equals("cp")) {
                            String source = "";
                            String dest = "";
                            int x;
                            for (x = 0; x < path.get(i).length(); x++) {
                                if (path.get(i).charAt(x) == ' ')
                                    break;

                                source += path.get(i).charAt(x);
                            }

                            dest = path.get(i).substring(x + 1, path.get(i).length());
                            System.out.println("Src: " + source);
                            System.out.println("Des: " + dest);
                            ob1.cp(source, dest);
                        } else if (cmd.get(i).equals("rmdir")) {
                            ob1.rmdir(path.get(i));
                        } else if (cmd.get(i).equals("rm")) {
                            ob1.rm(path.get(i));
                        } else if (cmd.get(i).equals("mkdir")) {
                            ob1.mkdir(path.get(i));
                        } else if (cmd.get(i).equals("date")) {
                            ob1.date();
                        } else if (cmd.get(i).equals("mv")) {
                            String source = "";
                            String dest = "";
                            int x;
                            for (x = 0; x < path.get(i).length(); x++) {
                                if (path.get(i).charAt(x) == ' ')
                                    break;

                                source += path.get(i).charAt(x);
                            }

                            dest = path.get(i).substring(x + 1, path.get(i).length());
                            ob1.mv(source, dest);
                        } else if (cmd.get(i).equals("help")) { //redirect
                            ob1.help(path.get(i));
                        } else if (cmd.get(i).equals("args")) { //redirect
                            ob1.args(path.get(i));
                        } else if (cmd.get(i).equals("cat")) { //redirect
                            ob1.cat(path.get(i));
                        } else if (cmd.get(i).equals("more")) { //redirect
                            ob1.more(path.get(i));
                        }
                    }
                }
                else {
                    System.out.println(ob.parser(test) + ", Enter your cmd-line correctly! ");
                }

            }
        }
    }

}

