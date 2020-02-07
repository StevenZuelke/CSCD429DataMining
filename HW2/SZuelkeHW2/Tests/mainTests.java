
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class mainTests {

    @Test
    public void Read_TrainingSet_Success(){
        //Arrange

        //Act
        ArrayList<String[]> trainingSet = main.readTrainingSet();

        //Assert
        assertEquals(7, trainingSet.get(0).length);
        assertEquals(4346, trainingSet.size());

    }
}
