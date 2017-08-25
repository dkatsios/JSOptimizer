//IntRealProblem variation

package jmetal.problems;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.IntRealSolutionType;
import jmetal.util.JMException;

import java.io.IOException;
import java.util.ArrayList;

import uiOutputHandlers.InputsCreator;
import uiOutputHandlers.JsRunner;


@SuppressWarnings("serial")
public class JaamSimProblem extends Problem {

  int intVariables_;
  int realVariables_;
  double[] jsOutput_;
  ArrayList<double[]> constraints_;

  ArrayList<Double[]> variables;
  int numberOfIntVariables;
  int numberOfObjectives;
  int numberOfConstraints;
  int numberOfIntegerConstraints;
  ArrayList<double[]> constraints;
  int[] outputIndices;
  long evaluationIndex = 1;


  /**
   * Constructor.
   * Creates a new instance of the JaamSimProblem problem.
   * @param intVariables Number of integer variables of the problem
   * @param realVariables Number of real variables of the problem
   */

  public JaamSimProblem() {
	  this("IntReal");
  }

  public JaamSimProblem(String solutionType) {

	variables = InputsCreator.getVariables();
    intVariables_  = InputsCreator.getNumberOfIntVariables();
    realVariables_ = variables.size() - intVariables_;
    constraints_ = InputsCreator.getConstraints();
    outputIndices = InputsCreator.getOutputindices();

    numberOfVariables_   = variables.size();
    numberOfObjectives_  = InputsCreator.getNumberOfObjectives();
    numberOfConstraints_ = InputsCreator.getNumberOfConstraints();
    problemName_         = "JaamSimProblem";

    upperLimit_ = new double[numberOfVariables_] ;
    lowerLimit_ = new double[numberOfVariables_] ;

    for (int i = 0; i < numberOfVariables_; i++) {
    	lowerLimit_[i] = variables.get(i)[0];
    	upperLimit_[i] =  variables.get(i)[1];

    }
    if (solutionType.compareTo("IntReal") == 0)
    	solutionType_ = new IntRealSolutionType(this, intVariables_, realVariables_) ;
    else {
    	System.out.println("Error: solution type " + solutionType + " invalid") ;
    	System.exit(-1) ;
    }

  }

  /**
  * Evaluates a solution
  * @param solution The solution to evaluate
   * @throws JMException
  */
  @Override
public void evaluate(Solution solution) throws JMException {

    Variable[] variable = solution.getDecisionVariables();
    double[] doubleVar = new double[variable.length];
    for (int i = 0; i < variable.length; i++) {
    	doubleVar[i] = variable[i].getValue() ;
    }

    double[] tempOutputs = new double[numberOfObjectives_ + numberOfConstraints_];
    jsOutput_ = new double[numberOfObjectives_ + numberOfConstraints_];
	try {
		tempOutputs = JsRunner.returnResults(doubleVar, evaluationIndex++);
	} catch (IOException | InterruptedException e) {
		e.printStackTrace();
	}

	int tempIndex = 0;
	for(int index: outputIndices) {
		jsOutput_[index] = tempOutputs[tempIndex];
		tempIndex++;
	}


    for (int i = 0; i < numberOfObjectives_; i++) {
    	solution.setObjective(i, jsOutput_[i]);
    }
  } // evaluate

  @Override
public void evaluateConstraints(Solution solution) throws JMException {
	    double [] constraint = new double[this.getNumberOfConstraints()];
	    for (int i = numberOfObjectives_; i < (numberOfObjectives_ + numberOfConstraints_); i++) {
	    	constraint[i - numberOfObjectives_] = jsOutput_[i];
	    }
	    double total = 0.0;
	    int number = 0;
	    for (int i = 0; i < numberOfConstraints_; i++) {
	      if (constraint[i] < constraints_.get(i)[0]) {
	        total-= Math.abs(constraints_.get(i)[0] - constraint[i]);
	        number++;
	      }
	      else if (constraints_.get(i)[1] < constraint[i]) {
	          total-= Math.abs(constraints_.get(i)[1] - constraint[i]);
	          number++;
	      }
	    }
	    solution.setOverallConstraintViolation(total);
	    solution.setNumberOfViolatedConstraint(number);
	  } // evaluateConstraints

} // JaamSimProblem
