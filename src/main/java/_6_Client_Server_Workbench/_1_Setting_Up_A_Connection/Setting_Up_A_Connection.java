package _6_Client_Server_Workbench._1_Setting_Up_A_Connection;

//Martin G. Setting up a connection
//https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8338336#questions/8965074


/*                                   root user connection

    ○ is created by default

                * must insert password to connect (365Pass in our case)

    ○ You can load more than one connection at a time in Workbench.

                * must create new user


                                          Users

    1. root user

                * In MySQL, the root user is a superuser account that

                            - has all privileges on all databases and tables within the MySQL server.

                * It is the highest level of access and should be used with caution

                            - as it has the ability to modify or delete any data on the server.

    2. Users in MySql

                * users are accounts that

                            - can connect to the MySQL server

                            - and perform various actions such as

                                        • creating,

                                        • modifying,

                                        • and deleting databases, tables, and data.

                * Each user is identified by

                              - a username

                      and authenticated using

                               - a password.
*/

/*
TOPIC:
    SELECT CURRENT_USER() întoarce contul sesiunii după ce serverul a acceptat conexiunea; click-ul din Workbench nu este propoziția.
FAMILIE:
    Server
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să știi cine este pe sesiune. Niciun rând de afaceri nu se schimbă.
MECHANISM:
    Serverul autentifică contul la conectare și CURRENT_USER() îl arată la sfârșitul propoziției. Parola nu stă în propoziție.
LOOKALIKES:
    CREATE USER face un cont nou și nu deschide sesiunea. mysql -u root -p este clientul.
BOOK THEORY:
    PDF-ul Client-Server+Model.pdf a fost deschis: este interfața, nu propoziția. Pagina SELECT 9.7 a fost deschisă. Parola din comentariul de curs nu este copiată în propoziția nouă.
BOOK EXAMPLE:
    currentUserShowsTheAccount
REPO DEMO:
    src/main/java/_6_Client_Server_Workbench/_1_Setting_Up_A_Connection/Setting_Up_A_Connection.java
BREAK LAB:
    createUserDoesNotOpenTheSession ține CREATE USER 'newuser'@'%' IDENTIFIED BY 'password123'. CREATE USER nu este sesiunea. CURRENT_USER() citește contul deja autentificat.
TRADE-OFFS:
    CREATE USER când contul lipsește. GRANT când contul există și nu are drepturi.
ANTI-PATTERNS:
    CREATE USER ca să afli cine este conectat. Vezi un cont nou, nu sesiunea.
STAFF QUESTIONS:
    currentUserShowsTheAccount și createUserDoesNotOpenTheSession. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    După ce un ghișeu se conectează, SELECT CURRENT_USER() arată contul. Nu este crearea unui casier nou.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SELECT CURRENT_USER()?
   Capcană: Click-ul de conexiune este propoziția.
   Cheie: currentUserShowsTheAccount: rezultatul este contul sesiunii și tabelele rămân. fără propoziție, nu vezi ce cont a acceptat serverul
   Demonstrație: currentUserShowsTheAccount
2. Enunț: Ce rămâne în tabelă sau în sesiune după SELECT CURRENT_USER()?
   Capcană: CREATE USER întoarce contul curent.
   Cheie: Efectul este: rezultatul este contul sesiunii și tabelele rămân. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: currentUserShowsTheAccount
3. Enunț: De ce SELECT CURRENT_USER() nu este vecinul său?
   Capcană: CURRENT_USER() schimbă parola.
   Cheie: CREATE USER face un cont nou și nu deschide sesiunea. mysql -u root -p este clientul.
   Demonstrație: currentUserShowsTheAccount
4. Enunț: Cum eșuează CREATE USER 'newuser'@'%' IDENTIFIED BY 'password123'?
   Capcană: SELECT CURRENT_USER() cere un rând din employees.
   Cheie: createUserDoesNotOpenTheSession: CREATE USER nu este sesiunea. CURRENT_USER() citește contul deja autentificat.
   Demonstrație: createUserDoesNotOpenTheSession
5. Enunț: Când nu mai trimiți SELECT CURRENT_USER()?
   Capcană: Fără Workbench, serverul nu știe contul.
   Cheie: CREATE USER când contul lipsește. GRANT când contul există și nu are drepturi.
   Demonstrație: currentUserShowsTheAccount

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei contul sesiunii, nu un cont nou -> currentUserShowsTheAccount -> SELECT CURRENT_USER() -> nu schimbă parola și nu deschide o a doua conexiune
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> createUserDoesNotOpenTheSession -> CREATE USER 'newuser'@'%' IDENTIFIED BY 'password123' -> CREATE USER nu este sesiunea. CURRENT_USER() citește contul deja autentificat.
    3. Granița
           -> CURRENT_USER() este propoziția. Formularul de conexiune este clientul.
    4. Alternativa
           -> CREATE USER face un cont nou și nu deschide sesiunea. mysql -u root -p este clientul.
    5. Anti-patternul
           -> CREATE USER ca să afli cine este conectat. Vezi un cont nou, nu sesiunea.
    6. Dovada
           -> currentUserShowsTheAccount ține propoziția. createUserDoesNotOpenTheSession ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> CURRENT_USER() arată contul sesiunii. Click-ul din Workbench nu este propoziția. CREATE USER face alt cont. Parola nu face parte din această propoziție.
*/

public class Setting_Up_A_Connection {
    /*
    🟢 Începător
        Ce este            rezultatul este contul sesiunii și tabelele rămân
        De ce e nevoie     fără propoziție, nu vezi ce cont a acceptat serverul
    🟡 Producție
        Cum se scrie       SELECT CURRENT_USER()
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este CREATE USER 'newuser'@'%' IDENTIFIED BY 'password123', care creează un cont și nu spune cine este conectat acum
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu CREATE USER 'newuser'@'%' IDENTIFIED BY 'password123', care creează un cont și nu spune cine este conectat acum.
    */
    // Începător: currentUserShowsTheAccount păstrează propoziția pentru rezultatul este contul sesiunii și tabelele rămân
    // Producție: public static String currentUserShowsTheAccount(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Un click de test în Workbench nu este CURRENT_USER().
    public static String currentUserShowsTheAccount() {
        // Începător: rezultatul este contul sesiunii și tabelele rămân
        // Producție: SELECT CURRENT_USER(). Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: Un click de test în Workbench nu este CURRENT_USER(). Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT CURRENT_USER()";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide currentUserShowsTheAccount. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            apare un cont nou, iar sesiunea curentă rămâne a contului care a trimis propoziția
        De ce e nevoie     fără textul greșit, crearea contului pare deschiderea conexiunii
    🟡 Producție
        Cum se scrie       CREATE USER 'newuser'@'%' IDENTIFIED BY 'password123'
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care apare un cont nou, iar sesiunea curentă rămâne a contului care a trimis propoziția
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu CREATE USER 'newuser'@'%' IDENTIFIED BY 'password123', care creează un cont și nu spune cine este conectat acum.
    */
    // Începător: createUserDoesNotOpenTheSession păstrează textul greșit pentru apare un cont nou, iar sesiunea curentă rămâne a contului care a trimis propoziția
    // Producție: public static String createUserDoesNotOpenTheSession(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. CREATE USER nu este sesiunea. CURRENT_USER() citește contul deja autentificat.
    public static String createUserDoesNotOpenTheSession() {
        // Începător: apare un cont nou, iar sesiunea curentă rămâne a contului care a trimis propoziția
        // Producție: CREATE USER 'newuser'@'%' IDENTIFIED BY 'password123'. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: CREATE USER nu este sesiunea. CURRENT_USER() citește contul deja autentificat. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE USER 'newuser'@'%' IDENTIFIED BY 'password123'";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide createUserDoesNotOpenTheSession. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
