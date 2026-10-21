package _7_Data_Types._2_Date_DateTime_Timestamp;

//Automatic Initialization and Updating for TIMESTAMP and DATETIME
// https://dev.mysql.com/doc/refman/8.0/en/timestamp-initialization.html

// Date and Time Data Types
// https://dev.mysql.com/doc/refman/8.0/en/date-and-time-types.html

// DATE, DATETIME, TIMESTAMP
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8339036#questions


/*                                           Date and Time Data Types


            * DATE                                              'YYYY-MM-DD'       format

            * DATETIME                                          'YYYY-MM-DD hh:mm:ss'     format

            * TIMESTAMP                                          'YYYY-MM-DD hh:mm:ss[.fraction]'

    ○ TIMESTAMP Example                                            '2022-12-31 23:59:59.999999'


                          Automatic Initialization and Updating for TIMESTAMP and DATETIME


    ○ Example

                * CREATE TABLE update_datetime_timestamp (
                  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                  name VARCHAR(50) NOT NULL,
                  timestamp_column TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                  datetime_column DATETIME DEFAULT CURRENT_TIMESTAMP
                  );

                     INSERT INTO update_datetime_timestamp (name) VALUES
                     ('Alice');

                     INSERT INTO update_datetime_timestamp (name) VALUES
                     ('John');




 */
/*
TOPIC:
    TIMESTAMP se convertește după time_zone; DATETIME păstrează '2022-12-31 23:59:59' cum a fost scris.
FAMILIE:
    Types
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu schimbi ora doar pentru că sesiunea are alt fus. DATETIME rămâne valoarea de calendar.
MECHANISM:
    Serverul stochează TIMESTAMP în UTC și îl citește după time_zone. DATETIME nu face această conversie. Intervalul TIMESTAMP se oprește în 2038.
LOOKALIKES:
    DATE nu are oră. DATETIME are oră și nu trece prin UTC.
BOOK THEORY:
    Manualul 9.7, 13.2.2, deschis în tur: DATE este 'YYYY-MM-DD'. DATETIME păstrează data și ora fără conversie de time_zone. TIMESTAMP se stochează în UTC și se citește după time_zone, până în 2038.  PDF-ul Data_Types.pdf din folder a fost deschis: DATE este YYYY-MM-DD, de la anul 1000 la 9999.
BOOK EXAMPLE:
    datetimeKeepsTheClock
REPO DEMO:
    src/main/java/_7_Data_Types/_2_Date_DateTime_Timestamp/DATE_DATETIME_TIMESTAMP.java
BREAK LAB:
    timestampFollowsTheZone ține CREATE TABLE mark (seen TIMESTAMP). TIMESTAMP trece prin UTC. DATETIME nu. Capătul lui TIMESTAMP este 2038.
TRADE-OFFS:
    TIMESTAMP când vrei momentul în UTC și accepți capătul din 2038.
ANTI-PATTERNS:
    TIMESTAMP pentru o oră de program care trebuie să rămână 23:59 după schimbarea fusului. Citirea se mută.
STAFF QUESTIONS:
    datetimeKeepsTheClock și timestampFollowsTheZone. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Ora unei ture de magazin este DATETIME, ca să rămână 08:00. Momentul unui eveniment de audit poate fi TIMESTAMP.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă CREATE TABLE mark (seen DATETIME)?
   Capcană: TIMESTAMP și DATETIME se citesc la fel după schimbarea fusului.
   Cheie: datetimeKeepsTheClock: seen păstrează data și ora scrise, fără conversie de time_zone. fără să desparți tipurile, o schimbare de time_zone pare că mută și DATETIME
   Demonstrație: datetimeKeepsTheClock
2. Enunț: Ce rămâne în tabelă sau în sesiune după CREATE TABLE mark (seen DATETIME)?
   Capcană: DATE ține și ora.
   Cheie: Efectul este: seen păstrează data și ora scrise, fără conversie de time_zone. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: datetimeKeepsTheClock
3. Enunț: De ce CREATE TABLE mark (seen DATETIME) nu este vecinul său?
   Capcană: TIMESTAMP ține anul 1000.
   Cheie: DATE nu are oră. DATETIME are oră și nu trece prin UTC.
   Demonstrație: datetimeKeepsTheClock
4. Enunț: Cum eșuează CREATE TABLE mark (seen TIMESTAMP)?
   Capcană: DATETIME se stochează în UTC.
   Cheie: timestampFollowsTheZone: TIMESTAMP trece prin UTC. DATETIME nu. Capătul lui TIMESTAMP este 2038.
   Demonstrație: timestampFollowsTheZone
5. Enunț: Când nu mai trimiți CREATE TABLE mark (seen DATETIME)?
   Capcană: Formatul din client alege tipul.
   Cheie: TIMESTAMP când vrei momentul în UTC și accepți capătul din 2038.
   Demonstrație: datetimeKeepsTheClock

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE ora trebuie să rămână cea scrisă, indiferent de fusul sesiunii -> datetimeKeepsTheClock -> CREATE TABLE mark (seen DATETIME) -> nu ține fusul în valoare și nu înlocuiește DATE când nu ai oră
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> timestampFollowsTheZone -> CREATE TABLE mark (seen TIMESTAMP) -> TIMESTAMP trece prin UTC. DATETIME nu. Capătul lui TIMESTAMP este 2038.
    3. Granița
           -> Tipul este în CREATE TABLE. Formatul din ecran nu este conversia.
    4. Alternativa
           -> DATE nu are oră. DATETIME are oră și nu trece prin UTC.
    5. Anti-patternul
           -> TIMESTAMP pentru o oră de program care trebuie să rămână 23:59 după schimbarea fusului. Citirea se mută.
    6. Dovada
           -> datetimeKeepsTheClock ține propoziția. timestampFollowsTheZone ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> DATETIME păstrează calendarul și ora. TIMESTAMP se convertește după time_zone și se oprește în 2038. DATE nu are oră.
*/

public class DATE_DATETIME_TIMESTAMP {
    /*
    🟢 Începător
        Ce este            seen păstrează data și ora scrise, fără conversie de time_zone
        De ce e nevoie     fără să desparți tipurile, o schimbare de time_zone pare că mută și DATETIME
    🟡 Producție
        Cum se scrie       CREATE TABLE mark (seen DATETIME)
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este seen TIMESTAMP, care se citește altfel după time_zone
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu seen TIMESTAMP, care se citește altfel după time_zone.
    */
    // Începător: datetimeKeepsTheClock păstrează propoziția pentru seen păstrează data și ora scrise, fără conversie de time_zone
    // Producție: public static String datetimeKeepsTheClock(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. TIMESTAMP nu ține un moment din anul 1000. DATETIME îl ține în intervalul lui.
    public static String datetimeKeepsTheClock() {
        // Începător: seen păstrează data și ora scrise, fără conversie de time_zone
        // Producție: CREATE TABLE mark (seen DATETIME). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: TIMESTAMP nu ține un moment din anul 1000. DATETIME îl ține în intervalul lui. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE mark (seen DATETIME)";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide datetimeKeepsTheClock. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            aceeași cifră de oră se citește diferit dacă time_zone al sesiunii se schimbă
        De ce e nevoie     fără textul greșit, TIMESTAMP pare un DATETIME mai scurt
    🟡 Producție
        Cum se scrie       CREATE TABLE mark (seen TIMESTAMP)
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care aceeași cifră de oră se citește diferit dacă time_zone al sesiunii se schimbă
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu seen TIMESTAMP, care se citește altfel după time_zone.
    */
    // Începător: timestampFollowsTheZone păstrează textul greșit pentru aceeași cifră de oră se citește diferit dacă time_zone al sesiunii se schimbă
    // Producție: public static String timestampFollowsTheZone(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. TIMESTAMP trece prin UTC. DATETIME nu. Capătul lui TIMESTAMP este 2038.
    public static String timestampFollowsTheZone() {
        // Începător: aceeași cifră de oră se citește diferit dacă time_zone al sesiunii se schimbă
        // Producție: CREATE TABLE mark (seen TIMESTAMP). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: TIMESTAMP trece prin UTC. DATETIME nu. Capătul lui TIMESTAMP este 2038. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE mark (seen TIMESTAMP)";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide timestampFollowsTheZone. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
