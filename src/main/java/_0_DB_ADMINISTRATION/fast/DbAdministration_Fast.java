package _0_DB_ADMINISTRATION.fast;

import _0_DB_ADMINISTRATION.DB_Administration;

/*
TOPIC:
    Alegi propoziția de administrare când mysqld trebuie să întoarcă numele schemelor; comanda de client rămâne în programul mysql.
FAMILIE:
    Server
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0
INTENT:
    Între copilul DB_Administration și copilul MySQL_Commands, lista de scheme o cere SHOW DATABASES. \c nu o cere.
MECHANISM:
    mysqld execută SHOW DATABASES și întoarce nume de directoare la sfârșitul propoziției. Programul mysql consumă \c și nu trimite nimic.
LOOKALIKES:
    clear (\c) din comenzile clientului. Se separă prin cine primește textul: serverul, pentru SHOW DATABASES; clientul, pentru \c.
BOOK THEORY:
    Manualul 9.7: SHOW DATABASES listează directoarele din data directory pe care contul le poate vedea. Un privilegiu global static arată toate numele, în afară de partial revoke. Cu --skip-show-database, fără privilegiul SHOW DATABASES, propoziția nu poate fi folosită. information_schema.SCHEMATA ține aceeași informație pentru o propoziție care unește rânduri. mysql trimite SQL-ul la server și interpretează separat help, clear, delimiter și status. Nu există PDF de curs în folder.
BOOK EXAMPLE:
    DB_Administration.showDatabases()
REPO DEMO:
    src/main/java/_0_DB_ADMINISTRATION/DB_Administration.java
BREAK LAB:
    \c nu listează scheme. Clientul golește bufferul și serverul nu întoarce directoare.
TRADE-OFFS:
    Rămâi la SHOW DATABASES pentru lista contului, cu LIKE sau WHERE. Treci la SCHEMATA când unești numele cu alte rânduri. mysqlshow este program de shell.
ANTI-PATTERNS:
    SET PASSWORD cu parola în clar poate lăsa secretul în log sau în istoricul clientului.
STAFF QUESTIONS:
    DbAdministration_Staff_Interview.recognizeAdministration()
NOVEL TRANSFER:
    Consola de facturare nu folosește \s ca să afle schemele unui chiriaș. Cere SHOW DATABASES pe contul acelui chiriaș.
SPACED REVIEW:
    O zi, șapte zile, treizeci de zile: cine execută textul, SHOW DATABASES sau \c.
BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei schemele văzute de cont -> DB_Administration.showDatabases() -> SHOW DATABASES; -> nu garantează că fiecare director este o schemă reală
    2. Eșecul cel mai mic
           -> DECIDE nu trimite clear ca SQL -> DB_Administration.clearBufferCommand() -> \c -> nu aduce lista de directoare
    3. Granița
           -> propoziția este a serverului; promptul mysql și click-ul din Workbench nu sunt mecanismul
    4. Alternativa
           -> SCHEMATA când unești numele cu alte rânduri; SHOW DATABASES când îți ajunge lista de directoare
    5. Anti-patternul
           -> SET PASSWORD poate scrie parola în clar în log sau în istoricul clientului
    6. Dovada
           -> DbAdministration_Fast.main tipărește propoziția ținută de DB_Administration.showDatabases()
    7. Interviu
           -> SHOW DATABASES întoarce directoare la sfârșitul propoziției; \c nu pleacă din client; privilegiul global lărgește lista, nu și peste partial revoke
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
🟢 Începător
    Ce este            lecția care alege propoziția de administrare, nu comanda de client
    De ce e nevoie     fără alegere, \c este trimis în locul lui SHOW DATABASES și nu vine nicio schemă
🟡 Producție
    Cum se scrie       SHOW DATABASES;
🔴 Staff
    Unde               textul pleacă din DB_Administration și ajunge la mysqld doar când un server îl execută
Pași                   1. citește propoziția deja scrisă
                       2. o tipărește, fără să deschidă o sesiune inventată
*/
// Începător: clasa arată ce propoziție ceri când vrei schemele, nu comanda care golește bufferul.
// Producție: exemplul citește SHOW DATABASES; din DB_Administration. Dacă clasa dispare, lecția scurtă nu mai are intrare.
// Expert: clasa nu predă fiecare grup din administrare și nu înlocuiește copilul MySQL_Commands. Nu garantează un mysqld pornit.
public final class DbAdministration_Fast
// Începător: de aici începe exemplul minim, cel care doar arată propoziția.
// Producție: fără acoladă, main nu poate citi SHOW DATABASES; .
// Expert: acolada nu este o sesiune mysql și nu acordă privilegiul SHOW DATABASES.
{

    /*
    🟢 Începător
        Ce este            exemplul care ia propoziția gata scrisă și o arată
        De ce e nevoie     fără main nu vezi textul pe care l-ai alege între cei doi copii
    🟡 Producție
        Cum se scrie       SHOW DATABASES;
    🔴 Staff
        Unde               println nu este mysqld; serverul vede textul abia când îl primește
    Pași                   1. citește showDatabases
                           2. tipărește textul
    */
    // Începător: de aici pornește exemplul care arată cererea de scheme.
    // Producție: main nu trimite \c. Dacă dispare, nu mai rămâne o intrare care să tipărească SHOW DATABASES; .
    // Expert: main nu deschide Workbench și nu este programul mysqlshow. Nu garantează că un cont fără privilegiu va primi lista.
    public static void main(String[] args)
    // Începător: înăuntru se citește propoziția, nu se creează o bază.
    // Producție: fără acoladă, apelul către showDatabases nu are corp.
    // Expert: acolada nu pornește mysqld și nu schimbă --skip-show-database.
    {
        // Începător: aici este cererea prin care serverul spune ce scheme vede contul.
        // Producție: valoarea este SHOW DATABASES; , luată din clasa deja scrisă. Fără linie, exemplul inventează un al doilea text.
        // Expert: apelul nu este \c și nu este SELECT pe SCHEMATA. Nu listează el directoarele; doar aduce textul propoziției.
        String sql = DB_Administration.showDatabases();
        // Începător: pe ecran apare cererea, nu numele bazelor de pe un server.
        // Producție: println scrie SHOW DATABASES; . Dacă dispare, lecția nu mai arată propoziția.
        // Expert: consola nu este clientul mysql, deci \c nu este executat aici. Linia nu dovedește privilegiul contului.
        System.out.println(sql);
    // Începător: exemplul se oprește după ce a arătat cererea.
    // Producție: fără închidere, main nu compilează.
    // Expert: închiderea nu închide o sesiune și nu revocă un privilegiu global.
    }
// Începător: lecția scurtă se termină la această clasă.
// Producție: fără închidere, fișierul nu compilează și propoziția nu poate fi tipărită.
// Expert: închiderea clasei nu oprește mysqld și nu șterge copilul MySQL_Commands.
}
