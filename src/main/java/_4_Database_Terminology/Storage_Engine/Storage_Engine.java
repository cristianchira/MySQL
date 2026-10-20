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
public class Storage_Engine {
    // Începător: locul motorului de stocare se oprește aici. Rândurile se schimbă abia când serverul execută propoziția.
    // Producție: acolada închide clasa. Fără ea, fișierul nu compilează pe Java 27.
    // Expert: închiderea nu alege InnoDB. Alegerea se vede pe SHOW ENGINES, în lecția scurtă a folderului părinte.
}
