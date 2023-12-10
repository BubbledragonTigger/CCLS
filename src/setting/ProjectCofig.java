package setting;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bubble dragon Tigger
 * @date 2022/9/30 13:28
 * @jdk 1.8
 * 用来控制实验中各种变量
 */
public class ProjectCofig {
    public static int privateVMMaxNum=2;    //私有云中VM的最大数量
    //public static int privateVMMaxNum = 10;  //如果数量无限

    //public static double vmIntraNetworkSpeed =  1 * 1000 * 1000*1000 ;
    public static double vmIntraNetworkSpeed =  50;
    //public static double vmInterNetworkSpeed = 10 * 1000*1000;
    public static double vmInterNetworkSpeed =  1;

    public static int privateVMComputeSpeedID= 0;
    public static int publicVMComputeSpeedID= 2;

    //控制隐私任务的数量所占比例,在Workflow.setPrivacy中用到
    public static int betaType = 1; //0:真实工作流.1:示例工作流 2：合成工作流
    public static double beta = 0.1;
    public static int seed = 3;
    public static int bound;//由读取的工作流大小决定


    //控制调用的列表调度方法
    public static TProperties.Type type = TProperties.Type.C_LEVEL;

    //控制是否复制调度
    // @param type = 0: no ne
     //             type = 1: rescheduling is enabled
     //             type = 2: duplication is supported
     //             besides, insert is always supported
    public static int adaptorType =2;
    public static Boolean isShared =false;//判断是否添加共享方案

    //控制调用的工作流MONTAGE.50.0,100.0   GENOME.50.2. 100.2 SIPHT.50.14, 100.5  CYberShake  50.xml 100.xml
    //public static String path = "D:\\workflowSamples\\MONTAGE\\MONTAGE.n.50.0.dax";

    //srasearch
    //public static String path = "D:\\pegasus-instances-master\\srasearch\\chameleon-cloud\\srasearch-chameleon-20a-001.json";
    //public static String path = "D:\\pegasus-instances-master\\srasearch\\chameleon-cloud\\srasearch-chameleon-50a-001.json";

    //epigenomics
    //public static String path = "D:\\pegasus-instances-master\\epigenomics\\chameleon-cloud\\epigenomics-chameleon-hep-1seq-100k-001.json";
    //public static String path = "D:\\pegasus-instances-master\\epigenomics\\chameleon-cloud\\epigenomics-chameleon-hep-2seq-100k-001.json";

    //1000genome
    //public static String path = "D:\\pegasus-instances-master\\1000genome\\chameleon-cloud\\1000genome-chameleon-2ch-100k-001.json";
    //public static String path = "D:\\pegasus-instances-master\\1000genome\\chameleon-cloud\\1000genome-chameleon-4ch-100k-001.json";

    //public static String path = "D:\\pegasus-instances-master\\seismology\\chameleon-cloud\\seismology-chameleon-200p-001.json";
    //public static String path = "D:\\pegasus-instances-master\\cycles\\chameleon-cloud\\cycles-chameleon-1l-2c-9p-001.json";


    //SoyKB
    //public static String path = "D:\\pegasus-instances-master\\soykb\\chameleon-cloud\\soykb-chameleon-30fastq-10ch-001.json";

    //Sight50-14 100.5
    //public static String path = "D:\\workflowSamples\\SIPHT\\SIPHT.n.100.5.dax";

    //CYberShake  50.xml 100.xml
    //public static String path = "D:\\workflowSamples\\WorkflowXML\\CYBERSHAKE_100.xml";

    //public static String path = "D:\\workflowSamples\\LIGO\\LIGO.n.100.2.dax";
    //public static String path = "D:\\workflowSamples\\GENOME\\GENOME.n.100.0.dax";
    //public static String path = "D:\\workflowSamples\\MONTAGE\\MONTAGE.n.100.0.dax";
    //public static String path = "D:\\workflowSamples\\CYBERSHAKE\\CYBERSHAKE.n.100.2.dax";
    //public static String path = "D:\\workflowSamples\\WorkflowXML\\CYBERSHAKE_100.xml";
    //public static String path = "D:\\workflowSamples\\WorkflowXML\\Sipht_100.xml";
    //public static String path = "D:\\workflowSamples\\SIPHT\\SIPHT.n.50.14.dax";
    //public static String path = "D:\\workflowSamples\\soykb.json";

    public static String path = "D:\\example_11.dot";  //模拟工作流

    public static TProperties.Type[] types = {TProperties.Type.C_LEVEL,
                                            TProperties.Type.B_LEVEL,
                                                TProperties.Type.PEFT,
                                                TProperties.Type.IPPTS};

    public static List<Double> avaerageMakespanBasedOnShared = new ArrayList<>();

}
