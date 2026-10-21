package _7_Data_Types._1_Numeric.Fixed_Point;

//Fixed-Point Types (Exact Value) - DECIMAL, NUMERIC
// https://dev.mysql.com/doc/refman/8.0/en/fixed-point-types.html

//Fixed and Floating-Point
//https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8406440#questions

/*                                     Fixed-Point (Exact Value)


    ○ The precision:
                   * represents the number of significant digits that are stored for values
     ○ The scale:
                   * represents the number of digits that can be stored following the decimal point.



            * DECIMAL                                 DECIMAL(5,2) ( precision 5, scale 2)       ---(321.56)

            * NUMERIC

 */

/*
TOPIC:
    DECIMAL(5,2) ține scara exactă: 321.56 rămâne cu două zecimale, nu o aproximare de FLOAT.
FAMILIE:
    Types
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu pierzi banii la stocare. Valoarea rămâne pe scară.
MECHANISM:
    Serverul stochează DECIMAL ca exact. Precizia este numărul de cifre, scara este după punct. FLOAT nu promite aceeași valoare.
LOOKALIKES:
    FLOAT(5,3) din curs este aproximativ. DECIMAL(5,2) este exact.
BOOK THEORY:
    PDF-ul Fixed_and_Floating_Point.pdf a fost deschis și numește precizia. FLOAT(5,3) din comentariul vecin este marcat istoric acolo. Aici propoziția este DECIMAL.
BOOK EXAMPLE:
    decimalKeepsTheScale
REPO DEMO:
    src/main/java/_7_Data_Types/_1_Numeric/Fixed_Point/Fixed_Point.java
BREAK LAB:
    floatDoesNotKeepTheCent ține CREATE TABLE price_list (amount FLOAT). FLOAT este aproximativ. DECIMAL ține scara. Precizia 5 și scara 2 nu sunt un FLOAT.
TRADE-OFFS:
    FLOAT când aproximația este acceptată și nu ții bani.
ANTI-PATTERNS:
    FLOAT pentru amount. Centul poate să nu rămână.
STAFF QUESTIONS:
    decimalKeepsTheScale și floatDoesNotKeepTheCent. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Prețul unui lot este DECIMAL(10,2). O măsurătoare de senzor poate fi FLOAT.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă CREATE TABLE price_list (amount DECIMAL(5,2))?
   Capcană: FLOAT ține 0.10 exact, ca DECIMAL.
   Cheie: decimalKeepsTheScale: amount ține până la 999.99 exact, nu o aproximare binară. fără DECIMAL, FLOAT poate să nu păstreze 0.10 exact
   Demonstrație: decimalKeepsTheScale
2. Enunț: Ce rămâne în tabelă sau în sesiune după CREATE TABLE price_list (amount DECIMAL(5,2))?
   Capcană: DECIMAL(5,2) ține 1000.00.
   Cheie: Efectul este: amount ține până la 999.99 exact, nu o aproximare binară. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: decimalKeepsTheScale
3. Enunț: De ce CREATE TABLE price_list (amount DECIMAL(5,2)) nu este vecinul său?
   Capcană: Precizia este numărul de zecimale.
   Cheie: FLOAT(5,3) din curs este aproximativ. DECIMAL(5,2) este exact.
   Demonstrație: decimalKeepsTheScale
4. Enunț: Cum eșuează CREATE TABLE price_list (amount FLOAT)?
   Capcană: DECIMAL este aproximativ, dar afișat frumos.
   Cheie: floatDoesNotKeepTheCent: FLOAT este aproximativ. DECIMAL ține scara. Precizia 5 și scara 2 nu sunt un FLOAT.
   Demonstrație: floatDoesNotKeepTheCent
5. Enunț: Când nu mai trimiți CREATE TABLE price_list (amount DECIMAL(5,2))?
   Capcană: Tipul se alege în ecran, nu în CREATE TABLE.
   Cheie: FLOAT când aproximația este acceptată și nu ții bani.
   Demonstrație: decimalKeepsTheScale

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE banii trebuie să rămână pe două zecimale -> decimalKeepsTheScale -> CREATE TABLE price_list (amount DECIMAL(5,2)) -> nu rotunjește singur dincolo de scară fără regula de mod și nu ține un număr mai lat decât precizia
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> floatDoesNotKeepTheCent -> CREATE TABLE price_list (amount FLOAT) -> FLOAT este aproximativ. DECIMAL ține scara. Precizia 5 și scara 2 nu sunt un FLOAT.
    3. Granița
           -> DECIMAL este tipul. O formatare de două zecimale în ecran nu este stocarea.
    4. Alternativa
           -> FLOAT(5,3) din curs este aproximativ. DECIMAL(5,2) este exact.
    5. Anti-patternul
           -> FLOAT pentru amount. Centul poate să nu rămână.
    6. Dovada
           -> decimalKeepsTheScale ține propoziția. floatDoesNotKeepTheCent ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> DECIMAL este exact, cu precizie și scară. FLOAT este aproximativ. DECIMAL(5,2) nu ține 1000.00. Ecranul nu este tipul.
*/

public class Fixed_Point {
    /*
    🟢 Începător
        Ce este            amount ține până la 999.99 exact, nu o aproximare binară
        De ce e nevoie     fără DECIMAL, FLOAT poate să nu păstreze 0.10 exact
    🟡 Producție
        Cum se scrie       CREATE TABLE price_list (amount DECIMAL(5,2))
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este amount FLOAT, care este aproximativ
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu amount FLOAT, care este aproximativ.
    */
    // Începător: decimalKeepsTheScale păstrează propoziția pentru amount ține până la 999.99 exact, nu o aproximare binară
    // Producție: public static String decimalKeepsTheScale(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. DECIMAL(5,2) nu ține 1000.00: nu are destule cifre.
    public static String decimalKeepsTheScale() {
        // Începător: amount ține până la 999.99 exact, nu o aproximare binară
        // Producție: CREATE TABLE price_list (amount DECIMAL(5,2)). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: DECIMAL(5,2) nu ține 1000.00: nu are destule cifre. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE price_list (amount DECIMAL(5,2))";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide decimalKeepsTheScale. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            valoarea este aproximativă și 0.10 poate să nu rămână 0.10
        De ce e nevoie     fără textul greșit, FLOAT pare scara exactă
    🟡 Producție
        Cum se scrie       CREATE TABLE price_list (amount FLOAT)
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care valoarea este aproximativă și 0.10 poate să nu rămână 0.10
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu amount FLOAT, care este aproximativ.
    */
    // Începător: floatDoesNotKeepTheCent păstrează textul greșit pentru valoarea este aproximativă și 0.10 poate să nu rămână 0.10
    // Producție: public static String floatDoesNotKeepTheCent(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. FLOAT este aproximativ. DECIMAL ține scara. Precizia 5 și scara 2 nu sunt un FLOAT.
    public static String floatDoesNotKeepTheCent() {
        // Începător: valoarea este aproximativă și 0.10 poate să nu rămână 0.10
        // Producție: CREATE TABLE price_list (amount FLOAT). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: FLOAT este aproximativ. DECIMAL ține scara. Precizia 5 și scara 2 nu sunt un FLOAT. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE price_list (amount FLOAT)";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide floatDoesNotKeepTheCent. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
