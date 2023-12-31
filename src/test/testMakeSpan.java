package test;

import contentionAware.Evaluation;
import setting.ProjectCofig;

import java.io.IOException;

/**
 * @author Bubble dragon Tigger
 * @date 2023/12/10 14:49
 * @jdk 1.8
 */
public class testMakeSpan {
    public static void main(String[] args) {
        //choose workflow
        ProjectCofig.path = "D:\\\\workflowSamples\\\\MONTAGE\\\\MONTAGE.n.50.0.dax";

        testMakespanWithoutTaskDuplication();
        //testMakespanwithTaskDuplication();
    }

    private static void testMakespanWithoutTaskDuplication(){
        Evaluation e = new Evaluation();
        parameterSetting();
        ProjectCofig.adaptorType = 0;
        try {
            e.calMakespan();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void testMakespanwithTaskDuplication(){
        Evaluation e = new Evaluation();
        parameterSetting();
        ProjectCofig.adaptorType = 2;
        try {
            e.calMakespanWithTaskDuplication(0,50);//0->50 seed's value
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private static void parameterSetting(){
        ProjectCofig.vmIntraNetworkSpeed = 1 * 1000 * 1000*1000;
        ProjectCofig.vmInterNetworkSpeed = 10 * 1000*1000;
        ProjectCofig.privateVMMaxNum = 4;
        ProjectCofig.betaType = 0;
        ProjectCofig.isShared = true;
    }
}

