package _6_Client_Server_Workbench._3_Creating_A_Database;

//Creating a database
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8338340#questions

/*                                            Creating a database


    ○ CREATE database [if not exists] database_name;

                * represent an object ready to be filled with information.
 */

/*
TOPIC:
    CREATE DATABASE clinic lasă o schemă goală; nu creează o tabelă și nu alege schema sesiunii până la USE.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să existe schema. După propoziție, nu există încă o tabelă în ea.
MECHANISM:
    Serverul execută CREATE DATABASE. USE clinic face schema curentă. CREATE TABLE face obiectul.
LOOKALIKES:
    USE clinic nu creează schema. CREATE TABLE clinic (...) eșuează ca tabelă numită clinic, nu ca schemă.
BOOK THEORY:
    Pagina CREATE DATABASE nu a fost adusă separat: NERULAT AICI. PDF-ul de client a fost deschis și este interfața. Comentariul de curs păstrează IF NOT EXISTS ca formă.
    HISTORICAL: pagina 15.1.14 a fost adusă după aceea. CREATE DATABASE face doar directorul schemei, fără tabele. Dacă numele există și lipsește IF NOT EXISTS, este eroare. Un director creat cu mkdir sub data directory nu este o schemă suportată pe 9.7.
BOOK EXAMPLE:
    createDatabaseClinic
REPO DEMO:
    src/main/java/_6_Client_Server_Workbench/_3_Creating_A_Database/Creating_A_Database.java
BREAK LAB:
    useDoesNotCreateClinic ține USE clinic. USE alege o schemă existentă. CREATE DATABASE o face.
TRADE-OFFS:
    USE când schema există. CREATE TABLE când vrei obiectul în schema curentă.
ANTI-PATTERNS:
    USE clinic ca să o creezi. Dacă lipsește, nu apare.
STAFF QUESTIONS:
    createDatabaseClinic și useDoesNotCreateClinic. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Schema de laborator se creează cu CREATE DATABASE lab. Tabelele de probe vin după, cu CREATE TABLE.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă CREATE DATABASE clinic?
   Capcană: CREATE DATABASE clinic creează și o tabelă clinic.
   Cheie: createDatabaseClinic: apare schema clinic, fără tabele și fără să devină schema sesiunii. fără schemă, USE clinic eșuează
   Demonstrație: createDatabaseClinic
2. Enunț: Ce rămâne în tabelă sau în sesiune după CREATE DATABASE clinic?
   Capcană: USE clinic creează schema.
   Cheie: Efectul este: apare schema clinic, fără tabele și fără să devină schema sesiunii. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: createDatabaseClinic
3. Enunț: De ce CREATE DATABASE clinic nu este vecinul său?
   Capcană: După CREATE DATABASE, SELECT DATABASE() este deja clinic.
   Cheie: USE clinic nu creează schema. CREATE TABLE clinic (...) eșuează ca tabelă numită clinic, nu ca schemă.
   Demonstrație: createDatabaseClinic
4. Enunț: Cum eșuează USE clinic?
   Capcană: CREATE DATABASE primește coloane.
   Cheie: useDoesNotCreateClinic: USE alege o schemă existentă. CREATE DATABASE o face.
   Demonstrație: useDoesNotCreateClinic
5. Enunț: Când nu mai trimiți CREATE DATABASE clinic?
   Capcană: Click-ul de schemă este propoziția.
   Cheie: USE când schema există. CREATE TABLE când vrei obiectul în schema curentă.
   Demonstrație: createDatabaseClinic

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei schema, încă fără tabele și fără să schimbi sesiunea -> createDatabaseClinic -> CREATE DATABASE clinic -> nu creează tabele și nu face USE
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> useDoesNotCreateClinic -> USE clinic -> USE alege o schemă existentă. CREATE DATABASE o face.
    3. Granița
           -> CREATE DATABASE este propoziția. Un folder nou în Workbench nu este schema până nu o trimite.
    4. Alternativa
           -> USE clinic nu creează schema. CREATE TABLE clinic (...) eșuează ca tabelă numită clinic, nu ca schemă.
    5. Anti-patternul
           -> USE clinic ca să o creezi. Dacă lipsește, nu apare.
    6. Dovada
           -> createDatabaseClinic ține propoziția. useDoesNotCreateClinic ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> CREATE DATABASE face schema goală. USE o alege. CREATE TABLE face obiectul. Sesiunea nu se mută singură pe schema nouă.
*/

public class Creating_A_Database {
    /*
    🟢 Începător
        Ce este            apare schema clinic, fără tabele și fără să devină schema sesiunii
        De ce e nevoie     fără schemă, USE clinic eșuează
    🟡 Producție
        Cum se scrie       CREATE DATABASE clinic
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este USE clinic, care o alege doar dacă există
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu USE clinic, care o alege doar dacă există.
    */
    // Începător: createDatabaseClinic păstrează propoziția pentru apare schema clinic, fără tabele și fără să devină schema sesiunii
    // Producție: public static String createDatabaseClinic(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. CREATE DATABASE clinic (id INT) nu este sintaxa schemei.
    public static String createDatabaseClinic() {
        // Începător: apare schema clinic, fără tabele și fără să devină schema sesiunii
        // Producție: CREATE DATABASE clinic. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: CREATE DATABASE clinic (id INT) nu este sintaxa schemei. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE DATABASE clinic";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide createDatabaseClinic. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            dacă clinic lipsește, schema nu apare și sesiunea nu se mută pe ea
        De ce e nevoie     fără textul greșit, USE pare crearea
    🟡 Producție
        Cum se scrie       USE clinic
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care dacă clinic lipsește, schema nu apare și sesiunea nu se mută pe ea
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu USE clinic, care o alege doar dacă există.
    */
    // Începător: useDoesNotCreateClinic păstrează textul greșit pentru dacă clinic lipsește, schema nu apare și sesiunea nu se mută pe ea
    // Producție: public static String useDoesNotCreateClinic(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. USE alege o schemă existentă. CREATE DATABASE o face.
    public static String useDoesNotCreateClinic() {
        // Începător: dacă clinic lipsește, schema nu apare și sesiunea nu se mută pe ea
        // Producție: USE clinic. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: USE alege o schemă existentă. CREATE DATABASE o face. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "USE clinic";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide useDoesNotCreateClinic. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
