package analyzer;

public class PredictLogAnalyzer extends LogAnalyzerDecorator{
    public PredictLogAnalyzer(LogAnalyzer logAnalyzer) {
        super(logAnalyzer);
    }
    @Override
    public String generateReport() {
        return super.generateReport() + "\n" + generatePrediction();

    }
    public String generatePrediction(){
        return "PredictLogAnalyzer make some predictions...\n";
    }

}
