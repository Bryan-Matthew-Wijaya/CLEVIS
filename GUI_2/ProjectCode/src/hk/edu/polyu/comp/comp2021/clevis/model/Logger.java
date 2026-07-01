package hk.edu.polyu.comp.comp2021.clevis.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class Logger {
    private String htmlPath;
    private String txtPath;
    private int operationIndex;
    private BufferedWriter htmlWriter;
    private BufferedWriter txtWriter;
    private boolean isLogged;

    public Logger(String htmlPath,String txtPath){
        this.htmlPath = htmlPath;
        this.txtPath = txtPath;
        this.operationIndex = 1;
        this.isLogged = false;
        logFile();

    }

    public boolean isLogged(){
        return isLogged;
    }

    private void logFile(){
        try{
            htmlWriter = new BufferedWriter(new FileWriter(htmlPath));
            htmlWriter.write("<table>\n");
            txtWriter = new BufferedWriter(new FileWriter(txtPath));
            this.isLogged = true;
        }catch (IOException e){
            System.err.println("Failed to log file: " + e.getMessage());
            closeResources();
        }
    }

    public void logCommand(String command){
        if(isLogged == false){
            return;
        }
        logHtmlFile(command);
        logTxtFile(command);
        operationIndex++;
    }
     private void logHtmlFile(String command){
        try{
            String row = String.format("<tr><td>%d</td><td>%s</td></tr>\n",operationIndex,command);
            htmlWriter.write(row);
            htmlWriter.flush();
        }catch (IOException e){
            System.err.println("Failed to write html file: " + e.getMessage());
        }
     }

    private void logTxtFile(String command){
        try{
            txtWriter.write(command + "\n");
            txtWriter.flush();
        }catch (IOException e){
            System.err.println("Failed to write txt file: " + e.getMessage());
        }
    }

    public void close(){
        closeResources();
    }
    private void closeResources(){
        try{
            if(htmlWriter != null){
                htmlWriter.write("</table>\n");
                htmlWriter.close();
            }

            if(txtWriter != null){
                txtWriter.close();
            }
        }catch (IOException e){
            System.err.println("Error closing logger: " + e.getMessage());
        }
    }


}
