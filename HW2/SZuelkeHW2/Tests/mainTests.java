
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class mainTests {

    @Test
    public void Read_TrainingSet_Success(){
        //Arrange

        //Act
        ArrayList<String[]> trainingSet = main.readSet(main.TrainingFile);
        System.out.println("Before Preprocessing");

        for(int i = 0; i < 9; i++){
            System.out.println(trainingSet.get(4346)[i]);
        }

        //Assert
        assertEquals(9, trainingSet.get(0).length);
        assertEquals(4347, trainingSet.size());

    }

    @Test
    public void PreProcess_TrainingSet_Success(){
        //Arrange
        ArrayList<String[]> trainingSet = main.readSet(main.TrainingFile);
        //Act
        trainingSet = main.preProcess(trainingSet);
        System.out.println("After Preprocessing");
        for(int i = 0; i < 8; i++){
            System.out.println(trainingSet.get(4345)[i]);
        }

        //Assert
        assertNotEquals("Localization", trainingSet.get(0)[7]);
        assertEquals(8, trainingSet.get(0).length);
        assertEquals(4346, trainingSet.size());
    }

    @Test
    public void Read_TestSet_Success(){
        //Arrange

        //Act
        ArrayList<String[]> testSet = main.readSet(main.TestFile);
        System.out.println("Test Before Preprocessing");

        for(int i = 0; i < 9; i++){
            System.out.println(testSet.get(1929)[i]);
        }

        //Assert
        assertEquals(9, testSet.get(0).length);
        assertEquals(1930, testSet.size());
    }

    @Test
    public void PreProcess_TestSet_Success(){
        //Arrange
        ArrayList<String[]> testSet = main.readSet(main.TestFile);
        //Act
        testSet = main.preProcess(testSet);
        System.out.println("Test After Preprocessing");

        for(int i = 0; i < 8; i++){
            System.out.println(testSet.get(1928)[i]);
        }

        //Assert
        assertEquals(8, testSet.get(0).length);
        assertEquals(1929, testSet.size());
    }

    @Test
    public void Read_Keys_Success(){
        //Arrange

        //Act
        ArrayList<String[]> keys = main.readKeys();
        System.out.println("Keys");
        System.out.println(keys.get(381)[0] + " " + keys.get(381)[1]);
        //Assert
        assertEquals(382, keys.size());
    }


}
