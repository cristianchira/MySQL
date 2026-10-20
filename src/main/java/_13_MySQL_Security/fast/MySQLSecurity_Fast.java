// Începător: pachetul ține lecția scurtă despre cine poate rula propoziția.
// Producție: package _13_MySQL_Security.fast. Fără linie, SHOW GRANTS nu are folderul fast.
// Expert: pachetul nu este schema mysql și nu înlocuiește folderul GRANT. Nu încarcă tabelele de grant în memorie.
package _13_MySQL_Security.fast;

// Începător: lecția citește propoziția deja scrisă în clasa de topic.
// Producție: import _13_MySQL_Security.MySQL_Security. Fără linie, main nu poate lua SHOW GRANTS.
// Expert: importul nu copiază GRANT și nu deschide o sesiune. Access denied din tur rămâne în afara acestei linii.
import _13_MySQL_Security.MySQL_Security;

/*
TOPIC:
    Alegi SHOW GRANTS când vrei privilegiile scrise pentru un user@host. GRANT rămâne propoziția care le scrie.
FAMILIE:
    Security
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: YES). Efectul pe server rămâne NERULAT AICI.
INTENT:
    joe de la birou și joe de acasă nu împart aceeași listă. După propoziție, rândurile de aplicație rămân.
MECHANISM:
    mysqld decide etapa 2 pe copia din memorie a tabelelor de grant. SHOW GRANTS citește lista la sfârșitul propoziției și nu reîncarcă memoria.
LOOKALIKES:
    GRANT. Se separă prin efect: GRANT scrie și reîncarcă, SHOW GRANTS citește contul numit cu user și host.
BOOK THEORY:
    Manualul 9.7, 8.2: privilegiile stau pe contul user@host, iar SHOW GRANTS le arată. GRANT reîncarcă memoria imediat. UPDATE direct pe mysql.user nu schimbă verificarea până la reîncărcare sau repornire. FLUSH PRIVILEGES este depreciat și face commit implicit. Un rol din listă nu este activ până la SET ROLE sau rolul implicit. Nu există PDF de curs: NO_CANONICAL_PDF, NO_BOOKS_DIR, NO_LOCAL_PDF.
BOOK EXAMPLE:
    MySQL_Security.showGrantsAtOffice()
REPO DEMO:
    src/main/java/_13_MySQL_Security/MySQL_Security.java
BREAK LAB:
    UPDATE mysql.user SET Select_priv = 'Y' WHERE User = 'jeffrey'; lasă etapa 2 pe copia veche.
TRADE-OFFS:
    SHOW GRANTS ca să citești. GRANT ca să scrii. Aplicația când regula este pe un rând, nu pe o tabelă.
ANTI-PATTERNS:
    Scrierea directă în mysql.user, sau un privilegiu global în tabela user pentru un cont de aplicație.
STAFF QUESTIONS:
    MySQLSecurity_Staff_Interview.recognizeShowGrants()
NOVEL TRANSFER:
    Casierul de la recepția clinicii și același nume de acasă au două liste. Rândul unui pacient nu se taie aici.
SPACED REVIEW:
    O zi, șapte zile, treizeci de zile: cine citește lista, cine o scrie, când regula rămâne în aplicație.
BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei lista lui joe de la birou -> MySQL_Security.showGrantsAtOffice() -> SHOW GRANTS FOR 'joe'@'office.example.com'; -> nu activează un rol din listă
    2. Eșecul cel mai mic
           -> DECIDE nu scrie direct în mysql.user -> MySQL_Security.directGrantTableUpdate() -> UPDATE mysql.user SET Select_priv = 'Y' WHERE User = 'jeffrey'; -> nu publică privilegiul în memorie
    3. Granița
           -> verificarea este a lui mysqld; click-ul din Workbench și driverul nu acordă privilegiul
    4. Alternativa
           -> GRANT când privilegiul lipsește; SHOW GRANTS când vrei lista; aplicația când filtrul este pe un rând
    5. Anti-patternul
           -> UPDATE pe mysql.user lasă etapa 2 pe copia veche
    6. Dovada
           -> MySQLSecurity_Fast.main tipărește propoziția ținută de MySQL_Security.showGrantsAtOffice()
    7. Interviu
           -> SHOW GRANTS citește user@host la sfârșitul propoziției; GRANT scrie și reîncarcă; un rând de salariu nu se filtrează cu privilegiul de tabelă
*/

/*
Definiția unei verificări de cerere (request verification):
       După conexiune, serverul verifică fiecare propoziție pe copia din memorie a privilegiilor.

Exemplu:
       MySQLSecurity_Terms.explainRequestVerification()
       Fără privilegiul din copie, rândurile aplicației rămân neatinse.

Definiția unui cont MySQL (account):
       Contul este numele împreună cu hostul. Același nume la alt host este alt cont.

Exemplu:
       MySQLSecurity_Terms.explainAccount()
       joe de la birou nu împrumută lista lui joe de acasă.

Definiția unor tabele de grant (grant tables):
       Privilegiile stau în schema mysql. Serverul decide pe copia lor din memorie.

Exemplu:
       MySQLSecurity_Terms.explainGrantTables()
       Un UPDATE direct nu schimbă copia până la reîncărcare sau repornire.

Definiția unei listări de privilegii (SHOW GRANTS):
       SHOW GRANTS citește privilegiile scrise pentru contul numit și nu le acordă.

Exemplu:
       MySQLSecurity_Terms.explainShowGrants()
       Un rol din listă nu este încă activ.

Definiția unei reîncărcări de privilegii (FLUSH PRIVILEGES):
       FLUSH PRIVILEGES recitește tabelele de grant. În 9.7 este depreciat și face commit implicit.

Exemplu:
       MySQLSecurity_Terms.explainFlushPrivileges()
       Nu este necesar după GRANT.
*/

/*
🟢 Începător
    Ce este            lecția care arată propoziția prin care citești drepturile unui cont
    De ce e nevoie     fără ea, GRANT pare singura propoziție de securitate
🟡 Producție
    Cum se scrie       SHOW GRANTS FOR 'joe'@'office.example.com';
🔴 Staff
    Unde               textul pleacă din MySQL_Security; serverul îl execută doar dacă o sesiune îl primește
Pași                   1. citește propoziția deja scrisă
                       2. o tipărește, fără să deschidă o sesiune
*/
// Începător: clasa arată ce propoziție ceri când vrei lista de drepturi, nu propoziția care le scrie.
// Producție: exemplul citește SHOW GRANTS din MySQL_Security. Dacă clasa dispare, lecția scurtă nu mai are intrare.
// Expert: clasa nu predă TLS, SELinux sau mascarea enterprise. Nu garantează un cont care poate rula propoziția: în tur root a primit Access denied.
public final class MySQLSecurity_Fast
// Începător: de aici începe exemplul minim, cel care doar arată propoziția.
// Producție: fără acoladă, main nu poate citi SHOW GRANTS.
// Expert: acolada nu reîncarcă tabelele de grant și nu acordă un privilegiu global.
{

    /*
    🟢 Începător
        Ce este            exemplul care ia propoziția gata scrisă și o arată
        De ce e nevoie     fără main nu vezi textul pe care îl alegi în locul lui GRANT
    🟡 Producție
        Cum se scrie       SHOW GRANTS FOR 'joe'@'office.example.com';
    🔴 Staff
        Unde               println nu este mysqld; lista apare doar după ce un server execută textul
    Pași                   1. citește showGrantsAtOffice
                           2. tipărește textul
    */
    // Începător: de aici pornește exemplul care arată citirea drepturilor de la birou.
    // Producție: main nu trimite UPDATE pe mysql.user. Dacă dispare, nu mai rămâne o intrare care să tipărească SHOW GRANTS.
    // Expert: main nu deschide Workbench și nu face GRANT. Nu garantează că rolul dintr-o listă viitoare este activ.
    public static void main(String[] args)
    // Începător: înăuntru se citește propoziția, nu se acordă un drept.
    // Producție: fără acoladă, apelul către showGrantsAtOffice nu are corp.
    // Expert: acolada nu face commit implicit și nu pornește --skip-grant-tables.
    {
        // Începător: aici este cererea prin care serverul spune ce drepturi are joe de la birou.
        // Producție: valoarea este SHOW GRANTS FOR 'joe'@'office.example.com'; , luată din clasa deja scrisă. Fără linie, exemplul inventează un al doilea text.
        // Expert: apelul nu este GRANT și nu este FLUSH PRIVILEGES. Nu citește el tabelele de grant; doar aduce textul propoziției.
        String sql = MySQL_Security.showGrantsAtOffice();
        // Începător: pe ecran apare cererea, nu lista de privilegii de pe un server.
        // Producție: println scrie SHOW GRANTS FOR 'joe'@'office.example.com'; . Dacă dispare, lecția nu mai arată propoziția.
        // Expert: consola nu este etapa 2. Linia nu dovedește că partial_revokes este pornit și nu închide o tranzacție.
        System.out.println(sql);
    // Începător: exemplul se oprește după ce a arătat cererea.
    // Producție: fără închidere, main nu compilează.
    // Expert: închiderea nu reîncarcă memoria și nu unește joe@office cu joe@home.
    }
// Începător: lecția scurtă se termină la această clasă.
// Producție: fără închidere, fișierul nu compilează și propoziția nu poate fi tipărită.
// Expert: închiderea clasei nu șterge clasa de topic și nu mută GRANT aici.
}
