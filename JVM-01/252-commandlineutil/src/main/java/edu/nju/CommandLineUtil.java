package edu.nju;

import org.apache.commons.cli.*;


public class CommandLineUtil {
    private static CommandLine commandLine;
    private static CommandLineParser parser = new DefaultParser();
    private static Options options = new Options();
    private boolean sideEffect;
    public static final String WRONG_MESSAGE = "Invalid input.";

    /**
     * you can define options here
     * or you can create a func such as [static void defineOptions()] and call it before parse input
     */
    static {
        options.addOption(new Option("h", "help",false, "print all options"));
        options.addOption(new Option("p", "print",true, "print args"));
        options.addOption(new Option("s", false, "set sideEffect to be true"));
    }

    public void main(String[] args){
        parseInput(args);
        handleOptions();
    }

    /**
     * Print the usage of all options
     */
    private static void printHelpMessage() {
        System.out.println("help");
    }

    /**
     * Parse the input and handle exception
     * @param args origin args form input
     */
    public void parseInput(String[] args){
        try{
            commandLine = parser.parse(options, args);
        }catch(ParseException e){
            System.out.println(WRONG_MESSAGE);
            System.exit(-1);
        }
    }

    /**
     * You can handle options here or create your own func
     */
    public void handleOptions() {
        if (commandLine.hasOption("h"))
            printHelpMessage();
        else {
            if (commandLine.hasOption("p")) {
                if (commandLine.getArgs() == null || commandLine.getArgs().length == 0)
                    System.out.println(WRONG_MESSAGE);
                else
                    System.out.println(commandLine.getOptionValue("p"));
            }
            if (commandLine.hasOption("s"))
                if (commandLine.getArgs() == null || commandLine.getArgs().length == 0)
                    System.out.println(WRONG_MESSAGE);
                else
                    sideEffect = true;
        }
    }

    public boolean getSideEffectFlag(){
        return sideEffect;
    }
}