package _7_Data_Types._1_Numeric.Floating_Point;

//Problems with Floating-Point Values
// https://dev.mysql.com/doc/refman/8.0/en/problems-with-float.html

//Floating-Point Types (Approximate Value) - FLOAT, DOUBLE
// https://dev.mysql.com/doc/refman/8.0/en/floating-point-types.html

//Fixed and Floating-Point
//https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8406440#questions

/*                                       Floating-Point


    ○ FLOAT și DOUBLE păstrează o aproximare. Patru octeți pentru precizie simplă, opt pentru dublă. FLOAT(p) cu p de la 0 la 23 alege 4 octeți; p de la 24 la 53 alege 8 octeți. FLOAT(M,D) și DOUBLE(M,D) sunt deprecate.
                  * HISTORICAL: FLOAT(5,3) -- 10.523, single precision -- 23 digits.
                  * HISTORICAL: DOUBLE(9,3), double precision -- 53 digits.
                  * Regula citită în tur, manualul 9.7, 13.1.4: 23 și 53 nu sunt cifre zecimale. Sunt limitele lui FLOAT(p) pentru mărimea stocării.


 */

public class Floating_Point {
}
