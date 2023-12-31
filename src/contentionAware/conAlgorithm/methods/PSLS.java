package contentionAware.conAlgorithm.methods;

import contentionAware.Evaluation;
import setting.ProjectCofig;
import setting.TProperties;
import setting.VM_Private;
import setting.VM_Public;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bubble dragon Tigger
 * @date 2023/12/31 17:42
 * @jdk 1.8
 */
public class PSLS {
    public static void main(String[] args) throws IOException {
        //choose workflow
        //ProjectCofig.path = "D:\\example_11.dot";
        //ProjectCofig.path = "D:\\\\workflowSamples\\\\MONTAGE\\\\MONTAGE.n.50.0.dax";
        //testExample();

        ProjectCofig.path = "D:\\\\workflowSamples\\\\MONTAGE\\\\MONTAGE.n.50.0.dax";
        testRealWorkflow();
    }

    private static void testExample(){
        Evaluation e = new Evaluation();
        parameterSettingInbeta1();
        try {
            System.out.println(e.test(ProjectCofig.path,false));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    //exvaluate in example
    private static void parameterSettingInbeta1(){
        ProjectCofig.vmIntraNetworkSpeed = 50;
        ProjectCofig.vmInterNetworkSpeed = 1;
        ProjectCofig.privateVMMaxNum = 2;
        ProjectCofig.betaType = 1;
        ProjectCofig.isShared = false;
        ProjectCofig.adaptorType = 2;
        ProjectCofig.type = TProperties.Type.B_LEVEL;
    }
    //exvaluate in real workflow
    private static void parameterSettingInbeta0(){
        ProjectCofig.vmIntraNetworkSpeed = 1 * 1000 * 1000*1000 ;
        ProjectCofig.vmInterNetworkSpeed = 10 * 1000*1000;
        ProjectCofig.privateVMMaxNum = 4;
        ProjectCofig.betaType = 0;
        ProjectCofig.isShared = true;
        ProjectCofig.adaptorType = 0;
        ProjectCofig.type = TProperties.Type.B_LEVEL;
    }

    private static void testRealWorkflow() throws IOException {
        parameterSettingInbeta0();
        System.out.println();
        Evaluation e = new Evaluation();
        System.out.print("PSLS:");
        List<Double> makespan = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            resettingStaticParameter();
            ProjectCofig.seed = i;
            makespan.add(e.test(ProjectCofig.path, false));
        }
        if (ProjectCofig.adaptorType == 0) {
            System.out.print(String.format("%.2f", e.mean(makespan)) + " ");
            //System.out.print(String.format("%.2f", e.mean(ProjectCofig.avaerageMakespanBasedOnShared)) + " ");
        }

        if (ProjectCofig.adaptorType == 2) {
            System.out.println(ProjectCofig.type + String.format("%.2f",e. mean(makespan)) + " ");
        }
    }

    private static void resettingStaticParameter() {
        VM_Public.idInterval = 0;
        VM_Private.idInterval = 0;
    }
}
