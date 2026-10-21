package _7_Data_Types._5_JSON;

/*
TOPIC:
    Tipul JSON validează documentul la scriere; un VARCHAR ține textul '{nu}' fără să îl refuze.
FAMILIE:
    Types
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să nu rămână un document stricat în coloană. După o scriere reușită, body este document, nu un șir oarecare.
MECHANISM:
    Serverul verifică documentul la INSERT în coloana JSON. Documentul invalid este eroare. Coloana nu se indexează direct: indexul stă pe o coloană generată care scoate un scalar.
LOOKALIKES:
    body VARCHAR(255) primește '{nu}' și nu îl validează.
BOOK THEORY:
    Manualul 9.7, 13.5, deschis în tur: JSON validează documentul, îl ține într-un format intern și nu se indexează direct. Indexul se pune pe o coloană generată. Folderul nu avea PDF.
BOOK EXAMPLE:
    jsonColumnValidatesTheDocument
REPO DEMO:
    src/main/java/_7_Data_Types/_5_JSON/JSON.java
BREAK LAB:
    varcharKeepsBrokenText ține CREATE TABLE doc (body VARCHAR(255)). '{nu}' poate intra, pentru că VARCHAR nu validează documentul.
TRADE-OFFS:
    VARCHAR când păstrezi textul primit, inclusiv pe cel invalid, și nu îl citești ca document.
ANTI-PATTERNS:
    CREATE TABLE doc (body VARCHAR(255)) ca să ții JSON. '{nu}' intră.
STAFF QUESTIONS:
    jsonColumnValidatesTheDocument și varcharKeepsBrokenText. Cele cinci întrebări de mai jos. fast al tipurilor stă la părintele _7_Data_Types.
NOVEL TRANSFER:
    Un mesaj de laborator stă în JSON ca să refuze un corp stricat. Un comentariu liber rămâne VARCHAR.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă CREATE TABLE doc (body JSON)?
   Capcană: VARCHAR(255) validează documentul la fel ca JSON.
   Cheie: jsonColumnValidatesTheDocument lasă o coloană care refuză documentul invalid. VARCHAR îl ține ca text.
   Demonstrație: jsonColumnValidatesTheDocument
2. Enunț: Ce rămâne după CREATE TABLE doc (body JSON)?
   Capcană: JSON șterge documentul invalid și lasă rândul.
   Cheie: Apare coloana body, încă fără rând. Validarea se vede la INSERT, nu la CREATE. Documentul invalid este eroare, nu un rând pe jumătate.
   Demonstrație: jsonColumnValidatesTheDocument
3. Enunț: De ce nu este VARCHAR?
   Capcană: VARCHAR și JSON resping la fel textul '{nu}'.
   Cheie: VARCHAR primește '{nu}'. JSON, la scriere, dă eroare. Manualul 13.5 desparte validarea de șir.
   Demonstrație: jsonColumnValidatesTheDocument
4. Enunț: Cum eșuează dacă ții documentul în VARCHAR?
   Capcană: INSERT cu '{nu}' intră și în coloana JSON.
   Cheie: varcharKeepsBrokenText nu validează. '{nu}' poate rămâne în tabelă.
   Demonstrație: varcharKeepsBrokenText
5. Enunț: Când nu folosești JSON?
   Capcană: Ca să cauți o cheie, indexezi direct coloana JSON.
   Cheie: VARCHAR când vrei textul chiar invalid. Indexul pe JSON nu este direct: stă pe o coloană generată.
   Demonstrație: jsonColumnValidatesTheDocument

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE coloana trebuie să refuze documentul stricat -> jsonColumnValidatesTheDocument -> CREATE TABLE doc (body JSON) -> nu indexează body direct și nu ține un document mai mare decât max_allowed_packet
    2. Eșecul cel mai mic
           -> DECIDE ții textul fără validare -> varcharKeepsBrokenText -> CREATE TABLE doc (body VARCHAR(255)) -> '{nu}' poate intra
    3. Granița
           -> JSON este tipul de pe server. Un câmp de text din client nu validează documentul.
    4. Alternativa
           -> VARCHAR când păstrezi textul primit, inclusiv pe cel invalid.
    5. Anti-patternul
           -> CREATE TABLE doc (body VARCHAR(255)) ca să ții JSON. '{nu}' intră.
    6. Dovada
           -> jsonColumnValidatesTheDocument ține tipul. varcharKeepsBrokenText ține șirul. În tur serverul nu a executat propoziția: NERULAT AICI.
    7. Interviu
           -> JSON validează și ține format intern. VARCHAR nu refuză '{nu}'. Indexul nu stă direct pe coloana JSON.
*/

public class JSON {
    /*
    🟢 Începător
        Ce este            coloana care primește doar un document, nu un șir stricat
        De ce e nevoie     fără ea, '{nu}' poate rămâne în tabelă
    🟡 Producție
        Cum se scrie       CREATE TABLE doc (body JSON)
    🔴 Staff
        Unde               validarea se vede la INSERT. Vecinul este VARCHAR, care nu validează. Indexul direct nu există.
    Pași
        1. Ține definiția într-un șir.
        2. Nu o trimite: sesiunea nu a fost deschisă.
        3. Nu o înlocui cu VARCHAR.
    */
    // Începător: jsonColumnValidatesTheDocument păstrează definiția care va refuza documentul stricat.
    // Producție: public static String jsonColumnValidatesTheDocument(). Dacă dispare, rămâne doar explicația, fără propoziție.
    // Expert: Metoda nu deschide JDBC. VARCHAR nu face această validare, iar indexul nu se pune direct pe body.
    public static String jsonColumnValidatesTheDocument() {
        // Începător: apare tabela doc cu o coloană care încă nu are rând.
        // Producție: CREATE TABLE doc (body JSON). Dacă linia dispare, nu mai ai tipul gata de trimis.
        // Expert: La INSERT, documentul invalid este eroare. Coloana nu se indexează direct; indexul stă pe o coloană generată. Linia nu a fost trimisă.
        return "CREATE TABLE doc (body JSON)";
        // Începător: metoda a înapoiat definiția, nu un document stocat.
        // Producție: acolada închide jsonColumnValidatesTheDocument. Șirul a rămas în proces.
        // Expert: Închiderea nu este INSERT-ul care validează. Efectul pe server rămâne NERULAT AICI.
    }

    /*
    🟢 Începător
        Ce este            coloana de text care primește și un document stricat
        De ce e nevoie     fără ea, pare că orice coloană de șir validează JSON
    🟡 Producție
        Cum se scrie       CREATE TABLE doc (body VARCHAR(255))
    🔴 Staff
        Unde               eșecul este că '{nu}' poate intra. Vecinul care refuză este JSON.
    Pași
        1. Ține textul greșit separat.
        2. Nu îl trimite în tur.
        3. Nu îl numi validare.
    */
    // Începător: varcharKeepsBrokenText păstrează definiția care nu verifică documentul.
    // Producție: public static String varcharKeepsBrokenText(). Dacă dispare, eșecul rămâne o poveste.
    // Expert: VARCHAR nu este coloana generată pe care ai pune indexul unui scalar scos din JSON.
    public static String varcharKeepsBrokenText() {
        // Începător: '{nu}' poate deveni un rând, pentru că nimeni nu verifică documentul.
        // Producție: CREATE TABLE doc (body VARCHAR(255)). Dacă linia dispare, nu mai ai contraexemplul.
        // Expert: Manualul 13.5 desparte validarea JSON de șir. Indexul direct pe JSON tot nu apare din acest VARCHAR.
        return "CREATE TABLE doc (body VARCHAR(255))";
        // Începător: metoda a înapoiat definiția de șir, nu un document valid.
        // Producție: acolada închide varcharKeepsBrokenText.
        // Expert: Închiderea nu validează nimic. Efectul pe server rămâne NERULAT AICI.
    }
}
