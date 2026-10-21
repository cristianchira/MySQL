package _2_SQL_Statements._3_DCL;

// Martin G. DCL
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8338038#overview

//MySQL 8.0.32
//Source Code Documentation
//  https://dev.mysql.com/doc/refman/8.0/en/account-management-statements.html


/*                                           Data Control Language (DCL)


    ○ GRANT și REVOKE nu sunt singurele propoziții de administrare de cont. Manualul 9.7, 15.7.1, mai numește ALTER USER, CREATE USER, DROP USER, RENAME USER, CREATE ROLE, DROP ROLE, SET PASSWORD, SET DEFAULT ROLE și SET ROLE. 15.3.3 spune că GRANT, REVOKE, ALTER USER, CREATE USER, DROP USER, RENAME USER și SET PASSWORD fac commit implicit, pentru că modifică tabele din schema mysql.
    HISTORICAL: DCL is a syntax containing only 2 statements:
    Regula citită în tur: MySQL 9.7 Reference Manual, 15.7.1 Account Management Statements și 15.3.3 Statements That Cause an Implicit Commit.

                   * GRANT

                                 - gives(or grants) certain permission to users

                                 - GRANT type_of_permission
                                   ON database_name.table_name
                                   TO 'username'@'localhost'

                   * REVOKE

           These statements allow us

                   * to manage the rights users have in the database

    ○ A database is used of very large numbers of users(people)

                   * Typically, users don't have

                   * same rights of access to the information.


                                            Database administrators


    ○ people who have complete rights to a database

                  * they can grant access to users

                  * and can revoke it


 */

/*
TOPIC:
    GRANT dă un drept unui cont care există; nu creează contul și nu schimbă rândurile din tabela numită.
FAMILIE:
    Security
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să poată cineva citi, nu să apară un rând de date. După GRANT, privilegiul este al contului.
MECHANISM:
    Serverul execută GRANT. CREATE USER face contul. Un cont fără privilegiu se poate autentifica și nu poate citi tabela.
LOOKALIKES:
    CREATE USER 'jeffrey'@'localhost' face contul și nu îi dă SELECT pe db1.
BOOK THEORY:
    Pagina GRANT din manualul 9.7 a fost deschisă în tur. Comentariul de curs are deja HISTORICAL: DCL nu este doar GRANT și REVOKE. CREATE USER rămâne vecinul.
BOOK EXAMPLE:
    grantSelectOnDb1
REPO DEMO:
    src/main/java/_2_SQL_Statements/_3_DCL/DCL.java
BREAK LAB:
    createUserGrantsNothing ține CREATE USER 'jeffrey'@'localhost' IDENTIFIED BY 'password123'. CREATE USER face contul. GRANT dă dreptul. Fără privilegiu, autentificarea nu citește tabela.
TRADE-OFFS:
    CREATE USER când contul lipsește. REVOKE când dreptul trebuie să dispară.
ANTI-PATTERNS:
    CREATE USER ca să poată citi db1. Contul intră și nu citește.
STAFF QUESTIONS:
    grantSelectOnDb1 și createUserGrantsNothing. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Un auditor de laborator există deja. GRANT SELECT ON lab.* TO 'audit'@'localhost' îi dă citirea. Nu creează contul.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă GRANT SELECT ON db1.* TO 'jeffrey'@'localhost'?
   Capcană: CREATE USER îi dă lui jeffrey și SELECT pe db1.
   Cheie: grantSelectOnDb1: jeffrey poate citi obiectele din db1, iar rândurile din tabele rămân. fără GRANT, contul creat nu citește db1
   Demonstrație: grantSelectOnDb1
2. Enunț: Ce rămâne în tabelă sau în sesiune după GRANT SELECT ON db1.* TO 'jeffrey'@'localhost'?
   Capcană: GRANT SELECT inserează un rând de permisiune în tabela de afaceri db1.t.
   Cheie: Efectul este: jeffrey poate citi obiectele din db1, iar rândurile din tabele rămân. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: grantSelectOnDb1
3. Enunț: De ce GRANT SELECT ON db1.* TO 'jeffrey'@'localhost' nu este vecinul său?
   Capcană: GRANT poate da și un rol, și un privilegiu, în aceeași propoziție.
   Cheie: CREATE USER 'jeffrey'@'localhost' face contul și nu îi dă SELECT pe db1.
   Demonstrație: grantSelectOnDb1
4. Enunț: Cum eșuează CREATE USER 'jeffrey'@'localhost' IDENTIFIED BY 'password123'?
   Capcană: Fără GRANT, root-ul din curs este singurul cont posibil.
   Cheie: createUserGrantsNothing: CREATE USER face contul. GRANT dă dreptul. Fără privilegiu, autentificarea nu citește tabela.
   Demonstrație: createUserGrantsNothing
5. Enunț: Când nu mai trimiți GRANT SELECT ON db1.* TO 'jeffrey'@'localhost'?
   Capcană: Ca să autorizezi, este de ajuns să creezi contul.
   Cheie: CREATE USER când contul lipsește. REVOKE când dreptul trebuie să dispară.
   Demonstrație: grantSelectOnDb1

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE contul există și îi dai SELECT pe db1 -> grantSelectOnDb1 -> GRANT SELECT ON db1.* TO 'jeffrey'@'localhost' -> nu creează jeffrey și nu schimbă rândurile din db1
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> createUserGrantsNothing -> CREATE USER 'jeffrey'@'localhost' IDENTIFIED BY 'password123' -> CREATE USER face contul. GRANT dă dreptul. Fără privilegiu, autentificarea nu citește tabela.
    3. Granița
           -> GRANT este propoziția. Bifarea unui drept în Workbench nu este mecanismul până nu trimite GRANT.
    4. Alternativa
           -> CREATE USER 'jeffrey'@'localhost' face contul și nu îi dă SELECT pe db1.
    5. Anti-patternul
           -> CREATE USER ca să poată citi db1. Contul intră și nu citește.
    6. Dovada
           -> grantSelectOnDb1 ține propoziția. createUserGrantsNothing ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> GRANT dă dreptul. CREATE USER face contul. Un cont fără privilegiu se autentifică și nu citește. Nu amesteci rol și privilegiu în același GRANT.
*/

public class DCL {
    /*
    🟢 Începător
        Ce este            jeffrey poate citi obiectele din db1, iar rândurile din tabele rămân
        De ce e nevoie     fără GRANT, contul creat nu citește db1
    🟡 Producție
        Cum se scrie       GRANT SELECT ON db1.* TO 'jeffrey'@'localhost'
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este CREATE USER 'jeffrey'@'localhost' IDENTIFIED BY 'password123', care face contul și nu dă SELECT
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu CREATE USER 'jeffrey'@'localhost' IDENTIFIED BY 'password123', care face contul și nu dă SELECT.
    */
    // Începător: grantSelectOnDb1 păstrează propoziția pentru jeffrey poate citi obiectele din db1, iar rândurile din tabele rămân
    // Producție: public static String grantSelectOnDb1(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. GRANT nu amestecă privilegii și roluri în aceeași propoziție.
    public static String grantSelectOnDb1() {
        // Începător: jeffrey poate citi obiectele din db1, iar rândurile din tabele rămân
        // Producție: GRANT SELECT ON db1.* TO 'jeffrey'@'localhost'. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: GRANT nu amestecă privilegii și roluri în aceeași propoziție. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "GRANT SELECT ON db1.* TO 'jeffrey'@'localhost'";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide grantSelectOnDb1. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            apare contul, fără SELECT pe db1, deci citirea tabelei eșuează
        De ce e nevoie     fără textul greșit, crearea contului pare și autorizarea
    🟡 Producție
        Cum se scrie       CREATE USER 'jeffrey'@'localhost' IDENTIFIED BY 'password123'
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care apare contul, fără SELECT pe db1, deci citirea tabelei eșuează
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu CREATE USER 'jeffrey'@'localhost' IDENTIFIED BY 'password123', care face contul și nu dă SELECT.
    */
    // Începător: createUserGrantsNothing păstrează textul greșit pentru apare contul, fără SELECT pe db1, deci citirea tabelei eșuează
    // Producție: public static String createUserGrantsNothing(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. CREATE USER face contul. GRANT dă dreptul. Fără privilegiu, autentificarea nu citește tabela.
    public static String createUserGrantsNothing() {
        // Începător: apare contul, fără SELECT pe db1, deci citirea tabelei eșuează
        // Producție: CREATE USER 'jeffrey'@'localhost' IDENTIFIED BY 'password123'. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: CREATE USER face contul. GRANT dă dreptul. Fără privilegiu, autentificarea nu citește tabela. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE USER 'jeffrey'@'localhost' IDENTIFIED BY 'password123'";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide createUserGrantsNothing. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
