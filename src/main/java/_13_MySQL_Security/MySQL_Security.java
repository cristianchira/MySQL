package _13_MySQL_Security;

//MySQL Security
//  https://dev.mysql.com/doc/refman/9.7/en/security.html
// HISTORICAL: https://dev.mysql.com/doc/refman/8.0/en/security.html
// Regula citită în tur: MySQL 9.7 Reference Manual, capitolul 8 Security.


/*                                         Security


    1. General Security Issues

    2. Access Control and Account Management

    3. Using Encrypted Connections

    4. Security Components and Plugins

    5. MySQL Enterprise Data Masking
    HISTORICAL: MySQL Enterprise Data Masking and De-Identification
    Regula citită în tur: MySQL 9.7 Reference Manual, secțiunea 8.5 se numește MySQL Enterprise Data Masking.

    6. MySQL Enterprise Encryption

    7. SELinux

    8. FIPS Support





 */

/*
TOPIC:
    mysqld verifică fiecare propoziție pe copia din memorie a tabelelor de grant pentru contul user@host; SHOW GRANTS citește privilegiile scrise pentru acel cont și nu schimbă rândurile aplicației.
FAMILIE:
    Security
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: YES). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Același nume, joe, vine de la două hosturi și trebuie să vezi ce poate rula fiecare. După SHOW GRANTS, tabelele de aplicație rămân; rămâne o listă de privilegii, iar un rol din listă nu este încă activ.
MECHANISM:
    La pornire, mysqld citește tabelele de grant din schema mysql în memorie. Etapa 2, la fiecare cerere, decide pe acea copie. SHOW GRANTS întoarce lista la sfârșitul propoziției. GRANT reîncarcă memoria imediat. UPDATE direct pe mysql.user nu schimbă verificarea până la FLUSH PRIVILEGES sau repornire, iar FLUSH PRIVILEGES este depreciat în 9.7.
LOOKALIKES:
    GRANT. Se separă așa: GRANT scrie privilegiul și, ca propoziție de administrare de cont, reîncarcă tabelele de grant și face commit implicit. SHOW GRANTS doar citește contul numit, user și host. Un GRANT către 'joe'@'%' nu este lista lui 'joe'@'office.example.com'.
BOOK THEORY:
    În manualul 9.7, capitolul 8 desparte controlul accesului de conexiunea criptată, de pluginuri, de mascarea enterprise, de SELinux și de FIPS. Secțiunea 8.2 leagă privilegiile de contul format din nume și host: 'joe'@'office.example.com' și 'joe'@'home.example.com' pot avea seturi diferite, citite cu SHOW GRANTS. Serverul ține privilegiile în tabelele de grant ale schemei mysql și decide pe copia din memorie. Etapa 1 acceptă sau respinge conexiunea. Etapa 2 verifică fiecare propoziție în user, global_grants, db, tables_priv, columns_priv sau procs_priv. Un privilegiu global care permite operația o acordă, în afară de un partial revoke. Privilegiile de administrare se caută doar în user și global_grants. Un privilegiu acordat contului este în vigoare; un rol din SHOW GRANTS nu este, până la rolul implicit sau SET ROLE. Cu partial_revokes oprit, REVOKE pe o schemă după un grant global dă ERROR 1141. Cu variabila pornită, SHOW GRANTS arată restricția ca linie REVOKE. GRANT, REVOKE, CREATE USER și celelalte propoziții care modifică tabele din mysql reîncarcă memoria imediat și fac commit implicit. INSERT, UPDATE sau DELETE direct în tabelele de grant nu schimbă verificarea până la FLUSH PRIVILEGES sau repornire. După reîncărcare, privilegiul de tabelă sau de coloană intră la cererea următoare, cel de bază la următorul USE, privilegiul global static și parola doar la o conexiune nouă, privilegiul global dinamic imediat. FLUSH, inclusiv FLUSH PRIVILEGES, este propoziție administrativă și face commit implicit. În 9.7, FLUSH PRIVILEGES este depreciat, cere RELOAD sau privilegiul depreciat FLUSH_PRIVILEGES, și nu este necesar după GRANT sau REVOKE. Cu --skip-grant-tables nu există controlul accesului până la FLUSH PRIVILEGES. Nu poți refuza explicit un utilizator, nu legi parola de o tabelă, și nu dai CREATE sau DROP pe tabelele unei baze fără CREATE sau DROP pe baza însăși. Scriptul de cărți nu a deschis niciun PDF: NO_CANONICAL_PDF, NO_BOOKS_DIR, NO_LOCAL_PDF.
BOOK EXAMPLE:
    MySQL_Security.showGrantsAtOffice()
REPO DEMO:
    src/main/java/_13_MySQL_Security/MySQL_Security.java
BREAK LAB:
    UPDATE mysql.user SET Select_priv = 'Y' WHERE User = 'jeffrey'; scrie rândul de pe disc și lasă copia din memorie neschimbată. Următoarea cerere a sesiunii tot folosește privilegiul vechi. Chiar după reîncărcare, Select_priv este privilegiu global static și nu intră în sesiunea deja deschisă.
TRADE-OFFS:
    Rămâi la SHOW GRANTS când vrei lista pentru un user@host. Treci la GRANT când lista nu conține privilegiul și trebuie să îl scrii. Treci la partial revoke când privilegiul global trebuie să cadă pe o singură schemă. Regula pe un rând de salariu rămâne în aplicație: privilegiul nu filtrează rânduri.
ANTI-PATTERNS:
    UPDATE mysql.user SET Select_priv = 'Y' WHERE User = 'jeffrey'; lasă etapa 2 pe copia veche. Un privilegiu global în tabela user dă operația pe orice bază, înainte de tables_priv.
STAFF QUESTIONS:
    MySQLSecurity_Staff_Interview.recognizeShowGrants(), effectOnGrantMemory(), separateFromGrant(), failDirectUpdate(), chooseGrantOrApplication()
NOVEL TRANSFER:
    La o clinică, casierul de la recepție și același nume de acasă sunt două conturi. SHOW GRANTS pentru fiecare host spune cine poate citi payroll. Rândul unui pacient nu se taie cu un privilegiu de tabelă.
SPACED REVIEW:
    O zi: cine citește SHOW GRANTS și că joe@office nu este joe@home. Șapte zile: UPDATE pe mysql.user față de memoria etapei 2, și de ce FLUSH PRIVILEGES este depreciat. Treizeci de zile: când rămâi la SHOW GRANTS, când treci la GRANT sau la partial revoke, și când regula rămâne în aplicație.
BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei privilegiile scrise pentru joe de la birou -> MySQL_Security.showGrantsAtOffice() -> SHOW GRANTS FOR 'joe'@'office.example.com'; -> nu garantează că un rol din listă este activ și nu scrie privilegiul
    2. Eșecul cel mai mic
           -> DECIDE nu scrie direct în mysql.user -> MySQL_Security.directGrantTableUpdate() -> UPDATE mysql.user SET Select_priv = 'Y' WHERE User = 'jeffrey'; -> nu schimbă verificarea din memorie și nu dă Select_priv sesiunii deja deschise
    3. Granița
           -> mecanismul MySQL este verificarea etapei 2 pe copia din memorie; Workbench, Connector/J și capitolul TLS doar duc propoziția, nu acordă privilegiul
    4. Alternativa
           -> rămâi la SHOW GRANTS ca să citești user@host; treci la GRANT ca să scrii privilegiul și să reîncarci memoria; partial revoke când un global trebuie să cadă pe o schemă; aplicația când regula este pe un rând
    5. Anti-patternul
           -> UPDATE mysql.user schimbă discul și lasă etapa 2 pe copia veche; un privilegiu global în user se aplică înainte de privilegiul de tabelă
    6. Dovada
           -> MySQL_Security.showGrantsAtOffice() și showGrantsAtHome() țin cele două conturi; directGrantTableUpdate() ține scrierea directă; reloadGrantTables() ține reîncărcarea depreciată
    7. Interviu
           -> SHOW GRANTS citește lista la sfârșitul propoziției, GRANT o scrie și reîncarcă memoria, UPDATE pe mysql.user nu publică privilegiul, iar un rând de salariu nu se filtrează aici
ÎNTREBĂRI:
    1. Enunț: Ce problemă rezolvă SHOW GRANTS FOR 'joe'@'office.example.com'?
       Capcană: „Arată privilegiile oricărui joe și le și activează.”
       Cheie: Citește privilegiile scrise pentru contul joe de la office.example.com. joe de la home.example.com este alt cont. Un rol din listă nu este folosit de etapa 2 până nu este activ.
       Demonstrație: MySQL_Security.showGrantsAtOffice()
    2. Enunț: Ce se întâmplă cu rândurile, schema, indexul sau tranzacția?
       Capcană: „SHOW GRANTS închide tranzacția și rescrie indexul tabelei de salarii.”
       Cheie: La sfârșitul propoziției primești lista. SHOW GRANTS nu este pe lista de commit implicit. Nu schimbă rândurile din payroll și nu atinge indexul lor.
       Demonstrație: MySQL_Security.showGrantsAtOffice()
    3. Enunț: De ce SHOW GRANTS nu este GRANT?
       Capcană: „Ambele publică privilegiul în memoria serverului.”
       Cheie: GRANT scrie privilegiul, reîncarcă tabelele de grant imediat și face commit implicit. SHOW GRANTS citește contul numit, cu user și host, și nu pune un privilegiu nou în memorie.
       Demonstrație: MySQL_Security.showGrantsAtOffice()
    4. Enunț: Cum eșuează când scrii privilegiul direct în mysql.user?
       Capcană: „UPDATE mysql.user SET Select_priv = 'Y' se vede la următorul SELECT al sesiunii deschise.”
       Cheie: Rândul de pe disc se poate schimba. Etapa 2 rămâne pe copia veche până la FLUSH PRIVILEGES sau repornire. Select_priv este privilegiu global static: după reîncărcare intră doar într-o conexiune nouă. FLUSH PRIVILEGES este depreciat în 9.7 și face commit implicit.
       Demonstrație: MySQL_Security.directGrantTableUpdate()
    5. Enunț: Ce alegi în locul lui SHOW GRANTS și în ce condiție?
       Capcană: „Repeți SHOW GRANTS până apare SELECT, sau filtrezi rândul de salariu cu un privilegiu de tabelă.”
       Cheie: Treci la GRANT când privilegiul lipsește și trebuie scris. Treci la partial revoke când un privilegiu global trebuie să cadă pe o schemă și partial_revokes este pornit. Regula pe un singur rând rămâne în aplicație.
       Demonstrație: MySQL_Security.showGrantsAtHome()
*/

/*
🟢 Începător
    Ce este            citirea privilegiilor unui cont user@host, înainte ca propoziția lui să atingă rândurile
    De ce e nevoie     fără ea, joe de la birou și joe de acasă par același drept
🟡 Producție
    Cum se scrie       SHOW GRANTS FOR 'joe'@'office.example.com';
🔴 Staff
    Unde               etapa 2 folosește copia din memorie; SHOW GRANTS nu o reîncarcă și nu activează un rol
Pași                   1. citește contul de la birou
                       2. citește contul de acasă
                       3. arată scrierea directă care nu publică privilegiul
                       4. arată reîncărcarea depreciată
*/
// Începător: clasa ține propozițiile prin care vezi ce poate rula un cont, fără să muți rândurile aplicației.
// Producție: metodele întorc SHOW GRANTS, UPDATE pe mysql.user și FLUSH PRIVILEGES. Dacă clasa dispare, propozițiile nu mai stau gata de trimis.
// Expert: clasa nu copiază GRANT și nu copiază SELECT CONNECTION_ID(), USER(), CURRENT_USER(). Nu garantează o sesiune: root@localhost a primit Access denied în tur.
public class MySQL_Security
// Începător: de aici încep propozițiile de citit privilegii.
// Producție: fără acoladă, SHOW GRANTS pentru cele două hosturi nu are clasă.
// Expert: acolada nu încarcă tabelele de grant în memorie și nu pornește partial_revokes.
{

    /*
    🟢 Începător
        Ce este            lista de privilegii pentru joe care vine de la birou
        De ce e nevoie     fără ea nu știi ce va lăsa etapa 2 să treacă pentru acest host
    🟡 Producție
        Cum se scrie       SHOW GRANTS FOR 'joe'@'office.example.com';
    🔴 Staff
        Unde               rezultatul apare la sfârșitul propoziției; un rol din listă așteaptă SET ROLE sau rolul implicit
    Pași                   1. ține propoziția
                           2. o predă mai departe
    */
    // Începător: metoda păstrează citirea drepturilor lui joe de la birou. Rândurile din tabelele aplicației rămân.
    // Producție: textul este SHOW GRANTS FOR 'joe'@'office.example.com'; . Dacă metoda dispare, nu mai ai propoziția acestui cont.
    // Expert: metoda nu este GRANT și nu reîncarcă memoria. Nu garantează că un rol din rezultat este folosit la etapa 2.
    public static String showGrantsAtOffice()
    // Începător: de aici începe locul în care stă citirea drepturilor de la birou.
    // Producție: fără această acoladă, SHOW GRANTS pentru office.example.com nu are metodă.
    // Expert: acolada nu scrie în mysql.user și nu face commit implicit.
    {
        // Începător: rămâne textul pe care serverul îl poate executa ca să spună ce drepturi are joe de la birou.
        // Producție: SHOW GRANTS FOR 'joe'@'office.example.com'; nu schimbă rânduri de aplicație. Fără această linie, metoda nu mai are ce trimite.
        // Expert: același nume la home.example.com este alt cont. Linia nu activează un rol și nu înlocuiește GRANT, care scrie privilegiul și reîncarcă memoria.
        String sql = "SHOW GRANTS FOR 'joe'@'office.example.com';";
        // Începător: înapoi pleacă cererea, nu lista deja citită de pe server.
        // Producție: return predă textul. În tur, root@localhost a primit Access denied, deci lista nu este produsă aici.
        // Expert: return nu face FLUSH PRIVILEGES și nu aplică un privilegiu global static unei sesiuni deja deschise.
        return sql;
    // Începător: aici se termină citirea drepturilor de la birou.
    // Producție: fără închidere, clasa nu compilează.
    // Expert: închiderea nu scoate o linie REVOKE de partial revoke din rezultat.
    }

    /*
    🟢 Începător
        Ce este            lista de privilegii pentru același nume, venit de acasă
        De ce e nevoie     ca să nu crezi că hostul de la birou acoperă și casa
    🟡 Producție
        Cum se scrie       SHOW GRANTS FOR 'joe'@'home.example.com';
    🔴 Staff
        Unde               al doilea cont, nu o copie a listei de la birou
    Pași                   1. ține propoziția pentru home
                           2. o predă mai departe
    */
    // Începător: metoda păstrează citirea drepturilor lui joe de acasă. Tabelele aplicației rămân.
    // Producție: textul este SHOW GRANTS FOR 'joe'@'home.example.com'; . Dacă metoda dispare, perechea de hosturi nu mai este în repo.
    // Expert: '%' nu înlocuiește această citire. Contul potrivit la conectare poate fi un rând mai specific, iar lista de aici rămâne a lui home.example.com.
    public static String showGrantsAtHome()
    // Începător: de aici începe citirea celuilalt cont.
    // Producție: fără acoladă, SHOW GRANTS pentru home.example.com nu are metodă.
    // Expert: acolada nu acordă privilegiul de la birou și nu deschide o conexiune.
    {
        // Începător: serverul poate întoarce alte drepturi decât la birou, pentru că hostul este altul.
        // Producție: SHOW GRANTS FOR 'joe'@'home.example.com'; . Fără linie, rămâne doar contul de la birou.
        // Expert: propoziția nu este GRANT către 'joe'@'%' și nu este verificarea de conectare. Nu promite că sesiunea curentă este acest cont.
        String sql = "SHOW GRANTS FOR 'joe'@'home.example.com';";
        // Începător: înapoi pleacă cererea pentru casa, nu pentru birou.
        // Producție: return predă textul. Serverul din tur nu a executat-o: Access denied pentru root.
        // Expert: return nu face USE și nu aplică un privilegiu de bază rămas în așteptarea unui USE.
        return sql;
    // Începător: citirea de acasă se termină aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu unește cele două conturi într-unul singur.
    }

    /*
    🟢 Începător
        Ce este            scrierea directă în tabela de conturi, care nu publică dreptul
        De ce e nevoie     ca să vezi de ce următorul SELECT tot poate fi refuzat
    🟡 Producție
        Cum se scrie       UPDATE mysql.user SET Select_priv = 'Y' WHERE User = 'jeffrey';
    🔴 Staff
        Unde               discul se poate schimba; etapa 2 rămâne pe copia din memorie
    Pași                   1. ține UPDATE-ul
                           2. îl predă ca eșec, nu ca exemplu de urmat
    */
    // Începător: metoda păstrează propoziția care umblă la dreptul global direct în tabela de conturi.
    // Producție: textul este UPDATE mysql.user SET Select_priv = 'Y' WHERE User = 'jeffrey'; . Dacă dispare, eșecul nu mai are propoziție.
    // Expert: nu este GRANT. În 9.7 scrierea directă în tabelele de grant este calea pe care manualul o tratează ca depreciată. Nu dă Select_priv unei sesiuni deja deschise.
    public static String directGrantTableUpdate()
    // Începător: de aici începe propoziția care nu publică dreptul.
    // Producție: fără acoladă, UPDATE pe mysql.user nu are metodă.
    // Expert: acolada nu reîncarcă memoria și nu cere privilegiul RELOAD.
    {
        // Începător: rândul lui jeffrey din tabela de conturi poate fi marcat ca având drept de citire, iar verificarea rămâne cea veche.
        // Producție: UPDATE mysql.user SET Select_priv = 'Y' WHERE User = 'jeffrey'; . Fără linie, nu ai scrierea care ocolește GRANT.
        // Expert: Select_priv este privilegiu global static. După o reîncărcare ulterioară intră la o conexiune nouă, nu în sesiunea deja legată. Linia nu filtrează un rând de salariu.
        String sql = "UPDATE mysql.user SET Select_priv = 'Y' WHERE User = 'jeffrey';";
        // Începător: înapoi pleacă propoziția periculoasă, nu un drept deja activ.
        // Producție: return predă textul. Nu se trimite în tur: root a primit Access denied, iar propoziția nu este un exemplu de rulat.
        // Expert: return nu face FLUSH PRIVILEGES și nu transformă scrierea directă în GRANT.
        return sql;
    // Începător: propoziția periculoasă se termină aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu pune înapoi valoarea veche din mysql.user.
    }

    /*
    🟢 Începător
        Ce este            reîncărcarea drepturilor ținute în memorie
        De ce e nevoie     doar după o scriere directă, sau când serverul a pornit fără verificarea drepturilor
    🟡 Producție
        Cum se scrie       FLUSH PRIVILEGES;
    🔴 Staff
        Unde               face commit implicit, este depreciat în 9.7 și nu acordă un privilegiu nou
    Pași                   1. ține FLUSH PRIVILEGES
                           2. îl predă ca reîncărcare, nu ca GRANT
    */
    // Începător: metoda păstrează propoziția care pune din nou în memorie drepturile de pe disc.
    // Producție: textul este FLUSH PRIVILEGES; . Dacă dispare, nu mai ai reîncărcarea de după un UPDATE direct.
    // Expert: după GRANT nu este necesară. În 9.7 este depreciată și cere RELOAD sau FLUSH_PRIVILEGES. Face commit implicit, ca orice FLUSH.
    public static String reloadGrantTables()
    // Începător: de aici începe reîncărcarea.
    // Producție: fără acoladă, FLUSH PRIVILEGES nu are metodă.
    // Expert: acolada nu pornește serverul cu --skip-grant-tables.
    {
        // Începător: serverul recitește drepturile. Nu adaugă un drept nou pe care nu l-ai scris.
        // Producție: FLUSH PRIVILEGES; . Fără linie, nu ai propoziția care reîncarcă memoria.
        // Expert: privilegiul de tabelă intră la cererea următoare, cel de bază la USE, cel global static la o conexiune nouă. Cu --skip-grant-tables, abia această propoziție pornește verificarea. Nu înlocuiește GRANT.
        String sql = "FLUSH PRIVILEGES;";
        // Începător: înapoi pleacă reîncărcarea, nu o listă de privilegii.
        // Producție: return predă textul. Nu se execută în tur. Propoziția închide tranzacția deschisă, pentru că FLUSH face commit implicit.
        // Expert: return nu eliberează el cache-ul caching_sha2_password; asta o face serverul doar dacă primește propoziția. Nu scoate deprecierea din 9.7.
        return sql;
    // Începător: reîncărcarea se termină aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu revoce un partial revoke și nu închide sesiunea.
    }
// Începător: clasa de securitate se termină la propozițiile de mai sus.
// Producție: fără închidere, fișierul nu compilează.
// Expert: închiderea nu șterge cuprinsul capitolului 8 și nu mută GRANT în acest folder.
}
