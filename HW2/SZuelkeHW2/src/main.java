import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class main {
    //TrainingData
    static ArrayList<String[]> TrainingSet = new ArrayList<String[]>();

    public static void main(String[] args){
        System.out.println("Hello");
    }
    public static ArrayList<String[]> readTrainingSet(){
        ArrayList<String[]> dataIn = new ArrayList<String[]>();
        File trainingFile = new File("../gene_files/Genes_relation.data");
        try {
            Scanner scan = new Scanner(trainingFile);
            int i, j = 0, k = 0;
            String string = scan.next();
            //while(scan.hasNextLine()){
            for(int z = 0; z<100; z++){
                dataIn.add(new String[9]);
                for(i = 0;i<9;i++){
                    dataIn.get(j)[i]="";
                    while(string.charAt(k)!=','|| (i == 8 && string.charAt(k) == ' ')){
                        dataIn.get(j)[i] += string.charAt(k);
                        k++;
                    }
                    k++;
                    if(!(k < string.length())) {
                        string = scan.next();
                        k = 0;
                    }
                }
                j++;
            }
            scan.close();
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
        return TrainingSet;
    }
}
