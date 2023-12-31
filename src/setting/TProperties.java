package setting;

import com.sun.javafx.scene.web.Debugger;
import contentionAware.Channel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import static java.lang.Math.random;

// Task properties: heuristic information of tasks, e.g., bLvel, tLevel, gamma, Probabilistic Upward Rank
public class TProperties extends HashMap<Task,Double> implements Comparator<Task>{
    //TProperties Type


    //public static enum Type{B_LEVEL, T_LEVEL, S_LEVEL, PU_RANK, GAMMA, PEFT, IPPTS, C_LEVEL}
    public static enum Type{ C_LEVEL,B_LEVEL, T_LEVEL, S_LEVEL, PU_RANK, GAMMA, PEFT, IPPTS}
    Workflow wf;

    public TProperties(Workflow wf, Type type){
        super();
        this.wf = wf;
        //需要后期改
        /*
        double speed= VM.SPEEDS[VM.FASTEST];  // VM.SPEEDS[4];
         */
        double speed=VM.SPEEDS[VM.FASTEST];;
        if(type == Type.B_LEVEL){
            if(ProjectCofig.betaType == 1){
                for (int j = wf.size() - 1; j >= 0; j--) {
                    double bLevel = 0;
                    Task task = wf.get(j);
                    for (Edge outEdge : task.getOutEdges()) {
                        Double childBLevel = this.get(outEdge.getDestination());
                        bLevel = Math.max(bLevel,childBLevel + (outEdge.getDataSize() / VM.NETWORK_SPEED+
                            outEdge.getDataSize() /Channel.getTransferSpeed())/2);
                        //bLevel = Math.max(bLevel, childBLevel + outEdge.getDataSize() / VM.NETWORK_SPEED);
                    }
                    bLevel = bLevel + (task.getTaskSize() / VM_Public.SPEEDS[VM_Public.FASTEST]);/*+
                        task.getTaskSize()/VM_Private.SPEEDS[VM_Private.SLOWEST])/2;*/
                /*if(task.getprivateAttribute()==true){
                    bLevel = bLevel + task.getTaskSize()/VM_Private.SPEEDS[VM_Private.SLOWEST];
                }
                else{
                    bLevel = bLevel + (task.getTaskSize()/VM_Private.SPEEDS[VM_Private.SLOWEST]
                            +task.getTaskSize()/VM_Public.SPEEDS[VM_Public.FASTEST])/2;
                }*/
                    this.put(task, bLevel);
                }
            }
            else if(ProjectCofig.betaType == 0 && ProjectCofig.adaptorType==2){
                for (int j = wf.size() - 1; j >= 0; j--) {
                    double bLevel = 0;
                    Task task = wf.get(j);
                    for (Edge outEdge : task.getOutEdges()) {
                        Double childBLevel = this.get(outEdge.getDestination());
                        bLevel = Math.max(bLevel,childBLevel + (outEdge.getDataSize() / VM.NETWORK_SPEED+
                              outEdge.getDataSize() /Channel.getTransferSpeed())/2);
                        bLevel = Math.max(bLevel, childBLevel + outEdge.getDataSize() / VM.NETWORK_SPEED);
                    }
                    bLevel = bLevel + task.getTaskSize() / VM_Public.SPEEDS[VM_Public.FASTEST];

                    this.put(task, bLevel);
                }
            }
            else{
                for (int j = wf.size() - 1; j >= 0; j--) {
                    double bLevel = 0;
                    Task task = wf.get(j);
                    for (Edge outEdge : task.getOutEdges()) {
                        Double childBLevel = this.get(outEdge.getDestination());
                    /*bLevel = Math.max(bLevel,childBLevel + (outEdge.getDataSize() / VM.NETWORK_SPEED+
                            outEdge.getDataSize() /Channel.getTransferSpeed())/2);*/
                        bLevel = Math.max(bLevel, childBLevel + outEdge.getDataSize() / VM.NETWORK_SPEED);
                    }
                    bLevel = bLevel + (task.getTaskSize() / VM_Public.SPEEDS[VM_Public.FASTEST]);/*+
                        task.getTaskSize()/VM_Private.SPEEDS[VM_Private.SLOWEST])/2;*/
                /*if(task.getprivateAttribute()==true){
                    bLevel = bLevel + task.getTaskSize()/VM_Private.SPEEDS[VM_Private.SLOWEST];
                }
                else{
                    bLevel = bLevel + (task.getTaskSize()/VM_Private.SPEEDS[VM_Private.SLOWEST]
                            +task.getTaskSize()/VM_Public.SPEEDS[VM_Public.FASTEST])/2;
                }*/
                    this.put(task, bLevel);
                }
            }
        }else if(type == Type.S_LEVEL){     //应用于混合云时应考虑任务的隐私属性
            for(int j= wf.size()-1; j>=0; j--){
                double sLevel = 0;
                Task task = wf.get(j);
                for(Edge outEdge : task.getOutEdges()){
                    Double childSLevel = this.get(outEdge.getDestination());
                    sLevel = Math.max(sLevel, childSLevel);
                }
                if(task.getprivateAttribute() == true){
                    sLevel += task.getTaskSize()/VM_Private.SPEEDS[VM_Private.SLOWEST];
                }
                else{
                    sLevel += task.getTaskSize()/VM_Public.SPEEDS[VM_Public.FASTEST];
                }
                this.put(task, sLevel);
            }
        }else if(type == Type.T_LEVEL) {    //T_LEVEL目前还没有使用的
            for (Task task : wf.getTaskList()) {
                double arrivalTime = 0;
                for (Edge inEdge : task.getInEdges()) {
                    Task parent = inEdge.getSource();
                    Double parentTLevel = this.get(parent);
                    arrivalTime = Math.max(arrivalTime, parentTLevel +
                            parent.getTaskSize() / speed + inEdge.getDataSize() / VM.NETWORK_SPEED);
                }
                this.put(task, arrivalTime);
            }
        }else if(type == Type.GAMMA){
            for(int j= wf.size()-1; j>=0; j--) {
                Task task = wf.get(j);
                double gamma = 0;
                for (Edge outEdge : task.getOutEdges()) {
                    Double childGamma = this.get(outEdge.getDestination());
                    gamma = Math.max(gamma, childGamma);
                }
                gamma += task.getTaskSize() / speed;
                for (Edge inEdge : task.getInEdges()) {
                    //后期需要改
                    gamma += inEdge.getDataSize() / VM.NETWORK_SPEED;
                }
                this.put(task, gamma);
            }
        }else if(type == Type.PEFT){
            //这里和原算法PEFT不太一样，因为我们的处理器数量是无法提前确定的
            // ，所以我们在进行优先级排序计算OCT时,假设一个公有云处理器，一个私有云处理器
                for(int j = wf.size() -1;j>=0;j--){
                    Task task = wf.get(j);

                    //exitTask
                    if(j == wf.size() -1){
                        double privateVMOCT = 0;
                        double publicVMOCT = 0;
                        task.setOCT(privateVMOCT, publicVMOCT);
                        double rankOCT = (privateVMOCT+publicVMOCT)/2;
                        this.put(task,rankOCT);
                        continue;

                    }

                    double privateVMOCT = 0;
                    double publicVMOCT = 0;
                    double maxSuccTaskRankOct=0;

                    for(Edge outEdge: task.getOutEdges()){
                        Task succTask = outEdge.getDestination();
                        double succTaskPrivateVMOCT = succTask.getOCT()[0];
                        double succTaskPublicVMOCT = succTask.getOCT()[1];
                        double succTaskPrivateWeight = succTask.getTaskSize()/VM_Private.SPEEDS[0];
                        double succTaskPublicWeight = succTask.getTaskSize()/VM_Public.SPEEDS[8];

                        //跨云传输时长
                        double transferCloud = outEdge.getDataSize()/ Channel.getTransferSpeed();

                        //云内传输时长
                        double unTransferCloud = outEdge.getDataSize() / VM.NETWORK_SPEED;

                        //taski的VM在公有云上,taskj的VM在公有云上OCT
                        //double public_public =  succTaskPublicVMOCT + succTaskPublicWeight + unTransferCloud;
                        double public_public =  succTaskPublicVMOCT + succTaskPublicWeight + unTransferCloud;

                        //taski的VM在公有云上，taskj的VM在私有云上OCT
                        double public_private = succTaskPrivateVMOCT + succTaskPrivateWeight + transferCloud;
                        //计算taski在公有云上处理器的OCT，先取较小值，再取较大值
                        if(public_public<=public_private){
                            if(public_public>publicVMOCT){
                                publicVMOCT = public_public;
                            }
                        }
                        else{
                            if(public_private>publicVMOCT){
                                publicVMOCT = public_private;
                            }
                        }

                        //taski的VM在私有云上，taskj的VM在公有云上OCT
                        double private_public = succTaskPublicVMOCT + succTaskPublicWeight + transferCloud;
                        //taski的VM在私有云上，taskj的VM在私有云上OCT
                        double private_private = succTaskPrivateVMOCT + succTaskPrivateWeight + unTransferCloud;
                        //计算taski再私有云上处理器的OCT,先取较小值，再取较大值
                        if(private_private<private_public){
                            if(private_private>privateVMOCT){
                                privateVMOCT = private_private;
                            }
                        }
                        else{
                            if(private_public > privateVMOCT){
                                privateVMOCT = private_public;
                            }
                        }
                        if((succTask.getOCT()[0]+succTask.getOCT()[1])/2>maxSuccTaskRankOct){
                            maxSuccTaskRankOct = (succTask.getOCT()[0]+succTask.getOCT()[1])/2;
                        }
                    }
                    task.setOCT(privateVMOCT, publicVMOCT);
                    double rankOCT = (privateVMOCT + publicVMOCT) /2+maxSuccTaskRankOct;
                    double maxChildOCTrank = 0;
                    for (Edge outEdge : task.getOutEdges()) {
                        Task child = outEdge.getDestination();
                        maxChildOCTrank = Math.max(this.get(child), maxChildOCTrank);
                    }
                    this.put(task,rankOCT+maxChildOCTrank);
                }
        }else if(type == Type.IPPTS){
            //这里和原算法IPPTS不太一样，因为我们的处理器数量是无法提前确定的
            // ，所以我们在进行优先级排序计算OCT时,假设一个公有云处理器，一个私有云处理器
            for(int j = wf.size() -1;j>=0;j--) {
                Task task = wf.get(j);
                //exitTask
                if(j == wf.size() -1){
                    double privateVMPCM = 0;
                    double publicVMPCM = 0;
                    task.setPCM(privateVMPCM, publicVMPCM);
                    double rankPCM = (privateVMPCM+publicVMPCM)/2;
                    double Prank = rankPCM*task.getOutEdges().size();
                    this.put(task,Prank);
                    task.setPrank(Prank);
                    continue;
                }

                double privateVMPCM = 0;
                double publicVMPCM = 0;
                double maxSuccTaskPrank=0;

                for(Edge outEdge: task.getOutEdges()){
                    Task succTask = outEdge.getDestination();
                    double succTaskPrivateVMPCM = succTask.getPCM()[0];
                    double succTaskPublicVMPCM = succTask.getPCM()[1];
                    double succTaskPrivateWeight = succTask.getTaskSize()/VM_Private.SPEEDS[ProjectCofig.privateVMComputeSpeedID];
                    double succTaskPublicWeight = succTask.getTaskSize()/VM_Public.SPEEDS[ProjectCofig.publicVMComputeSpeedID];
                    double taskPrivateWeight = task.getTaskSize()/VM_Private.SPEEDS[ProjectCofig.privateVMComputeSpeedID];
                    double taskPublicWeight = task.getTaskSize()/VM_Public.SPEEDS[ProjectCofig.publicVMComputeSpeedID];

                    //跨云传输时长
                    double transferCloud = outEdge.getDataSize()/ Channel.getTransferSpeed();

                    //云内传输时长
                    double unTransferCloud = outEdge.getDataSize() / VM.NETWORK_SPEED;

                    //taski的VM在公有云上,taskj的VM在公有云上OCT
                    //double public_public =  succTaskPublicVMOCT + succTaskPublicWeight + unTransferCloud;
                    double public_public =  succTaskPublicVMPCM + taskPublicWeight + succTaskPublicWeight + unTransferCloud;

                    //taski的VM在公有云上，taskj的VM在私有云上OCT
                    double public_private = succTaskPrivateVMPCM + taskPublicWeight + succTaskPrivateWeight + transferCloud;
                    //计算taski在公有云上处理器的OCT，先取较小值，再取较大值
                    if(public_public<=public_private){
                        if(public_public>publicVMPCM){
                            publicVMPCM = public_public;
                        }
                    }
                    else{
                        if(public_private>publicVMPCM){
                            publicVMPCM = public_private;
                        }
                    }

                    //taski的VM在私有云上，taskj的VM在公有云上OCT
                    double private_public = succTaskPublicVMPCM +taskPrivateWeight+ succTaskPublicWeight + transferCloud;
                    //taski的VM在私有云上，taskj的VM在私有云上OCT
                    double private_private = succTaskPrivateVMPCM +taskPrivateWeight+ succTaskPrivateWeight + unTransferCloud;
                    //计算taski再私有云上处理器的OCT,先取较小值，再取较大值
                    if(private_private<private_public){
                        if(private_private>privateVMPCM){
                            privateVMPCM = private_private;
                        }
                    }
                    else{
                        if(private_public > privateVMPCM){
                            privateVMPCM = private_public;
                        }
                    }
                    if(succTask.getPrank()>maxSuccTaskPrank){
                       maxSuccTaskPrank = succTask.getPrank();
                    }
                }
                task.setPCM(privateVMPCM, publicVMPCM);

                double rankPCM = (privateVMPCM + publicVMPCM) /2;  //此时加入maxSuccTaskRankPCM是防止有时候不满足拓扑排序
                double Prank =rankPCM *task.getOutEdges().size()+maxSuccTaskPrank;
                task.setPrank(Prank);
                this.put(task,Prank);

            }
        }else if(type == Type.C_LEVEL){

            //Firstly,get maxOutd of the graph
            int maxOutd = Workflow.maxOutd;


            for(int j = wf.size()-1;j>=0;j--){
                Task task = wf.get(j);
                double clevel = 0;
                ;
                for(Edge outEdge: task.getOutEdges()){
                    Double childClevel = this.get(outEdge.getDestination());

                    clevel = Math.max(clevel,childClevel);
                    //为了防止因为精度产生父子结点clevel相等的情况，我们在这里给clevel加一个微小数值，使之必须满足拓扑排序
                    clevel+=0.000000001;
                }
                if(task.getprivateAttribute() == true){

                    clevel +=task.getTaskSize()/VM_Private.SPEEDS[VM_Private.SLOWEST];
                    double outdData = 0;  //结果乘outd
                    double maxInnerCloudTime = 0.0;
                    double sumInterCloudTIme = 0.0;
                    for(Edge outEdge: task.getOutEdges()) {
                        Task succTask = outEdge.getDestination();
                        if(succTask.getprivateAttribute()== true) {
                            maxInnerCloudTime = Math.max(maxInnerCloudTime, outEdge.getDataSize() / VM.NETWORK_SPEED);

                        }
                        else {
                            sumInterCloudTIme += outEdge.getDataSize() / Channel.getTransferSpeed();
                        }
                    }
                    outdData = Math.max(maxInnerCloudTime,sumInterCloudTIme);
                    if(task.getOutEdges() != null || task.getOutEdges().size() !=0 ){
                        outdData = outdData*(1+task.getOutEdges().size()*1.0/maxOutd);
                    }
                    clevel +=outdData;
                }
                else{
                    clevel +=task.getTaskSize()/VM_Public.SPEEDS[VM_Public.FASTEST];
                    double outdData = 0;  //结果乘outd
                    double maxInnerCloudTime = 0.0;
                    double sumInterCloudTIme = 0.0;
                    for(Edge outEdge: task.getOutEdges()) {
                        Task succTask = outEdge.getDestination();
                        if(succTask.getRunOnPrivateOrPublic()== false) {
                            maxInnerCloudTime = Math.max(maxInnerCloudTime, outEdge.getDataSize() / VM.NETWORK_SPEED);
                        }
                        else {
                            sumInterCloudTIme += outEdge.getDataSize() / Channel.getTransferSpeed();
                        }
                    }
                    outdData = Math.max(maxInnerCloudTime,sumInterCloudTIme);
                    if(task.getOutEdges() != null && task.getOutEdges().size() !=0 ){
                        outdData = outdData*(1+task.getOutEdges().size()*1.0/maxOutd);
                    }
                    clevel +=outdData;
                }
                this.put(task,clevel);


            }
        }
    }

    @Override
    public int compare(Task o1, Task o2) {
        // to keep entry node ranking last, and exit node first
        if(o1.getName().equals("entry") || o2.getName().equals("exit"))
            return 1;
        if(o1.getName().equals("exit") || o2.getName().equals("entry"))
            return -1;
        double value1 = this.get(o1);
        double value2 = this.get(o2);
        if(value1 > value2)
            return 1;
        else if(value1 < value2)
            return -1;
        else{	//避免直接相等导致在TreeSet等数据结构中就产生了覆盖；此处并不会影响排序结果的(已测试过)
            //其实并不是覆盖，而是当前这个就会消失
            return wf.indexOf(o1) - wf.indexOf(o2);
        }
    }
}
