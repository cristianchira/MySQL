package _0_DB_ADMINISTRATION;

// https://dev.mysql.com/doc/refman/8.0/en/sql-server-administration-statements.html


/*                                         Database Administration Statements


    1. Account Management Statements
                  *  https://dev.mysql.com/doc/refman/8.0/en/account-management-statements.html
                  *  Account Management Statements
                                 1. ALTER USER Statement
                                              - https://dev.mysql.com/doc/refman/8.0/en/alter-user.html
                                 2. CREATE ROLE Statement
                                              - https://dev.mysql.com/doc/refman/8.0/en/create-role.html
                                 3. CREATE USER Statement
                                              - https://dev.mysql.com/doc/refman/8.0/en/create-user.html
                                 4. DROP ROLE Statement
                                               - https://dev.mysql.com/doc/refman/8.0/en/drop-role.html
                                 5. DROP USER Statement
                                               - https://dev.mysql.com/doc/refman/8.0/en/drop-user.html
                                 6. GRANT Statement
                                               - https://dev.mysql.com/doc/refman/8.0/en/grant.html
                                 7. RENAME USER Statement
                                               - https://dev.mysql.com/doc/refman/8.0/en/rename-user.html
                                 8. REVOKE Statement
                                                - https://dev.mysql.com/doc/refman/8.0/en/revoke.html
                                  9. SET DEFAULT ROLE Statement
                                                 - https://dev.mysql.com/doc/refman/8.0/en/set-default-role.html
                                  10. SET PASSWORD Statement
                                                 - https://dev.mysql.com/doc/refman/8.0/en/set-password.html
                                  11. SET ROLE Statement
                                                 - https://dev.mysql.com/doc/refman/8.0/en/set-role.html

    2. Resource Group Management Statements

                   * https://dev.mysql.com/doc/refman/8.0/en/resource-group-statements.html

    3. Table Maintenance Statements

                   * https://dev.mysql.com/doc/refman/8.0/en/table-maintenance-statements.html

    4. Component, Plugin, and Loadable Function Statements

                   * https://dev.mysql.com/doc/refman/8.0/en/component-statements.html

    5. CLONE Statement

                   * https://dev.mysql.com/doc/refman/8.0/en/clone.html

    6. SET Statements

                   * https://dev.mysql.com/doc/refman/8.0/en/set-statement.html

    7. SHOW Statements

                   * https://dev.mysql.com/doc/refman/8.0/en/show.html

    8. Other Administrative Statements

                   * https://dev.mysql.com/doc/refman/8.0/en/other-administrative-statements.html



 */

/*
TOPIC:
    O propoziție de administrare este executată de mysqld și întoarce sau schimbă starea serverului, nu rândurile unei tabele de aplicație.
FAMILIE:
    Server
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0
INTENT:
    Contul trebuie să vadă ce scheme există pe instanță. După SHOW DATABASES rămân aceleași rânduri în tabelele de aplicație; rezultatul este o listă de nume.
MECHANISM:
    mysqld execută propoziția și, la sfârșitul ei, întoarce nume de directoare. Manualul descrie lista, nu o modificare de rânduri într-o tabelă de aplicație.
LOOKALIKES:
    Comanda clientului mysql, cum este clear (\c) sau status (\s). Se separă așa: SHOW DATABASES ajunge la server; \c este consumată de programul mysql și nu pleacă spre mysqld.
BOOK THEORY:
    În manualul 9.7, mysqld este programul care face lucrul pe instanță. Propozițiile de administrare stau în opt grupuri, iar SHOW este unul dintre ele. SHOW DATABASES listează directoarele din data directory; contul vede doar schemele pentru care are un privilegiu, iar orice privilegiu global static arată toate numele, în afară de cele oprite prin partial revoke. Dacă serverul a pornit cu --skip-show-database, propoziția nu poate fi folosită fără privilegiul SHOW DATABASES. Aceeași informație se citește din information_schema.SCHEMATA, iar programul mysqlshow poate aduce lista din afara sesiunii. Clientul mysql trimite SQL-ul la server și, separat, interpretează el însuși help, clear, delimiter și status. În 9.7, comanda system este oprită până pornești clientul cu --system-command=ON. SET PASSWORD încă atribuie parola, dar ALTER USER este forma preferată, iar parola în clar poate rămâne în logul serverului sau în istoricul clientului. Nu există PDF de curs în folderul acesta.
BOOK EXAMPLE:
    DB_Administration.showDatabases()
REPO DEMO:
    src/main/java/_0_DB_ADMINISTRATION/DB_Administration.java
BREAK LAB:
    \c trimis ca și cum ar fi SQL de administrare. Clientul mysql îl consumă și golește bufferul, fără rezultat de la server. În afara clientului, serverul nu primește o propoziție SHOW și nu listează directoare.
TRADE-OFFS:
    SHOW DATABASES acceptă LIKE și WHERE pe lista de nume. Treci la SELECT * FROM information_schema.SCHEMATA când acele nume trebuie unite cu alte rânduri în aceeași propoziție. mysqlshow când ești în shell, nu într-o sesiune.
ANTI-PATTERNS:
    SET PASSWORD = 'auth_string' pe un cont al cărui istoric de client sau general log păstrează textul. Parola în clar rămâne citibilă. ALTER USER este forma preferată pentru atribuirea parolei.
STAFF QUESTIONS:
    DbAdministration_Staff_Interview.recognizeAdministration(), effectOnSchemaList(), separateFromClientCommand(), failWithoutPrivilege(), chooseSchemata()
NOVEL TRANSFER:
    O consolă de operații care listează schemele unui cont de facturare. Un privilegiu global static face vizibil fiecare nume de director, inclusiv un director care nu este o schemă reală.
SPACED REVIEW:
    O zi: spui cine execută SHOW DATABASES. Șapte zile: separi propoziția de \c pe un prompt nou. Treizeci de zile: alegi SCHEMATA sau SHOW sub un privilegiu global pe care nu vrei să îl acorzi.
BEST PRACTICES:
    1. Cazul corect
           -> DECIDE contul trebuie să vadă schemele de pe instanță -> DB_Administration.showDatabases() -> SHOW DATABASES; -> nu garantează că fiecare nume din listă este o schemă reală și nu arată schemele oprite prin partial revoke
    2. Eșecul cel mai mic
           -> DECIDE nu trimite comanda de client ca SQL -> DB_Administration.clearBufferCommand() -> \c -> nu garantează o listă de scheme și nu anulează o tranzacție deja trimisă
    3. Granița
           -> mecanismul MySQL este propoziția executată de mysqld; programul mysql, Workbench și numele folderului din repo nu sunt propoziția
    4. Alternativa
           -> rămâi la SHOW DATABASES când îți ajunge LIKE sau WHERE pe nume; treci la information_schema.SCHEMATA când numele trebuie unite cu alte rânduri în aceeași propoziție
    5. Anti-patternul
           -> SET PASSWORD cu parola în clar poate scrie secretul în log sau în istoricul clientului
    6. Dovada
           -> DB_Administration.showDatabases() ține propoziția; DB_Administration.schemataNames() ține citirea din SCHEMATA
    7. Interviu
           -> SHOW DATABASES întoarce nume de directoare la sfârșitul propoziției, nu șterge rânduri; \c nu ajunge la server; un privilegiu global static lărgește lista
ÎNTREBĂRI:
    1. Enunț: Ce problemă rezolvă o propoziție de administrare ca SHOW DATABASES?
       Capcană: „SHOW DATABASES este o comandă de Workbench și aduce toate bazele de pe disc, indiferent de cont.”
       Cheie: mysqld execută SHOW DATABASES și întoarce numele de directoare pe care contul are dreptul să le vadă. Fără privilegiul global, lista este mai scurtă.
       Demonstrație: DB_Administration.showDatabases()
    2. Enunț: Ce se întâmplă cu rândurile, schema, indexul sau tranzacția?
       Capcană: „SHOW DATABASES blochează tabelele până la commit și poate șterge o schemă goală.”
       Cheie: La sfârșitul propoziției primești nume de directoare. Manualul nu descrie ștergerea unei scheme și nu leagă lista de un commit.
       Demonstrație: DB_Administration.showDatabases()
    3. Enunț: De ce SHOW DATABASES nu este comanda de client \c?
       Capcană: „Ambele sunt comenzi administrative: \c cere serverului să curețe sesiunea.”
       Cheie: \c este clear, interpretată de programul mysql, care aruncă textul încă netrimis. SHOW DATABASES pleacă la mysqld și întoarce un rezultat.
       Demonstrație: DB_Administration.clearBufferCommand()
    4. Enunț: Cum eșuează când este folosit greșit?
       Capcană: „Dacă serverul a pornit cu --skip-show-database, SHOW DATABASES tot întoarce lista pentru orice cont autentificat.”
       Cheie: Cu --skip-show-database, propoziția nu poate fi folosită dacă lipsește privilegiul SHOW DATABASES. Nu apare lista de directoare.
       Demonstrație: DB_Administration.showDatabases()
    5. Enunț: Ce alegi în locul lui SHOW DATABASES și în ce condiție?
       Capcană: „mysqlshow și SHOW DATABASES sunt aceeași propoziție, deci le pui una în locul celeilalte în JDBC.”
       Cheie: LIKE și WHERE rămân pe SHOW DATABASES. SELECT * FROM information_schema.SCHEMATA intră când numele trebuie unite cu alte rânduri. mysqlshow este un program de shell, nu o propoziție trimisă de driver.
       Demonstrație: DB_Administration.schemataNames()
*/

/*
🟢 Începător
    Ce este            propoziția prin care serverul spune ce scheme vede contul
    De ce e nevoie     fără ea nu știi ce directoare de schemă sunt vizibile, iar o comandă de client nu întreabă serverul
🟡 Producție
    Cum se scrie       SHOW DATABASES;
🔴 Staff
    Unde               rezultatul vine de la mysqld la sfârșitul propoziției; \c nu părăsește clientul
Pași                   1. păstrează SHOW DATABASES
                       2. păstrează citirea din SCHEMATA pentru lista care trebuie unită
                       3. păstrează \c doar ca să arăți ce nu se trimite
*/
public class DB_Administration {

    /*
    🟢 Începător
        Ce este            cererea prin care contul află numele schemelor vizibile
        De ce e nevoie     fără propoziție nu există listă de la server
    🟡 Producție
        Cum se scrie       SHOW DATABASES;
    🔴 Staff
        Unde               mysqld listează directoarele la sfârșitul propoziției; status (\s) nu este această propoziție
    Pași                   1. ține textul SHOW DATABASES;
                           2. îl întoarce fără să deschidă o sesiune
    */
    // Începător: metoda păstrează cererea prin care serverul spune ce scheme vede contul.
    // Producție: textul trimis este SHOW DATABASES; . Dacă metoda dispare, repo-ul nu mai are propoziția de administrare.
    // Expert: metoda nu pornește programul mysql și nu înlocuiește privilegiul SHOW DATABASES. Nu garantează că un director din listă este o schemă reală.
    public static String showDatabases()
    // Începător: de aici începe locul în care stă cererea către server.
    // Producție: fără această acoladă, SHOW DATABASES; nu are o metodă care să o poată preda.
    // Expert: acolada nu deschide data directory și nu transformă lista într-o modificare de rânduri.
    {
        // Începător: rămâne textul pe care serverul îl poate executa ca să întoarcă nume de scheme.
        // Producție: SHOW DATABASES; nu șterge rânduri. Fără această linie, metoda nu mai are ce trimite.
        // Expert: punctul și virgula este delimitatorul clientului mysql. mysqld execută SHOW DATABASES. Linia nu este \c și nu citește information_schema.SCHEMATA.
        String sql = "SHOW DATABASES;";
        // Începător: înapoi pleacă cererea, nu lista deja citită de pe disc.
        // Producție: return predă textul. Rularea pe un mysqld care răspunde nu este în această linie.
        // Expert: return nu face commit și nu golește bufferul clientului. Lista se vede doar după ce un server execută textul.
        return sql;
    // Începător: aici se termină metoda care ține cererea de scheme.
    // Producție: fără închidere, clasa nu compilează și SHOW DATABASES; nu poate fi citită din afară.
    // Expert: închiderea nu închide sesiunea și nu eliberează un privilegiu global.
    }

    /*
    🟢 Începător
        Ce este            aceeași listă de scheme, citită din tabela de metadate
        De ce e nevoie     SHOW DATABASES filtrează numele cu LIKE sau WHERE, dar nu le unește cu alte rânduri în aceeași propoziție
    🟡 Producție
        Cum se scrie       SELECT * FROM information_schema.SCHEMATA;
    🔴 Staff
        Unde               tot mysqld răspunde, dar sursa este tabela SCHEMATA, nu comanda de client mysqlshow
    Pași                   1. ține SELECT-ul pe SCHEMATA
                           2. îl întoarce ca alternativă la SHOW DATABASES
    */
    // Începător: metoda păstrează citirea listei de scheme din tabela de metadate.
    // Producție: propoziția este SELECT * FROM information_schema.SCHEMATA; . Dacă dispare, nu mai ai alternativa care se poate uni cu alt rezultat.
    // Expert: SCHEMATA nu este programul mysqlshow și nu este \s. Partial revoke ascunde numele și aici, la fel ca la SHOW DATABASES; privilegiul global static nu trece peste acea excepție.
    public static String schemataNames()
    // Începător: de aici începe citirea din tabela de metadate.
    // Producție: fără acoladă, SELECT-ul pe SCHEMATA nu stă într-o metodă.
    // Expert: acolada nu acordă privilegiul SHOW DATABASES și nu pornește un al doilea mysqld.
    {
        // Începător: serverul citește metadatele schemelor, nu rândurile unei tabele de aplicație.
        // Producție: SELECT * FROM information_schema.SCHEMATA; . Fără linie, nu rămâne alternativa la SHOW DATABASES.
        // Expert: este tot o propoziție de server, ca SHOW, dar rezultatul poate fi unit cu alte rânduri. Partial revoke ascunde și aici numele oprite; linia nu este un index pe tabelele de aplicație.
        String sql = "SELECT * FROM information_schema.SCHEMATA;";
        // Începător: înapoi pleacă propoziția de metadate, nu numele deja filtrate.
        // Producție: return predă SELECT-ul. Nu deschide el conexiunea.
        // Expert: return nu este mysqlshow și nu schimbă schema implicită a sesiunii, cum face comanda de client use.
        return sql;
    // Începător: aici se termină citirea din SCHEMATA.
    // Producție: fără închidere, alternativa nu compilează.
    // Expert: închiderea nu revocă privilegiul global care lărgește lista de nume.
    }

    /*
    🟢 Începător
        Ce este            textul pe care clientul mysql îl aruncă înainte să întrebe serverul
        De ce e nevoie     ca să vezi ce nu trebuie trimis când voiai lista de scheme
    🟡 Producție
        Cum se scrie       \c
    🔴 Staff
        Unde               programul mysql golește bufferul; mysqld nu primește propoziția și nu întoarce directoare
    Pași                   1. ține \c
                           2. îl întoarce ca exemplu care nu se trimite la server
    */
    // Începător: metoda păstrează comanda prin care clientul anulează textul încă netrimis.
    // Producție: textul este \c . Dacă metoda dispare, nu mai ai contraexemplul lângă SHOW DATABASES.
    // Expert: clear nu este în grupul SHOW și nu listează data directory. Nu garantează că un driver care primește același text îl va trata ca pe clientul mysql.
    public static String clearBufferCommand()
    // Începător: de aici începe contraexemplul care nu cere scheme.
    // Producție: fără acoladă, \c nu poate fi comparat cu SHOW DATABASES; din cod.
    // Expert: acolada nu pornește clientul mysql și nu activează --commands.
    {
        // Începător: acest text golește ce ai scris în client și nu aduce nume de scheme.
        // Producție: \c este clear. Fără linie, dispare cel mai mic exemplu care nu trebuie trimis ca SQL de administrare.
        // Expert: în --binary-mode clientul nu mai acceptă \c. Serverul nu are o propoziție clear în grupul de administrare, deci linia nu ține loc de SHOW DATABASES.
        String command = "\\c";
        // Începător: înapoi pleacă comanda de client, nu o listă de baze.
        // Producție: return predă \c ca să fie arătat, nu ca să fie executat de mysqld.
        // Expert: return nu anulează o tranzacție și nu șterge rânduri. Dacă textul a fost deja trimis cu delimitatorul, \c nu îl mai poate retrage.
        return command;
    // Începător: aici se termină contraexemplul clientului.
    // Producție: fără închidere, clasa nu ține \c lângă propoziția de administrare.
    // Expert: închiderea nu repornește clientul cu --system-command=ON și nu schimbă delimiterul.
    }
    /*
    🟢 Începător
        Ce este            forma care poate lăsa parola în clar în log sau în istoric
        De ce e nevoie     ca să nu o alegi când vrei doar să schimbi parola unui cont
    🟡 Producție
        Cum se scrie       SET PASSWORD = 'auth_string'
    🔴 Staff
        Unde               ALTER USER este forma preferată; placeholderul auth_string nu este o parolă a repo-ului
    Pași                   1. ține propoziția periculoasă
                           2. o întoarce ca să fie recunoscută, nu ca să fie rulată
    */
    // Începător: metoda păstrează propoziția care poate scrie parola în clar.
    // Producție: textul este SET PASSWORD = 'auth_string' . Dacă dispare, anti-patternul nu mai are propoziție în cod.
    // Expert: ALTER USER este forma preferată pentru atribuirea parolei. Linia nu este SHOW DATABASES și nu listează directoare.
    public static String passwordInClearText()
    // Începător: înăuntru stă propoziția periculoasă, nu lista de scheme.
    // Producție: fără acoladă, SET PASSWORD nu poate fi citită din clasă.
    // Expert: acolada nu scrie în general log și nu umple ~/.mysql_history.
    {
        // Începător: acest text poate lăsa secretul vizibil după ce a fost trimis.
        // Producție: SET PASSWORD = 'auth_string' folosește placeholderul din manual, nu o parolă nouă. Fără linie, nu rămâne anti-patternul.
        // Expert: manualul 9.7 preferă ALTER USER user IDENTIFIED BY 'auth_string'. Un plugin extern nu își primește parola din această propoziție.
        String sql = "SET PASSWORD = 'auth_string'";
        // Începător: înapoi pleacă propoziția periculoasă, nu o parolă schimbată.
        // Producție: return predă textul ca să fie recunoscut. Nu îl trimite la un server.
        // Expert: return nu acordă UPDATE pe mysql și nu cere CONNECTION_ADMIN când read_only este pornit.
        return sql;
    // Începător: metoda parolei se încheie aici.
    // Producție: fără închidere, anti-patternul nu compilează.
    // Expert: închiderea nu șterge un rând din mysql.user și nu curăță istoricul clientului.
    }
// Începător: clasa se închide după propoziție, citirea din SCHEMATA, comanda \c și propoziția de parolă.
// Producție: fără această acoladă, SHOW DATABASES; nu rămâne într-o clasă compilabilă.
// Expert: închiderea nu oprește mysqld și nu șterge comentariul vechi cu cele opt grupuri de administrare.
}
