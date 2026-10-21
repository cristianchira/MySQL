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

/*
TOPIC:
    FLOAT stochează o aproximare; 0.10 poate să nu rămână 0.10, iar DECIMAL(5,2) ține scara.
FAMILIE:
    Types
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu pui bani în FLOAT. Valoarea aproximativă este pentru măsurători, nu pentru cenți.
MECHANISM:
    Serverul stochează FLOAT pe 4 octeți și DOUBLE pe 8. FLOAT(p) alege lățimea după p, nu un număr de zecimale. Comentariul vechi FLOAT(5,3) este deja HISTORICAL în fișier.
LOOKALIKES:
    DECIMAL(5,2) este exact. FLOAT nu este.
BOOK THEORY:
    Manualul nu a avut pagina FLOAT adusă separat: NERULAT AICI pentru acel URL. Fișierul are deja regula citită: 23 și 53 nu sunt cifre zecimale. PDF-ul Fixed_and_Floating_Point a fost deschis.
    HISTORICAL: pagina 13.1.4 a fost adusă după aceea. FLOAT ocupă 4 octeți, DOUBLE 8. FLOAT(p) de la 0 la 23 rămâne pe 4 octeți, de la 24 la 53 devine DOUBLE. FLOAT(M,D) este deprecat. Comparația de egalitate pe o valoare aproximativă poate rata rândul.
BOOK EXAMPLE:
    floatIsApproximate
REPO DEMO:
    src/main/java/_7_Data_Types/_1_Numeric/Floating_Point/Floating_Point.java
BREAK LAB:
    equalsMissesTheFloat ține SELECT reading FROM sample WHERE reading = 0.10. FLOAT este aproximativ. Egalul exact nu este testul sigur. DECIMAL ține valoarea pe care o compari.
TRADE-OFFS:
    DECIMAL când valoarea trebuie să rămână, mai ales la bani.
ANTI-PATTERNS:
    WHERE reading = 0.10 ca dovadă că valoarea s-a păstrat. Poate să nu o găsești.
STAFF QUESTIONS:
    floatIsApproximate și equalsMissesTheFloat. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Un senzor de temperatură poate fi FLOAT. Prețul lotului nu. Egalul pe FLOAT nu este proba că 0.10 a rămas.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă CREATE TABLE sample (reading FLOAT)?
   Capcană: FLOAT păstrează 0.10 exact.
   Cheie: floatIsApproximate: reading este aproximativ, pe 4 octeți. fără să știi că este aproximativ, compari reading = 0.10 și poți să nu găsești rândul
   Demonstrație: floatIsApproximate
2. Enunț: Ce rămâne în tabelă sau în sesiune după CREATE TABLE sample (reading FLOAT)?
   Capcană: WHERE reading = 0.10 găsește mereu rândul tocmai stocat.
   Cheie: Efectul este: reading este aproximativ, pe 4 octeți. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: floatIsApproximate
3. Enunț: De ce CREATE TABLE sample (reading FLOAT) nu este vecinul său?
   Capcană: FLOAT și DECIMAL(5,2) sunt același tip.
   Cheie: DECIMAL(5,2) este exact. FLOAT nu este.
   Demonstrație: floatIsApproximate
4. Enunț: Cum eșuează SELECT reading FROM sample WHERE reading = 0.10?
   Capcană: FLOAT(5,3) înseamnă cinci cifre și trei zecimale, ca regulă actuală.
   Cheie: equalsMissesTheFloat: FLOAT este aproximativ. Egalul exact nu este testul sigur. DECIMAL ține valoarea pe care o compari.
   Demonstrație: equalsMissesTheFloat
5. Enunț: Când nu mai trimiți CREATE TABLE sample (reading FLOAT)?
   Capcană: DOUBLE este exact pentru că are mai mulți octeți.
   Cheie: DECIMAL când valoarea trebuie să rămână, mai ales la bani.
   Demonstrație: floatIsApproximate

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE acceptezi aproximația și nu compari cu egal exact pe bani -> floatIsApproximate -> CREATE TABLE sample (reading FLOAT) -> nu păstrează 0.10 și nu este DECIMAL
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> equalsMissesTheFloat -> SELECT reading FROM sample WHERE reading = 0.10 -> FLOAT este aproximativ. Egalul exact nu este testul sigur. DECIMAL ține valoarea pe care o compari.
    3. Granița
           -> FLOAT este tipul. Numărul de zecimale din ecran nu este stocarea.
    4. Alternativa
           -> DECIMAL(5,2) este exact. FLOAT nu este.
    5. Anti-patternul
           -> WHERE reading = 0.10 ca dovadă că valoarea s-a păstrat. Poate să nu o găsești.
    6. Dovada
           -> floatIsApproximate ține propoziția. equalsMissesTheFloat ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> FLOAT aproximează. DECIMAL ține scara. Egalul pe FLOAT poate rata valoarea. FLOAT(p) nu este un număr de zecimale.
*/

public class Floating_Point {
    /*
    🟢 Începător
        Ce este            reading este aproximativ, pe 4 octeți
        De ce e nevoie     fără să știi că este aproximativ, compari reading = 0.10 și poți să nu găsești rândul
    🟡 Producție
        Cum se scrie       CREATE TABLE sample (reading FLOAT)
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este amount DECIMAL(5,2), care ține scara
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu amount DECIMAL(5,2), care ține scara.
    */
    // Începător: floatIsApproximate păstrează propoziția pentru reading este aproximativ, pe 4 octeți
    // Producție: public static String floatIsApproximate(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. WHERE reading = 0.10 poate rata rândul pe care l-ai stocat din 0.10.
    public static String floatIsApproximate() {
        // Începător: reading este aproximativ, pe 4 octeți
        // Producție: CREATE TABLE sample (reading FLOAT). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: WHERE reading = 0.10 poate rata rândul pe care l-ai stocat din 0.10. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE sample (reading FLOAT)";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide floatIsApproximate. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            rândul stocat dintr-o zecimală poate să nu treacă egalul
        De ce e nevoie     fără textul greșit, egalul pare sigur pe FLOAT
    🟡 Producție
        Cum se scrie       SELECT reading FROM sample WHERE reading = 0.10
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care rândul stocat dintr-o zecimală poate să nu treacă egalul
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu amount DECIMAL(5,2), care ține scara.
    */
    // Începător: equalsMissesTheFloat păstrează textul greșit pentru rândul stocat dintr-o zecimală poate să nu treacă egalul
    // Producție: public static String equalsMissesTheFloat(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. FLOAT este aproximativ. Egalul exact nu este testul sigur. DECIMAL ține valoarea pe care o compari.
    public static String equalsMissesTheFloat() {
        // Începător: rândul stocat dintr-o zecimală poate să nu treacă egalul
        // Producție: SELECT reading FROM sample WHERE reading = 0.10. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: FLOAT este aproximativ. Egalul exact nu este testul sigur. DECIMAL ține valoarea pe care o compari. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT reading FROM sample WHERE reading = 0.10";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide equalsMissesTheFloat. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
