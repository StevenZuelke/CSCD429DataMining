import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static private File dataFile = new File("synthetic_control_data.txt");
    static private ArrayList<Double[]> dataList = new ArrayList<Double[]>();
    static private ArrayList<ArrayList<Double[]>> clusterList = new ArrayList<ArrayList<Double[]>>();

    public static void main(String[] args) throws IOException {
        dataList = readData();
        clusterList = kMeans();
        for(int i = 0; i < clusterList.size(); i++){
            System.out.println("Cluster: "+i);
            for(int j = 0; j < 60; j++){
                System.out.print(clusterList.get(i).get(0)[j]+ " ");
            }
            System.out.println();
        }
        saveClusters();
    }

    private static void saveClusters() throws IOException {
        for(int i = 0; i < clusterList.size(); i++){
            String fileName = "cluster"+i+".txt";
            File file = new File(fileName);
            FileWriter fw = new FileWriter(file);
            for(int j = 0; j < clusterList.get(i).size(); j++){
                for(int k = 0; k < 60; k++){
                    fw.write(clusterList.get(i).get(j)[k].toString()+" ");
                }
                fw.write("\n");
            }
            fw.close();
        }
    }

    public static ArrayList<ArrayList<Double[]>> kMeans(){
        //DECIDE ARBITRARY POINTS TO PICK AS CLUSTER REPS
        int[] initInds = new int[6];
        for(int i = 0; i < 6; i++){
            initInds[i] = Math.abs((new Random()).nextInt() % 600);
            Boolean taken = true;
            while(taken){
                taken = false;
                for(int j = 0; j < i; j++){
                    if(initInds[i] == initInds[j]) {
                        taken = true;
                        initInds[i] = Math.abs((new Random()).nextInt() % 600);
                    }
                }
            }
        }
        // Insert those points
        for(int i = 0; i < 6; i++){
            clusterList.add(new ArrayList<Double[]>());
            clusterList.get(i).add(dataList.get(initInds[i]));
        }
        //While the mean changes keep reassigning every point to closest centroid
        double meanChange = 100;
        ArrayList<Double[]> means = new ArrayList<Double[]>();
        for(int i = 0; i < 6; i ++){
            means.add(clusterList.get(i).get(0));
        }
        while(meanChange >= 5){
            for(int i = 0; i < dataList.size(); i++){
                //WORKING HERE WRITE THE LOOP TO ADD THE POINT TO CLOSEST MEAN
                double leastDist = Integer.MAX_VALUE;
                int clusterNum = -1;
                for(int j = 0; j < means.size(); j++){
                    double curDist = calcDistance(dataList.get(i), means.get(j));
                    if(curDist < leastDist){
                        leastDist = curDist;
                        clusterNum = j;
                    }
                }
                clusterList.get(clusterNum).add(dataList.get(i));
            }//End dataList for loop
            //Recalculate Means
            ArrayList<Double[]> newMeans = recalcMeans(means);
            //Compare difference to determine continuance
            meanChange = 0;
            for(int i = 0; i < 6; i++){
                for(int j = 0; j < 60; j++){
                    meanChange += Math.abs(newMeans.get(i)[j] - means.get(i)[j]);
                }
            }
            means = newMeans;
        }//End meanChange While Loop
        return clusterList;
    }

    private static double calcDistance(Double[] point, Double[] clusterMean){
        double distance = 0;
        double sumDiff = 0;
        for(int i = 0; i < 60; i++){
            sumDiff += Math.pow(clusterMean[i]-point[i], 2);
        }
        distance = Math.pow(sumDiff, 1.0/60.0);
        return distance;
    }

    private static ArrayList<Double[]> recalcMeans(ArrayList<Double[]> means){
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 60; j++){
                double sum = 0;
                for(int k = 0; k < clusterList.get(i).size(); k++){
                    sum += clusterList.get(i).get(k)[j];
                }
                means.get(i)[j] = (sum / clusterList.get(i).size());
            }
        }//end cluster loop
        return means;
    }

    public static ArrayList<Double[]> readData() throws FileNotFoundException {
        ArrayList<Double[]> list = new ArrayList<Double[]>();
        Scanner scan = new Scanner(dataFile);
        int listInd = 0;
        while(scan.hasNextLine()){
            list.add(new Double[60]);
            for(int i = 0; i < 60; i++){
                list.get(listInd)[i] = scan.nextDouble();
            }
            listInd++;
        }
        scan.close();
        return list;
    }
}
