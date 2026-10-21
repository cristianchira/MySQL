package _7_Data_Types._4_Spatial;

//https://dev.mysql.com/doc/refman/8.0/en/spatial-types.html

/*                   Spatial Data Types


    1. Spatial Data Types

    2. The OpenGIS Geometry Model

    3. Supported Spatial Data Formats

    4.Geometry Well-Formedness and Validity

    5. Spatial Reference System Support

    6. Creating Spatial Columns

    7.Populating Spatial Columns

    8.Fetching Spatial Data

    9.Optimizing Spatial Analysis

    10. Creating Spatial Indexes

    11. Using Spatial Indexes




 */

/*
TOPIC:
    POINT ține o poziție; VARCHAR cu 'lat,long' este text, nu o valoare spațială pe care ST_X o citește.
FAMILIE:
    Types
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să stea locul ca geometrie. Un șir de coordonate nu este POINT.
MECHANISM:
    Serverul stochează geometria în coloana spațială. Un index spațial este CREATE SPATIAL INDEX, nu un index obișnuit pe text.
LOOKALIKES:
    location VARCHAR(80) ține textul și nu este argumentul lui ST_X. Un INDEX obișnuit nu este index spațial.
BOOK THEORY:
    Pagina spațială nu a fost adusă: NERULAT AICI pentru acel URL. Nu există PDF local în extrasul acestui folder. Propoziția de mai jos este definiția coloanei, nu o măsurătoare inventată dintr-un capitol închis.
    HISTORICAL: pagina 13.4.1 a fost adusă după aceea. POINT ține o singură geometrie. VARCHAR ține text. Un index SPATIAL cere coloana NOT NULL și cu SRID. Un INDEX obișnuit pe text nu este indexul spațial.
BOOK EXAMPLE:
    pointHoldsThePlace
REPO DEMO:
    src/main/java/_7_Data_Types/_4_Spatial/Spatial_Data_Type.java
BREAK LAB:
    varcharHoldsThePair ține CREATE TABLE place (id INT, location VARCHAR(80)). VARCHAR ține caractere. POINT ține geometria. Indexul spațial nu este INDEX pe text.
TRADE-OFFS:
    VARCHAR când păstrezi textul primit și nu îl întrebi ca geometrie.
ANTI-PATTERNS:
    VARCHAR(80) ca POINT. ST_X nu are geometrie.
STAFF QUESTIONS:
    pointHoldsThePlace și varcharHoldsThePair. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Un depozit are coordonata în POINT. Un cod poștal rămâne VARCHAR. Nu le pui pe amândouă în același tip.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă CREATE TABLE place (id INT, location POINT)?
   Capcană: VARCHAR '45,26' este POINT.
   Cheie: pointHoldsThePlace: location este geometrie, nu un șir 'lat,long'. fără POINT, coordonatele rămân text și ST_X nu are geometrie
   Demonstrație: pointHoldsThePlace
2. Enunț: Ce rămâne în tabelă sau în sesiune după CREATE TABLE place (id INT, location POINT)?
   Capcană: CREATE INDEX pe location VARCHAR este index spațial.
   Cheie: Efectul este: location este geometrie, nu un șir 'lat,long'. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: pointHoldsThePlace
3. Enunț: De ce CREATE TABLE place (id INT, location POINT) nu este vecinul său?
   Capcană: POINT ține un cod poștal.
   Cheie: location VARCHAR(80) ține textul și nu este argumentul lui ST_X. Un INDEX obișnuit nu este index spațial.
   Demonstrație: pointHoldsThePlace
4. Enunț: Cum eșuează CREATE TABLE place (id INT, location VARCHAR(80))?
   Capcană: ST_X citește un VARCHAR.
   Cheie: varcharHoldsThePair: VARCHAR ține caractere. POINT ține geometria. Indexul spațial nu este INDEX pe text.
   Demonstrație: varcharHoldsThePair
5. Enunț: Când nu mai trimiți CREATE TABLE place (id INT, location POINT)?
   Capcană: Tipul spațial se alege în ecran, fără coloană.
   Cheie: VARCHAR când păstrezi textul primit și nu îl întrebi ca geometrie.
   Demonstrație: pointHoldsThePlace

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE poziția trebuie să fie geometrie -> pointHoldsThePlace -> CREATE TABLE place (id INT, location POINT) -> nu creează singur indexul spațial și nu validează un șir 'lat,long'
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> varcharHoldsThePair -> CREATE TABLE place (id INT, location VARCHAR(80)) -> VARCHAR ține caractere. POINT ține geometria. Indexul spațial nu este INDEX pe text.
    3. Granița
           -> POINT este tipul. O pereche de numere dintr-un formular nu este geometrie până nu o scrii ca atare.
    4. Alternativa
           -> location VARCHAR(80) ține textul și nu este argumentul lui ST_X. Un INDEX obișnuit nu este index spațial.
    5. Anti-patternul
           -> VARCHAR(80) ca POINT. ST_X nu are geometrie.
    6. Dovada
           -> pointHoldsThePlace ține propoziția. varcharHoldsThePair ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> POINT este geometria. VARCHAR este textul coordonatei. Indexul SPATIAL cere NOT NULL și SRID. Un INDEX pe text nu este indexul spațial.
*/

public class Spatial_Data_Type {
    /*
    🟢 Începător
        Ce este            location este geometrie, nu un șir 'lat,long'
        De ce e nevoie     fără POINT, coordonatele rămân text și ST_X nu are geometrie
    🟡 Producție
        Cum se scrie       CREATE TABLE place (id INT, location POINT)
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este location VARCHAR(80)
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu location VARCHAR(80).
    */
    // Începător: pointHoldsThePlace păstrează propoziția pentru location este geometrie, nu un șir 'lat,long'
    // Producție: public static String pointHoldsThePlace(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. CREATE INDEX obișnuit pe un text nu este indexul spațial.
    public static String pointHoldsThePlace() {
        // Începător: location este geometrie, nu un șir 'lat,long'
        // Producție: CREATE TABLE place (id INT, location POINT). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: CREATE INDEX obișnuit pe un text nu este indexul spațial. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE place (id INT, location POINT)";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide pointHoldsThePlace. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            location este text și nu este un POINT
        De ce e nevoie     fără textul greșit, coordonata scrisă pare tipul spațial
    🟡 Producție
        Cum se scrie       CREATE TABLE place (id INT, location VARCHAR(80))
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care location este text și nu este un POINT
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu location VARCHAR(80).
    */
    // Începător: varcharHoldsThePair păstrează textul greșit pentru location este text și nu este un POINT
    // Producție: public static String varcharHoldsThePair(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. VARCHAR ține caractere. POINT ține geometria. Indexul spațial nu este INDEX pe text.
    public static String varcharHoldsThePair() {
        // Începător: location este text și nu este un POINT
        // Producție: CREATE TABLE place (id INT, location VARCHAR(80)). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: VARCHAR ține caractere. POINT ține geometria. Indexul spațial nu este INDEX pe text. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE place (id INT, location VARCHAR(80))";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide varcharHoldsThePair. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
