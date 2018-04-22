/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import org.pcj.*;
@RegisterStorage(HelloWorld.Shared.class)
public class HelloWorld implements StartPoint {
     @Storage(HelloWorld.class)
     enum Shared{
     array,
     points}
     private int points;
      private int[] array=new int[PCJ.threadCount()];
    
    
    public static void main(String[] args) throws IOException {
           
        PCJ.deploy(HelloWorld.class, new NodesDescription(new String[]{
          "localhost",
          "localhost",
          "localhost:8090",}));
    }

    @Override
    public void main() throws Throwable {
        if(PCJ.myId()==0){
            PCJ.broadcast(100_000_000, Shared.points);
        }
        PCJ.waitFor(Shared.points);
        Random random=new Random();
        int count=0;
        int rand =random.nextInt(PCJ.threadCount());
        for(int i=0;i<points;i++){
        double x=random.nextDouble();
        double y=random.nextDouble();
        if(x*x+y*y<1)
        {count++;}
            
        
        }
        PCJ.put(count,0,Shared.array,PCJ.myId());
        for(int i=0;i<PCJ.threadCount();i++){
            if(PCJ.myId()==i){
                System.out.println("Hello World from PCJ Thread " + PCJ.myId()
                                   + " out of " + PCJ.threadCount()+" with value : "+count );
            }
            PCJ.barrier();
            
        }
        if(PCJ.myId()==0){
            PCJ.waitFor(Shared.array,PCJ.threadCount());
        long sum=0;
        for(int i=0;i<PCJ.threadCount();i++){
        sum=sum+array[i];
        
        }
     
        System.out.println("Sum: "+sum);
        System.out.println("PI=:"+4.0*sum/PCJ.threadCount()/points);
        }
    }
}

 