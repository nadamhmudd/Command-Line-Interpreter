package com.company;

import java.util.Vector;

public class parser {
    static enum availcmd{cd,ls,cp,cat,more,mkdir,rmdir,mv,rm,args,date,help,pwd,clear,exit}
    Vector args= new Vector();
    Vector cmd= new Vector();
    //"path"
    // "command|command|command"
    // cmd arg arg arg ...
    private static Vector split(String stream, String key){
        Vector v= new Vector();
        String entry="";
        if(key.equals("commands")){
            for(int i=0; i<stream.length() ; i++){
                if(String.valueOf(stream.charAt(i)).compareTo("|")!=0){
                    entry+=String.valueOf(stream.charAt(i));
                }else{
                    v.add(entry);
                    entry="";
                }
            }
            v.add(entry); //add last entry
        }
        else if(key.equals("data")){
            for(int i=0; i<stream.length() ; i++){
                if(String.valueOf(stream.charAt(i)).compareTo(" ")!=0){
                    entry+=String.valueOf(stream.charAt(i));
                }else{
                    v.add(entry);
                    entry="";
                }
            }
            v.add(entry); //add last entry
        }
        return v;
    }

    private boolean checkcmd(){
        boolean result= false;
        for(int i=0; i<cmd.size(); i++){
            for(availcmd c : availcmd.values()) {
                if(c.name().equals(cmd.get(i))){
                    result=true;
                    break;
                }
                result=false; //not available
            }
            if(result==false) //if one cmd not available, return false
                break;
        }

        return result;
    }

    private boolean checkargs(){

        boolean result= false;

        for(int i=0; i<args.size(); i++){

            Vector prm= new Vector();
            String prmentry="";

            //split args for each cmd
            String arg= String.valueOf(args.get(i));

            for(int j=0; j<arg.length(); j++){
                if(arg.charAt(j)!=' '){
                    prmentry+=String.valueOf(arg.charAt(j));
                }
                else{
                    prm.add(prmentry);
                    prmentry="";
                }
            }
            prm.add(prmentry); //add last arg

            //check args of single cmd
            if(prm.contains(">>") || prm.contains(">")){
                if((cmd.get(i).equals("ls") || cmd.get(i).equals("help") || cmd.get(i).equals("cat") || cmd.get(i).equals("more") || cmd.get(i).equals("args")))
                {
                    result= true;
                    prm.removeElementAt(prm.size()-2); //remove redirect symbol
                    prm.removeElementAt(prm.size()-1); //remove file name
                }
                else
                    return false;
            }
            else{
                //no Arguments
                if(cmd.get(i).equals("ls")||
                        cmd.get(i).equals("date")||
                        cmd.get(i).equals("help")||
                        cmd.get(i).equals("pwd")||
                        cmd.get(i).equals("clear")||
                        cmd.get(i).equals("exit")
                ){
                    if(prm.get(0).equals("0")|| prm.get(0).equals(" "))
                        result=true;
                    else
                        return false;
                }
                //one Argument
                else if(cmd.get(i).equals("cd")||
                        cmd.get(i).equals("args")||
                        cmd.get(i).equals("mkdir")||
                        cmd.get(i).equals("rmdir")||
                        cmd.get(i).equals("rm")
                ){
                    if(prm.size()==1 && !(prm.get(0).equals("0")))
                        result=true;
                    else
                        return false;

                }
                //two Arguments
                else if(cmd.get(i).equals("cp") || cmd.get(i).equals("mv")){
                    if(prm.size()==2)
                        result=true;
                    else
                        return false;
                }
                //else
                else if(cmd.get(i).equals("cat")){
                    if(prm.size()>=1 && !(prm.get(0).equals("0")))
                        result=true;
                    else
                        return false;
                }
                else if(cmd.get(i).equals("more")){
                    if( (prm.size()==1 || prm.size()==2)&& !(prm.get(0).equals("0")))
                        result=true;
                    else
                        return false;
                }
            }
        }

        return result;
    }

    public boolean parser(String input){

        //input.replace('/', '"');
        boolean cmdresult= false,argsresult= false;
        Vector commands= new Vector();
        Vector data= new Vector();

        if(input.length()==0)
            return false;
        else{
            if(input.charAt(0)=='"' && input.charAt(input.length()-1)=='"'){
                //delete the quotes begin and end
                input=input.substring(1, input.length()-1);

                commands=split(input,"commands"); //each index has one command

                for(int i = 0; i < commands.size(); i++){

                    //split all data of each command(cmd args)
                    data=split(String.valueOf(commands.get(i)),"data");
                    // System.out.println(data);

                    String arg="";
                    for(int j=0; j <data.size(); j++){

                        //case 1: cmd without args
                        if(data.size()==1){
                            cmd.add(data.get(0)); //first word will be the command
                            arg+="0 ";
                        }
                        //case 2: cmd with args
                        else{
                            if(j==0)
                                cmd.add(data.get(0));
                            else{
                                arg+=data.get(j);
                                arg+=" ";
                            }
                        }
                    }
                    arg=arg.substring(0, arg.length()-1); //remove trailing spaces
                    args.add(arg); //add last arg
                }
                //check if the command in available list or not
                cmdresult=checkcmd();
                //check args
                argsresult=checkargs();

            }else{
                System.out.println("Error Parsing the Command");

            }
        }

        return cmdresult && argsresult;
    }

    public Vector getCmd(){
        return cmd;
    }

    public Vector getArguments(){
        return args;
    }

}
