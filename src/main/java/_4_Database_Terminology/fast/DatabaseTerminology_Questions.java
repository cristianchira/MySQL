// Începător: fișierul ține întrebările lecției despre cine execută schimbarea pe tabelă.
// Producție: pachetul este _4_Database_Terminology.fast. Clasa nu primește metode, ca să rămână locul în care adaugi întrebări.
// Expert: întrebările sunt despre motorul de pe SHOW ENGINES. Nu se lipesc pe WHERE, pentru că aici se alege componenta, nu filtrul de rânduri.
package _4_Database_Terminology.fast;

/*
TOPIC:
    Motorul de stocare numit de ENGINE, sau InnoDB când clauza lipsește pe MySQL 9.7, este componenta care execută schimbarea de rânduri pe acea tabelă și care decide dacă ROLLBACK mai poate scoate rândul.
FAMILIE:
    Server
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0
INTENT:
    Problema este să numești schema sau încărcarea și să crezi că rândul stă deja într-o tranzacție.
MECHANISM:
    SHOW ENGINES arată Support și Transactions. ENGINE pe CREATE TABLE leagă tabela de componentă.
LOOKALIKES:
    MyISAM. Îi separă Transactions NO și lacătul de tabelă.
BOOK THEORY:
    Pe MySQL 9.7 InnoDB este implicit, cu commit, rollback, lacăt de rând și FOREIGN KEY. MyISAM are Transactions NO. ENGINE=MISSING fără NO_ENGINE_SUBSTITUTION cade pe motorul implicit și avertizează; cu modul pornit, tabela nu se creează. PDF-ul SQL+Keywords.pdf nu descrie motorul.
BOOK EXAMPLE:
    DatabaseTerminology_Fast.chooseEngine
REPO DEMO:
    src/main/java/_4_Database_Terminology/fast/DatabaseTerminology_Questions.java
BREAK LAB:
    CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) ENGINE=MISSING
TRADE-OFFS:
    MyISAM când accepți că ROLLBACK nu scoate rândul. InnoDB când commit trebuie să păstreze rândul.
ANTI-PATTERNS:
    ENGINE=MyISAM urmat de ROLLBACK pe o scriere care trebuia să dispară.
STAFF QUESTIONS:
    DatabaseTerminology_Staff_Interview.showEngines
NOVEL TRANSFER:
    Patul de spital: citești Transactions înainte să anulezi a doua ocupare.
SPACED REVIEW:
    O zi, șapte zile, treizeci de zile: Support DEFAULT, CREATE fără ENGINE, ENGINE=MISSING.
*/

/*
Definiția unui motor de stocare (storage engine):
       Componenta care execută propoziția pentru un tip de tabelă.

Exemplu:
       DatabaseTerminology_Terms.explainStorageEngine()
       SHOW ENGINES, rândul InnoDB, Support DEFAULT, Transactions YES.
*/

/*
🟢 Începător
    Ce este            lista de întrebări despre alegerea motorului, fără răspuns executat aici
    De ce e nevoie     fără ea, lecția nu are ce recapitula înainte de banca de cincizeci
🟡 Producție
    Cum se scrie       cinci întrebări în comentariu și Întrebări adăugate gol
🔴 Staff
    Unde               întrebarea se închide cu Support și Transactions, nu cu un click în Workbench
Pași                   1. Citești enunțul. 2. Răspunsul stă în DatabaseTerminology_Staff_Interview. 3. Nu umpli corpul clasei.
*/
// Începător: clasa este goală ca să poți adăuga întrebări fără să amesteci o a doua propoziție.
// Producție: nu există metodă. Propoziția SHOW ENGINES stă în DatabaseTerminology_Fast.chooseEngine. Dacă apare o metodă aici, clasa nu mai este lista goală.
// Expert: cele cinci întrebări despart InnoDB de MyISAM prin Transactions. Nu întreabă de filtrul WHERE.
public class DatabaseTerminology_Questions {
    /*
    1. Ce vezi pe SHOW ENGINES înainte să scrii primul rând în order_line, și ce îți spune coloana Transactions despre un ROLLBACK?
    2. CREATE TABLE order_line (id BIGINT NOT NULL, PRIMARY KEY (id)) fără ENGINE, pe MySQL 9.7, cu serverul lăsat pe motorul implicit: ce componentă ține rândul de mâine?
    3. De ce ENGINE=MyISAM nu este același lucru cu ENGINE=INNODB când a doua sesiune vrea să schimbe un alt rând din aceeași tabelă?
    4. ENGINE=MISSING: în ce caz tabela apare pe alt motor decât numele scris, și în ce caz nu apare deloc?
    5. Când lași InnoDB pe comenzile care trebuie să rămână după commit și când muți o căutare temporară pe MEMORY?

    Întrebări adăugate
    */
    // Începător: lista se oprește aici, fără o metodă care să răspundă în locul tău.
    // Producție: acolada închide clasa goală. Dacă adaugi o metodă deasupra ei, dispare contractul de corp gol.
    // Expert: închiderea nu execută SHOW ENGINES. Răspunsul cu Transactions stă în banca de interviu, nu în această acoladă.
}
