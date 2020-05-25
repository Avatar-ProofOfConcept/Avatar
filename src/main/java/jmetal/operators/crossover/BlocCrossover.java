package jmetal.operators.crossover;

import jmetal.core.Solution;
import jmetal.encodings.solutionType.ArrayIntSolutionType;
import jmetal.encodings.solutionType.ArrayRealSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.encodings.variable.ArrayInt;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.wrapper.XReal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class BlocCrossover extends Crossover {
    /**
     * EPS defines the minimum difference allowed between real values
     */
    private static final double EPS = 1.0e-14;

     private Double crossoverProbability_ = 0.9;


    /**
     * Valid solution types to apply this operator
     */
    private static final List VALID_TYPES = Arrays.asList(
            ArrayIntSolutionType.class, ArrayRealSolutionType.class);

    /**
     * Constructor Create a new SBX crossover operator whit a default index
     * given by <code>DEFAULT_INDEX_CROSSOVER</code>
     */
    public BlocCrossover(HashMap<String, Object> parameters) {
        super(parameters);

        if (parameters.get("probability") != null)
            crossoverProbability_ = (Double) parameters.get("probability");

    } // BlocCrossover

    /**
     * Perform the crossover operation.
     *
     * @param probability
     *            Crossover probability
     * @param parent1
     *            The first parent
     * @param parent2
     *            The second parent
     * @return An array containing the two offsprings
     */
    public Solution[] doCrossover(double probability, Solution parent1,
                                  Solution parent2) throws JMException {

        Solution[] offSpring = new Solution[2];

        offSpring[0] = new Solution(parent1);
        offSpring[1] = new Solution(parent2);

        Random random = new Random();
        int index1=0;



        if (PseudoRandom.randDouble() <= probability)
        {
            index1=random.nextInt(offSpring[0].getDecisionVariables().length);
            offSpring[0].getDecisionVariables()[index1]=parent2.getDecisionVariables()[index1];
            offSpring[1].getDecisionVariables()[index1]=parent1.getDecisionVariables()[index1];
           /* System.out.println("parent1");
            showChromoTest(parent1);
            System.out.println("parent2");
            showChromoTest(parent2);
            System.out.println("enfant1");
            showChromoTest(offSpring[0]);
            System.out.println("enfant2");
            showChromoTest(offSpring[1]);*/
        }   // if


        return offSpring;
    } // doCrossover

    /**
     * Executes the operation
     *
     * @param object
     *            An object containing an array of two parents
     * @return An object containing the offSprings
     */
    public Object execute(Object object) throws JMException {
        Solution[] parents = (Solution[]) object;

        if (parents.length != 2) {
            Configuration.logger_
                    .severe("BlocCrossover.execute: operator needs two "
                            + "parents");
            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        } // if

        if (!(VALID_TYPES.contains(parents[0].getType().getClass()) && VALID_TYPES
                .contains(parents[1].getType().getClass()))) {
            Configuration.logger_.severe("BlocCrossover.execute: the solutions "
                    + "type " + parents[0].getType()
                    + " is not allowed with this operator");

            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        } // if

        Solution[] offSpring;
        offSpring = doCrossover(crossoverProbability_, parents[0], parents[1]);

        // for (int i = 0; i < offSpring.length; i++)
        // {
        // offSpring[i].setCrowdingDistance(0.0);
        // offSpring[i].setRank(0);
        // }
        return offSpring;
    } // execute

    public void showChromoTest(Solution sol)
    {
        for(int i=0;i<sol.getDecisionVariables().length;i++)
        {
            System.out.println(sol.getDecisionVariables()[i].toString());
        }
    }
}
