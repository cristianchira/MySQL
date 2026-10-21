package _7_Data_Types._3_String_Data_Type._5_ENUM;

//ENUM Type
//https://dev.mysql.com/doc/refman/8.0/en/enum.html


/*                                            Enum Type


    ○ CREATE TABLE shirts (
       name VARCHAR(40),
       size ENUM('x-small', 'small', 'medium', 'large', 'x-large')
       );

       INSERT INTO shirts (name, size) VALUES ('dress shirt','large'), ('t-shirt','medium'),
       ('polo shirt','small');

        SELECT name, size FROM shirts WHERE size = 'medium';
        +---------+--------+
        | name    | size   |
        +---------+--------+
        | t-shirt | medium |
        +---------+--------+
        UPDATE shirts SET size = 'small' WHERE size = 'large';


 */

/*
TOPIC:
    ENUM('x-small','small','medium','large','x-large') refuză, în mod strict, mărimea din afara listei; VARCHAR o acceptă.
FAMILIE:
    Types
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu intre 'huge' ca mărime. Rândul cu 'medium' rămâne o valoare din listă.
MECHANISM:
    Serverul păstrează membrii declarați. În mod strict, o valoare necunoscută este eroare. Fără strict, se poate stoca șirul gol cu index 0. Numărul fără ghilimele este index, nu eticheta.
LOOKALIKES:
    size VARCHAR(40) acceptă 'huge'. ENUM nu, când modul este strict.
BOOK THEORY:
    Manualul 9.7, 13.3.6, deschis în tur: ENUM primește un membru din listă. În mod strict, valoarea din afara listei este eroare, nu un VARCHAR care o acceptă.  Exemplul shirts din manual este și în comentariul de curs.
BOOK EXAMPLE:
    enumKeepsMedium
REPO DEMO:
    src/main/java/_7_Data_Types/_3_String_Data_Type/_5_ENUM/ENUM_Data_Type.java
BREAK LAB:
    varcharAcceptsHuge ține CREATE TABLE shirts (name VARCHAR(40), size VARCHAR(40)). VARCHAR nu refuză valoarea din afara listei. ENUM, în mod strict, o refuză.
TRADE-OFFS:
    VARCHAR când lista se schimbă des și nu vrei ALTER pe membri.
ANTI-PATTERNS:
    VARCHAR(40) pentru o mărime închisă. 'huge' intră.
STAFF QUESTIONS:
    enumKeepsMedium și varcharAcceptsHuge. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Starea unui lot este ENUM('sealed','open','spent'). VARCHAR ar accepta 'done', care nu există în proces.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă CREATE TABLE shirts (name VARCHAR(40), size ENUM('x-small', 'small', 'medium', 'large', 'x-large'))?
   Capcană: VARCHAR refuză 'huge' dacă ai o listă în comentariu.
   Cheie: enumKeepsMedium: size poate fi doar un membru din listă, iar 'medium' este acel membru, nu un șir liber. fără ENUM, VARCHAR acceptă orice mărime, inclusiv una care nu există în magazin
   Demonstrație: enumKeepsMedium
2. Enunț: Ce rămâne în tabelă sau în sesiune după CREATE TABLE shirts (name VARCHAR(40), size ENUM('x-small', 'small', 'medium', 'large', 'x-large'))?
   Capcană: INSERT 2 scrie șirul '2'.
   Cheie: Efectul este: size poate fi doar un membru din listă, iar 'medium' este acel membru, nu un șir liber. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: enumKeepsMedium
3. Enunț: De ce CREATE TABLE shirts (name VARCHAR(40), size ENUM('x-small', 'small', 'medium', 'large', 'x-large')) nu este vecinul său?
   Capcană: ENUM sortează alfabetic.
   Cheie: size VARCHAR(40) acceptă 'huge'. ENUM nu, când modul este strict.
   Demonstrație: enumKeepsMedium
4. Enunț: Cum eșuează CREATE TABLE shirts (name VARCHAR(40), size VARCHAR(40))?
   Capcană: Fără strict, 'huge' rămâne 'huge' vizibil ca text normal.
   Cheie: varcharAcceptsHuge: VARCHAR nu refuză valoarea din afara listei. ENUM, în mod strict, o refuză.
   Demonstrație: varcharAcceptsHuge
5. Enunț: Când nu mai trimiți CREATE TABLE shirts (name VARCHAR(40), size ENUM('x-small', 'small', 'medium', 'large', 'x-large'))?
   Capcană: ENUM este un FOREIGN KEY către o tabelă de mărimi.
   Cheie: VARCHAR când lista se schimbă des și nu vrei ALTER pe membri.
   Demonstrație: enumKeepsMedium

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE mărimea trebuie să fie din listă -> enumKeepsMedium -> CREATE TABLE shirts (name VARCHAR(40), size ENUM('x-small', 'small', 'medium', 'large', 'x-large')) -> nu sortează alfabetic: sortează după indexul din listă
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> varcharAcceptsHuge -> CREATE TABLE shirts (name VARCHAR(40), size VARCHAR(40)) -> VARCHAR nu refuză valoarea din afara listei. ENUM, în mod strict, o refuză.
    3. Granița
           -> ENUM este tipul. O listă din aplicație care nu este în CREATE TABLE nu oprește INSERT-ul.
    4. Alternativa
           -> size VARCHAR(40) acceptă 'huge'. ENUM nu, când modul este strict.
    5. Anti-patternul
           -> VARCHAR(40) pentru o mărime închisă. 'huge' intră.
    6. Dovada
           -> enumKeepsMedium ține propoziția. varcharAcceptsHuge ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> ENUM limitează lista. VARCHAR nu. În mod strict, valoarea străină este eroare. Numărul neghilimele este indexul, nu eticheta. Sortarea este după index.
*/

public class ENUM_Data_Type {
    /*
    🟢 Începător
        Ce este            size poate fi doar un membru din listă, iar 'medium' este acel membru, nu un șir liber
        De ce e nevoie     fără ENUM, VARCHAR acceptă orice mărime, inclusiv una care nu există în magazin
    🟡 Producție
        Cum se scrie       CREATE TABLE shirts (name VARCHAR(40), size ENUM('x-small', 'small', 'medium', 'large', 'x-large'))
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este size VARCHAR(40)
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu size VARCHAR(40).
    */
    // Începător: enumKeepsMedium păstrează propoziția pentru size poate fi doar un membru din listă, iar 'medium' este acel membru, nu un șir liber
    // Producție: public static String enumKeepsMedium(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. INSERT cu 2, neghilimele, scrie membrul de la indexul 2, nu șirul '2', dacă '2' nu este în listă.
    public static String enumKeepsMedium() {
        // Începător: size poate fi doar un membru din listă, iar 'medium' este acel membru, nu un șir liber
        // Producție: CREATE TABLE shirts (name VARCHAR(40), size ENUM('x-small', 'small', 'medium', 'large', 'x-large')). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: INSERT cu 2, neghilimele, scrie membrul de la indexul 2, nu șirul '2', dacă '2' nu este în listă. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE shirts (name VARCHAR(40), size ENUM('x-small', 'small', 'medium', 'large', 'x-large'))";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide enumKeepsMedium. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            'huge' poate intra și nu mai este lista închisă
        De ce e nevoie     fără textul greșit, VARCHAR pare ENUM mai lung
    🟡 Producție
        Cum se scrie       CREATE TABLE shirts (name VARCHAR(40), size VARCHAR(40))
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care 'huge' poate intra și nu mai este lista închisă
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu size VARCHAR(40).
    */
    // Începător: varcharAcceptsHuge păstrează textul greșit pentru 'huge' poate intra și nu mai este lista închisă
    // Producție: public static String varcharAcceptsHuge(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. VARCHAR nu refuză valoarea din afara listei. ENUM, în mod strict, o refuză.
    public static String varcharAcceptsHuge() {
        // Începător: 'huge' poate intra și nu mai este lista închisă
        // Producție: CREATE TABLE shirts (name VARCHAR(40), size VARCHAR(40)). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: VARCHAR nu refuză valoarea din afara listei. ENUM, în mod strict, o refuză. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE shirts (name VARCHAR(40), size VARCHAR(40))";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide varcharAcceptsHuge. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
