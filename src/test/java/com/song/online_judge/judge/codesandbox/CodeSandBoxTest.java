package com.song.online_judge.judge.codesandbox;

import com.song.online_judge.MainApplication;
import com.song.online_judge.judge.codesandbox.model.ExecuteCodeRequest;
import com.song.online_judge.judge.codesandbox.model.ExecuteCodeResponse;
import com.song.online_judge.model.enums.QuestionSubmitLanguageEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = {MainApplication.class})
class CodeSandBoxTest {


    @Value("${codesandbox.type:EXAMPLE}")
    private CodeSandBoxTypeEnum type;
    @Test
    void executeTest() {
        CodeSandBox codeSandBox = CodeSandBoxFactory.createCodeSandBox(type);

        String code = "int main() {}";
        String language = QuestionSubmitLanguageEnum.JAVA.getValue();
        List<String> inputList = Arrays.asList("1 2", "3 4");

        ExecuteCodeRequest request = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();

        ExecuteCodeResponse response = codeSandBox.executeCode(request);
        Assertions.assertNull(response);
    }

    @Test
    void executeProxyTest() {
        CodeSandBox codeSandBox = CodeSandBoxFactory.createProxyCodeSandBox(type);

        String code = "public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        int x = Integer.parseInt(args[0]);\n" +
                "        int y = Integer.parseInt(args[1]);\n" +
                "        System.out.println(\"x+y的结果是: \" + (x + y));\n" +
                "    }\n" +
                "}";
        String language = QuestionSubmitLanguageEnum.JAVA.getValue();
        List<String> inputList = Arrays.asList("1 2", "3 4");

        ExecuteCodeRequest request = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();

        ExecuteCodeResponse response = codeSandBox.executeCode(request);
        System.out.println(response);
    }
}