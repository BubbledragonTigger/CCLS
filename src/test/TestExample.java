package test;

import contentionAware.Evaluation;
import setting.ProjectCofig;

import java.io.IOException;

/**
 * @author Bubble dragon Tigger
 * @date 2023/12/10 13:55
 * @jdk 1.8
 */
public class TestExample {
    public static void main(String[] args) throws IOException {
        testIllustrativeExamples();
    }
    private static  void testIllustrativeExamples() throws IOException {
        Evaluation e = new Evaluation();
        System.out.println(e.test(ProjectCofig.path,false));
    }
}
