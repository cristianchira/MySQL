// Începător: pachetul ține propozițiile prin care serverul își arată firul, contul și calea bazelor.
// Producție: package _6_Client_Server_Workbench. Fără linie, SELECT CONNECTION_ID(), USER(), CURRENT_USER() nu stă în acest folder.
// Expert: pachetul nu deschide host și port și nu scrie schema. CREATE USER și CREATE DATABASE rămân copiii la care treci doar când contul sau schema lipsesc.
package _6_Client_Server_Workbench;

/*
TOPIC:
    mysqld execută propoziția pe firul conexiunii și întoarce rezultatul; clientul, inclusiv Workbench, doar o trimite și o afișează
FAMILIE:
    Server
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0
INTENT:
    clientul pare locul în care stau tabelele; după propoziție, rândurile rămân în data directory, iar sesiunea are un CONNECTION_ID și contul CURRENT_USER()
MECHANISM:
    mysqld ascultă, dă fiecărui client un fir care autentifică și execută; rezultatul se vede la sfârșitul propoziției, în client
LOOKALIKES:
    Workbench, mysql și Connector/J sunt clienți. Se despart de conexiune: host și port deschid firul, SELECT CONNECTION_ID(), USER(), CURRENT_USER() se citește după. Se despart de CREATE USER și CREATE DATABASE: acelea scriu contul sau schema
BOOK THEORY:
    În 9.7, mysqld gestionează data directory și ascultă clienții; fără server pornit, clientul nu ajunge la baze. Fiecare conexiune are un fir. CONNECTION_ID() este unic între clienții conectați și coincide cu Id din SHOW PROCESSLIST. USER() este numele trimis de client; CURRENT_USER() este contul cu care serverul a autentificat și care decide privilegiile. În exemplul manualului, USER() este davida@localhost, CURRENT_USER() este @localhost, iar SELECT * FROM mysql.user dă ERROR 1044. @@datadir este calea directorului de date de pe server. PDF-ul de curs arată același drum, fără o propoziție: scrii SQL în Workbench, serverul calculează, rezultatul se întoarce pe ecran
BOOK EXAMPLE:
    sessionOnServer
REPO DEMO:
    src/main/java/_6_Client_Server_Workbench/Client_Server_Model.java
BREAK LAB:
    SELECT * FROM mysql.user când CURRENT_USER() este @localhost: ERROR 1044 Access denied for user ''@'localhost' to database 'mysql'
TRADE-OFFS:
    dacă lipsește sesiunea, treci la conexiune; dacă lipsește contul, la CREATE USER; dacă lipsește schema, la CREATE DATABASE. Grila rămâne în client
ANTI-PATTERNS:
    autorizezi cu USER() sau crezi că fișierul salvat în Workbench este deja baza din data directory
STAFF QUESTIONS:
    sessionOnServer, resultLeavesSchemaUntouched, serverThreadNotWorkbenchGrid, breakUserIsNotCurrentUser, datadirOrSwitchToCreateDatabase
NOVEL TRANSFER:
    la recepția unei clinici, ecranul este clientul; programările stau pe mysqld. Închiderea ecranului nu șterge o programare deja confirmată de server
SPACED REVIEW:
    o zi: cine produce CONNECTION_ID(); șapte zile: USER() față de CURRENT_USER() și ERROR 1044; treizeci de zile: când rămâi la această propoziție și când treci la conexiune, CREATE USER sau CREATE DATABASE

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE sesiunea există și vrei firul plus contul care autorizează -> Client_Server_Model.sessionOnServer -> SELECT CONNECTION_ID() AS session_id, USER() AS name_from_client, CURRENT_USER() AS account_on_server -> nu creează schema și nu deschide conexiunea
    2. Eșecul cel mai mic
           -> DECIDE nu confunda numele trimis de client cu contul autentificat -> Client_Server_Model.breakUserIsNotCurrentUser -> SELECT * FROM mysql.user -> ERROR 1044 când CURRENT_USER() este @localhost; propoziția nu repară contul
    3. Granița
           -> mysqld execută și ține data directory; click-ul, editorul și grila din Workbench sunt clientul. Connector/J este alt client, nu serverul
    4. Alternativa
           -> sesiune lipsă: conexiunea, host și port. Cont lipsă: CREATE USER. Schemă lipsă: CREATE DATABASE. Această propoziție doar citește firul, contul și calea
    5. Anti-patternul
           -> decizie de acces pe USER(), sau SQL lăsat în fișierul din Workbench și netrimis. Privilegiile urmează CURRENT_USER(). Data directory nu primește propoziția netrimisă
    6. Dovada
           -> Client_Server_Model.sessionOnServer ține propoziția. Client_Server_Model.breakUserIsNotCurrentUser ține SELECT * FROM mysql.user, eșecul din manual
    7. Interviu
           -> serverul execută pe firul conexiunii și întoarce un rând; SELECT CONNECTION_ID(), USER(), CURRENT_USER(); închiderea Workbench nu mută tabelele înapoi în client

1. Ce problemă rezolvă mecanismul
Enunț: Unde se execută SELECT CONNECTION_ID(), USER(), CURRENT_USER() pe care îl tastezi în Workbench, și ce rămâne în tabelă?
Capcană: Grila din Workbench este baza de date, deci închiderea ferestrei anulează ce a calculat serverul.
Cheie: mysqld execută pe firul conexiunii și întoarce un rând de rezultat. Rândurile din data directory nu se mută în client. CONNECTION_ID() identifică firul, CURRENT_USER() este contul care autorizează.
Demonstrație: sessionOnServer

2. Ce se întâmplă cu rândurile, schema, indexul sau tranzacția
Enunț: După SELECT CONNECTION_ID(), USER(), CURRENT_USER(), ce s-a schimbat în data directory și când vezi rezultatul?
Capcană: Orice propoziție trimisă din client scrie un rând sau deschide o tranzacție care ține lacăt până închizi Workbench.
Cheie: Propoziția nu inserează, nu schimbă schema și nu cere un index. Rezultatul se vede la sfârșitul ei, în client. Firul rămâne până se termină conexiunea, apoi poate intra în thread cache.
Demonstrație: resultLeavesSchemaUntouched

3. De ce nu este mecanismul vecin
Enunț: De ce SELECT CONNECTION_ID() nu este nici dialogul de conexiune, nici CREATE DATABASE?
Capcană: Dacă Workbench este deschis, schema există, iar CONNECTION_ID() creează sesiunea.
Cheie: Host și port deschid firul. CREATE DATABASE scrie schema în data directory. CONNECTION_ID() se citește doar pe un fir deja conectat și nu creează baza.
Demonstrație: serverThreadNotWorkbenchGrid

4. Cum eșuează când este folosit greșit
Enunț: Clientul a cerut davida, iar SELECT * FROM mysql.user eșuează. Ce cont a folosit serverul?
Capcană: USER() este contul cu privilegii, deci accesul refuzat este o problemă de afișare în Workbench.
Cheie: Manualul 9.7 arată USER() = davida@localhost și CURRENT_USER() = @localhost, apoi ERROR 1044 Access denied for user ''@'localhost' to database 'mysql'. Privilegiile urmează contul autentificat.
Demonstrație: breakUserIsNotCurrentUser

5. Ce alegi în locul lui și în ce condiție
Enunț: SELECT @@datadir, CONNECTION_ID() îți arată calea și firul, dar baza de care ai nevoie nu există. Ce schimbi?
Capcană: Repeți aceeași interogare până apare schema, sau salvezi scriptul pe discul clientului.
Cheie: Rămâi la această propoziție când sesiunea există și vrei firul, contul sau calea. Pentru o schemă nouă treci la CREATE DATABASE, pentru un cont nou la CREATE USER, pentru o sesiune lipsă la conexiune.
Demonstrație: datadirOrSwitchToCreateDatabase
*/

/*
🟢 Începător
    Ce este            despărțirea dintre clientul care trimite SQL și mysqld, care execută și ține data directory
    De ce e nevoie     fără ea, Workbench pare locul în care trăiesc rândurile
🟡 Producție
    Cum se scrie       SELECT CONNECTION_ID() AS session_id, USER() AS name_from_client, CURRENT_USER() AS account_on_server
🔴 Staff
    Unde               firul de conexiune; Id din SHOW PROCESSLIST; privilegiul decis de CURRENT_USER()
Pași                   1. Citești firul și contul. 2. Vezi că schema nu s-a scris. 3. Separi clientul de server. 4. Arăți ERROR 1044. 5. Alegi copilul doar când lipsește sesiunea, contul sau schema.
*/
// Începător: clasa ține propozițiile pe care mysqld le execută pentru sesiunea acestui client.
// Producție: Client_Server_Model păstrează SELECT CONNECTION_ID(), USER(), CURRENT_USER(). Fără clasă, propozițiile nu au unde sta gata de trimis.
// Expert: clasa nu înlocuiește dialogul de conexiune și nu copiază CREATE DATABASE. Workbench rămâne clientul care afișează rezultatul.
public class Client_Server_Model {

    /*
    🟢 Începător
        Ce este            propoziția prin care serverul spune ce fir are acest client și cu ce cont l-a autentificat
        De ce e nevoie     fără ea, numele tastat în client pare contul care are drepturile
    🟡 Producție
        Cum se scrie       SELECT CONNECTION_ID() AS session_id, USER() AS name_from_client, CURRENT_USER() AS account_on_server
    🔴 Staff
        Unde               firul conexiunii; USER() poate diferi de CURRENT_USER(); Id din SHOW PROCESSLIST
    Pași                   1. Ține propoziția. 2. O predă mai departe, gata de trimis la mysqld.
    */
    // Începător: metoda pregătește întrebarea către server: ce sesiune am și ce cont m-a primit.
    // Producție: sessionOnServer întoarce SELECT CONNECTION_ID(), USER(), CURRENT_USER(). Fără metodă, propoziția nu stă în cod.
    // Expert: metoda nu deschide host și port și nu rulează CREATE USER. CONNECTION_ID() există pe un fir deja legat; linia nu pornește mysqld.
    public static String sessionOnServer() {
        // Începător: serverul va întoarce un rând cu numărul sesiunii, numele trimis de client și contul ales de el. Tabelele aplicației rămân neschimbate.
        // Producție: SELECT CONNECTION_ID() AS session_id, USER() AS name_from_client, CURRENT_USER() AS account_on_server. Dacă linia dispare, nu mai ai propoziția de trimis.
        // Expert: CONNECTION_ID() este unic între clienții conectați acum. USER() poate rămâne davida@localhost când CURRENT_USER() este @localhost. Linia nu ține lacăt pe mysql.user.
        String sql = "SELECT CONNECTION_ID() AS session_id, USER() AS name_from_client, CURRENT_USER() AS account_on_server";
        // Începător: înapoi pleacă textul propoziției, nu rândurile din data directory.
        // Producție: return predă SELECT-ul. Fără return, textul nu ajunge la clientul care trebuie să-l trimită.
        // Expert: return nu este rezultatul mysqld. Rândul apare după ce un client trimite textul pe fir. Connector/J nu calculează CONNECTION_ID() în JVM.
        return sql;
        // Începător: pregătirea propoziției s-a terminat. Serverul încă nu a văzut-o din această metodă.
        // Producție: acolada închide sessionOnServer. Închiderea metodei nu retrage un rezultat pe care mysqld l-a trimis deja.
        // Expert: acolada nu deconectează clientul. Firul se întoarce în thread cache doar când conexiunea se termină și cache-ul nu este plin.
    }

    /*
    🟢 Începător
        Ce este            aceeași citire de sesiune, ca să vezi că data directory nu primește un rând nou
        De ce e nevoie     fără ea, orice text trimis din Workbench pare o scriere în tabelă
    🟡 Producție
        Cum se scrie       SELECT CONNECTION_ID() AS session_id, USER() AS name_from_client, CURRENT_USER() AS account_on_server
    🔴 Staff
        Unde               rezultat la sfârșitul propoziției; niciun index și niciun lacăt de rând pe tabelele aplicației
    Pași                   1. Ține propoziția de citire. 2. O predă mai departe.
    */
    // Începător: metoda ține o citire. După ea, schemele din data directory sunt aceleași.
    // Producție: resultLeavesSchemaUntouched întoarce același SELECT de sesiune. Fără ea, nu ai în cod propoziția care nu scrie rânduri.
    // Expert: efectul se vede la sfârșitul propoziției, nu la COMMIT. Nu este CREATE DATABASE și nu deschide o tranzacție care să țină lacăt după ce rezultatul a plecat.
    public static String resultLeavesSchemaUntouched() {
        // Începător: serverul calculează un rând de rezultat și nu adaugă o bază și nici un rând într-o tabelă de programări sau de utilizatori ai aplicației.
        // Producție: SELECT CONNECTION_ID() AS session_id, USER() AS name_from_client, CURRENT_USER() AS account_on_server. Dacă linia dispare, dispare dovada că această citire nu scrie schema.
        // Expert: propoziția nu folosește un index secundar și nu ține lacăt de rând. pseudo_thread_id poate schimba valoarea afișată de CONNECTION_ID() fără să schimbe data directory.
        String sql = "SELECT CONNECTION_ID() AS session_id, USER() AS name_from_client, CURRENT_USER() AS account_on_server";
        // Începător: mai departe pleacă propoziția de citire, nu o confirmare că s-a scris ceva.
        // Producție: return predă textul. Fără return, nu poți arăta că propoziția de sesiune lasă schema nemodificată.
        // Expert: return nu face COMMIT și nu golește thread cache. Firul rămâne al conexiunii până clientul pleacă.
        return sql;
        // Începător: citirea pregătită s-a încheiat. Directorul de date nu s-a schimbat aici.
        // Producție: acolada închide resultLeavesSchemaUntouched. Închiderea nu șterge un rezultat deja primit de client.
        // Expert: acolada nu este ROLLBACK. Nu exista o scriere de rând de anulat, pentru că propoziția doar citește firul și contul.
    }

    /*
    🟢 Începător
        Ce este            propoziția care arată că numărul sesiunii este al firului de pe server, nu al grilei
        De ce e nevoie     fără ea, tab-ul din Workbench pare creatorul lui CONNECTION_ID()
    🟡 Producție
        Cum se scrie       SELECT CONNECTION_ID() AS session_id
    🔴 Staff
        Unde               valoarea coincide cu Id din SHOW PROCESSLIST și cu PROCESSLIST_ID din performance_schema.threads
    Pași                   1. Ține propoziția scurtă. 2. O predă mai departe.
    */
    // Începător: metoda pregătește întrebarea „ce număr de sesiune mi-a dat serverul”.
    // Producție: serverThreadNotWorkbenchGrid întoarce SELECT CONNECTION_ID() AS session_id. Fără metodă, rămâne doar ideea că grila calculează numărul.
    // Expert: dialogul de conexiune deschide firul; această propoziție îl citește după. CREATE DATABASE nu este aici: numărul nu creează o schemă în @@datadir.
    public static String serverThreadNotWorkbenchGrid() {
        // Începător: serverul întoarce numărul firului acestui client. Doi clienți conectați acum nu primesc același număr.
        // Producție: SELECT CONNECTION_ID() AS session_id. Dacă linia dispare, nu mai ai propoziția care separă firul de grila Workbench.
        // Expert: valoarea este de același fel cu coloana Id din SHOW PROCESSLIST. Un fișier .sql salvat local nu are acest număr până nu ajunge la mysqld.
        String sql = "SELECT CONNECTION_ID() AS session_id";
        // Începător: pleacă întrebarea către server, nu o cifră desenată în editor.
        // Producție: return predă SELECT CONNECTION_ID(). Fără return, propoziția nu poate fi trimisă mai departe.
        // Expert: return nu deschide socket-ul și nu crește max_connections. Dacă serverul refuză conexiunea, Connection_errors_max_connections crește și această propoziție nu are fir.
        return sql;
        // Începător: întrebarea despre numărul sesiunii este gata. Baza din data directory nu s-a creat.
        // Producție: acolada închide serverThreadNotWorkbenchGrid. Închiderea metodei nu închide tab-ul și nu oprește mysqld.
        // Expert: acolada nu eliberează firul. Eliberarea se întâmplă când conexiunea se termină, iar firul poate intra în cache dacă thread_cache_size are loc.
    }

    /*
    🟢 Începător
        Ce este            propoziția care pică când serverul a autentificat alt cont decât numele trimis de client
        De ce e nevoie     fără ea, accesul refuzat pare o eroare de afișare în Workbench
    🟡 Producție
        Cum se scrie       SELECT * FROM mysql.user
    🔴 Staff
        Unde               ERROR 1044 pentru ''@'localhost' când CURRENT_USER() este contul anonim
    Pași                   1. Ține propoziția care cere mysql.user. 2. O predă mai departe.
    */
    // Începător: metoda pregătește citirea conturilor din server, citire pe care serverul o poate refuza.
    // Producție: breakUserIsNotCurrentUser întoarce SELECT * FROM mysql.user. Fără metodă, eșecul rămâne doar o poveste.
    // Expert: eșecul nu este lipsa unui index. Este contul autentificat. USER() poate spune davida@localhost în timp ce privilegiile sunt ale lui @localhost.
    public static String breakUserIsNotCurrentUser() {
        // Începător: serverul caută rândurile de conturi. Dacă te-a autentificat anonim, nu primești tabela și rândurile ei rămân necitite de tine.
        // Producție: SELECT * FROM mysql.user. Dacă linia dispare, nu mai ai SQL-ul care în manual dă ERROR 1044.
        // Expert: ERROR 1044 Access denied for user ''@'localhost' to database 'mysql' urmează când CURRENT_USER() este @localhost. Propoziția nu creează contul davida și nu ține loc de CREATE USER.
        String sql = "SELECT * FROM mysql.user";
        // Începător: mai departe pleacă cererea de citire a conturilor, nu parola și nu un cont nou.
        // Producție: return predă SELECT * FROM mysql.user. Fără return, nu poți trimite propoziția care expune refuzul.
        // Expert: return nu acordă CONNECTION_ADMIN și nu ocolește max_connections. Refuzul de privilegiu apare doar pe un fir pe care serverul a autentificat deja.
        return sql;
        // Începător: cererea este pregătită. Niciun rând din mysql.user nu a fost modificat de această metodă.
        // Producție: acolada închide breakUserIsNotCurrentUser. Închiderea nu transformă ERROR 1044 într-un rezultat de rânduri.
        // Expert: acolada nu face GRANT. Dreptul rămâne la contul CURRENT_USER(), iar numele din client rămâne doar USER().
    }

    /*
    🟢 Începător
        Ce este            propoziția care arată unde ține serverul bazele și ce fir are clientul, fără să creeze o bază
        De ce e nevoie     fără ea, repeți citirea sesiunii și aștepți să apară schema
    🟡 Producție
        Cum se scrie       SELECT @@datadir AS data_directory, CONNECTION_ID() AS session_id
    🔴 Staff
        Unde               @@datadir este calea de pe server; o cale relativă se rezolvă în directorul curent al serverului
    Pași                   1. Ține propoziția. 2. O predă mai departe.
    */
    // Începător: metoda pregătește întrebarea „unde sunt bazele pe server și ce sesiune am”.
    // Producție: datadirOrSwitchToCreateDatabase întoarce SELECT @@datadir, CONNECTION_ID(). Fără metodă, nu ai propoziția care se oprește înainte de CREATE DATABASE.
    // Expert: dacă schema lipsește, următorul pas este CREATE DATABASE din Creating_A_Database, nu încă un CONNECTION_ID(). Dacă lipsește contul, pasul este CREATE USER, nu această citire.
    public static String datadirOrSwitchToCreateDatabase() {
        // Începător: serverul spune folderul în care stau bazele și numărul sesiunii. Folderul de pe laptop în care ai salvat scriptul nu este acest răspuns.
        // Producție: SELECT @@datadir AS data_directory, CONNECTION_ID() AS session_id. Dacă linia dispare, nu mai ai citirea care lasă schema necreată.
        // Expert: @@datadir nu alocă un index și nu deschide conexiunea. O cale relativă se rezolvă față de directorul curent al mysqld. CREATE DATABASE este alt mecanism, folosit doar când schema chiar lipsește.
        String sql = "SELECT @@datadir AS data_directory, CONNECTION_ID() AS session_id";
        // Începător: pleacă întrebarea despre locul bazelor, nu comanda care creează o bază.
        // Producție: return predă SELECT @@datadir, CONNECTION_ID(). Fără return, nu poți arăta limita propoziției.
        // Expert: return nu scrie în data directory. Pentru un cont nou rămâi la CREATE USER; pentru o sesiune inexistentă rămâi la conexiune, înainte de orice CONNECTION_ID().
        return sql;
        // Începător: întrebarea despre locul bazelor este gata. Nicio bază nouă nu a apărut din metodă.
        // Producție: acolada închide datadirOrSwitchToCreateDatabase. Închiderea nu salvează scriptul în @@datadir.
        // Expert: acolada nu comută pe CREATE DATABASE. Comutarea este decizia de după ce ai văzut că schema lipsește din directorul serverului.
    }
    // Începător: propozițiile s-au oprit. Data directory nu a primit o bază nouă din această clasă.
    // Producție: acolada închide Client_Server_Model. Închiderea nu trimite SELECT-urile și nu retrage un rezultat deja plecat de la mysqld.
    // Expert: închiderea clasei nu deconectează firul și nu repară ERROR 1044. Contul care autorizează rămâne CURRENT_USER().
}
