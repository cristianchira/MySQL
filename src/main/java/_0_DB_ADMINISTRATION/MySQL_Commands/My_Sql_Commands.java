package _0_DB_ADMINISTRATION.MySQL_Commands;

/*                          MySql commands   - usefully from command line


    ○ connect to MySql using command:
                * mysql -u root -p
                * add root password

    ○ switch on database you want
                 * USE database_name;

    ○ check current database you use
                 * SELECT DATABASE();

    ○ See all tables from your current database(schema)
                 * SHOW tables;


    SHOW DATABASES de mai jos este o propoziție pe care o execută serverul. Nu este o comandă pe care clientul mysql o consumă singur.
                                 HISTORICAL: MYSQL  -- administrative command. Manualul 9.7: SHOW DATABASES listează directoarele din data directory pentru contul care are privilegiu; programul mysqlshow poate aduce aceeași listă. Comenzile interpretate de client sunt help, clear, delimiter și status, nu SHOW.


    ○ See all databases

                * SHOW DATABASES;

    ○ See all tables from a specified database

                * SHOW TABLES IN your_database;

    ○ The command which retrieves the names of all tables across all schemas (databases) in a MySQL server.
          It queries the information_schema.tables table, which contains metadata about the tables in
          the MySQL server

               * SELECT table_name FROM information_schema.tables;


    ○ To show all tables within the information_schema database,
           which is a built-in database containing metadata about the MySQL server and its databases,
           use the following command:

                * SELECT table_name FROM information_schema.tables WHERE table_schema = 'information_schema';

    ○ See all users from MySql(Database Server)

                  * SELECT User, Host FROM mysql.user;

 */



/*
TOPIC:
    SHOW DATABASES îi cere serverului lista schemelor vizibile contului; programul mysql doar trimite propoziția și nu inventează lista.
FAMILIE:
    Server
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să vezi schemele care există pentru contul curent. După propoziție, nicio tabelă nu pierde rânduri.
MECHANISM:
    Serverul execută SHOW DATABASES la sfârșitul propoziției și întoarce numele schemelor. mysql -u root -p este clientul, înainte de orice propoziție.
LOOKALIKES:
    SELECT table_name FROM information_schema.tables citește metadatele tabelelor, nu lista schemelor. USE schimbă schema curentă și nu listează nimic.
BOOK THEORY:
    Manualul 9.7 nu a avut pagina SHOW deschisă separat: rândul acela este NERULAT AICI. Comentariul de curs deja corect spune că SHOW DATABASES este propoziția serverului, nu o comandă pe care clientul mysql o consumă singur. PDF local în acest folder nu a fost găsit la deschiderea PDF-urilor din src.
    HISTORICAL: pagina 15.7.7.17 a fost adusă după aceea. SHOW DATABASES listează schemele vizibile contului. mysql -u root -p nu este propoziția. SELECT table_name FROM information_schema.tables listează tabele, nu scheme.
BOOK EXAMPLE:
    showDatabasesTheServerLists
REPO DEMO:
    src/main/java/_0_DB_ADMINISTRATION/MySQL_Commands/My_Sql_Commands.java
BREAK LAB:
    clientSwitchIsNotAStatement ține mysql -u root -p. Serverul respinge linia înainte să listeze scheme. Nu se șterge niciun rând.
TRADE-OFFS:
    SELECT table_name FROM information_schema.tables WHERE table_schema = DATABASE() când vrei tabelele din schema curentă, nu numele schemelor.
ANTI-PATTERNS:
    Să trimiți mysql -u root -p ca și cum ar fi propoziția. Serverul nu pornește lista, iar schemele rămân nemișcate.
STAFF QUESTIONS:
    showDatabasesTheServerLists și clientSwitchIsNotAStatement. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Un cont de audit pe un depozit de facturi trebuie să vadă ce scheme există înainte să citească un rând de stoc. SHOW DATABASES îi dă numele. Nu îi dă cantitățile.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SHOW DATABASES față de deschiderea clientului mysql?
   Capcană: Clientul mysql listează schemele doar pentru că ai scris -p.
   Cheie: showDatabasesTheServerLists cere serverului lista. mysql -u root -p nu ajunge la server ca propoziție. Niciun rând dintr-o tabelă nu se schimbă.
   Demonstrație: showDatabasesTheServerLists
2. Enunț: Ce se întâmplă cu schemele și cu rândurile când serverul termină SHOW DATABASES?
   Capcană: Propoziția creează o schemă goală ca să ai ce arăta.
   Cheie: Rezultatul este lista numelor vizibile contului, la sfârșitul propoziției. Tabelele și rândurile lor rămân. Nu se ia un lacăt de rând.
   Demonstrație: showDatabasesTheServerLists
3. Enunț: De ce nu este aceeași propoziție cu SELECT DATABASE()?
   Capcană: Ambele întorc toate schemele de pe disc.
   Cheie: SELECT DATABASE() întoarce schema curentă, una, sau NULL dacă nu ai dat USE. SHOW DATABASES întoarce lista. USE schimbă curentul și nu listează.
   Demonstrație: showDatabasesTheServerLists
4. Enunț: Cum eșuează dacă trimiți linia de lansare a clientului?
   Capcană: mysql -u root -p este planul pe care optimizerul îl execută și întoarce schemele.
   Cheie: clientSwitchIsNotAStatement nu este SQL. Serverul nu începe lista. Rândurile rămân.
   Demonstrație: clientSwitchIsNotAStatement
5. Enunț: Când rămâi în client și când trimiți SHOW DATABASES?
   Capcană: Orice privire asupra serverului trebuie să deschidă Workbench, altfel nu există scheme.
   Cheie: Rămâi în client când doar pornești sesiunea. Trimiți SHOW DATABASES când îți trebuie lista. Pentru tabelele unei scheme treci la information_schema, nu repeți SHOW DATABASES.
   Demonstrație: showDatabasesTheServerLists

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei schemele vizibile contului, nu lansarea clientului -> showDatabasesTheServerLists -> SHOW DATABASES -> nu garantează că vezi schemele la care contul nu are privilegiu
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> clientSwitchIsNotAStatement -> mysql -u root -p -> Serverul respinge linia înainte să listeze scheme. Nu se șterge niciun rând.
    3. Granița
           -> SHOW DATABASES este propoziția. mysql -u root -p și click-ul din Workbench sunt clientul.
    4. Alternativa
           -> SELECT table_name FROM information_schema.tables citește metadatele tabelelor, nu lista schemelor. USE schimbă schema curentă și nu listează nimic.
    5. Anti-patternul
           -> Să trimiți mysql -u root -p ca și cum ar fi propoziția. Serverul nu pornește lista, iar schemele rămân nemișcate.
    6. Dovada
           -> showDatabasesTheServerLists ține propoziția. clientSwitchIsNotAStatement ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> SHOW DATABASES întoarce schemele contului. mysql -u root -p nu este propoziția. Dacă îți trebuie tabelele, ești la information_schema, nu la lista de scheme.
*/

public class My_Sql_Commands {
    /*
    🟢 Începător
        Ce este            rămâne lista schemelor vizibile contului, fără un rând șters
        De ce e nevoie     fără propoziție, mysql -u root -p doar deschide clientul și nu arată nicio schemă
    🟡 Producție
        Cum se scrie       SHOW DATABASES
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este SELECT DATABASE(), care întoarce o singură schemă curentă, nu lista
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu SELECT DATABASE(), care întoarce o singură schemă curentă, nu lista.
    */
    // Începător: showDatabasesTheServerLists păstrează propoziția pentru rămâne lista schemelor vizibile contului, fără un rând șters
    // Producție: public static String showDatabasesTheServerLists(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. SHOW DATABASES nu este USE și nu ține lacăt pe tabele.
    public static String showDatabasesTheServerLists() {
        // Începător: rămâne lista schemelor vizibile contului, fără un rând șters
        // Producție: SHOW DATABASES. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: SHOW DATABASES nu este USE și nu ține lacăt pe tabele. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SHOW DATABASES";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide showDatabasesTheServerLists. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            clientul pornește și serverul nu primește o propoziție, deci nu apare nicio listă
        De ce e nevoie     fără textul greșit, pare că lansarea clientului este deja SHOW DATABASES
    🟡 Producție
        Cum se scrie       mysql -u root -p
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care clientul pornește și serverul nu primește o propoziție, deci nu apare nicio listă
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu SELECT DATABASE(), care întoarce o singură schemă curentă, nu lista.
    */
    // Începător: clientSwitchIsNotAStatement păstrează textul greșit pentru clientul pornește și serverul nu primește o propoziție, deci nu apare nicio listă
    // Producție: public static String clientSwitchIsNotAStatement(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Serverul respinge linia înainte să listeze scheme. Nu se șterge niciun rând.
    public static String clientSwitchIsNotAStatement() {
        // Începător: clientul pornește și serverul nu primește o propoziție, deci nu apare nicio listă
        // Producție: mysql -u root -p. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: Serverul respinge linia înainte să listeze scheme. Nu se șterge niciun rând. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "mysql -u root -p";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide clientSwitchIsNotAStatement. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
