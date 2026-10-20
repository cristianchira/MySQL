// Începător: pachetul ține întrebările despre cine poate rula propoziția.
// Producție: package _13_MySQL_Security.fast. Fără linie, întrebările nu au folder.
// Expert: pachetul nu execută SHOW GRANTS și nu scrie în mysql.user.
package _13_MySQL_Security.fast;

/*
TOPIC:
    Întrebările de aici aleg între citirea privilegiilor cu SHOW GRANTS și scrierea lor cu GRANT.
FAMILIE:
    Security
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: YES). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Vrei lista lui joe de la birou. După propoziția corectă, rândurile de aplicație rămân, iar lista nu activează un rol.
MECHANISM:
    mysqld verifică etapa 2 pe copia din memorie. SHOW GRANTS citește contul user@host la sfârșitul propoziției.
LOOKALIKES:
    GRANT. SHOW GRANTS citește; GRANT scrie și reîncarcă memoria.
BOOK THEORY:
    Manualul 9.7: SHOW GRANTS pentru 'joe'@'office.example.com' nu este lista lui 'joe'@'home.example.com'. Un privilegiu acordat este în vigoare; un rol nu este, până la SET ROLE sau rolul implicit. UPDATE direct pe mysql.user nu schimbă verificarea până la reîncărcare. FLUSH PRIVILEGES este depreciat și face commit implicit. Nu există PDF de curs în folder.
BOOK EXAMPLE:
    MySQL_Security.showGrantsAtOffice()
REPO DEMO:
    src/main/java/_13_MySQL_Security/MySQL_Security.java
BREAK LAB:
    UPDATE mysql.user SET Select_priv = 'Y' WHERE User = 'jeffrey'; nu publică privilegiul global static în sesiunea deja deschisă.
TRADE-OFFS:
    SHOW GRANTS ca să citești user@host. GRANT ca să scrii. Aplicația când filtrul este pe un rând.
ANTI-PATTERNS:
    Scrierea directă în mysql.user, sau credința că un privilegiu de tabelă alege rândurile.
STAFF QUESTIONS:
    MySQLSecurity_Staff_Interview.recognizeShowGrants()
NOVEL TRANSFER:
    Două recepții ale unei clinici, același nume de casier, două liste de privilegii.
SPACED REVIEW:
    O zi, șapte zile, treizeci de zile.
BEST PRACTICES:
    1. Cazul corect
           -> DECIDE citești drepturile lui joe de la birou -> MySQL_Security.showGrantsAtOffice() -> SHOW GRANTS FOR 'joe'@'office.example.com'; -> nu activează un rol
    2. Eșecul cel mai mic
           -> DECIDE nu scrii direct în mysql.user -> MySQL_Security.directGrantTableUpdate() -> UPDATE mysql.user SET Select_priv = 'Y' WHERE User = 'jeffrey'; -> etapa 2 rămâne pe copia veche
    3. Granița
           -> mecanismul este verificarea serverului; întrebarea din acest fișier nu este mecanismul
    4. Alternativa
           -> GRANT când privilegiul lipsește; aplicația când regula este pe un rând
    5. Anti-patternul
           -> UPDATE pe mysql.user nu reîncarcă memoria
    6. Dovada
           -> MySQL_Security.showGrantsAtOffice()
    7. Interviu
           -> lista vine la sfârșitul lui SHOW GRANTS; GRANT o scrie; FLUSH PRIVILEGES nu acordă un privilegiu nou
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
1. Enunț: Ce problemă rezolvă SHOW GRANTS FOR 'joe'@'office.example.com'?
   Capcană: „Arată și activează privilegiile oricărui joe.”
   Cheie: Citește lista contului de la birou. Contul de acasă este altul. Un rol din listă nu intră în etapa 2 până nu este activ.
   Demonstrație: MySQL_Security.showGrantsAtOffice()
2. Enunț: Ce rămâne în tabelele de aplicație după SHOW GRANTS?
   Capcană: „Propoziția închide tranzacția și rescrie indexul.”
   Cheie: Primești lista la sfârșitul propoziției. SHOW GRANTS nu este pe lista de commit implicit și nu atinge rândurile din payroll.
   Demonstrație: MySQL_Security.showGrantsAtOffice()
3. Enunț: De ce nu repeți SHOW GRANTS ca să apară un privilegiu care lipsește?
   Capcană: „SHOW GRANTS publică privilegiul la fel ca GRANT.”
   Cheie: GRANT scrie privilegiul și reîncarcă memoria. SHOW GRANTS doar citește user@host.
   Demonstrație: MySQL_Security.showGrantsAtOffice()
4. Enunț: Ce vezi la etapa 2 imediat după UPDATE mysql.user SET Select_priv = 'Y' WHERE User = 'jeffrey'?
   Capcană: „Următorul SELECT al sesiunii deschise trece.”
   Cheie: Copia din memorie rămâne veche până la reîncărcare sau repornire. Select_priv, fiind global static, nici după reîncărcare nu intră în sesiunea deja deschisă.
   Demonstrație: MySQL_Security.directGrantTableUpdate()
5. Enunț: Când lași regula în aplicație, nu în SHOW GRANTS?
   Capcană: „Un privilegiu de tabelă poate ascunde rândul unui singur pacient.”
   Cheie: Privilegiul este global, pe schemă, tabelă, coloană sau rutină. Nu refuză explicit un utilizator și nu filtrează un rând. Pentru un rând, regula rămâne în aplicație.
   Demonstrație: MySQL_Security.showGrantsAtHome()

Întrebări adăugate:
*/

/*
🟢 Începător
    Ce este            locul întrebărilor despre cine poate rula propoziția
    De ce e nevoie     fără ele, SHOW GRANTS și GRANT rămân aceeași propoziție
🟡 Producție
    Cum se scrie       SHOW GRANTS FOR 'joe'@'office.example.com';
🔴 Staff
    Unde               răspunsul stă în propoziția serverului, nu în corpul acestei clase
Pași                   1. citești enunțul
                       2. corpul rămâne gol
*/
// Începător: clasa ține întrebările și nu trimite ea o propoziție.
// Producție: nu există metodă aici. Propoziția rămâne în MySQL_Security.showGrantsAtOffice().
// Expert: un corp umplut ar muta demonstrația din clasa de topic. Clasa nu reîncarcă tabelele de grant.
public final class MySQLSecurity_Questions
// Începător: înăuntru nu se execută SQL.
// Producție: fără acoladă, întrebările nu au o clasă în care să rămână.
// Expert: acolada goală nu face GRANT și nu schimbă partial_revokes.
{
// Începător: corpul rămâne gol ca întrebările să stea în comentariu.
// Producție: nu se adaugă o metodă care să repete SHOW GRANTS FOR 'joe'@'office.example.com'; .
// Expert: golul nu este FLUSH PRIVILEGES și nu închide o tranzacție.
}
