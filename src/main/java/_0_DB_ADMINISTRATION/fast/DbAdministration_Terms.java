package _0_DB_ADMINISTRATION.fast;

import _0_DB_ADMINISTRATION.DB_Administration;

/*
🟢 Începător
    Ce este            glosarul care spune cine execută lista de scheme
    De ce e nevoie     fără termeni, SHOW DATABASES și \c rămân aceeași comandă
🟡 Producție
    Cum se scrie       SHOW DATABASES;
🔴 Staff
    Unde               fiecare metodă întoarce textul termenului, fără să deschidă o sesiune
Pași                   1. propoziția de administrare
                       2. comanda de client
                       3. listarea, directorul, SCHEMATA, privilegiul
*/
// Începător: clasa ține termenii prin care separi serverul de clientul mysql.
// Producție: metodele întorc SHOW DATABASES; , SELECT-ul pe SCHEMATA sau \c. Dacă clasa dispare, exemplele din definiții nu mai au metodă.
// Expert: glosarul nu predă GRANT și nu predă InnoDB. Nu garantează că un nume din listă este o schemă reală.
public final class DbAdministration_Terms
// Începător: de aici încep termenii despre listarea schemelor.
// Producție: fără acoladă, metodele care întorc propoziția nu au clasă.
// Expert: acolada nu pornește mysqld și nu interpretează \c.
{

    /*
    Definiția unei propoziții de administrare (database administration statement):
       Serverul execută propoziția. SHOW DATABASES întoarce nume de directoare și nu schimbă rândurile unei tabele de aplicație.

    Exemplu:
       DbAdministration_Terms.explainAdministrationStatement()
       Lista apare la sfârșitul propoziției.
    */
    /*
    🟢 Începător
        Ce este            propoziția pe care o execută mysqld ca să spună ce scheme vede contul
        De ce e nevoie     fără ea, clientul nu are lista din data directory
    🟡 Producție
        Cum se scrie       SHOW DATABASES;
    🔴 Staff
        Unde               rezultatul vine la sfârșitul propoziției, de la server
    Pași                   1. întoarce showDatabases
    */
    // Începător: metoda dă textul prin care serverul listează schemele vizibile.
    // Producție: întoarce SHOW DATABASES; . Dacă dispare, definiția nu mai are exemplu executabil.
    // Expert: nu este \c și nu este unul dintre celelalte șapte grupuri de administrare, luate pe rând. Nu creează un cont.
    public static String explainAdministrationStatement()
    // Începător: înăuntru pleacă cererea de scheme.
    // Producție: fără acoladă, SHOW DATABASES; nu iese din metodă.
    // Expert: acolada nu deschide data directory.
    {
        // Începător: înapoi pleacă cererea pe care serverul o poate executa.
        // Producție: return DB_Administration.showDatabases() predă SHOW DATABASES; . Fără linie, termenul nu are propoziție.
        // Expert: apelul nu trece prin programul mysqlshow. Partial revoke poate scoate un nume din lista pe care o va întoarce serverul.
        return DB_Administration.showDatabases();
    // Începător: termenul propoziției de administrare se încheie aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu acordă privilegiul SHOW DATABASES.
    }

    /*
    Definiția unei comenzi de client mysql (mysql client command):
       Programul mysql interpretează comanda și nu o trimite lui mysqld ca propoziție de administrare.

    Exemplu:
       DbAdministration_Terms.explainClientCommand()
       \c golește bufferul și nu listează scheme.
    */
    /*
    🟢 Începător
        Ce este            textul pe care îl consumă programul mysql
        De ce e nevoie     ca să nu îl trimiți când voiai lista de scheme
    🟡 Producție
        Cum se scrie       \c
    🔴 Staff
        Unde               clear rămâne în client; serverul nu întoarce directoare
    Pași                   1. întoarce clearBufferCommand
    */
    // Începător: metoda dă textul pe care clientul îl aruncă înainte de server.
    // Producție: întoarce \c . Dacă dispare, nu mai ai termenul vecin lângă SHOW DATABASES.
    // Expert: în --binary-mode, \c nu mai este acceptat de client. Linia nu ține loc de privilegiu.
    public static String explainClientCommand()
    // Începător: înăuntru stă comanda care nu cere scheme.
    // Producție: fără acoladă, \c nu poate fi întors.
    // Expert: acolada nu activează --system-command=ON.
    {
        // Începător: înapoi pleacă clear, nu o listă de baze.
        // Producție: return DB_Administration.clearBufferCommand() predă \c . Fără linie, termenul de client este gol.
        // Expert: textul nu este în grupul SHOW. Un driver care îl primește nu golește bufferul clientului mysql, pentru că nu este acel program.
        return DB_Administration.clearBufferCommand();
    // Începător: termenul comenzii de client se încheie aici.
    // Producție: fără închidere, \c nu rămâne în clasă.
    // Expert: închiderea nu schimbă delimiterul și nu închide sesiunea.
    }

    /*
    Definiția unei listări de baze (SHOW DATABASES):
       SHOW DATABASES cere serverului numele directoarelor din data directory pe care contul le poate vedea.

    Exemplu:
       DbAdministration_Terms.explainShowDatabases()
       LIKE și WHERE filtrează numele, nu șterg scheme.
    */
    /*
    🟢 Începător
        Ce este            listarea numelor de scheme pe care contul le poate vedea
        De ce e nevoie     fără listă nu știi ce directoare sunt vizibile
    🟡 Producție
        Cum se scrie       SHOW DATABASES;
    🔴 Staff
        Unde               LIKE și WHERE aleg nume din rezultat; nu șterg directorul
    Pași                   1. întoarce showDatabases
    */
    // Începător: metoda dă propoziția care cere numele bazelor.
    // Producție: întoarce SHOW DATABASES; . Dacă dispare, termenul de listare nu mai are text.
    // Expert: SHOW SCHEMAS este același enunț. Nu este programul mysqlshow și nu schimbă schema implicită, cum face use în client.
    public static String explainShowDatabases()
    // Începător: înăuntru pleacă listarea.
    // Producție: fără acoladă, SHOW DATABASES; nu iese.
    // Expert: acolada nu pune --skip-show-database.
    {
        // Începător: înapoi pleacă cererea de nume, nu directoarele copiate în Java.
        // Producție: return DB_Administration.showDatabases() predă SHOW DATABASES; . Fără linie, nu rămâne listarea.
        // Expert: un privilegiu global static lărgește lista, dar partial revoke tot poate ascunde un nume. Linia nu creează schema.
        return DB_Administration.showDatabases();
    // Începător: termenul listării se încheie aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu șterge un director din data directory.
    }

    /*
    Definiția unei scheme ca director (data directory):
       O bază este un director în data directory. Lista poate conține un nume care nu este o schemă reală.

    Exemplu:
       DbAdministration_Terms.explainDataDirectory()
       Numele din rezultat nu dovedește că directorul ține tabele.
    */
    /*
    🟢 Începător
        Ce este            faptul că serverul întoarce nume de directoare, nu doar baze verificate
        De ce e nevoie     un nume din listă poate să nu aibă tabele
    🟡 Producție
        Cum se scrie       SHOW DATABASES;
    🔴 Staff
        Unde               mysqld citește data directory la sfârșitul propoziției
    Pași                   1. întoarce aceeași propoziție care listează directoarele
    */
    // Începător: metoda arată că lista este de directoare de pe instanță.
    // Producție: tot SHOW DATABASES; este propoziția care le cere. Dacă metoda dispare, termenul de director nu are exemplu.
    // Expert: un director care nu este bază poate apărea. Linia nu transformă directorul într-o tabelă.
    public static String explainDataDirectory()
    // Începător: înăuntru pleacă cererea care umple lista de directoare.
    // Producție: fără acoladă, nu ai de unde întoarce SHOW DATABASES; pentru acest termen.
    // Expert: acolada nu mută data directory.
    {
        // Începător: serverul, nu Java, se uită în locul unde stau bazele.
        // Producție: return DB_Administration.showDatabases() predă SHOW DATABASES; . Fără linie, termenul nu spune ce propoziție listează directoarele.
        // Expert: rezultatul poate include un nume care nu este o schemă. Nu este același lucru cu SELECT pe SCHEMATA unit cu alte rânduri.
        return DB_Administration.showDatabases();
    // Începător: termenul de director se încheie aici.
    // Producție: fără închidere, clasa nu compilează.
    // Expert: închiderea nu șterge un director străin din data directory.
    }

    /*
    Definiția unei tabele de metadate SCHEMATA (INFORMATION_SCHEMA SCHEMATA):
       SCHEMATA ține informația despre baze și se citește cu SELECT când numele trebuie unite cu alte rânduri.

    Exemplu:
       DbAdministration_Terms.explainSchemata()
       mysqlshow rămâne un program, nu această propoziție.
    */
    /*
    🟢 Începător
        Ce este            citirea listei de scheme din tabela de metadate
        De ce e nevoie     când numele trebuie să stea lângă alte rânduri în aceeași propoziție
    🟡 Producție
        Cum se scrie       SELECT * FROM information_schema.SCHEMATA;
    🔴 Staff
        Unde               tot mysqld răspunde; mysqlshow nu este această tabelă
    Pași                   1. întoarce schemataNames
    */
    // Începător: metoda dă citirea din tabela care ține informația despre baze.
    // Producție: întoarce SELECT * FROM information_schema.SCHEMATA; . Dacă dispare, nu mai ai alternativa care se unește cu alte rânduri.
    // Expert: partial revoke ascunde nume și aici. Linia nu este programul mysqlshow.
    public static String explainSchemata()
    // Începător: înăuntru pleacă citirea de metadate.
    // Producție: fără acoladă, SELECT-ul pe SCHEMATA nu are metodă.
    // Expert: acolada nu acordă un privilegiu global static.
    {
        // Începător: înapoi pleacă propoziția care citește metadatele bazelor.
        // Producție: return DB_Administration.schemataNames() predă SELECT * FROM information_schema.SCHEMATA; . Fără linie, termenul este gol.
        // Expert: SHOW DATABASES rămâne destul când îți ajunge LIKE sau WHERE pe nume. Această linie este pentru unirea cu alte rânduri.
        return DB_Administration.schemataNames();
    // Începător: termenul SCHEMATA se încheie aici.
    // Producție: fără închidere, alternativa nu compilează.
    // Expert: închiderea nu pornește mysqlshow în shell.
    }

    /*
    Definiția unei restricții de privilegiu (SHOW DATABASES privilege):
       Fără privilegiul global, contul vede doar bazele pentru care are vreun privilegiu. Cu --skip-show-database, fără acest privilegiu, propoziția nu poate fi folosită.

    Exemplu:
       DbAdministration_Terms.explainShowDatabasesPrivilege()
       Un privilegiu global static arată toate numele, în afară de partial revoke.
    */
    /*
    🟢 Începător
        Ce este            dreptul care hotărăște dacă numele unei baze intră în listă
        De ce e nevoie     fără el, cu --skip-show-database, propoziția nu poate fi folosită
    🟡 Producție
        Cum se scrie       SHOW DATABASES;
    🔴 Staff
        Unde               filtrarea este a serverului, la execuția propoziției, nu a clientului \c
    Pași                   1. întoarce showDatabases
    */
    // Începător: metoda ține propoziția a cărei listă depinde de privilegiul contului.
    // Producție: întoarce SHOW DATABASES; . Dacă dispare, termenul de privilegiu nu arată ce propoziție este oprită.
    // Expert: orice privilegiu global static arată toate numele, în afară de cele oprite prin partial revoke. Linia nu acordă ea privilegiul.
    public static String explainShowDatabasesPrivilege()
    // Începător: înăuntru pleacă propoziția pe care privilegiul o poate opri.
    // Producție: fără acoladă, nu ai unde întoarce SHOW DATABASES; pentru acest termen.
    // Expert: acolada nu setează --skip-show-database.
    {
        // Începător: serverul decide ce nume vede contul, nu textul din Java.
        // Producție: return DB_Administration.showDatabases() predă SHOW DATABASES; . Fără linie, nu rămâne propoziția afectată de privilegiu.
        // Expert: \c nu testează privilegiul, pentru că nu ajunge la server. Un privilegiu global nu trece peste partial revoke.
        return DB_Administration.showDatabases();
    // Începător: termenul privilegiului se încheie aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu face GRANT și nu revocă un privilegiu deja dat.
    }
// Începător: glosarul se termină după privilegiu.
// Producție: fără închidere, clasa de termeni nu compilează.
// Expert: închiderea nu șterge termenii deja scriși și nu închide mysqld.
}
