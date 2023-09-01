package contentionAware;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import setting.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Evaluation {
    public static void main(String[] args) throws IOException {
        //test("D:\\workflowSamples\\MONTAGE\\MONTAGE.n.100.0.dax",false);  //真实工作流
        //calMakespanBasedOnBeta();
        /*for(int i = 4;i<8;i++){
            ProjectCofig.beta = 0.05+i*1.0/20.0;
            //48,49
            int start = 0;
            int end = 50;
            calCCLSImprPerc(calCCLSMakespan(start,end),start,end);
        }*/
        //calMakespanBasedOnbext;
        /*for(int i = 1;i<=5;i++){
            ProjectCofig.vmInterNetworkSpeed = 10 * 1000*1000*i;
            Channel.transferSpeed = ProjectCofig.vmInterNetworkSpeed;
            int start = 0;
            int end = 50;
            //calMakespan();
            calCCLSImprPerc(calCCLSMakespan(start,end),start,end);
        }*/
        //calMakespan();

        //basedOnVMNumber
        /*for(int i = 2;i<=10;i+=2){
            ProjectCofig.privateVMMaxNum =i;
            int start = 0;
            int end = 50;
            //calMakespan();
            calCCLSImprPerc(calCCLSMakespan(start,end),start,end);
        }*/

        //BasedOnVMSpeed从1.5开始
        /*int i = 0;
        ProjectCofig.publicVMComputeSpeedID = 1;
        while (i <= 7) {
            int start = 0;
            int end = 50;
            VM_Public.FASTEST = ProjectCofig.publicVMComputeSpeedID;
            calCCLSImprPerc(calCCLSMakespan(start, end), start, end);
            ProjectCofig.publicVMComputeSpeedID = ProjectCofig.publicVMComputeSpeedID + 1;
            i++;
        }*/

        //runTime

        calMakespan();

    }

    //Improvement Percentage
    private static void calCCLSImprPerc(Double cclsMakespan, int start, int end) throws IOException {
        System.out.println("CCLS-WO  PSLS  PEFT  IPPTS  HEFD");
        TProperties.Type[] types = TProperties.Type.values();
        //non-duplication
        ProjectCofig.adaptorType = 0;
        for (TProperties.Type type : types) {
            if (type == TProperties.Type.GAMMA ||
                    type == TProperties.Type.S_LEVEL || type == TProperties.Type.T_LEVEL || type == TProperties.Type.PU_RANK)
                continue;
            List<Double> makespan = new ArrayList<>();
            ProjectCofig.type = type;
            //1000genome
            for (int i = start; i < end; i++) {
                resettingStaticParameter();
                //1000genome
                ProjectCofig.seed = i;
                makespan.add(test(ProjectCofig.path, false));
            }
            System.out.print(String.format("%.2f", (1 - cclsMakespan / mean(makespan)) * 100) + " ");
            //System.out.print(String.format("%.2f", mean(makespan)) + " ");
        }
        //duplication
        ProjectCofig.type = TProperties.Type.B_LEVEL;
        ProjectCofig.adaptorType = 2;
        List<Double> makespan = new ArrayList<>();
        for (int i = start; i < end; i++) {
            resettingStaticParameter();
            ProjectCofig.seed = i;
            makespan.add(test(ProjectCofig.path, false));
        }
        //System.out.print(String.format("%.2f", mean(makespan)) + " ");
        System.out.println(String.format("%.2f", (1 - cclsMakespan / mean(makespan)) * 100) + " ");
    }

    private static Double calCCLSMakespan(int start, int end) throws IOException {
        List<Double> makespan = new ArrayList<>();
        ProjectCofig.type = TProperties.Type.C_LEVEL;
        ProjectCofig.adaptorType = 2;
        for (int i = start; i < end; i++) {
            ProjectCofig.seed = i;
            resettingStaticParameter();
            makespan.add(test(ProjectCofig.path, false));
        }
        return mean(makespan);
    }

    private static void resettingStaticParameter() {
        VM_Public.idInterval = 0;
        VM_Private.idInterval = 0;
    }

    private static void calMakespan() throws IOException {
        TProperties.Type[] types = TProperties.Type.values();
        System.out.println();
        for (TProperties.Type type : types) {
            if (type == TProperties.Type.GAMMA ||
                    type == TProperties.Type.S_LEVEL || type == TProperties.Type.T_LEVEL || type == TProperties.Type.PU_RANK)
                continue;
            ProjectCofig.type = type;
            if (ProjectCofig.adaptorType == 0) {
                System.out.print(ProjectCofig.type + "/E's  ");
                System.out.print(ProjectCofig.type + "/S's  ");
            }

        }
        System.out.println();
        for (TProperties.Type type : types) {
            if (type == TProperties.Type.GAMMA ||
                    type == TProperties.Type.S_LEVEL || type == TProperties.Type.T_LEVEL || type == TProperties.Type.PU_RANK)
                continue;
            ProjectCofig.type = type;
            List<Double> makespan = new ArrayList<>();
            if (ProjectCofig.adaptorType == 2 && (ProjectCofig.type == TProperties.Type.PEFT || ProjectCofig.type == TProperties.Type.IPPTS))
                continue;
            //1000genome
            for (int i = 0; i < 1; i++) {
                resettingStaticParameter();
                ProjectCofig.seed = i;
                makespan.add(test(ProjectCofig.path, false));
            }
            if (ProjectCofig.adaptorType == 0) {
                System.out.print(String.format("%.2f", mean(makespan)) + " ");
                System.out.print(String.format("%.2f", mean(ProjectCofig.avaerageMakespanBasedOnShared)) + " ");
            }
            //System.out.print(String.format("%.2f",(1-cclsMakespan/mean(makespan))*100)+"% ");
            //System.out.print(String.format("%.2f",mean(makespan))+" ");
            //System.out.println("std:"+standardDeviaction(makespan));
            //System.out.print(String.format("%.2f",(1-cclsMakespan/mean(ProjectCofig.avaerageMakespanBasedOnShared))*100)+"% ");
            //System.out.print(String.format("%.2f",mean(ProjectCofig.avaerageMakespanBasedOnShared))+" ");
            if (ProjectCofig.adaptorType == 2) {
                System.out.println(ProjectCofig.type + String.format("%.2f", mean(makespan)) + " ");
            }
        }
    }

    private static Double test(String file, boolean visualizeFlag)
            throws IOException {
        Workflow wf = new Workflow(file);
        List<CSolution> list = new ArrayList<CSolution>();

        CCSH ccsh = new CCSH();
        long t1 = System.currentTimeMillis();


        list.add(ccsh.listSchedule(wf, ProjectCofig.type, ProjectCofig.adaptorType));
        long t2 = System.currentTimeMillis();
        System.out.println("runTime: " +  (t2-t1));
        String result = "";

        String runtime = "";
//        for(CSolution c : list){
//            result += c.getMakespan()+"\t"+c.getCost() + "\t";
//            System.out.println(c.getMakespan()+"\t"+c.getCost() + "\t"+ c.validate(wf));
//            if(visualizeFlag)
//                ChartUtil.visualizeScheduleNew(c);
//        }
        //used for data collection
        //result += wf.getSequentialLength() +"\t" + wf.getCPTaskLength()+"\t" + wf.getCCR();
        /* System.out.println(result);*/

        String[] rtnValues = {result, runtime};
        list.clear();

        return ccsh.getMakespan();
    }


    public static double standardDeviaction(List<Double> list) {
        double sum = 0;
        double meanValue = mean(list);                //平均数
        for (int i = 0; i < list.size(); i++) {
            sum += Math.pow(list.get(i) - meanValue, 2);
        }
        return Math.sqrt(sum / list.size());
    }

    //计算和
    public static double calcSum(List<Double> list) {
        double sum = 0;
        for (int i = 0; i < list.size(); i++) {
            sum += list.get(i);
        }
        return sum;
    }

    //求平均值
    public static double mean(List<Double> list) {
        return calcSum(list) / list.size();
    }


}
