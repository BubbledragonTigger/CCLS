package setting;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class WorkflowParser2 {
	//------------------???JSON??? start----------------
	class CFile{
		String link, name, sizeInBytes;
	}
	class Job{
		String name,id, runtimeInSeconds;	//type
		ArrayList<String> parents;
		ArrayList<CFile> files;
	}
	class WF{	//???????Workflow????????????
		ArrayList<Job> tasks;
		//String executedAt,makespan;	ArrayList<String> machines;
	}
	class JSONFile{	//???????????????
		String name;	//?????????????
		WF workflow;	//?????workflow???????wf
		//Author author;	//class Author{ String name, email;}
		//String description, createdAt, schemaVersion;ArrayList<String>  wms;
	}
	public static List<Task> parseJSONFile(String file) throws IOException {
		Gson gson = new Gson();
		Reader reader = new FileReader(file);
		JSONFile json = gson.fromJson(reader, JSONFile.class);
		ArrayList<Job> jobs = json.workflow.tasks;

		List<Task> list = new ArrayList<>();
		HashMap<String, Task> nameTaskMapping = new HashMap<String, Task>();
		HashMap<String, Job> nameJobMapping = new HashMap<String, Job>();
		for(Job job : jobs){	//????飬??????????tasks
			Task task = new Task(job.name, Double.parseDouble(job.runtimeInSeconds));
			list.add(task);
			nameTaskMapping.put(job.name, task);
			nameJobMapping.put(job.name, job);
		}
		for(Job job : jobs){	//????飬????task?????
			Task task = nameTaskMapping.get(job.name);
			List<CFile> inFiles = job.files.stream()
					.filter(e -> e.link.equals("input"))
					.collect(Collectors.toList());
//			outFiles.forEach(e -> System.out.println(e.link)); //test
			for(String parent : job.parents){
				Task parentTask = nameTaskMapping.get(parent);
				Job parentJob = nameJobMapping.get(parent);
				Edge e = new Edge(parentTask, task);

				List<CFile> outFiles = parentJob.files.stream()
						.filter(t -> t.link.equals("output"))
						.collect(Collectors.toList());
				double weight = 0;
				int i = 0;
				for(CFile outFile : outFiles){
					for(CFile inFile : inFiles){
						if(outFile.name.equals(inFile.name)){
							weight += Double.parseDouble(outFile.sizeInBytes);
							i++;
							break;
						}
					}
				}
				e.setDataSize((long)(weight)*8);
				task.insertEdge(Task.TEdges.IN, e);
				parentTask.insertEdge(Task.TEdges.OUT, e);
			}
		}
		WorkflowParser.addDummyTasks(list);
		return list;
	}
	//------------------???JSON??? end----------------

	public static void main(String[] args) throws IOException{
		parseJSONFile("D:\\seismology-workflow.json");
	}
}