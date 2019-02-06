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
// The Grasshopper Optimization Algorithm



import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;

/**
 *
 * @author Raneem
 */
public final class GOA {
    
    double[] convergenceCurve;
    double[][] trajectories;
    double[][] fitnessHistory;
    double[][][] positionHistory;
    double[] targetPosition;
    double targetFitness  = Integer.MAX_VALUE;
    final double EPSILON = 1E-14;

    public double[] getTargetPosition() {
        return targetPosition;
    }

    public double getTargetFitness() {
        return targetFitness;
    }

    public double[] getConvergenceCurve() {
        return convergenceCurve;
    }

    public double[][] getTrajectories() {
        return trajectories;
    }

    public double[][] getFitnessHistory() {
        return fitnessHistory;
    }

    public double[][][] getPositionHistory() {
        return positionHistory;
    }
    
    
    
    //TargetFitness,TargetPosition
            
    public double[][] init(int n, int dim,double[] up,double[] down){
        double[][] values = new double[n][dim];
        for(int i = 0; i < values.length; i++)
            for(int j = 0; j < values[i].length; j++){
                values[i][j] = Math.random()*(up[j]-down[j])+down[j];
            }
        return values;
    }
    
    public double euclideanDistance(double[] a, double b[]) throws Exception{
        if(a.length != b.length){
            System.out.println("A and B should be of same dimensionality");
            throw new Exception("A and B should be of same dimensionality");
        }
        else{
            double distance = 0.0;
            for(int i = 0; i < a.length; i++){
                distance += Math.pow((a[i] - b[i]),2);
            }
            return Math.sqrt(distance);
        }
    }
    
    public double S_func(double r){
        double f=0.5;
        double l=1.5;
        return f*Math.exp(-r/l)-Math.exp(-r);  // Eq. (2.3) in the paper
    }


    
                
    public GOA(int n, int maxIter, double[] lb, double[] ub, int dim, String fuunctionName, boolean oddDim) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, Exception{
        long time = System.nanoTime();
        NumberFormat formatter = new DecimalFormat("#0.0000");     
        System.out.println("GOA is now estimating the global optimum for your problem....");
        
        //Initialize the population of grasshoppers
        double[][] grassHopperPositions = init(n, dim, ub, lb);
        
        double[] grassHopperFitness = new double[n];//initialization of zeros
        fitnessHistory = new double[n][maxIter];//initialization of zeros
        positionHistory = new double[n][maxIter][dim];//initialization of zeros
        convergenceCurve = new double[maxIter];//initialization of zeros
        trajectories = new double[n][maxIter];//initialization of zeros
        double cMax=1;
        double cMin=0.00004;
        FunctionsDetails functionsDetails = new FunctionsDetails(fuunctionName);
        java.lang.reflect.Method method = functionsDetails.getClass().getMethod("f1", double[].class);
        
        //Calculate the fitness of initial grasshoppers
        for(int i = 0; i < grassHopperPositions.length; i++){
            
            if(oddDim){                
                grassHopperFitness[i] = (Double)method.invoke(functionsDetails, 
                        Arrays.copyOf(grassHopperPositions[i], grassHopperPositions[i].length - 1));
            }else{
                grassHopperFitness[i] = (Double)method.invoke(functionsDetails, grassHopperPositions[i]);                
            }
            fitnessHistory[i][0] = grassHopperFitness[i];
            positionHistory[i][0] = grassHopperPositions[i];
            trajectories[i][0]=grassHopperPositions[i][0];        
        }
        
        //Find the best grasshopper (target) in the first population 
        for(int i = 0; i < grassHopperFitness.length; i++){
            if(grassHopperFitness[i] < targetFitness){
                targetFitness = grassHopperFitness[i];
                targetPosition = grassHopperPositions[i];
            }
        }
        //Main loop
        int l = 1;//Start from the second iteration since the first iteration was dedicated to calculating the fitness of antlions
        while(l < maxIter){
            double c = cMax-(l*((cMax-cMin)/maxIter)); // Eq. (2.8) in the paper
            
            ////////////////////////////////////////////////////////////
            for(int i = 0; i < grassHopperPositions.length; i++){
                
                double[] S_i = new double[dim];//initialization of zeros
                for(int j = 0; j < n; j++){//check
                    if(i != j){
                        double distance = euclideanDistance(grassHopperPositions[i], grassHopperPositions[j]); //Calculate the distance between two grasshoppers
                        double[] r_ij_vec = new double[dim];
                        for(int p=0;p < r_ij_vec.length; p++)
                            r_ij_vec[p] = (grassHopperPositions[j][p] - grassHopperPositions[i][p])/(distance+EPSILON);// xj-xi/dij in Eq. (2.7)
                        double xj_xi = 2 + BigDecimal.valueOf(distance).remainder(BigDecimal.valueOf(2)).doubleValue();// |xjd - xid| in Eq. (2.7) 
                        
                        double[] s_ij = new double[dim];
                        for(int p=0;p < r_ij_vec.length; p++)
                            s_ij[p]=((ub[p] - lb[p])*c/2)*S_func(xj_xi)*r_ij_vec[p];// The first part inside the big bracket in Eq. (2.7)
                        
                        for(int p=0;p < S_i.length; p++)
                            S_i[p]=S_i[p]+s_ij[p];                                            
                    }
                }

                double[] S_i_total = S_i;
                double[] X_new = new double[dim];
                for(int p=0;p < S_i.length; p++)
                    X_new[p] = c * S_i_total[p] + targetPosition[p];// Eq. (2.7) in the paper      
                
                grassHopperPositions[i]=X_new;
            }
            ////////////////////////////////////////
            //GrassHopperPositions
    
            for(int i=0; i < grassHopperPositions.length;i++){
                // Relocate grasshoppers that go outside the search space 
                
                for(int j = 0; j < grassHopperPositions[i].length ; j++){
                    if(grassHopperPositions[i][j] > ub[j])grassHopperPositions[i][j]=ub[j];
                    if(grassHopperPositions[i][j] < lb[j])grassHopperPositions[i][j]=lb[j];
                }
                
                //Calculating the objective values for all grasshoppers
                if(oddDim){                
                    grassHopperFitness[i] = (Double)method.invoke(functionsDetails, 
                            Arrays.copyOf(grassHopperPositions[i], grassHopperPositions[i].length - 1));
                }else{
                    grassHopperFitness[i] = (Double)method.invoke(functionsDetails, grassHopperPositions[i]);                
                }
                fitnessHistory[i][0] = grassHopperFitness[i];
                positionHistory[i][0] = grassHopperPositions[i];
                trajectories[i][0]=grassHopperPositions[i][0];   

                //Update the target
                if(grassHopperFitness[i]<targetFitness){
                    targetPosition=grassHopperPositions[i];
                    targetFitness=grassHopperFitness[i];
                }
            }
            
            convergenceCurve[l]=targetFitness;
            System.out.println("In iteration #" + l + " , target\'s objective = " + formatter.format(targetFitness));
    
            l++;
        }
        
   
        if(oddDim)targetPosition = Arrays.copyOf(targetPosition, targetPosition.length - 1);
        System.out.println("time:" + (double)(System.nanoTime() - time) / 1000000000.0 + " seconds");
    } 
}
