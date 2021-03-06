package os.hw1.master;

import os.hw1.server.Server;
import os.hw1.util.ErrorLogger;
import os.hw1.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MasterMain {
    public static int portNumber, workersPort = 9812, cachePort = 3247;
    private static int numberOfWorkers, w, numberOfArgs, numberOfPrograms;
    private static List<String> commonArgs = new ArrayList<>();

    private static List<Program> programs = new ArrayList<>();

    private static void input(){
        Scanner scanner = new Scanner(System.in);

        portNumber = scanner.nextInt();
        numberOfWorkers = scanner.nextInt();
        w = scanner.nextInt();

        numberOfArgs = scanner.nextInt();;

        for(int i = 0; i < numberOfArgs; i++){
            String arg = scanner.next();
            commonArgs.add(arg);
        }

        numberOfPrograms = scanner.nextInt();

        for(int i = 0; i < numberOfPrograms; i++){
            String className = scanner.next();
            int weight = scanner.nextInt();

            Program program = new Program(className, weight, i + 1);
            programs.add(program);
        }
    }

    public static int getWeightOfProgram(int programId){
        for(Program program: programs){
            if(program.getId() == programId)
                return program.getW();
        }
        return -1;
    }

    public static String getClassNameOfProgram(int programId){
        for(Program program: programs){
            if(program.getId() == programId)
                return program.getClassName();
        }
        return "";
    }

    public static String[] getCommonArgs(){
        String ans[] = new String[commonArgs.size()];
        for(int i = 0; i < commonArgs.size(); i++)
            ans[i] = commonArgs.get(i);
        return ans;
    }

    public static void main(String[] args) {

        input();

        Logger.getInstance().log("master start " + ProcessHandle.current().pid() + " " + portNumber);

        Server server = new Server(portNumber, numberOfWorkers, w, numberOfArgs,
                numberOfPrograms, commonArgs, programs);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                server.shutdownHook();
                Logger.getInstance().log("master stop " + ProcessHandle.current().pid() + " " + portNumber);
            }
        }));

        server.start(portNumber);
    }
}
