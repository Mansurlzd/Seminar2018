/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication4.algo;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import org.pcj.*;

/**
 *
 * @author Mansur
 */
public class JavaApplication4 extends Storage implements StartPoint {
  
    public final static int SIZE = 3 * 4;
    @Shared
    private int[] PCJ_numbers;
    private int[] temp_numbers;
    private int[] random_numbers;
   
   
    
    
 private void sort() {
     
        int rank=PCJ.myId();
        
        int odd_rank,even_rank;
        if(rank%2==0){
        odd_rank=rank-1;
        even_rank=rank+1;
        }
        else{
        odd_rank=rank+1;
        even_rank=rank-1;
        }
        
        Arrays.sort(random_numbers);
        
        
            if (odd_rank < PCJ.threadCount() && odd_rank >= 0) {
                PCJ.put(odd_rank, "PCJ_numbers", random_numbers);
                PCJ.waitFor("PCJ_numbers");}
            
            if (even_rank < PCJ.threadCount() && even_rank >= 0) {
                PCJ.put(even_rank, "PCJ_numbers", random_numbers);
                PCJ.waitFor("PCJ_numbers");}   
            
                if (odd_rank < PCJ.myId() || even_rank<PCJ.myId()) {
                    smallerId();
                }
                else {
                    greaterId();
                }
                int[] s = temp_numbers;
                temp_numbers = random_numbers;
                random_numbers = s;
                   
            PCJ.barrier();
        
    }    
        private void greaterId() {
        
        for(int i=0;i<temp_numbers.length;i++){
            
         if(random_numbers[i]<=PCJ_numbers[i]){
            temp_numbers[i]=random_numbers[i++];
            }else {
            temp_numbers[i]=PCJ_numbers[i++];
            }
        
        
        }
    }
    
    private void smallerId() {
        
        int len=temp_numbers.length - 1;
        for(int i=0;i>=len;i++){
            
             if(random_numbers[len]>PCJ_numbers[len]){
            temp_numbers[len]=random_numbers[len--];
            }else{ 
                temp_numbers[len]=PCJ_numbers[len--];
                
             }
        }
     
    }
    
    
    public static void randomize(int[] array) {
        Random r = new Random();
        
        for (int i = 0; i < array.length; ++i) {
            array[i] = r.nextInt();
        }
    }
    
    @Override
    public void main() throws Throwable {      
        
        long currentTime = 0,max = 0,min=0;
        random_numbers = new int[SIZE/PCJ.threadCount()];
        temp_numbers =     new int[SIZE/PCJ.threadCount()];
      
       
        for (int i = 0; i < 10; ++i) {
            randomize(random_numbers);
            PCJ.barrier();
            PCJ.monitor("PCJ_numbers");
            
            if (PCJ.myId() == 0)
                currentTime = System.currentTimeMillis();
            sort();
            if (PCJ.myId() == 0) {
                 

                currentTime = System.currentTimeMillis() - currentTime;
                
                if(max<currentTime){
                max=currentTime;
                }
                if (min > currentTime || min == 0) {
                    min = currentTime;
                }
            }
        }
        if (PCJ.myId() == 0) {
            System.out.println("Max time is: "+" "+max+" Min time is :"+" "+min);
          
        }
        
        
        
     /*   for(int i =0;i <PCJ_numbers.length;i++){
        System.out.println("ReceivedData:"+PCJ_numbers[i]);
        System.out.println("Numbers:"+random_numbers[i]);
        }
        
     */ 
    }

}
