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

// This function containts full information and implementations of the benchmark 
// functions in Table 1, Table 2, and Table 3 in the paper

// lb is the lower bound: lb=[lb_1,lb_2,...,lb_d]
// up is the uppper bound: ub=[ub_1,ub_2,...,ub_d]
// dim is the number of variables (dimension of the problem)



import java.util.List;

/**
 *
 * @author Raneem
 */
public class FunctionsDetails {
    private double[] lb;
    private double[] ub;
    private int dim = 0;
    private String functionName = "f1";
    private boolean oddDimension = false;

    public boolean isOddDimension() {
        return oddDimension;
    }

    public void setOddDimension(boolean oddDimension) {
        this.oddDimension = oddDimension;
    }
    
    public double[] getLb() {
        return lb;
    }

    public double[] getUb() {
        return ub;
    }

    public int getDim() {
        return dim;
    }

    public void setLb(double[] lb) {
        this.lb = lb;
    }

    public void setUb(double[] ub) {
        this.ub = ub;
    }

    public void setDim(int dim) {
        this.dim = dim;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    } 
       
    public FunctionsDetails(String functionName){
        int lbValue;
        int ubValue;
        switch(functionName){
            case "F1": 
                this.functionName = functionName;
                this.dim=30;
                lbValue = -100;
                ubValue = 100;
                correctDimension();
                lb = new double[dim];
                ub = new double[dim];
                repeatBound(lbValue, ubValue, dim);
                break;
            case "F2": 
                this.functionName = functionName;
                this.dim=5;
                lbValue = -10;
                ubValue = 10;
                correctDimension();
                lb = new double[dim];
                ub = new double[dim];
                repeatBound(lbValue, ubValue, dim);
                break;
        }
    }
    
    public void correctDimension(){
        if(dim % 2 != 0){            
            dim += 1;
            oddDimension = true;
        }
    }
    
    public void repeatBound(double lbValue, double ubValue, int dim){
        for(int i = 0; i < lb.length ; i++){
            lb[i] = lbValue;
            ub[i] = ubValue;
        }
    }
    
    public double f1(double[] array){
        double sum = 0;
        for(int i = 0 ; i < array.length; i++){
            double element = array[i];
            sum += element * element;
        }
        return sum;
    }
    
    
    public double f2(double[] array){
        
        double sum = 0;
        double prod = 0;
        for(int i = 0 ; i < array.length; i++){
            double element = Math.abs(array[i]);
            sum += element;
            prod *= element;
        }
        return sum + prod;
    }
    
}
