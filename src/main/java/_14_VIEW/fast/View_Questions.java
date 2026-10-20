package _14_VIEW.fast;

/*
TOPIC:
    Întrebările de aici aleg între interogarea salvată v_dept_emp_latest_date și tabela dept_emp.
FAMILIE:
    Schema
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost cu parolă goală a primit Access denied (using password: NO). Nu există parolă în repo. Efectul pe server rămâne NERULAT AICI.
INTENT:
    Cine citește grupurile nu copiază rândurile. După propoziția corectă, 10001 rămâne în dept_emp.
MECHANISM:
    Serverul salvează SELECT-ul la CREATE VIEW și îl reexecută la citire. INSERT-ul în view-ul cu GROUP BY este respins.
LOOKALIKES:
    Tabela dept_emp. View-ul nu ține rândurile și nu poate lua același nume.
BOOK THEORY:
    Manualul 9.7: definiția îngheață, MERGE este preferat când se poate, TEMPTABLE face view-ul non-updatable, WITH CHECK OPTION păzește WHERE-ul. SQL+Views.pdf spune că nu se poate scrie niciodată; manualul limitează refuzul la view-ul fără relație unu-la-unu.
BOOK EXAMPLE:
    DB_VIEW.createLatestDateView()
REPO DEMO:
    src/main/java/_14_VIEW/DB_VIEW.java
BREAK LAB:
    INSERT în v_dept_emp_latest_date nu scrie data 1986-06-26 în dept_emp.
TRADE-OFFS:
    Tabelă derivată fără commit implicit. v_dept_emp_open când departamentul trebuie schimbat în dept_emp.
ANTI-PATTERNS:
    INSERT în view-ul agregat, sau LIMIT exterior peste LIMIT-ul din definiție.
STAFF QUESTIONS:
    View_Staff_Interview.recognizeStoredQuery()
NOVEL TRANSFER:
    Un panou de ture deschise, nu demo-ul dept_emp.
SPACED REVIEW:
    O zi, șapte zile, treizeci de zile.
BEST PRACTICES:
    1. Cazul corect
           -> DECIDE salvezi agregatul -> DB_VIEW.createLatestDateView() -> CREATE OR REPLACE VIEW -> nu copiază rândurile
    2. Eșecul cel mai mic
           -> DECIDE INSERT în agregat -> DB_VIEW.insertIntoLatestDateView() -> scrierea este respinsă
    3. Granița
           -> întrebarea din acest fișier nu este mecanismul; propoziția este a serverului
    4. Alternativa
           -> v_dept_emp_open pentru UPDATE; tabelă derivată când tranzacția trebuie să rămână deschisă
    5. Anti-patternul
           -> INSERT în v_dept_emp_latest_date
    6. Dovada
           -> DB_VIEW.createLatestDateView()
    7. Interviu
           -> rândul rămâne în dept_emp; view-ul cu GROUP BY nu primește INSERT

Definiția unei vederi (view):
       View-ul este interogarea salvată. Rândurile rămân în dept_emp.

Exemplu:
       View_Terms.explainView()
       v_dept_emp_latest_date nu copiază rândul 10001.

Definiția unei tabele de bază (base table):
       Tabela ține rândurile. View-ul nu poate purta numele ei.

Exemplu:
       View_Terms.explainBaseTable()
       DROP VIEW lasă emp_no 10001 în dept_emp.

Definiția unei coloane de view (view column):
       max_date este alias înghețat, nu o coloană stocată.

Exemplu:
       View_Terms.explainViewColumn()
       Coloana nouă din dept_emp nu intră în view.

Definiția unui index folosit prin îmbinare (MERGE index):
       Indexul stă pe dept_emp. View-ul agregat nu se îmbină.

Exemplu:
       View_Terms.explainMergeIndex()
       GROUP BY și LIMIT opresc MERGE.

Definiția unui lacăt eliberat la tabela temporară (TEMPTABLE lock):
       TEMPTABLE poate elibera lacătele de pe dept_emp după umplere.

Exemplu:
       View_Terms.explainTemptableLock()
       View-ul cu agregat nu este updatable.

Definiția unei tranzacții întrerupte de view (implicit commit):
       CREATE VIEW închide tranzacția deschisă. UPDATE-ul prin fereastra updatable nu o închide la fel.

Exemplu:
       View_Terms.explainImplicitCommit()
       CREATE OR REPLACE VIEW face commit implicit.
*/

/*
1. Enunț: Ce rămâne în dept_emp după CREATE OR REPLACE VIEW v_dept_emp_latest_date?
   Capcană: „View-ul copiază cele zece grupuri și dept_emp poate fi golit.”
   Cheie: Rândurile rămân în dept_emp. View-ul salvează SELECT-ul. CREATE VIEW face commit implicit.
   Demonstrație: DB_VIEW.createLatestDateView()
2. Enunț: De ce INSERT-ul cu 10001 și 1986-06-26 în v_dept_emp_latest_date nu adaugă un rând?
   Capcană: „Orice view este o tabelă virtuală în care se poate scrie.”
   Cheie: MAX, MIN, GROUP BY și LIMIT fac view-ul non-updatable. Serverul respinge INSERT-ul.
   Demonstrație: DB_VIEW.insertIntoLatestDateView()
3. Enunț: Când UPDATE v_dept_emp_open SET dept_no = 'd005' schimbă tabela, iar setarea to_date = '2025-06-05' nu?
   Capcană: „WHERE-ul din view filtrează doar citirea, deci ambele UPDATE-uri trec.”
   Cheie: dept_no lasă rândul în fereastra to_date = 9999-01-01. to_date = 2025-06-05 este respins de WITH CHECK OPTION.
   Demonstrație: DB_VIEW.updateDepartmentThroughOpenView()
4. Enunț: De ce SELECT * FROM v_dept_emp_latest_date LIMIT 5 nu garantează cinci grupuri?
   Capcană: „Limita din afară o înlocuiește pe cea din view, ca un ORDER BY exterior.”
   Cheie: Definiția are deja LIMIT 10. Două clauze LIMIT lasă nedefinit care se aplică. ORDER BY exterior chiar înlocuiește ORDER BY din view.
   Demonstrație: DB_VIEW.outerLimitOnLimitedView()
5. Enunț: Când nu creezi view-ul și lași SELECT-ul în propoziția curentă?
   Capcană: „CREATE VIEW este mereu mai ieftin și nu atinge tranzacția.”
   Cheie: CREATE VIEW face commit implicit. O tabelă derivată trăiește o propoziție și lasă tranzacția deschisă. Tabela reală este alegerea când grupul trebuie stocat.
   Demonstrație: DB_VIEW.createLatestDateView()

Întrebări adăugate:
*/

/*
🟢 Începător
    Ce este            locul întrebărilor despre interogarea salvată pe dept_emp
    De ce e nevoie     fără ele, view-ul și tabela rămân același obiect
🟡 Producție
    Cum se scrie       CREATE OR REPLACE VIEW v_dept_emp_latest_date AS SELECT emp_no, MAX(from_date) AS max_date, MIN(from_date) AS min_date FROM dept_emp GROUP BY emp_no LIMIT 10
🔴 Staff
    Unde               răspunsul stă în propoziția din DB_VIEW, nu în corpul acestei clase
Pași                   1. citești enunțul
                       2. corpul rămâne gol
*/
// Începător: clasa ține întrebările și nu trimite ea o propoziție.
// Producție: nu există metodă aici. Propoziția rămâne în DB_VIEW.createLatestDateView().
// Expert: un corp umplut ar muta demonstrația din clasa de topic. Clasa nu este mysqld.
public final class View_Questions
// Începător: înăuntru nu se execută SQL.
// Producție: fără acoladă, întrebările nu au o clasă în care să rămână.
// Expert: acolada goală nu creează v_dept_emp_latest_date și nu face commit implicit.
{
// Începător: corpul rămâne gol ca întrebările să stea în comentariu.
// Producție: nu se adaugă o metodă care să repete CREATE VIEW.
// Expert: golul nu este IS_UPDATABLE și nu respinge INSERT-ul.
}
