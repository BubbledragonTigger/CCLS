package contentionAware;

import contentionFree.Allocation;
import setting.Edge;
import setting.VM;

/**
 * Edge Allocation Information
 * @author wu
 */
public class EAllocation extends Allocation{
	private Edge edge;
	private VM sourceVM, destVM;

//	����Ҫ�洢source & destination task allocations����ΪֻҪ��task��vm�����ҵ���Ӧ��TaskAllocation
	
	public EAllocation(Edge edge, VM sourceVM, VM destVM, double startTime,double speed){
		this.edge = edge;
		this.sourceVM = sourceVM;
		this.destVM = destVM;
		this.startTime = startTime;
		this.finishTime = startTime + edge.getDataSize() / speed ;
	}
	
	public EAllocation(Edge edge, VM sourceVM, VM destVM, double startTime,double speed, double finishTime){
		this(edge, sourceVM, destVM, startTime,speed);
		this.finishTime = finishTime ;
	}

	public EAllocation(Edge edge, VM sourceVM, VM destVM, double startTime){
		this.edge = edge;
		this.sourceVM = sourceVM;
		this.destVM = destVM;
		this.startTime = startTime;
		this.finishTime = startTime + edge.getDataSize() / VM.NETWORK_SPEED ;
	}
	
	//used by Adaptor��waitingEdges����ҪsetStartTime
	public EAllocation(Edge edge, VM sourceVM, VM destVM){
		this.edge = edge;
		this.sourceVM = sourceVM;
		this.destVM = destVM;
	}
	//used by Adaptor
	public void setStartTime(double startTime) {
		this.startTime = startTime;
		//this.finishTime = startTime + edge.getDataSize() / VM.NETWORK_SPEED ;
		this.finishTime = startTime + edge.getDataSize();
	}
	
	//----------------------------getters-----------------------------------
	public Edge getEdge() {		return edge;	}
	public VM getSourceVM() {	return sourceVM;		}
	public VM getDestVM() {	return destVM;		}
	
	//----------------------------override-----------------------------------
    public boolean equals(Object obj) {		
    	return this == obj;
    }
	public String toString() {
		return "EAllocation [edge = " + edge.getSource().getName()+ " -> " 
				+ edge.getDestination().getName()
				+ ": " + startTime + ", " + finishTime + "]";
	}
}