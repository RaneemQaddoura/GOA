//_________________________________________________________________________//
//  Grasshopper Optimization Algorithm (GOA) source codes demo 1.0         //
//                                                                         //
//  Programmer of the original Matlab version: Seyedali Mirjalili          //
//                                                                         //
//  Main paper: S. Saremi, S. Mirjalili, A. Lewis                          //
//              Grasshopper Optimisation Algorithm: Theory and Application //
//               Advances in Engineering Software , in press,              //
//               DOI: http://dx.doi.org/10.1016/j.advengsoft.2017.01.004   //
//                                                                         //
//  Developed in Java 1.8                                                  //
//                                                                         //
//  Java Implementation by: Raneem Qaddoura and Waref AlManaseer           //
//_________________________________________________________________________//

// You can simply define your cost in a seperate file and load its handle to fobj 
// The initial parameters that you need are:
//__________________________________________
// fobj = @YourCostFunction
// dim = number of your variables
// Max_iteration = maximum number of generations
// SearchAgents_no = number of search agents
// lb=[lb1;lb2;...;lbn] where lbn is the lower bound of variable n 
// ub=[ub1;ub2;...;ubn] where ubn is the upper bound of variable n
// If all the variables have equal lower bound you can just
// define lb and ub as two single number numbers




import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 *
 * @author Raneem
 */
public class Example {
    
    private static int searchAgentsNo=100; // Number of search agents
    private static String functionName="F1"; // Name of the test function that can be from F1 to F23 (Table 1,2,3 in the paper)
    private static int maxIteration=100; // Maximum number of iterations
    private static double[] lb;
    private static double[] ub;
    private static int dim = 0;
    private static boolean oddDimention = false;
    //private static String fobj = "";

    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, Exception{
        //Load details of the selected benchmark function
        //String fuunctionName = args[0];
        //String functionName = "f1";
        FunctionsDetails functionDetails = new FunctionsDetails(functionName);
        lb = functionDetails.getLb();
        ub = functionDetails.getUb();
        dim = functionDetails.getDim();
        oddDimention = functionDetails.isOddDimension();
        //fobj=functionDetails.getfobj();
        
        
        //TargetFitness,TargetPosition,Convergence_curve,Trajectories,fitness_history, position_history
        GOA goa = new GOA(searchAgentsNo, maxIteration, lb, ub, dim, functionName, oddDimention);
        
        NumberFormat formatter = new DecimalFormat("#0.0000");     
        System.out.print("The best solution obtained by GOA is : ");
        for(double value:goa.getTargetPosition())
            System.out.print(formatter.format(value) + " ");
        System.out.println("");
        System.out.println("");
        System.out.println("The best optimal value of the objective funciton found by GOA is : " + formatter.format(goa.getTargetFitness()));

    }
}

