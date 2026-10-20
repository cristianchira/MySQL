package _0_DB_ADMINISTRATION.fast;

/*
TOPIC:
    Întrebările de aici aleg între propoziția de administrare SHOW DATABASES și comanda de client \c.
FAMILIE:
    Server
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0
INTENT:
    Contul vrea numele schemelor. După propoziția corectă, rândurile de aplicație rămân, iar lista este de directoare.
MECHANISM:
    mysqld răspunde la SHOW DATABASES la sfârșitul propoziției. Programul mysql consumă \c fără să trimită SQL.
LOOKALIKES:
    clear (\c). SHOW DATABASES ajunge la server; \c rămâne în client.
BOOK THEORY:
    Manualul 9.7: SHOW DATABASES listează directoare din data directory, filtrate de privilegiu. SCHEMATA este citirea SQL pentru aceeași informație când o unești cu alte rânduri. Clientul interpretează separat clear, delimiter și status. Nu există PDF de curs în folder.
BOOK EXAMPLE:
    DB_Administration.showDatabases()
REPO DEMO:
    src/main/java/_0_DB_ADMINISTRATION/DB_Administration.java
BREAK LAB:
    \c nu întoarce directoare. Cu --skip-show-database și fără privilegiul SHOW DATABASES, propoziția nu poate fi folosită.
TRADE-OFFS:
    SHOW DATABASES cu LIKE sau WHERE pentru nume. SCHEMATA când unești. mysqlshow în shell.
ANTI-PATTERNS:
    SET PASSWORD poate lăsa parola în clar în log sau în istoricul clientului.
STAFF QUESTIONS:
    DbAdministration_Staff_Interview.recognizeAdministration()
NOVEL TRANSFER:
    Un panou de operații pentru schemele unui cont de facturare, nu demo-ul gol din repo.
SPACED REVIEW:
    O zi, șapte zile, treizeci de zile.
BEST PRACTICES:
    1. Cazul corect
           -> DECIDE întrebi serverul ce scheme vede contul -> DB_Administration.showDatabases() -> SHOW DATABASES; -> nu garantează scheme reale pentru fiecare director
    2. Eșecul cel mai mic
           -> DECIDE nu confunda clear cu SQL -> DB_Administration.clearBufferCommand() -> \c -> nu aduce lista
    3. Granița
           -> mecanismul este propoziția serverului; întrebarea din acest fișier nu este mecanismul
    4. Alternativa
           -> SCHEMATA când unești numele; SHOW DATABASES când listezi directoarele contului
    5. Anti-patternul
           -> SET PASSWORD cu parolă în clar în log sau în istoric
    6. Dovada
           -> DB_Administration.showDatabases()
    7. Interviu
           -> lista vine de la mysqld la sfârșitul lui SHOW DATABASES; \c nu pleacă din client
*/

/*
Definiția unei propoziții de administrare (database administration statement):
       Serverul execută propoziția. SHOW DATABASES întoarce nume de directoare și nu schimbă rândurile unei tabele de aplicație.

Exemplu:
       DbAdministration_Terms.explainAdministrationStatement()
       Lista apare la sfârșitul propoziției.

Definiția unei comenzi de client mysql (mysql client command):
       Programul mysql interpretează comanda și nu o trimite lui mysqld ca propoziție de administrare.

Exemplu:
       DbAdministration_Terms.explainClientCommand()
       \c golește bufferul și nu listează scheme.

Definiția unei listări de baze (SHOW DATABASES):
       SHOW DATABASES cere serverului numele directoarelor din data directory pe care contul le poate vedea.

Exemplu:
       DbAdministration_Terms.explainShowDatabases()
       LIKE și WHERE filtrează numele, nu șterg scheme.

Definiția unei scheme ca director (data directory):
       O bază este un director în data directory. Lista poate conține un nume care nu este o schemă reală.

Exemplu:
       DbAdministration_Terms.explainDataDirectory()
       Numele din rezultat nu dovedește că directorul ține tabele.

Definiția unei tabele de metadate SCHEMATA (INFORMATION_SCHEMA SCHEMATA):
       SCHEMATA ține informația despre baze și se citește cu SELECT când numele trebuie unite cu alte rânduri.

Exemplu:
       DbAdministration_Terms.explainSchemata()
       mysqlshow rămâne un program, nu această propoziție.

Definiția unei restricții de privilegiu (SHOW DATABASES privilege):
       Fără privilegiul global, contul vede doar bazele pentru care are vreun privilegiu. Cu --skip-show-database, fără acest privilegiu, propoziția nu poate fi folosită.

Exemplu:
       DbAdministration_Terms.explainShowDatabasesPrivilege()
       Un privilegiu global static arată toate numele, în afară de partial revoke.
*/

/*
1. Enunț: Cine execută SHOW DATABASES, serverul sau programul mysql?
   Capcană: „Este o comandă de client, ca \c, și serverul nu o vede.”
   Cheie: mysqld execută SHOW DATABASES și întoarce directoare. \c rămâne în client.
   Demonstrație: DB_Administration.showDatabases()
2. Enunț: Ce rămâne în tabelele de aplicație după SHOW DATABASES?
   Capcană: „Propoziția golește schemele fără tabele.”
   Cheie: Manualul descrie o listă de nume, nu o ștergere de rânduri.
   Demonstrație: DB_Administration.showDatabases()
3. Enunț: Când treci de la SHOW DATABASES la information_schema.SCHEMATA?
   Capcană: „Le schimbi mereu, pentru că SHOW nu acceptă WHERE.”
   Cheie: SHOW DATABASES acceptă LIKE și WHERE. SCHEMATA este pentru unirea numelor cu alte rânduri.
   Demonstrație: DB_Administration.schemataNames()
4. Enunț: Ce vezi dacă contul nu are niciun privilegiu pe o bază și nici privilegiul global?
   Capcană: „SHOW DATABASES arată tot data directory oricum.”
   Cheie: Fără privilegiu pe bază și fără privilegiul global, numele acelei baze nu intră în listă.
   Demonstrație: DB_Administration.showDatabases()
5. Enunț: De ce \c nu înlocuiește SHOW DATABASES într-un driver?
   Capcană: „Driverul înțelege \c și curăță sesiunea pe server.”
   Cheie: \c este clear pentru programul mysql. Driverul nu este acel client, iar serverul nu listează directoare pentru acest text.
   Demonstrație: DB_Administration.clearBufferCommand()

Întrebări adăugate:
*/

/*
🟢 Începător
    Ce este            locul întrebărilor despre cine execută lista de scheme
    De ce e nevoie     fără ele, \c și SHOW DATABASES rămân confundate
🟡 Producție
    Cum se scrie       SHOW DATABASES;
🔴 Staff
    Unde               răspunsul stă în propoziția serverului, nu în corpul acestei clase
Pași                   1. citești enunțul
                       2. corpul rămâne gol
*/
// Începător: clasa ține întrebările și nu trimite ea o propoziție.
// Producție: nu există metodă aici. Propoziția rămâne în DB_Administration.showDatabases().
// Expert: un corp umplut ar muta demonstrația din clasa de topic. Clasa nu este clientul mysql.
public final class DbAdministration_Questions
// Începător: înăuntru nu se execută SQL.
// Producție: fără acoladă, întrebările nu au o clasă în care să rămână.
// Expert: acolada goală nu listează data directory și nu acordă SHOW DATABASES.
{
// Începător: corpul rămâne gol ca întrebările să stea în comentariu.
// Producție: nu se adaugă o metodă care să repete SHOW DATABASES; .
// Expert: golul nu este o comandă \c și nu închide o sesiune.
}
