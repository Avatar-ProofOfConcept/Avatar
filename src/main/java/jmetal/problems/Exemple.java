package jmetal.problems;


import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.ArrayIntSolutionType;
import jmetal.encodings.variable.ArrayInt;
import jmetal.util.JMException;

public class Exemple extends Problem {

    double [] [] le_;
    //Utilités p
    double [] [] u_ ;

    //Fluctuation f
    double [] [] fl_ ;

    double cntTime_;

    double ctTro_;

    int nbCluster_;


    public Exemple(String solutionType) throws ClassNotFoundException {
        this(solutionType, 2, 2);
    }

    /**
     * Creates an Exemple problem instance
     *
     * @param numberOfVariables
     *            Number of variables
     * @param numberOfObjectives
     *            Number of objective functions
     * @param solutionType
     *            The solution type must "Real" or "BinaryReal".
     */
    public Exemple(String solutionType, Integer numberOfVariables,
                   Integer numberOfObjectives) throws ClassNotFoundException {
        numberOfVariables_ = numberOfVariables;
        numberOfObjectives_ = numberOfObjectives;
        numberOfConstraints_ = 4;
        problemName_ = "Exemple";

        lowerLimit_ = new double[numberOfVariables_];
        upperLimit_ = new double[numberOfVariables_];
        for (int var = 0; var < numberOfVariables; var++) {
            lowerLimit_[var] = 0;
            upperLimit_[var] = 2;//nb niveau -1
        } // for
        solutionType_ = new ArrayIntSolutionType(this);

    }

    public Exemple(Integer nbCluster,double cntTime, double ctTro, double [][]le,double [][] u, double [][] fl,int d) throws ClassNotFoundException {
        numberOfVariables_ = nbCluster;
        numberOfObjectives_ = 2;
        numberOfConstraints_ =2;//to review
        problemName_ = "Exemple";
        le_=le;
        u_=u;
        fl_=fl;
        cntTime_=cntTime;
        ctTro_=ctTro;
        nbCluster_=nbCluster;

        lowerLimit_ = new double[numberOfVariables_];
        upperLimit_ = new double[numberOfVariables_];
        for (int var = 0; var < nbCluster; var++) {
            lowerLimit_[var] = 0;
            upperLimit_[var] = d-1;//nb niveau -1
        } // for
        solutionType_ = new ArrayIntSolutionType(this);

    }
    /**
     * Evaluates a solution
     *
     * @param solution
     *            The solution to evaluate
     * @throws JMException
     */

    public void evaluate(Solution solution) throws JMException {

        double sum1, sum2;

        sum1 = sum2 = 0.0;
        double[] f = new double[numberOfObjectives_];
        //System.out.println(solution.getDecisionVariables().length);


        for(int i=0;i<2;i++)
        {
            for(int j=0;j<nbCluster_;j++)
            {
               // int cpt=(i+(j)*4);
                //System.out.println("length array :"+((ArrayInt)(solution.getDecisionVariables()[i])).array_.length+" lenght var : "+solution.getDecisionVariables().length);
                sum1 = sum1 + (-1)*u_[((ArrayInt)(solution.getDecisionVariables()[i])).array_[j]][i+(j)*2] ;
                sum2 = sum2 +  fl_[((ArrayInt)(solution.getDecisionVariables()[i])).array_[j]][i+(j)*2]  ;
            }
        }

        f[0] = sum1 ;
        f[1] = sum2;

        for (int i = 0; i < numberOfObjectives_; i++)
            solution.setObjective(i, f[i]);
    } // evaluate

    /**
     * Evaluates the constraint overhead of a solution
     *
     * @param solution
     *            The solution
     * @throws JMException
     */
    public void evaluateConstraints(Solution solution) throws JMException {

        double[] constraint = new double[4];
        double g_1, g_2;

        g_1 = g_2=0.0 ;
        int nx = numberOfVariables_;
        double[] x = new double[numberOfVariables_];
        //Time

        for(int j=0;j<nbCluster_;j++)
        {
            g_1=g_1+ le_[((ArrayInt)(solution.getDecisionVariables()[0])).array_[j]][0+(j)*2];
        }
        //dispo
        for(int j=0;j<nbCluster_;j++)
        {
            g_2=g_2+ le_[((ArrayInt)(solution.getDecisionVariables()[1])).array_[j]][1+(j)*2];
        }




        constraint[0] =cntTime_-g_1;
        constraint[1] =g_2-ctTro_;


        double total = 0.0;
        int number = 0;
        for(int i = 0; i < 2; i++){
            if (constraint[i] < 0.0) {
                total += constraint[i];
                number++;
                solution.setConstraint(i,constraint[i]);
            }
        }

        solution.setOverallConstraintViolation(total);
        solution.setNumberOfViolatedConstraint(number);
    } // evaluateConstraints

}
