package bg.coderunner.utils;

public class LanguageDetector {

    public static String detectLanguage(String code) {
        if (code.contains("System.out.println") || code.contains("public class")) {
            return "java";
        } else if (code.contains("def ") || code.contains("import ")) {
            return "python";
        } else if (code.contains("console.log") || code.contains("function")) {
            return "javascript";
        } else if (code.contains("#include")) {
            return "c++";
        } else if (code.contains("Console.WriteLine") || code.contains("namespace") || code.contains("using")) {
            return "csharp";
        }
        return "python";
    }
}
