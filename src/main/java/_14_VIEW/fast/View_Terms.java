package _14_VIEW.fast;

import _14_VIEW.DB_VIEW;

/*
🟢 Începător
    Ce este            glosarul care spune că view-ul salvează interogarea, iar dept_emp ține rândurile
    De ce e nevoie     fără termeni, CREATE VIEW și CREATE TABLE rămân aceeași propoziție
🟡 Producție
    Cum se scrie       CREATE OR REPLACE VIEW v_dept_emp_latest_date AS SELECT emp_no, MAX(from_date) AS max_date, MIN(from_date) AS min_date FROM dept_emp GROUP BY emp_no LIMIT 10
🔴 Staff
    Unde               fiecare metodă întoarce textul termenului, fără să deschidă o sesiune
Pași                   1. view-ul
                       2. tabela de bază
                       3. coloana, indexul prin MERGE, lacătul de TEMPTABLE, commit-ul implicit
*/
// Începător: clasa ține termenii prin care separi interogarea salvată de rândurile din dept_emp.
// Producție: metodele întorc CREATE VIEW, SELECT-ul, UPDATE-ul sau DROP VIEW. Dacă clasa dispare, definițiile nu mai au metodă.
// Expert: glosarul nu predă HeatWave MATERIALIZED ca depozit pe acest server. Nu garantează că IS_UPDATABLE a fost citit.
public final class View_Terms
// Începător: de aici încep termenii despre view.
// Producție: fără acoladă, metodele care întorc propoziția nu au clasă.
// Expert: acolada nu creează v_dept_emp_latest_date.
{

    /*
    Definiția unei vederi (view):
           View-ul este interogarea salvată prin CREATE VIEW. Rândurile rămân în dept_emp. SELECT-ul din view reexecută interogarea.

    Exemplu:
           View_Terms.explainView()
           v_dept_emp_latest_date nu primește o copie a rândului 10001.
    */
    /*
    🟢 Începător
        Ce este            interogarea pe care serverul o ține minte pentru grupurile de date
        De ce e nevoie     fără ea, fiecare cerere rescrie MAX și MIN
    🟡 Producție
        Cum se scrie       CREATE OR REPLACE VIEW v_dept_emp_latest_date AS SELECT emp_no, MAX(from_date) AS max_date, MIN(from_date) AS min_date FROM dept_emp GROUP BY emp_no LIMIT 10
    🔴 Staff
        Unde               obiectul apare la sfârșitul lui CREATE VIEW și închide tranzacția
    Pași                   1. întoarce createLatestDateView
    */
    // Începător: metoda dă textul prin care serverul salvează agregatul.
    // Producție: întoarce CREATE OR REPLACE VIEW. Dacă dispare, definiția nu mai are exemplu.
    // Expert: nu este CREATE TABLE dept_emp și nu copiază rândul 10001.
    public static String explainView()
    // Începător: înăuntru pleacă interogarea salvată.
    // Producție: fără acoladă, CREATE VIEW nu iese din metodă.
    // Expert: acolada nu pune ALGORITHM = MERGE pe un view care are GROUP BY.
    {
        // Începător: înapoi pleacă propoziția pe care serverul o poate salva.
        // Producție: return DB_VIEW.createLatestDateView() predă CREATE OR REPLACE VIEW. Fără linie, termenul nu are propoziție.
        // Expert: OR REPLACE cere privilegiul DROP. Un al doilea CREATE VIEW fără OR REPLACE este 1050, nu un al doilea depozit de rânduri.
        return DB_VIEW.createLatestDateView();
    // Începător: termenul view-ului se încheie aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu fixează IS_UPDATABLE pe server.
    }

    /*
    Definiția unei tabele de bază (base table):
           Tabela de bază ține rândurile. View-ul doar le arată, iar cele două nu pot avea același nume.

    Exemplu:
           View_Terms.explainBaseTable()
           emp_no 10001 rămâne în dept_emp după DROP VIEW.
    */
    /*
    🟢 Începător
        Ce este            locul în care rămâne angajatul după ce view-ul dispare
        De ce e nevoie     fără el, DROP VIEW pare că șterge datele
    🟡 Producție
        Cum se scrie       DROP VIEW v_dept_emp_latest_date
    🔴 Staff
        Unde               obiectul view dispare; rândul din dept_emp rămâne
    Pași                   1. întoarce dropLatestDateView
    */
    // Începător: metoda dă scoaterea view-ului, care nu golește tabela.
    // Producție: întoarce DROP VIEW v_dept_emp_latest_date. Dacă dispare, termenul de tabelă de bază nu are exemplu.
    // Expert: nu este DROP TABLE dept_emp. Același nume nu poate fi și tabelă, și view.
    public static String explainBaseTable()
    // Începător: înăuntru pleacă scoaterea numelui, nu a rândului.
    // Producție: fără acoladă, DROP VIEW nu poate fi întors.
    // Expert: acolada nu șterge indexul lui dept_emp.
    {
        // Începător: înapoi pleacă propoziția după care angajatul rămâne în tabelă.
        // Producție: return DB_VIEW.dropLatestDateView() predă DROP VIEW. Fără linie, nu arăți că rândurile nu pleacă.
        // Expert: DROP VIEW face commit implicit. Nu eliberează un depozit de rânduri al view-ului, pentru că acela nu există.
        return DB_VIEW.dropLatestDateView();
    // Începător: termenul tabelei de bază se încheie aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu golește dept_emp.
    }

    /*
    Definiția unei coloane de view (view column):
           Numele coloanelor se îngheață la CREATE VIEW. max_date este aliasul agregatului, nu o coloană stocată.

    Exemplu:
           View_Terms.explainViewColumn()
           O coloană nouă în dept_emp nu apare în v_dept_emp_latest_date.
    */
    /*
    🟢 Începător
        Ce este            lista emp_no, max_date, min_date, fixată când view-ul a fost creat
        De ce e nevoie     fără ea, SELECT * pare că urmărește orice coloană nouă din dept_emp
    🟡 Producție
        Cum se scrie       SELECT * FROM v_dept_emp_latest_date
    🔴 Staff
        Unde               citirea folosește coloanele înghețate; o coloană ștearsă din dept_emp face citirea să eșueze
    Pași                   1. întoarce readLatestDates
    */
    // Începător: metoda dă citirea care arată coloanele salvate, nu structura de azi a tabelei.
    // Producție: întoarce SELECT * FROM v_dept_emp_latest_date. Dacă dispare, termenul de coloană nu are exemplu.
    // Expert: max_date nu este insertable. Nu este o coloană generată a lui dept_emp.
    public static String explainViewColumn()
    // Începător: înăuntru pleacă citirea coloanelor înghețate.
    // Producție: fără acoladă, SELECT-ul nu iese din metodă.
    // Expert: acolada nu reface definiția după ALTER TABLE.
    {
        // Începător: înapoi pleacă cererea care aduce grupurile cu numele de la creare.
        // Producție: return DB_VIEW.readLatestDates() predă SELECT * FROM v_dept_emp_latest_date. Fără linie, înghețarea nu are propoziție.
        // Expert: Aliasul din view este verificat la 64 de caractere, nu la 256. O coloană adăugată în dept_emp nu intră în acest SELECT *.
        return DB_VIEW.readLatestDates();
    // Începător: termenul coloanei se încheie aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu schimbă lista înghețată de pe server.
    }

    /*
    Definiția unui index folosit prin îmbinare (MERGE index):
           La MERGE, propoziția este rescrisă pe dept_emp și planul poate folosi indexul tabelei. View-ul nu are depozit propriu de rânduri.

    Exemplu:
           View_Terms.explainMergeIndex()
           GROUP BY și LIMIT din v_dept_emp_latest_date opresc îmbinarea.
    */
    /*
    🟢 Începător
        Ce este            semnul că acest view nu lasă planul să se sprijine pe o îmbinare în dept_emp
        De ce e nevoie     fără el, un index pe view pare obiectul pe care îl creezi
    🟡 Producție
        Cum se scrie       SELECT IS_UPDATABLE FROM information_schema.VIEWS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'v_dept_emp_latest_date'
    🔴 Staff
        Unde               GROUP BY și LIMIT opresc MERGE; steagul de updatable este NO
    Pași                   1. întoarce viewUpdatableFlag
    */
    // Începător: metoda dă citirea semnului pus la creare, nu un CREATE INDEX pe view.
    // Producție: întoarce SELECT IS_UPDATABLE. Dacă dispare, termenul de index prin MERGE nu are exemplu.
    // Expert: indexul care poate intra în plan stă pe dept_emp, și doar dacă algoritmul chiar îmbină. Acest view nu îmbină.
    public static String explainMergeIndex()
    // Începător: înăuntru se întreabă dacă view-ul poate fi scris, semn că îmbinarea nu a fost posibilă.
    // Producție: fără acoladă, SELECT-ul pe VIEWS nu are metodă.
    // Expert: acolada nu creează un index secundar pe max_date.
    {
        // Începător: înapoi pleacă întrebarea al cărei răspuns, pentru agregat, este că nu se poate scrie.
        // Producție: return DB_VIEW.viewUpdatableFlag() predă citirea lui IS_UPDATABLE. Fără linie, nu legi indexul de algoritm.
        // Expert: TEMPTABLE nu folosește view-ul ca să actualizeze dept_emp. Indexul lui dept_emp rămâne al tabelei, nu al obiectului view.
        return DB_VIEW.viewUpdatableFlag();
    // Începător: termenul indexului prin îmbinare se încheie aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu citește EXPLAIN. Planul rămâne NERULAT AICI.
    }

    /*
    Definiția unui lacăt eliberat la tabela temporară (TEMPTABLE lock):
           La TEMPTABLE, lacătele de pe tabelele de bază pot fi eliberate după umplerea tabelei temporare. La MERGE rămân în propoziția îmbinată.

    Exemplu:
           View_Terms.explainTemptableLock()
           View-ul cu agregat nu rămâne updatable.
    */
    /*
    🟢 Începător
        Ce este            scrierea care nu ajunge în dept_emp tocmai pentru că view-ul nu se îmbină
        De ce e nevoie     fără ea, lacătul pare ținut tot timpul cât există view-ul
    🟡 Producție
        Cum se scrie       INSERT INTO v_dept_emp_latest_date (emp_no, max_date, min_date) VALUES (10001, '1986-06-26', '1986-06-26')
    🔴 Staff
        Unde               view-ul agregat nu este updatable; TEMPTABLE poate elibera lacătele după umplere, pe durata unei citiri
    Pași                   1. întoarce insertIntoLatestDateView
    */
    // Începător: metoda dă scrierea care dovedește că acest view nu ține rândul.
    // Producție: întoarce INSERT-ul în view-ul agregat. Dacă dispare, termenul de lacăt nu are exemplu.
    // Expert: lacătul nu rămâne după ce rezultatul a plecat. Nu este lacătul de rând al unui UPDATE pe dept_emp.
    public static String explainTemptableLock()
    // Începător: înăuntru pleacă scrierea respinsă.
    // Producție: fără acoladă, INSERT-ul nu poate fi întors.
    // Expert: acolada nu setează ALGORITHM = TEMPTABLE explicit; GROUP BY și LIMIT ajung tot la tabelă temporară.
    {
        // Începător: înapoi pleacă încercarea care nu pune data în dept_emp.
        // Producție: return DB_VIEW.insertIntoLatestDateView() predă INSERT-ul. Fără linie, nu ai propoziția pe care serverul o respinge.
        // Expert: Un view MERGE updatable ține tabelele de bază pe durata propoziției îmbinate. Acest INSERT nu ajunge acolo.
        return DB_VIEW.insertIntoLatestDateView();
    // Începător: termenul lacătului se încheie aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu eliberează un lacăt, pentru că șirul nu a fost trimis.
    }

    /*
    Definiția unei tranzacții întrerupte de view (implicit commit):
           CREATE VIEW și DROP VIEW fac commit implicit înainte și după. UPDATE-ul prin view-ul updatable rămâne în tranzacția lui.

    Exemplu:
           View_Terms.explainImplicitCommit()
           CREATE OR REPLACE VIEW v_dept_emp_latest_date închide tranzacția deschisă.
    */
    /*
    🟢 Începător
        Ce este            schimbarea de departament care rămâne în tranzacția curentă, spre deosebire de CREATE VIEW
        De ce e nevoie     fără ea, orice propoziție cu view pare că face commit
    🟡 Producție
        Cum se scrie       UPDATE v_dept_emp_open SET dept_no = 'd005' WHERE emp_no = 10001
    🔴 Staff
        Unde               UPDATE-ul nu este în lista de commit implicit; CREATE VIEW este
    Pași                   1. întoarce updateDepartmentThroughOpenView
    */
    // Începător: metoda dă scrierea care schimbă dept_emp fără să închidă tranzacția doar pentru că trece prin view.
    // Producție: întoarce UPDATE v_dept_emp_open SET dept_no = 'd005'. Dacă dispare, termenul de tranzacție nu desparte DML de DDL.
    // Expert: CREATE VIEW pe v_dept_emp_open chiar închide tranzacția. Acest UPDATE nu o închide.
    public static String explainImplicitCommit()
    // Începător: înăuntru pleacă mutarea de departament.
    // Producție: fără acoladă, UPDATE-ul nu iese din metodă.
    // Expert: acolada nu face COMMIT.
    {
        // Începător: înapoi pleacă schimbarea care ajunge în dept_emp și așteaptă tranzacția ei.
        // Producție: return DB_VIEW.updateDepartmentThroughOpenView() predă UPDATE-ul. Fără linie, commit-ul implicit rămâne lipit de orice view.
        // Expert: WITH CHECK OPTION nu este COMMIT. to_date rămâne 9999-01-01, deci fereastra lasă scrierea. CREATE VIEW de deasupra este propoziția care închide tranzacția.
        return DB_VIEW.updateDepartmentThroughOpenView();
    // Începător: termenul tranzacției se încheie aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu dă commit pe 3306.
    }
// Începător: glosarul se termină la termenii view-ului.
// Producție: fără închidere, cele șase metode nu compilează.
// Expert: închiderea nu șterge definițiile și nu creează view-ul pe server.
}
