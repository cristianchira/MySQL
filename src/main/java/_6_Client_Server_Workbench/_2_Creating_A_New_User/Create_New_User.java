package _6_Client_Server_Workbench._2_Creating_A_New_User;

// Martin G.
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/10404638#questions

//Create a new user MYSql 8.0
//https://dev.mysql.com/doc/refman/8.0/en/create-user.html


/*                                       Create a new usr


    ○ Basic syntax:

                * CREATE USER 'username'@'hostname' IDENTIFIED BY 'password';


    ○ To create a new user called 'newuser' with the password 'password123'

                 * and allow them to connect from any host, use the following command:

                             - CREATE USER 'newuser'@'%' IDENTIFIED BY 'password123';


 */

/*
TOPIC:
    CREATE USER 'newuser'@'%' IDENTIFIED BY 'password123' face contul; fără GRANT, contul se poate autentifica și nu poate citi tabela.
FAMILIE:
    Security
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să existe contul. După propoziție, nu are încă SELECT pe o schemă de afaceri.
MECHANISM:
    Serverul creează contul la sfârșitul propoziției. GRANT este propoziția care dă dreptul. Hostul '%' nu este parola.
LOOKALIKES:
    GRANT SELECT ON db1.* TO 'newuser'@'%' dă citirea și nu creează contul dacă lipsește.
BOOK THEORY:
    Pagina CREATE USER nu a fost adusă separat: NERULAT AICI pentru acel URL. Pagina GRANT 9.7 a fost deschisă: privilegiul și contul sunt propoziții diferite. Exemplul din comentariul de curs este propoziția.
    HISTORICAL: pagina 15.7.1.3 a fost adusă după aceea. CREATE USER creează contul și autentificarea. Nu dă SELECT pe o schemă. Dacă un cont din listă eșuează, întreaga propoziție nu lasă niciun cont nou. GRANT rămâne propoziția care dă dreptul.
BOOK EXAMPLE:
    createNewUserOnly
REPO DEMO:
    src/main/java/_6_Client_Server_Workbench/_2_Creating_A_New_User/Create_New_User.java
BREAK LAB:
    grantDoesNotCreateTheAccount ține GRANT SELECT ON db1.* TO 'newuser'@'%'. GRANT dă dreptul unui cont. CREATE USER face contul. Fără privilegiu, autentificarea nu citește tabela.
TRADE-OFFS:
    GRANT când contul există și îi lipsește un drept.
ANTI-PATTERNS:
    GRANT către un cont care nu există, ca să îl și creezi. Contul nu apare din GRANT.
STAFF QUESTIONS:
    createNewUserOnly și grantDoesNotCreateTheAccount. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Un casier nou este CREATE USER. Dreptul pe vânzări este GRANT, separat.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă CREATE USER 'newuser'@'%' IDENTIFIED BY 'password123'?
   Capcană: CREATE USER îi dă lui newuser SELECT pe toate tabelele.
   Cheie: createNewUserOnly: apare contul newuser de pe orice host, fără un privilegiu nou pe tabele. fără CREATE USER, GRANT nu are cui să dea dreptul
   Demonstrație: createNewUserOnly
2. Enunț: Ce rămâne în tabelă sau în sesiune după CREATE USER 'newuser'@'%' IDENTIFIED BY 'password123'?
   Capcană: GRANT creează newuser dacă lipsește.
   Cheie: Efectul este: apare contul newuser de pe orice host, fără un privilegiu nou pe tabele. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: createNewUserOnly
3. Enunț: De ce CREATE USER 'newuser'@'%' IDENTIFIED BY 'password123' nu este vecinul său?
   Capcană: Hostul '%' este parola.
   Cheie: GRANT SELECT ON db1.* TO 'newuser'@'%' dă citirea și nu creează contul dacă lipsește.
   Demonstrație: createNewUserOnly
4. Enunț: Cum eșuează GRANT SELECT ON db1.* TO 'newuser'@'%'?
   Capcană: CREATE USER schimbă sesiunea curentă pe newuser.
   Cheie: grantDoesNotCreateTheAccount: GRANT dă dreptul unui cont. CREATE USER face contul. Fără privilegiu, autentificarea nu citește tabela.
   Demonstrație: grantDoesNotCreateTheAccount
5. Enunț: Când nu mai trimiți CREATE USER 'newuser'@'%' IDENTIFIED BY 'password123'?
   Capcană: Fără Workbench, CREATE USER nu există.
   Cheie: GRANT când contul există și îi lipsește un drept.
   Demonstrație: createNewUserOnly

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei contul, încă fără drept pe db1 -> createNewUserOnly -> CREATE USER 'newuser'@'%' IDENTIFIED BY 'password123' -> nu dă SELECT și nu schimbă contul cu care ești conectat
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> grantDoesNotCreateTheAccount -> GRANT SELECT ON db1.* TO 'newuser'@'%' -> GRANT dă dreptul unui cont. CREATE USER face contul. Fără privilegiu, autentificarea nu citește tabela.
    3. Granița
           -> CREATE USER este propoziția. Ecranul de user din Workbench trebuie să o trimită.
    4. Alternativa
           -> GRANT SELECT ON db1.* TO 'newuser'@'%' dă citirea și nu creează contul dacă lipsește.
    5. Anti-patternul
           -> GRANT către un cont care nu există, ca să îl și creezi. Contul nu apare din GRANT.
    6. Dovada
           -> createNewUserOnly ține propoziția. grantDoesNotCreateTheAccount ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> CREATE USER face contul. GRANT dă dreptul. Un cont fără privilegiu se autentifică și nu citește. Nu sunt aceeași propoziție.
*/

public class Create_New_User {
    /*
    🟢 Începător
        Ce este            apare contul newuser de pe orice host, fără un privilegiu nou pe tabele
        De ce e nevoie     fără CREATE USER, GRANT nu are cui să dea dreptul
    🟡 Producție
        Cum se scrie       CREATE USER 'newuser'@'%' IDENTIFIED BY 'password123'
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este GRANT SELECT ON db1.* TO 'newuser'@'%'
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu GRANT SELECT ON db1.* TO 'newuser'@'%'.
    */
    // Începător: createNewUserOnly păstrează propoziția pentru apare contul newuser de pe orice host, fără un privilegiu nou pe tabele
    // Producție: public static String createNewUserOnly(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. IDENTIFIED BY nu este GRANT. Contul fără privilegiu nu citește db1.
    public static String createNewUserOnly() {
        // Începător: apare contul newuser de pe orice host, fără un privilegiu nou pe tabele
        // Producție: CREATE USER 'newuser'@'%' IDENTIFIED BY 'password123'. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: IDENTIFIED BY nu este GRANT. Contul fără privilegiu nu citește db1. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE USER 'newuser'@'%' IDENTIFIED BY 'password123'";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide createNewUserOnly. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            dacă newuser nu există, serverul nu pornește de la un cont lipsă doar pentru că ai scris GRANT
        De ce e nevoie     fără textul greșit, GRANT pare că face contul
    🟡 Producție
        Cum se scrie       GRANT SELECT ON db1.* TO 'newuser'@'%'
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care dacă newuser nu există, serverul nu pornește de la un cont lipsă doar pentru că ai scris GRANT
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu GRANT SELECT ON db1.* TO 'newuser'@'%'.
    */
    // Începător: grantDoesNotCreateTheAccount păstrează textul greșit pentru dacă newuser nu există, serverul nu pornește de la un cont lipsă doar pentru că ai scris GRANT
    // Producție: public static String grantDoesNotCreateTheAccount(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. GRANT dă dreptul unui cont. CREATE USER face contul. Fără privilegiu, autentificarea nu citește tabela.
    public static String grantDoesNotCreateTheAccount() {
        // Începător: dacă newuser nu există, serverul nu pornește de la un cont lipsă doar pentru că ai scris GRANT
        // Producție: GRANT SELECT ON db1.* TO 'newuser'@'%'. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: GRANT dă dreptul unui cont. CREATE USER face contul. Fără privilegiu, autentificarea nu citește tabela. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "GRANT SELECT ON db1.* TO 'newuser'@'%'";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide grantDoesNotCreateTheAccount. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
