//  PolynomialMutation.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package jmetal.operators.mutation;

import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.ArrayIntSolutionType;
import jmetal.encodings.solutionType.ArrayRealSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.wrapper.XReal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * This class implements a polynomial mutation operator. 
 */
public class MyMutation extends Mutation {
	private static final double ETA_M_DEFAULT_ = 20.0;
	private final double eta_m_=ETA_M_DEFAULT_;
	
  private Double mutationProbability_ = null ;
  private Double distributionIndex_ = eta_m_;

  /**
   * Valid solution types to apply this operator 
   */
  private static final List VALID_TYPES = Arrays.asList(
          ArrayIntSolutionType.class, ArrayRealSolutionType.class);
	/**
	 * Constructor
	 * Creates a new instance of the polynomial mutation operator
	 */
	public MyMutation(HashMap<String, Object> parameters) {
		super(parameters) ;
  	if (parameters.get("probability") != null)
  		mutationProbability_ = (Double) parameters.get("probability") ;  		
    		
	} // PolynomialMutation

	/**
	 * Perform the mutation operation
	 * @param probability Mutation probability
	 * @param solution The solution to mutate
	 * @throws JMException 
	 */
	public void doMutation(double probability, Solution solution) throws JMException {   
		    Random random = new Random();
	        int index1,index2=0;
		    Variable v,v1;
		    //System.out.println("before mutation");
		   // showChromoTest(solution);
			if (PseudoRandom.randDouble() <= probability)
			{
				 index1=random.nextInt(solution.getDecisionVariables().length);
				 index2=random.nextInt(solution.getDecisionVariables().length);
		          v=solution.getDecisionVariables()[index1];
		          v1=solution.getDecisionVariables()[index2];   
		          
		          solution.getDecisionVariables()[index1]=v1;
		          solution.getDecisionVariables()[index2]=v;
		          
		          //System.out.println("after mutation");
				 // showChromoTest(solution);
			}
	    

	} // doMutation

	/**
	 * Executes the operation
	 * @param object An object containing a solution
	 * @return An object containing the mutated solution
	 * @throws JMException 
	 */  
	public Object execute(Object object) throws JMException {
		Solution solution = (Solution)object;

		if (!VALID_TYPES.contains(solution.getType().getClass())) {
			Configuration.logger_.severe("MyMutation.execute: the solution " +
					"type " + solution.getType() + " is not allowed with this operator");

			Class cls = java.lang.String.class;
			String name = cls.getName(); 
			throw new JMException("Exception in " + name + ".execute()") ;
		} // if 

		doMutation(mutationProbability_, solution);
		return solution;      
	} // execute
	
	public void showChromoTest(Solution sol)
    {
        for(int i=0;i<sol.getDecisionVariables().length;i++)
        {
            System.out.println(sol.getDecisionVariables()[i].toString());
        }
    }

} 
