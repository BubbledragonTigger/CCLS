package contentionFree;

import java.util.List;

import static java.lang.Math.max;

/**
 * It has two concrete sub-classes: TAllocation, EAllocation. 
 * They are used for allocating tasks and edges, respectively
 * ������ ��   �������౻����Ƴɣ����е���Ϣ�п��ܱ��޸�
 * @author wu
 */
public abstract class Allocation implements Comparable<Allocation>{
	protected double startTime;   //��ǰ�������Ŀ�ʼʱ��
	protected double finishTime;  //��ǰ�����������ʱ��
	
	public double getStartTime() {
		return startTime;
	}   //��õ�ǰ��������ʼ�����ʱ��
	public double getFinishTime() {
		return finishTime;
	}  //��õ�ǰ��������ɴ����ʱ��
	//used by ProLiS and ICPCP classes
	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}   //���õ�ǰ��������ʼ�����ʱ��
	//used by Solution when VM is upgraded, by Adaptor
	public void setFinishTime(double finishTime) {
		this.finishTime = finishTime;
	}  //���õ�ǰ���������������ʱ��
	
	public int compareTo(Allocation o) {            // �����ǰ����ߵ�����׼���õ�ʱ����ڲ��������׼���õ�ʱ�䣬�򷵻�1
		if(this.getStartTime() > o.getStartTime())
			return 1;
		else if(this.getStartTime() < o.getStartTime())
			return -1;
		return 0;
	}
	
	/**
	 * Search the earliest time slot in @param allocList from @param readyTime,
	 * which is no less than @param period
	 * ��allocList�ϴ�readytime��ʼ��Ѱ�������֧��period���ȵ�free time slot
	 */

	public static double searchFreeTimeSlot(List<? extends Allocation> allocList,  //Ѱ����������������϶��ע����һ��������
			double readyTime, double period ) {
		/*
		��i,j)i�кö�
			readytime:������ڵ�i���ʱ�䣬����һ���ǿ�ʼ�ߴ����ʱ�䣬���統ǰ������һ�ߴ���һ���ߵ����ݣ�һ�ߴ���i�ڵ㡣����
			i�ڵ㴦����֮�������һ���߻�û�д������
			period���ߴ���ʱ��
		 */
		if(allocList == null || allocList.size() ==0)
			return readyTime;

		if(readyTime + period <= allocList.get(0).getStartTime()){// case1: ��������ǰ�棬#��ʱ���е�����߻�û�п�ʼ����ͨ��
			return readyTime;
		}
		double EST = 0;
		for(int j = allocList.size() - 1; j >= 0 ; j--){
			Allocation alloc = allocList.get(j);//����alloc�ĺ���
			double startTime = max(readyTime, alloc.getFinishTime());   //
			double finishTime = startTime + period;
			
			if(j == allocList.size() - 1){			// case2: ��������棬EST�϶����Դ���ߣ�������Ѱ�Ҽ�϶
				EST = startTime;             //ESTҲ�Ǵ�����������Դ���ߵ�ʱ��
			}else {								//case3: ���뵽�м䡣alloc�������һ����������allocNext����test�ܷ����
				Allocation allocNext = allocList.get(j+1);
				if(finishTime > allocNext.getStartTime())//���ǽ�����ʱ������˺�����һ���߿�ʼ�����ʱ�䣬����overlap�޷�����
					continue;

				EST = startTime;//����Ϳ���,���ǽ�����ʱ��С���˺�����һ���߿�ʼ�����ʱ��
			}
			if(readyTime>alloc.getFinishTime())	//�ս�ѭ������Ϊ��readTime֮��Ķ��Ѿ��������ˣ�ǰ���alloc�Ŀ����϶��С��readytime��
												//�϶�����
				break;
		}
		return EST;
	}
}
