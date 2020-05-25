/**
 * Created by liwenji  Email: wenji_li@126.com
 */
package jmetal.CMOEAs;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.Exemple;
import jmetal.util.FileUtils;

import java.util.HashMap;


public class CMOEAs_main {

    public static void main(String[] args) throws Exception {
        int crossoverMethod = 1;  // 0 represents for DE, 1 represents for SBX
        //batchRun(new String[]{"MOEAD_IEpsilon","MOEAD_Epsilon","MOEAD_SR","MOEAD_CDP","C_MOEAD"},crossoverMethod);

        batchRun(new String[]{"MOEAD_IEpsilon"},crossoverMethod);
    }

    private static void batchRun(String[] methods, int crossMethod) throws Exception {
        String[] algorithmSet = methods;
        int algorithmNo = algorithmSet.length;
        for (int i = 0; i < algorithmNo; i++) {
            System.out.println("The tested algorithm: " + algorithmSet[i]);
            System.out.println("The process: " + (100.0 * i / algorithmNo) + "%");
            singleRun(algorithmSet[i],crossMethod); // 0 represents for DE, 1 represents for SBX
        }
    }

    private static void singleRun(String algorithmName, int crossMethod) throws Exception {
        Problem problem;                // The problem to solve
        Algorithm algorithm;            // The algorithm to use
        Operator crossover;            // Crossover operator
        Operator mutation;             // Mutation operator
        Operator selection;            // Selection operator
        HashMap parameters;           // Operator parameters

/////////////////////////////////////////// parameter setting //////////////////////////////////

        int popSize = 40;
        int neighborSize = (int) (0.1 * popSize);
        int maxFES = 300;
        int updateNumber = 2;
        double deDelta = 0.9;
        double DeCrossRate = 1.0;
        double DeFactor = 0.5;

        double tao = 0.1;
        double alpha = 0.9;

        String AlgorithmName = algorithmName;


        String mainPath = System.getProperty("user.dir");
        String weightPath = mainPath + "/weight";
        int runtime = 1;
        Boolean isDisplay = true;
        int plotFlag = 0; // 0 for the working population; 1 for the external archive


        // MOEAD_SR parameters
        double srFactor = 0.05;


        String resultFile = mainPath + "/" + AlgorithmName + ".db";
        FileUtils.deleteFile(resultFile);



//////////////////////////////////////// End parameter setting //////////////////////////////////
        double [] [] le =
                {
                        {25 , 0.7  ,35.5 , 0.2 },
                        {17.8, 0.35,  30 , 0.18 },
                        {26, 0.8, 42, 0.35}
                };
        //Utilités p
        double [] [] u ={
                {0.8245 ,0.8269, 0.2860, 0.6501},
                {0.292425, 0.6352, 0.3022, 0.5322},
                {0.4225, 0.2022, 0.392428, 0.692425}};

        //Fluctuation f
        double [] [] fl = {
                {0.8040, 0.2524, 0.3598, 0.5771},
                {0.1664, 0.5206 , 0.9165, 0.4658},
                {0.0717, 0.8052 ,0.9899  ,0.1428}};
             problem = new Exemple(2,1000,0.001,le, u,  fl,3);
            Object[] algorithmParams = {problem};
            algorithm = (new Utils()).getAlgorithm(AlgorithmName, algorithmParams);

            //define pareto file path
            String paretoPath = mainPath + "/pf_data/Exemple.pf";
            // Algorithm parameters
            algorithm.setInputParameter("AlgorithmName", AlgorithmName);
            algorithm.setInputParameter("populationSize", popSize);
            algorithm.setInputParameter("maxEvaluations", maxFES);
            algorithm.setInputParameter("dataDirectory", weightPath);
            algorithm.setInputParameter("T", neighborSize);
            algorithm.setInputParameter("delta", deDelta);
            algorithm.setInputParameter("nr", updateNumber);
            algorithm.setInputParameter("isDisplay", isDisplay);
            algorithm.setInputParameter("plotFlag", plotFlag);
            algorithm.setInputParameter("paretoPath", paretoPath);
            algorithm.setInputParameter("srFactor", srFactor);
            algorithm.setInputParameter("tao", tao);
            algorithm.setInputParameter("alpha", alpha);

            // Crossover operator
            if (crossMethod == 0)
            {                      // DE operator
                parameters = new HashMap();
                parameters.put("CR", DeCrossRate);
                parameters.put("F", DeFactor);
                crossover = CrossoverFactory.getCrossoverOperator(
                        "DifferentialEvolutionCrossover", parameters);
                algorithm.addOperator("crossover", crossover);
            }
            else if (crossMethod == 1) {                // SBX operator
                parameters = new HashMap();
                parameters.put("probability", 1.0);
                //parameters.put("distributionIndex", 20.0);
                crossover = CrossoverFactory.getCrossoverOperator("BlocCrossover",
                        parameters);
                algorithm.addOperator("crossover", crossover);
            }

            // Mutation operator
            parameters = new HashMap();
            parameters.put("probability", 1.0 / problem.getNumberOfVariables());
            
            mutation = MutationFactory.getMutationOperator("MyMutation", parameters);
            algorithm.addOperator("mutation", mutation);

            // Selection Operator
            parameters = null;
            selection = SelectionFactory.getSelectionOperator("BinaryTournament2", parameters);
            algorithm.addOperator("selection", selection);
            // each problem runs runtime times
            for (int j = 0; j < runtime; j++) {
                algorithm.setInputParameter("runningTime", j);
                // Execute the Algorithm
                System.out.println("The " + j + " run of " );
                long initTime = System.currentTimeMillis();
                SolutionSet pop = algorithm.execute();
                long estimatedTime = System.currentTimeMillis() - initTime;
                // Result messages
                System.out.println("Total execution time: " + estimatedTime + "ms");

            }
        }



}
