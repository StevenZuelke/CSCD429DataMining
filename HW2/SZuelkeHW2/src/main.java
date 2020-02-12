import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class main {
    //TrainingData
    static ArrayList<String[]> TrainingSet = new ArrayList<String[]>();
    static ArrayList<String[]> TestSet = new ArrayList<String[]>();
    static ArrayList<String[]> Keys = new ArrayList<String[]>();
    static ArrayList<String[]> Predictions = new ArrayList<String[]>();
    static final File TrainingFile = new File("gene_files/Genes_relation.data");
    static final File TestFile = new File("gene_files/Genes_relation.test");
    static final File KeysFile = new File("gene_files/keys.txt");

    public static void main(String[] args){
        prepare();
        Predictions = evaluatePredictions();
        System.out.println("Accuracy:");
        System.out.println(evaluateAccuracy());
        savePredictions();
    }

    //Use keys to evaluate accuracy
    public static double evaluateAccuracy(){
        double accuracy = 0;
        double ncorrect = 0;
        double nwrong = 0;
        for(String[] predict : Predictions){
            for(String[] actual : Keys){
                if(predict[0].equals(actual[0])){
                    if(predict[1].equals(actual[1])) ncorrect++;
                    else nwrong++;
                }
            }
        }
        accuracy = ncorrect / (ncorrect + nwrong);
        return accuracy;
    }

    //Use k-nearest neighbor to predict localization
    public static ArrayList<String[]> evaluatePredictions(){
        ArrayList<String[]> predict = new ArrayList<String[]>();
        for(int i = 0; i < TestSet.size(); i++){ //for all tuples in test data
            predict.add(new String[2]);
            predict.get(i)[0] = TestSet.get(i)[0];
            int maxWeight = 0;
            ArrayList<Integer> maxInds = new ArrayList<Integer>(); //List of indexes to training tuples with max weight
            maxInds.add(0); // Default neighbor 0
            for(int j = 0; j < TrainingSet.size(); j++){ //Compare test tuple to each Training tuple
                int weight = 0;
                for(int attribute = 1; attribute < TestSet.get(0).length - 2; attribute++){
                    if(TestSet.get(i)[attribute].equals(TrainingSet.get(j)[attribute]) && !TestSet.get(i)[attribute].equals("null")){
                        weight++;
                    }
                }
                if(weight >= maxWeight){ //Tuple is max
                    if(weight > maxWeight) maxInds.clear(); //new max
                    maxWeight = weight;
                    maxInds.add(j); //record index
                }
            }//End training tuple
            //Compute the prediction based on up to 3 relative tuples
            int k = 5;
            int avg = 0;
            String local = TrainingSet.get(maxInds.get(0))[7];
            if(maxInds.size() < k) k = maxInds.size();
            for(int ind = 0; ind < k; ind++){
                int count = 0;
                String localInd = TrainingSet.get(maxInds.get(ind))[7];
                for(int ind2 = 0; ind2 < k; ind2++){
                    String localInd2 = TrainingSet.get(maxInds.get(ind2))[7];
                    if((localInd == localInd2) && (ind != ind2)){
                        count++;
                        if(count >= avg) {
                            avg = count;
                            local = localInd;
                        }
                    }
                }
            }
            predict.get(i)[1] = local;
        }//End test tuple
        return predict;
    }

    //read and preprocess the files
    public static void prepare(){
        TrainingSet = readSet(TrainingFile);
        TestSet = readSet(TestFile);
        TrainingSet = preProcess(TrainingSet);
        TestSet = preProcess(TestSet);
        Keys = readKeys();
    }

    //PreProcess Training or Test data set (take out function column and fill the variety of missing datas with "null"
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
                if(str.equals("?") || str.equals("Unknown")){
                    str = "null";
                }
                postSet.get(q)[i] = str;
            }
        }
        return postSet;
    }

    //Read the keys without the column labels
    public static ArrayList<String[]> readKeys(){
        ArrayList<String[]> dataIn = new ArrayList<String[]>();
        try{
            Scanner scan = new Scanner(KeysFile);
            int i, j = 0, k = 0;
            String string = scan.nextLine();
            while(scan.hasNextLine()){
                dataIn.add(new String[2]);
                for(i = 0; i < 2; i++){
                    if(!(k < string.length())) { //If at the end of the line goto next
                        string = scan.nextLine();
                        k = 0;
                    }
                    dataIn.get(j)[i] = "";
                    while(k < string.length() && string.charAt(k) != ','){
                        dataIn.get(j)[i] += string.charAt(k);
                        k++;
                    }
                    k++; //Skip the comma
                }
                j++; //Goto next tuple
            }
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
        dataIn.remove(0); //Remove the labels
        return dataIn;
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
                    k++; //Skip the comma
                }
                j++; //Goto next tuple
            }
            scan.close();
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
        return dataIn;
    }

    //Save the prediction to a file
    public static void savePredictions(){
       try {
           File file = new File("Predictions.txt");
           FileWriter writer = new FileWriter(file);
           for(int i = 0; i < Predictions.size(); i++){
               writer.write(Predictions.get(i)[0]);
               writer.write(",");
               writer.write(Predictions.get(i)[1]);
               writer.write("\n");
           }
           writer.close();
       }catch(IOException e){
           e.printStackTrace();
           return;
       }
    }

}
