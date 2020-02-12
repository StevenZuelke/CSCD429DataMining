import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class main {
    //TrainingData
    static ArrayList<String[]> TrainingSet = new ArrayList<String[]>();
    static ArrayList<String[]> TestSet = new ArrayList<String[]>();
    static final File TrainingFile = new File("../gene_files/Genes_relation.data");
    static final File TestFile = new File("../gene_files/Genes_relation.test");

    public static void main(String[] args){
        TrainingSet = readSet(TrainingFile);
        TestSet = readSet(TestFile);
        TrainingSet = preProcess(TrainingSet);
        TestSet = preProcess(TestSet);
    }

    public static ArrayList<String[]> preProcess(ArrayList<String[]> preSet){
        Boolean testSet = true;
        if(preSet.get(0).length == 9) testSet = false;
        ArrayList<String[]> postSet = new ArrayList<String[]>();
        int k, q;
        for(int j = 1; j < preSet.size(); j++){
            q = j-1;
            postSet.add(new String[8]);
            for(int i = 0; i < preSet.get(0).length-1; i++){
                k = i;
                if(i >= 7) k = i+1;
                String str = preSet.get(j)[k];
                //Declare Missing values '?' and '.' non null, "null" value,  to ignore later
                if((str.equals("?") || str.equals("Unknown")) && (i != preSet.get(0).length-1 || testSet == false)){
                    str = "null";
                }
                postSet.get(q)[i] = str;
            }
        }
        return postSet;
    }

    public static ArrayList<String[]> readSet(File file){
        ArrayList<String[]> dataIn = new ArrayList<String[]>();
        try {
            Scanner scan = new Scanner(file);
            int i, j = 0, k = 0;
            String string = scan.nextLine();
            int inQuotes = 0;
            while(scan.hasNextLine()){
                dataIn.add(new String[9]);
                for(i = 0;i<9;i++){
                    if(!(k < string.length())) { //If at the end of the line goto next
                        string = scan.nextLine();
                        k = 0;
                    }
                    dataIn.get(j)[i] = "";
                    while(k < string.length() && ((string.charAt(k)!=',' || inQuotes == 1)|| (i == 8 && string.charAt(k) != ' '))){ //Next char qualifies to be added to current string
                        if(string.charAt(k) == '\"') inQuotes = (inQuotes + 1) % 2; //Tell whether the comma is inside an attribute (in quotes)
                        dataIn.get(j)[i] += string.charAt(k);
                        k++;
                    }
                    k++;
                }
                j++;
            }
            scan.close();
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
        return dataIn;
    }

}
