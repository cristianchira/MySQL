package _14_VIEW.fast;

import _14_VIEW.DB_VIEW;

/*
TOPIC:
    Lecția arată interogarea salvată pe dept_emp. Rândurile rămân în tabelă.
FAMILIE:
    Schema
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost cu parolă goală a primit Access denied (using password: NO). Nu există parolă în repo. Efectul pe server rămâne NERULAT AICI.
INTENT:
    Apelantul citește v_dept_emp_latest_date în loc să rescrie agregatul. După CREATE VIEW, rândul 10001 rămâne în dept_emp.
MECHANISM:
    main tipărește CREATE OR REPLACE VIEW din DB_VIEW. Serverul, când primește textul, face commit implicit și nu copiază rândurile.
LOOKALIKES:
    Tabela dept_emp. View-ul este interogarea salvată. Tabela ține rândurile. Nu pot purta același nume.
BOOK THEORY:
    Manualul 9.7, CREATE VIEW și algoritmii, deschise în tur: citirea din view este SELECT-ul salvat. GROUP BY și LIMIT opresc MERGE. PDF-ul SQL+Views.pdf spune că view-ul copiază comportamentul unei tabele și că nu se poate scrie în el; manualul admite UPDATE doar pe view-ul cu relație unu-la-unu.
BOOK EXAMPLE:
    DB_VIEW.createLatestDateView()
REPO DEMO:
    src/main/java/_14_VIEW/DB_VIEW.java
BREAK LAB:
    INSERT în v_dept_emp_latest_date este respins. Șirul nu a fost trimis.
TRADE-OFFS:
    Tabelă derivată când nu vrei commit implicit. v_dept_emp_open când scrierea trebuie să schimbe dept_emp.
ANTI-PATTERNS:
    INSERT în view-ul cu MAX și GROUP BY, sau LIMIT exterior peste LIMIT 10 din definiție.
STAFF QUESTIONS:
    View_Staff_Interview.recognizeStoredQuery()
NOVEL TRANSFER:
    Registrul de ture: v_shift_latest pe shift_log, nu dept_emp.
SPACED REVIEW:
    O zi, șapte zile, treizeci de zile: unde rămâne rândul, cine respinge INSERT-ul, cine face commit implicit.
BEST PRACTICES:
    1. Cazul corect
           -> DECIDE salvezi agregatul pe emp_no -> DB_VIEW.createLatestDateView() -> CREATE OR REPLACE VIEW v_dept_emp_latest_date -> nu garantează că dept_emp există
    2. Eșecul cel mai mic
           -> DECIDE INSERT în view-ul agregat -> DB_VIEW.insertIntoLatestDateView() -> scrierea este respinsă -> nu apare rândul în dept_emp
    3. Granița
           -> propoziția este a serverului; println din main nu creează view-ul
    4. Alternativa
           -> tabelă derivată când tranzacția curentă trebuie să rămână deschisă; v_dept_emp_open când UPDATE-ul schimbă dept_no
    5. Anti-patternul
           -> INSERT în v_dept_emp_latest_date sau LIMIT 5 peste un view care are deja LIMIT 10
    6. Dovada
           -> main tipărește DB_VIEW.createLatestDateView(). Planul nu a fost citit
    7. Interviu
           -> view-ul salvează SELECT-ul, rândul rămâne în dept_emp, agregatul nu primește INSERT

Definiția unei vederi (view):
       View-ul este interogarea salvată prin CREATE VIEW. Rândurile rămân în dept_emp.

Exemplu:
       View_Terms.explainView()
       v_dept_emp_latest_date nu primește o copie a rândului 10001.

Definiția unei tabele de bază (base table):
       Tabela de bază ține rândurile. View-ul doar le arată.

Exemplu:
       View_Terms.explainBaseTable()
       emp_no 10001 rămâne în dept_emp după DROP VIEW.

Definiția unei coloane de view (view column):
       Numele coloanelor se îngheață la CREATE VIEW. max_date este aliasul agregatului.

Exemplu:
       View_Terms.explainViewColumn()
       O coloană nouă în dept_emp nu apare în view.

Definiția unui index folosit prin îmbinare (MERGE index):
       La MERGE, planul poate folosi indexul lui dept_emp. Acest view nu se îmbină.

Exemplu:
       View_Terms.explainMergeIndex()
       GROUP BY și LIMIT opresc îmbinarea.

Definiția unui lacăt eliberat la tabela temporară (TEMPTABLE lock):
       La TEMPTABLE, lacătele de pe dept_emp pot fi eliberate după umplerea tabelei temporare.

Exemplu:
       View_Terms.explainTemptableLock()
       View-ul cu agregat nu rămâne updatable.

Definiția unei tranzacții întrerupte de view (implicit commit):
       CREATE VIEW face commit implicit înainte și după. UPDATE-ul prin view-ul updatable nu este în acea listă.

Exemplu:
       View_Terms.explainImplicitCommit()
       CREATE OR REPLACE VIEW închide tranzacția deschisă.
*/

/*
🟢 Începător
    Ce este            lecția care arată interogarea salvată, nu o tabelă nouă
    De ce e nevoie     fără ea, CREATE VIEW este confundat cu o copie a lui dept_emp
🟡 Producție
    Cum se scrie       CREATE OR REPLACE VIEW v_dept_emp_latest_date AS SELECT emp_no, MAX(from_date) AS max_date, MIN(from_date) AS min_date FROM dept_emp GROUP BY emp_no LIMIT 10
🔴 Staff
    Unde               textul pleacă din DB_VIEW; serverul îl execută doar când îl primește
Pași                   1. citește createLatestDateView
                       2. tipărește textul, fără sesiune inventată
*/
// Începător: clasa arată propoziția prin care salvezi agregatul pe emp_no.
// Producție: exemplul citește CREATE VIEW din DB_VIEW. Dacă clasa dispare, lecția scurtă nu mai are intrare.
// Expert: clasa nu predă fiecare clauză ALGORITHM și nu înlocuiește DB_VIEW. Nu garantează un cont autentificat.
public final class View_Fast
// Începător: de aici începe exemplul minim.
// Producție: fără acoladă, main nu poate citi CREATE VIEW.
// Expert: acolada nu este o sesiune și nu face commit.
{

    /*
    🟢 Începător
        Ce este            exemplul care ia propoziția gata scrisă și o arată
        De ce e nevoie     fără main nu vezi textul care salvează agregatul
    🟡 Producție
        Cum se scrie       CREATE OR REPLACE VIEW v_dept_emp_latest_date AS SELECT emp_no, MAX(from_date) AS max_date, MIN(from_date) AS min_date FROM dept_emp GROUP BY emp_no LIMIT 10
    🔴 Staff
        Unde               println nu este mysqld; commit-ul implicit se întâmplă doar pe server
    Pași                   1. citește createLatestDateView
                           2. tipărește textul
    */
    // Începător: de aici pornește exemplul care arată interogarea salvată.
    // Producție: main nu trimite INSERT-ul în view. Dacă dispare, nu mai rămâne o intrare care să tipărească CREATE VIEW.
    // Expert: main nu deschide Workbench. Nu garantează că IS_UPDATABLE este NO până când serverul execută CREATE VIEW.
    public static void main(String[] args)
    // Începător: înăuntru se citește propoziția, nu se copiază dept_emp.
    // Producție: fără acoladă, apelul către createLatestDateView nu are corp.
    // Expert: acolada nu eliberează lacătele de TEMPTABLE.
    {
        // Începător: aici este interogarea pe care serverul o salvează pentru cele zece grupuri.
        // Producție: valoarea este CREATE OR REPLACE VIEW din clasa deja scrisă. Fără linie, exemplul inventează un al doilea text.
        // Expert: apelul nu este INSERT-ul respins și nu este UPDATE-ul prin v_dept_emp_open. Nu calculează el MAX(from_date).
        String sql = DB_VIEW.createLatestDateView();
        // Începător: pe ecran apare interogarea salvată, nu rândurile din dept_emp.
        // Producție: println scrie CREATE OR REPLACE VIEW v_dept_emp_latest_date. Dacă dispare, lecția nu mai arată propoziția.
        // Expert: consola nu este mysqld, deci tranzacția deschisă nu se închide aici. Linia nu dovedește privilegiul CREATE VIEW.
        System.out.println(sql);
    // Începător: exemplul se oprește după ce a arătat interogarea.
    // Producție: fără închidere, main nu compilează.
    // Expert: închiderea nu execută DROP VIEW și nu lasă rândul 10001 în dept_emp, pentru că nu a deschis sesiunea.
    }
// Începător: lecția scurtă se termină la această clasă.
// Producție: fără închidere, fișierul nu compilează și propoziția nu poate fi tipărită.
// Expert: închiderea clasei nu șterge DB_VIEW și nu schimbă steagul IS_UPDATABLE.
}
