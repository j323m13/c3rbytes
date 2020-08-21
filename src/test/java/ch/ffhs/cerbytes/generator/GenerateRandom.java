/**
 * A Java program to demonstrate secure random number generation using java.security.SecureRandom 

 */
package ch.ffhs.cerbytes.generator;
import java.security.SecureRandom; 

/**
 * @author jeremieequey
 *
 */
public class GenerateRandom {
	
	public static void main(String args[]) 
    { 
        // create instance of SecureRandom class 
        SecureRandom rand = new SecureRandom(); 
  
        // Generate random integers in range 0 to 999 
        int rand_int1 = rand.nextInt(1000); 
        int rand_int2 = rand.nextInt(1000); 
  
        // Print random integers 
        System.out.println("Random Integers: " + rand_int1); 
        System.out.println("Random Integers: " + rand_int2); 
    } 

}
