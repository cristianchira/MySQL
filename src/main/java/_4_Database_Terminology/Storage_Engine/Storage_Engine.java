// Începător: fișierul ține numele componentei care execută schimbarea pe un tip de tabelă.
// Producție: pachetul este _4_Database_Terminology.Storage_Engine. Dacă linia dispare, folderul din catalog nu mai are clasă.
// Expert: clasa nu înlocuiește SHOW ENGINES din DatabaseTerminology_Fast.chooseEngine. MyISAM rămâne vecinul cu Transactions NO.
package _4_Database_Terminology.Storage_Engine;

/*
Pe MySQL 9.7, storage engine este componenta care execută propoziția pentru un tip de tabelă.
InnoDB este implicit: un CREATE TABLE fără ENGINE creează o tabelă InnoDB, cu commit, rollback,
recuperare după oprire, lacăt de rând și FOREIGN KEY.
SHOW ENGINES arată Support DEFAULT și Transactions. MyISAM are Transactions NO și lacăt de tabelă.
ENGINE pe CREATE TABLE sau pe ALTER TABLE leagă tabela de componentă.
Dacă motorul cerut lipsește, serverul creează tabela cu motorul implicit și avertizează.
Cu NO_ENGINE_SUBSTITUTION dă eroare și nu creează tabela.
Propoziția gata de trimis stă în DatabaseTerminology_Fast.chooseEngine: SHOW ENGINES.
*/

/*
🟢 Începător
    Ce este            numele motorului de stocare, componenta care ține rândurile unui tip de tabelă
    De ce e nevoie     fără folder, catalogul arată un copil care nu era pe disc
🟡 Producție
    Cum se scrie       SHOW ENGINES, apoi ENGINE pe CREATE TABLE sau ALTER TABLE
🔴 Staff
    Unde               Support și Transactions înainte de scriere. Vecinul MyISAM nu ține tranzacția
Pași                   1. Citești SHOW ENGINES. 2. Alegi ENGINE. 3. Nu confunzi cuvântul OLTP cu motorul.
*/
// Începător: clasa dă locul de pe disc pentru motorul de stocare. Nu scrie ea însăși un rând.
// Producție: nu are metodă proprie, ca să nu apară a doua copie a lui SHOW ENGINES. Propoziția este DatabaseTerminology_Fast.chooseEngine.
// Expert: folderul nu predă până la capăt commit-ul, lacătul și forma normală. Le desparte de motor prin coloana Transactions.
/*
TOPIC:
    SHOW ENGINES arată componenta care execută pe un tip de tabelă; InnoDB are Transactions YES, MyISAM are Transactions NO.
FAMILIE:
    Server
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să vezi motorul înainte să scrii. SHOW ENGINES nu schimbă rândurile.
MECHANISM:
    Serverul execută SHOW ENGINES. ENGINE pe CREATE TABLE leagă tabela. Propoziția este deja în DatabaseTerminology_Fast.chooseEngine.
LOOKALIKES:
    CREATE TABLE ... ENGINE=MyISAM alege componenta fără tranzacție. OLTP nu este un rând din SHOW ENGINES.
BOOK THEORY:
    PDF-ul SQL+Keywords.pdf nu descrie motorul. chooseEngine din fast întoarce SHOW ENGINES. Support DEFAULT este motorul unui CREATE fără ENGINE, dacă pornirea nu a schimbat implicitul.
BOOK EXAMPLE:
    DatabaseTerminology_Fast.chooseEngine
REPO DEMO:
    src/main/java/_4_Database_Terminology/Storage_Engine/Storage_Engine.java
BREAK LAB:
    myisamEngineHasNoTransaction ține CREATE TABLE sales (purchase_number INT) ENGINE=MyISAM. MyISAM este vecinul cu Transactions NO. SHOW ENGINES o arată înainte de scriere.
TRADE-OFFS:
    ENGINE pe CREATE TABLE când ai citit deja Transactions și vrei să legi sales.
ANTI-PATTERNS:
    ENGINE=MyISAM când ai nevoie de ROLLBACK. Coloana Transactions spune NO.
STAFF QUESTIONS:
    DatabaseTerminology_Fast.chooseEngine și myisamEngineHasNoTransaction. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Înainte de tabela de paturi, SHOW ENGINES îți arată dacă motorul implicit are Transactions. Abia apoi creezi tabela.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SHOW ENGINES?
   Capcană: SHOW ENGINES inserează un rând în sales.
   Cheie: DatabaseTerminology_Fast.chooseEngine: vezi Support și Transactions și nu se schimbă niciun rând de afaceri. fără SHOW ENGINES, nu știi dacă ROLLBACK mai are ce scoate
   Demonstrație: DatabaseTerminology_Fast.chooseEngine
2. Enunț: Ce rămâne în tabelă sau în sesiune după SHOW ENGINES?
   Capcană: MyISAM are Transactions YES.
   Cheie: Efectul este: vezi Support și Transactions și nu se schimbă niciun rând de afaceri. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: DatabaseTerminology_Fast.chooseEngine
3. Enunț: De ce SHOW ENGINES nu este vecinul său?
   Capcană: OLTP este un motor din SHOW ENGINES.
   Cheie: CREATE TABLE ... ENGINE=MyISAM alege componenta fără tranzacție. OLTP nu este un rând din SHOW ENGINES.
   Demonstrație: DatabaseTerminology_Fast.chooseEngine
4. Enunț: Cum eșuează CREATE TABLE sales (purchase_number INT) ENGINE=MyISAM?
   Capcană: ENGINE=MyISAM ține FOREIGN KEY ca InnoDB.
   Cheie: myisamEngineHasNoTransaction: MyISAM este vecinul cu Transactions NO. SHOW ENGINES o arată înainte de scriere.
   Demonstrație: myisamEngineHasNoTransaction
5. Enunț: Când nu mai trimiți SHOW ENGINES?
   Capcană: CREATE fără ENGINE este mereu MyISAM pe 9.7.
   Cheie: ENGINE pe CREATE TABLE când ai citit deja Transactions și vrei să legi sales.
   Demonstrație: DatabaseTerminology_Fast.chooseEngine

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei să citești motoarele, nu să creezi sales -> DatabaseTerminology_Fast.chooseEngine -> SHOW ENGINES -> nu schimbă default-storage-engine și nu scrie un rând în sales
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> myisamEngineHasNoTransaction -> CREATE TABLE sales (purchase_number INT) ENGINE=MyISAM -> MyISAM este vecinul cu Transactions NO. SHOW ENGINES o arată înainte de scriere.
    3. Granița
           -> SHOW ENGINES este propoziția. Cuvântul motor dintr-o diagramă nu este rezultatul.
    4. Alternativa
           -> CREATE TABLE ... ENGINE=MyISAM alege componenta fără tranzacție. OLTP nu este un rând din SHOW ENGINES.
    5. Anti-patternul
           -> ENGINE=MyISAM când ai nevoie de ROLLBACK. Coloana Transactions spune NO.
    6. Dovada
           -> DatabaseTerminology_Fast.chooseEngine ține propoziția. myisamEngineHasNoTransaction ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> SHOW ENGINES arată Support și Transactions. InnoDB pe 9.7 este implicitul obișnuit, cu Transactions YES. MyISAM are Transactions NO. Propoziția nu schimbă rânduri.
*/

public class Storage_Engine {
    /*
    🟢 Începător
        Ce este            dacă propoziția este acceptată, sales nu are Transactions YES
        De ce e nevoie     fără textul greșit, orice CREATE pare InnoDB
    🟡 Producție
        Cum se scrie       CREATE TABLE sales (purchase_number INT) ENGINE=MyISAM
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care dacă propoziția este acceptată, sales nu are Transactions YES
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu CREATE TABLE sales (purchase_number INT) ENGINE=MyISAM.
    */
    // Începător: myisamEngineHasNoTransaction păstrează textul greșit pentru dacă propoziția este acceptată, sales nu are Transactions YES
    // Producție: public static String myisamEngineHasNoTransaction(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. MyISAM este vecinul cu Transactions NO. SHOW ENGINES o arată înainte de scriere.
    public static String myisamEngineHasNoTransaction() {
        // Începător: dacă propoziția este acceptată, sales nu are Transactions YES
        // Producție: CREATE TABLE sales (purchase_number INT) ENGINE=MyISAM. Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: MyISAM este vecinul cu Transactions NO. SHOW ENGINES o arată înainte de scriere. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE TABLE sales (purchase_number INT) ENGINE=MyISAM";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide myisamEngineHasNoTransaction. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

    // Începător: locul motorului de stocare se oprește aici. Rândurile se schimbă abia când serverul execută propoziția.
    // Producție: acolada închide clasa. Fără ea, fișierul nu compilează pe Java 27.
    // Expert: închiderea nu alege InnoDB. Alegerea se vede pe SHOW ENGINES, în lecția scurtă a folderului părinte.
}
