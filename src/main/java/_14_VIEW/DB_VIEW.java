package _14_VIEW;

//365 Careers
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8565322#questions/3445222


/*                                          SQL VIEW


    ▬▬▐ A virtual table whose contents
             are obtained from an existing
                 table or
                 tables,
                     called base tables

    Think of a view object as
        a view into the base table
        the view itself
            DOES NOT CONTAIN ANY REAL DATA;
        the data is physically stored in the base table
        the view simply shows the data contained in the base table

    A view acts as a shortcut for writing
        the same SELECT statement every time a new request has been made

    Acts as a dynamic table because it
          instantly reflects
              data and
              structural changes in the base table
    HISTORICAL: propoziția de deasupra nu este regula pentru schemă. Manualul 9.7, CREATE VIEW, deschis în tur: definiția este înghețată la creare. Un SELECT * nu primește o coloană adăugată mai târziu în dept_emp. O coloană ștearsă din dept_emp face citirea din view să eșueze. Rândul schimbat în dept_emp se vede la SELECT-ul următor, pentru că serverul reexecută interogarea salvată și nu ține o copie.

    Don’t forget they are not real, physical data sets,
        we cannot perform CRUD operations on view
            reflected in base table
                the information that has already been extracted.
        they should be seen as temporary virtual data tables
        retrieving information from base tables
    HISTORICAL: propoziția de deasupra nu este regula. Manualul 9.7, Updatable and Insertable Views, deschis în tur: un view cu coloane simple, pe o singură tabelă, poate primi UPDATE, iar rândul se schimbă în dept_emp. v_dept_emp_latest_date are MAX, MIN, GROUP BY și LIMIT, deci nu este updatable și INSERT-ul este respins. Nu există CREATE TEMPORARY VIEW, iar view-ul nu poate referi o tabelă TEMPORARY. ALGORITHM = TEMPTABLE umple o tabelă temporară la execuție; obiectul rămâne în schemă până la DROP VIEW.


                                             SQL


    ○ CREATE [OR REPLACE] VIEW v_name AS
       SELECT
           column_1, column_2,… column_n
        FROM
          table_name;


                                         EXAMPLE


    ○ CREATE OR REPLACE VIEW v_dept_emp_latest_date AS
        SELECT
            emp_no,
            MAX(from_date) AS max_date,
            MIN(from_date) AS min_date
        FROM
            dept_emp
        GROUP BY emp_no
        LIMIT 10;


                                           Execute VIEW


    ○  select * employees.from v_dept_emp_latest_date;
    HISTORICAL: propoziția de deasupra nu este SQL. Manualul 9.7: citirea este SELECT * FROM v_dept_emp_latest_date. Forma stă în readLatestDates().



                                         Using SQL Views


    ○ Saves a lot of coding time

    ○ Occupies no extra memory
    HISTORICAL: propoziția de deasupra nu este regula. Manualul 9.7, View Processing Algorithms, deschis în tur: MERGE nu păstrează rezultatul. TEMPTABLE aduce rezultatul într-o tabelă temporară pentru propoziție și poate elibera lacătele de pe dept_emp după ce tabela temporară există. Definiția stă în dicționar, nu rândurile.

 */

/*
TOPIC:
    CREATE VIEW salvează interogarea pe dept_emp. Rândurile rămân în tabelă. SELECT-ul din view le recalculează.
FAMILIE:
    Schema
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost cu parolă goală a primit Access denied (using password: NO). Nu există parolă în repo. Efectul pe server rămâne NERULAT AICI.
INTENT:
    Aceeași agregare de date pe emp_no nu se rescrie la fiecare cerere. După CREATE VIEW, în schemă există v_dept_emp_latest_date, iar rândurile lui 10001 rămân în dept_emp.
MECHANISM:
    mysqld execută CREATE OR REPLACE VIEW și face commit implicit înainte și după. SELECT * FROM v_dept_emp_latest_date reexecută SELECT-ul salvat. GROUP BY, MAX, MIN și LIMIT opresc îmbinarea, deci view-ul nu este updatable.
LOOKALIKES:
    Tabela dept_emp. View-ul este interogarea salvată. Tabela ține rândurile. Nu pot purta același nume. O tabelă derivată trăiește o propoziție și nu face commit implicit.
BOOK THEORY:
    Manualul 9.7, CREATE VIEW, deschis în tur: view-ul este interogarea stocată; citirea din el este, în efect, SELECT-ul din definiție. Definiția îngheață la creare. Tabela și view-ul împart spațiul de nume. OR REPLACE și IF NOT EXISTS nu stau împreună; al doilea CREATE VIEW fără ele este respins cu 1050 Table already exists. ORDER BY din view este ignorat dacă propoziția exterioară are propriul ORDER BY. LIMIT în view plus LIMIT în afară lasă nedefinit care limită se aplică. ALGORITHM MERGE îmbină textul, TEMPTABLE umple o tabelă temporară și face view-ul non-updatable, UNDEFINED preferă MERGE. Fără clauză, alege derived_merge din optimizer_switch. CREATE VIEW și DROP VIEW sunt în lista de commit implicit. Updatable and Insertable Views, deschis în tur: fără relație unu-la-unu, sau cu agregat, DISTINCT, GROUP BY, HAVING, UNION, subcerere în lista de SELECT ori ALGORITHM = TEMPTABLE, scrierea este respinsă. IS_UPDATABLE se fixează la CREATE VIEW. WITH CHECK OPTION, implicit CASCADED, refuză scrierea care nu trece WHERE-ul. MATERIALIZED este doar HeatWave. SQL+Views.pdf, deschis în tur, 28 de pagini: view-ul este tabelă virtuală, datele stau în tabela de bază, iar CREATE VIEW salvează SELECT-ul. PDF-ul spune că ocupă zero memorie, că reflectă schimbarea de structură și că nu se poate scrie în el. Manualul contrazice cele trei propoziții.
BOOK EXAMPLE:
    createLatestDateView
REPO DEMO:
    src/main/java/_14_VIEW/DB_VIEW.java
BREAK LAB:
    insertIntoLatestDateView ține INSERT-ul în view-ul cu GROUP BY. Serverul îl respinge. rejectClosingDateThroughCheckOption schimbă to_date în 2025-06-05 pe view-ul cu WHERE și WITH CHECK OPTION. În tur șirurile nu au plecat: root@localhost a fost refuzat.
TRADE-OFFS:
    Repeți SELECT-ul, sau îl pui ca tabelă derivată, când nu vrei obiect în schemă și nu vrei commit implicit. Tabela reală când agregatul trebuie păstrat. v_dept_emp_open când UPDATE-ul trebuie să schimbe rândul din dept_emp.
ANTI-PATTERNS:
    INSERT în v_dept_emp_latest_date, sau SELECT * FROM v_dept_emp_latest_date LIMIT 5 cât definiția are deja LIMIT 10.
STAFF QUESTIONS:
    Întrebările 1–50 din View_Staff_Interview. Cheia este scrisă acolo. Nu există main care să o citească.
NOVEL TRANSFER:
    Un registru de ture: v_shift_latest ține MAX(started_at) pe angajat, din shift_log. Rândurile rămân în shift_log. INSERT-ul în view-ul agregat este respins. Fereastra turei deschise, cu WHERE ended_at IS NULL și WITH CHECK OPTION, primește UPDATE pe zona, nu pe ended_at.
SPACED REVIEW:
    O zi: 21 octombrie 2026, rândul rămâne în dept_emp. Șapte zile: 27 octombrie 2026, INSERT-ul în view-ul cu GROUP BY este respins. Treizeci de zile: 19 noiembrie 2026, CREATE VIEW face commit implicit, tabela derivată nu.

Definiția unei vederi (view):
       View-ul este interogarea salvată prin CREATE VIEW. Rândurile rămân în dept_emp. SELECT-ul din view reexecută interogarea.

Exemplu:
       View_Terms.explainView()
       v_dept_emp_latest_date nu primește o copie a rândului 10001.

Definiția unei tabele de bază (base table):
       Tabela de bază ține rândurile. View-ul doar le arată, iar cele două nu pot avea același nume.

Exemplu:
       View_Terms.explainBaseTable()
       emp_no 10001 rămâne în dept_emp după DROP VIEW.

Definiția unei coloane de view (view column):
       Numele coloanelor se îngheață la CREATE VIEW. max_date este aliasul agregatului, nu o coloană stocată.

Exemplu:
       View_Terms.explainViewColumn()
       O coloană nouă în dept_emp nu apare în v_dept_emp_latest_date.

Definiția unui index folosit prin îmbinare (MERGE index):
       La MERGE, propoziția este rescrisă pe dept_emp și planul poate folosi indexul tabelei. View-ul nu are depozit propriu de rânduri.

Exemplu:
       View_Terms.explainMergeIndex()
       GROUP BY și LIMIT din v_dept_emp_latest_date opresc îmbinarea.

Definiția unui lacăt eliberat la tabela temporară (TEMPTABLE lock):
       La TEMPTABLE, lacătele de pe tabelele de bază pot fi eliberate după umplerea tabelei temporare. La MERGE rămân în propoziția îmbinată.

Exemplu:
       View_Terms.explainTemptableLock()
       View-ul cu agregat nu rămâne updatable.

Definiția unei tranzacții întrerupte de view (implicit commit):
       CREATE VIEW și DROP VIEW fac commit implicit înainte și după. UPDATE-ul prin view-ul updatable rămâne în tranzacția lui.

Exemplu:
       View_Terms.explainImplicitCommit()
       CREATE OR REPLACE VIEW v_dept_emp_latest_date închide tranzacția deschisă.

1. Enunț: Ce problemă rezolvă CREATE OR REPLACE VIEW v_dept_emp_latest_date pe dept_emp?
   Capcană: View-ul copiază cele zece grupuri într-o tabelă nouă, deci dept_emp poate fi golit.
   Cheie: createLatestDateView salvează SELECT-ul cu MAX(from_date), MIN(from_date) și GROUP BY emp_no. Rândurile rămân în dept_emp. Apelantul citește cu readLatestDates. Obiectul apare la sfârșitul lui CREATE VIEW și închide tranzacția deschisă.
   Demonstrație: createLatestDateView
2. Enunț: Ce se întâmplă cu rândul 10001 și cu tranzacția când creezi view-ul și apoi schimbi from_date în dept_emp?
   Capcană: View-ul ține data veche până la COMMIT, iar structura nouă a lui dept_emp intră singură în SELECT *.
   Cheie: CREATE VIEW nu copiază rândul și face commit implicit înainte și după. Următorul readLatestDates reexecută agregatul, deci data nouă se vede la sfârșitul acelui SELECT. Coloana adăugată după creare nu intră: definiția este înghețată. dropLatestDateView scoate obiectul și lasă rândul în dept_emp.
   Demonstrație: readLatestDates
3. Enunț: De ce v_dept_emp_latest_date nu este tabela dept_emp, nici o tabelă derivată din același SELECT?
   Capcană: Rezultatul este același, deci planul, numele și tranzacția sunt aceleași.
   Cheie: Tabela ține rândurile și poate purta indexul grupat. View-ul este interogarea salvată și nu poate lua numele dept_emp. Tabela derivată trăiește o propoziție și nu este în lista de commit implicit. createLatestDateView rămâne în schemă până la dropLatestDateView.
   Demonstrație: createLatestDateView
4. Enunț: Cum eșuează scrierea în view-ul agregat și închiderea datei prin fereastra deschisă?
   Capcană: INSERT-ul în v_dept_emp_latest_date scrie grupul în dept_emp, iar to_date = 2025-06-05 trece pentru că view-ul este doar un SELECT.
   Cheie: insertIntoLatestDateView este respins: MAX, MIN, GROUP BY și LIMIT nu lasă relație unu-la-unu, iar IS_UPDATABLE este NO. rejectClosingDateThroughCheckOption schimbă to_date din 9999-01-01 în 2025-06-05; WITH CHECK OPTION refuză rândul care nu ar mai trece WHERE-ul. updateDepartmentThroughOpenView, care lasă to_date neschimbat, schimbă dept_no în dept_emp.
   Demonstrație: insertIntoLatestDateView
5. Enunț: Când lași view-ul, când repeți SELECT-ul și când scrii o tabelă?
   Capcană: Orice SELECT repetat trebuie să fie CREATE VIEW, altfel serverul recalculează degeaba și nu ține datele.
   Cheie: View-ul când mai mulți apelanți citesc aceleași zece grupuri și acceptă commit-ul implicit. Tabelă derivată când agregatul trebuie să rămână în tranzacția curentă. Tabelă reală când grupul trebuie stocat: pe serverul 9.7, MATERIALIZED nu este acest view, este HeatWave. v_dept_emp_open când scrierea trebuie să ajungă în dept_emp. outerLimitOnLimitedView nu este o limită stabilă: definiția are deja LIMIT 10.
   Demonstrație: createOpenAssignmentView

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE aceleași zece grupuri pe emp_no, fără să copiezi dept_emp -> createLatestDateView -> CREATE OR REPLACE VIEW v_dept_emp_latest_date ... GROUP BY emp_no LIMIT 10 -> nu garantează că dept_emp există deja și nu garantează că rezultatul a fost citit: root@localhost a fost refuzat
    2. Eșecul cel mai mic
           -> DECIDE INSERT în view-ul agregat ca să „adaugi” data -> insertIntoLatestDateView -> INSERT INTO v_dept_emp_latest_date (emp_no, max_date, min_date) VALUES (10001, '1986-06-26', '1986-06-26') -> nu scrie rândul în dept_emp
    3. Granița
           -> Propoziția este a serverului. Șirul din Java nu creează view-ul. Click-ul din Workbench nu este mecanismul. Numele v_dept_emp_latest_date, fără CREATE VIEW, nu salvează SELECT-ul.
    4. Alternativa
           -> Tabelă derivată când nu ai voie să închizi tranzacția. Tabela dept_emp când rândul trebuie stocat. v_dept_emp_open când UPDATE-ul trebuie să schimbe dept_no și să rămână în fereastra to_date = 9999-01-01.
    5. Anti-patternul
           -> INSERT în view-ul cu GROUP BY, sau SELECT * FROM v_dept_emp_latest_date LIMIT 5 peste un view care are deja LIMIT 10. Prima scriere este respinsă. A doua lasă nedefinit care LIMIT se aplică.
    6. Dovada
           -> createLatestDateView ține CREATE VIEW. insertIntoLatestDateView ține INSERT-ul respins. viewUpdatableFlag ține citirea lui IS_UPDATABLE. Planul nu a fost citit: root@localhost a fost refuzat.
    7. Interviu
           -> View-ul salvează SELECT-ul, rândul rămâne în dept_emp, CREATE VIEW face commit implicit. View-ul cu MAX și GROUP BY nu primește INSERT. Tabela ține rândurile și nu poate avea același nume.
*/

public class DB_VIEW {

    /*
    🟢 Începător
        Ce este            interogarea salvată care arată, pe fiecare emp_no, prima și ultima from_date
        De ce e nevoie     fără ea, fiecare cerere rescrie agregatul, iar o tabelă nouă ar copia rândurile
    🟡 Producție
        Cum se scrie       CREATE OR REPLACE VIEW v_dept_emp_latest_date AS SELECT emp_no, MAX(from_date) AS max_date, MIN(from_date) AS min_date FROM dept_emp GROUP BY emp_no LIMIT 10
    🔴 Staff
        Unde               obiectul intră în schemă la sfârșitul propoziției și închide tranzacția; GROUP BY și LIMIT opresc MERGE, deci view-ul nu este updatable
    Pași
        1. Ține CREATE OR REPLACE VIEW pe dept_emp.
        2. Nu trimite șirul: root@localhost a fost refuzat.
        3. Nu îl numi copie a lui dept_emp.
    */
    // Începător: metoda păstrează propoziția prin care serverul salvează agregatul, fără să mute rândurile.
    // Producție: public static String createLatestDateView(). Dacă dispare, fast nu mai are view-ul de pe dept_emp.
    // Expert: Metoda nu deschide JDBC. CREATE VIEW face commit implicit. Nu înlocuiește view-ul cu CREATE TABLE.
    public static String createLatestDateView()
    // Începător: înăuntru stă interogarea salvată, nu rândul 10001.
    // Producție: fără acoladă, CREATE OR REPLACE VIEW nu poate fi întors.
    // Expert: acolada nu pornește mysqld și nu pune ALGORITHM = TEMPTABLE explicit.
    {
        // Începător: serverul ține minte SELECT-ul pe emp_no, iar angajații rămân în dept_emp.
        // Producție: CREATE OR REPLACE VIEW v_dept_emp_latest_date cu MAX(from_date), MIN(from_date), GROUP BY emp_no și LIMIT 10. Dacă linia dispare, rămâne comentariul că view-ul copiază tabela.
        // Expert: LIMIT din definiție, împreună cu GROUP BY, oprește MERGE. Un SELECT exterior cu alt LIMIT lasă nedefinit care limită se aplică. OR REPLACE cere și privilegiul DROP.
        return "CREATE OR REPLACE VIEW v_dept_emp_latest_date AS SELECT emp_no, MAX(from_date) AS max_date, MIN(from_date) AS min_date FROM dept_emp GROUP BY emp_no LIMIT 10";
        // Începător: metoda a înapoiat interogarea salvată, nu un rând din dept_emp.
        // Producție: acolada închide createLatestDateView. Șirul nu a plecat către 3306.
        // Expert: Refuzul root@localhost lasă efectul pe server NERULAT. Acolada nu este eroarea 1050, care apare la al doilea CREATE VIEW fără OR REPLACE.
    }

    /*
    🟢 Începător
        Ce este            citirea prin care vezi cele zece grupuri, luate din dept_emp
        De ce e nevoie     fără ea, pare că view-ul ține datele și că SELECT-ul nu mai atinge tabela
    🟡 Producție
        Cum se scrie       SELECT * FROM v_dept_emp_latest_date
    🔴 Staff
        Unde               la sfârșitul propoziției serverul reexecută definiția; coloanele sunt cele înghețate la CREATE VIEW
    Pași
        1. Ține SELECT-ul separat de CREATE.
        2. Nu îl trimite în tur.
        3. Nu scrie forma employees.from din comentariul vechi.
    */
    // Începător: metoda păstrează citirea care aduce grupurile din tabela de bază.
    // Producție: public static String readLatestDates(). Dacă dispare, efectul pe rânduri rămâne o poveste.
    // Expert: SELECT-ul nu este un index și nu ține lacăt după ce rezultatul a plecat. Nu dezgheață coloanele adăugate după creare.
    public static String readLatestDates()
    // Începător: înăuntru se cere rezultatul, nu se creează obiectul.
    // Producție: fără acoladă, SELECT * FROM v_dept_emp_latest_date nu iese din metodă.
    // Expert: acolada nu alege derived_merge.
    {
        // Începător: pe ecranul clientului apar cel mult zece grupuri, calculate din dept_emp.
        // Producție: SELECT * FROM v_dept_emp_latest_date. Dacă linia dispare, rămâne textul invalid select * employees.from.
        // Expert: Lista de coloane este emp_no, max_date, min_date, înghețată la creare. Un rând nou în dept_emp intră la această citire doar dacă grupul lui încape în LIMIT 10.
        return "SELECT * FROM v_dept_emp_latest_date";
        // Începător: metoda s-a oprit la citire.
        // Producție: acolada închide readLatestDates. Șirul a rămas în proces.
        // Expert: Nu s-a citit un plan. MERGE nu s-a putut aplica din cauza lui GROUP BY și LIMIT, dar șirul nu a fost trimis.
    }

    /*
    🟢 Începător
        Ce este            încercarea de a băga un grup direct în view, care nu ajunge în dept_emp
        De ce e nevoie     fără ea, pare că orice view primește INSERT
    🟡 Producție
        Cum se scrie       INSERT INTO v_dept_emp_latest_date (emp_no, max_date, min_date) VALUES (10001, '1986-06-26', '1986-06-26')
    🔴 Staff
        Unde               serverul respinge propoziția pentru că view-ul nu este updatable; IS_UPDATABLE a fost fixat la CREATE VIEW
    Pași
        1. Ține INSERT-ul pe view-ul agregat.
        2. Nu îl trimite în tur.
        3. Nu îl confunda cu UPDATE-ul prin v_dept_emp_open.
    */
    // Începător: metoda păstrează scrierea care nu are unde să pună rândul.
    // Producție: public static String insertIntoLatestDateView(). Dacă dispare, eșecul rămâne doar în PDF.
    // Expert: Agregatul, GROUP BY și LIMIT sunt motivul. Nu este refuzul lui WITH CHECK OPTION de pe cealaltă fereastră.
    public static String insertIntoLatestDateView()
    // Începător: înăuntru stă grupul care nu se scrie.
    // Producție: fără acoladă, INSERT-ul respins nu are metodă.
    // Expert: acolada nu setează updatable_views_with_limit.
    {
        // Începător: data 1986-06-26 pentru angajatul 10001 nu apare în dept_emp prin acest view.
        // Producție: INSERT INTO v_dept_emp_latest_date (emp_no, max_date, min_date) VALUES (10001, '1986-06-26', '1986-06-26'). Dacă linia dispare, nu mai ai cel mai mic eșec.
        // Expert: max_date este expresie, deci view-ul nu este nici insertable. ALGORITHM = TEMPTABLE ar fi respins scrierea și pe un view altfel simplu. Șirul nu a produs un rând de eroare în tur.
        return "INSERT INTO v_dept_emp_latest_date (emp_no, max_date, min_date) VALUES (10001, '1986-06-26', '1986-06-26')";
        // Începător: metoda a păstrat scrierea periculoasă, fără să adauge un rând.
        // Producție: acolada închide insertIntoLatestDateView. Șirul nu a plecat către 3306.
        // Expert: 1050 este numele deja folosit la CREATE. Aici semnul este refuzul de updatable, nu numele ocupat.
    }

    /*
    🟢 Începător
        Ce este            fereastra pe rândurile încă deschise, prin care poți schimba dept_emp
        De ce e nevoie     fără ea, rămâne afirmația că niciun view nu primește scriere
    🟡 Producție
        Cum se scrie       CREATE OR REPLACE VIEW v_dept_emp_open AS SELECT emp_no, dept_no, from_date, to_date FROM dept_emp WHERE to_date = '9999-01-01' WITH CHECK OPTION
    🔴 Staff
        Unde               relația este unu-la-unu, deci IS_UPDATABLE poate fi YES; CHECK OPTION, implicit CASCADED, păzește WHERE-ul
    Pași
        1. Ține view-ul simplu separat de agregat.
        2. Nu îl trimite în tur.
        3. Nu îl numi tabelă temporară.
    */
    // Începător: metoda păstrează fereastra prin care un rând deschis poate fi schimbat în tabela de bază.
    // Producție: public static String createOpenAssignmentView(). Dacă dispare, corecția la „nu se poate scrie” nu are propoziție.
    // Expert: Coloanele sunt referințe simple. Nu este MATERIALIZED și nu este ALGORITHM = TEMPTABLE.
    public static String createOpenAssignmentView()
    // Începător: înăuntru stau doar rândurile a căror to_date este 9999-01-01.
    // Producție: fără acoladă, CREATE VIEW cu WITH CHECK OPTION nu iese din metodă.
    // Expert: acolada nu creează un trigger pe view. Trigger-ul nu se poate lega de un view.
    {
        // Începător: vezi angajații încă deschiși și, la o scriere potrivită, se schimbă rândul din dept_emp.
        // Producție: CREATE OR REPLACE VIEW v_dept_emp_open pe emp_no, dept_no, from_date, to_date, cu WHERE to_date = '9999-01-01' și WITH CHECK OPTION. Dacă linia dispare, nu mai ai view-ul updatable.
        // Expert: CHECK OPTION fără LOCAL sau CASCADED este CASCADED. Pe o singură tabelă nu mai există un view dedesubt de verificat. SQL SECURITY rămâne DEFINER, contul care a creat view-ul.
        return "CREATE OR REPLACE VIEW v_dept_emp_open AS SELECT emp_no, dept_no, from_date, to_date FROM dept_emp WHERE to_date = '9999-01-01' WITH CHECK OPTION";
        // Începător: metoda a înapoiat fereastra, nu rândul mutat.
        // Producție: acolada închide createOpenAssignmentView. Șirul nu a fost executat.
        // Expert: Commit-ul implicit al lui CREATE VIEW nu este commit-ul UPDATE-ului de mai jos.
    }

    /*
    🟢 Începător
        Ce este            schimbarea departamentului pentru angajatul 10001, făcută prin fereastră și ajunsă în dept_emp
        De ce e nevoie     fără ea, nu se vede că scrierea updatable schimbă tabela de bază
    🟡 Producție
        Cum se scrie       UPDATE v_dept_emp_open SET dept_no = 'd005' WHERE emp_no = 10001
    🔴 Staff
        Unde               rândul se schimbă în dept_emp la sfârșitul UPDATE-ului; to_date rămâne 9999-01-01, deci CHECK OPTION nu refuză
    Pași
        1. Schimbă doar dept_no.
        2. Nu trimite șirul în tur.
        3. Nu îl pune pe view-ul cu GROUP BY.
    */
    // Începător: metoda păstrează scrierea care mută angajatul în departamentul d005.
    // Producție: public static String updateDepartmentThroughOpenView(). Dacă dispare, efectul pe rândul din dept_emp nu are propoziție.
    // Expert: SET nu atinge o coloană expresie. Nu închide tranzacția: UPDATE-ul nu este în lista de commit implicit.
    public static String updateDepartmentThroughOpenView()
    // Începător: înăuntru pleacă schimbarea de departament.
    // Producție: fără acoladă, UPDATE-ul prin view nu are metodă.
    // Expert: acolada nu pune lacăt după ce propoziția s-a terminat.
    {
        // Începător: departamentul angajatului 10001 se schimbă în dept_emp, nu într-o copie a view-ului.
        // Producție: UPDATE v_dept_emp_open SET dept_no = 'd005' WHERE emp_no = 10001. Dacă linia dispare, nu mai ai scrierea care trece.
        // Expert: Doar coloane dintr-o singură tabelă. Un join view ar fi cerut ca SET să numească o singură parte updatable. Șirul nu a fost trimis.
        return "UPDATE v_dept_emp_open SET dept_no = 'd005' WHERE emp_no = 10001";
        // Începător: metoda a păstrat mutarea de departament.
        // Producție: acolada închide updateDepartmentThroughOpenView. Rândul nu s-a schimbat în tur.
        // Expert: Nu este INSERT-ul în view-ul agregat și nu este refuzul de CHECK OPTION.
    }

    /*
    🟢 Începător
        Ce este            încercarea de a închide perioada punând to_date pe 2025-06-05, oprită de fereastră
        De ce e nevoie     fără ea, WHERE-ul pare doar un filtru de citire
    🟡 Producție
        Cum se scrie       UPDATE v_dept_emp_open SET to_date = '2025-06-05' WHERE emp_no = 10001
    🔴 Staff
        Unde               WITH CHECK OPTION refuză rândul care nu ar mai îndeplini to_date = 9999-01-01; dept_emp nu primește data nouă pe această cale
    Pași
        1. Schimbă to_date în data din slide.
        2. Nu trimite șirul în tur.
        3. Nu îl trata ca pe un UPDATE reușit.
    */
    // Începător: metoda păstrează închiderea de perioadă pe care fereastra nu o lasă să iasă din condiție.
    // Producție: public static String rejectClosingDateThroughCheckOption(). Dacă dispare, granița lui CHECK OPTION rămâne o frază.
    // Expert: Refuzul este al view-ului updatable, nu al view-ului cu GROUP BY. Nu ține loc de un trigger.
    public static String rejectClosingDateThroughCheckOption()
    // Începător: înăuntru data de închidere nu are voie să înlocuiască 9999-01-01 prin acest view.
    // Producție: fără acoladă, UPDATE-ul respins de CHECK OPTION nu are metodă.
    // Expert: acolada nu schimbă implicitul CASCADED în LOCAL.
    {
        // Începător: perioada angajatului 10001 nu se închide pe 2025-06-05 dacă scrii prin fereastra deschisă.
        // Producție: UPDATE v_dept_emp_open SET to_date = '2025-06-05' WHERE emp_no = 10001. Dacă linia dispare, nu mai ai eșecul de lângă scrierea care trece.
        // Expert: Rândul ar dispărea din view. CHECK OPTION îl refuză. Un UPDATE direct pe dept_emp ar scrie data, pentru că nu trece prin view. Șirul nu a fost trimis.
        return "UPDATE v_dept_emp_open SET to_date = '2025-06-05' WHERE emp_no = 10001";
        // Începător: metoda a păstrat închiderea respinsă.
        // Producție: acolada închide rejectClosingDateThroughCheckOption. Data din dept_emp nu s-a schimbat în tur.
        // Expert: Nu este 1050 și nu este refuzul de updatable al agregatului.
    }

    /*
    🟢 Începător
        Ce este            ștergerea view-ului, care lasă angajații în dept_emp
        De ce e nevoie     fără ea, DROP VIEW pare că golește tabela
    🟡 Producție
        Cum se scrie       DROP VIEW v_dept_emp_latest_date
    🔴 Staff
        Unde               obiectul dispare la sfârșitul propoziției și tranzacția se închide implicit; rândurile din dept_emp rămân
    Pași
        1. Ține DROP VIEW doar pe view-ul agregat.
        2. Nu îl trimite în tur.
        3. Nu îl înlocui cu DROP TABLE dept_emp.
    */
    // Începător: metoda păstrează scoaterea obiectului, nu a rândurilor.
    // Producție: public static String dropLatestDateView(). Dacă dispare, diferența față de tabelă nu are propoziție de ieșire.
    // Expert: DROP VIEW este commit implicit. Nu este TRUNCATE pe dept_emp și nu eliberează un index al view-ului, pentru că view-ul nu ține rândurile.
    public static String dropLatestDateView()
    // Începător: înăuntru dispare numele prin care citeai grupurile.
    // Producție: fără acoladă, DROP VIEW nu iese din metodă.
    // Expert: acolada nu șterge v_dept_emp_open.
    {
        // Începător: după această propoziție nu mai există v_dept_emp_latest_date, dar angajatul 10001 rămâne în dept_emp.
        // Producție: DROP VIEW v_dept_emp_latest_date. Dacă linia dispare, nu mai ai dovada că rândurile nu pleacă odată cu view-ul.
        // Expert: Propoziția este în lista de commit implicit, înainte și după. Un view cu același nume ca o tabelă nu se creează, deci DROP VIEW nu este calea pe care golești dept_emp.
        return "DROP VIEW v_dept_emp_latest_date";
        // Începător: metoda a păstrat scoaterea numelui.
        // Producție: acolada închide dropLatestDateView. Șirul nu a plecat către 3306.
        // Expert: Nu s-a citit information_schema.VIEWS. Obiectul nu a fost șters în tur.
    }

    /*
    🟢 Începător
        Ce este            o a doua limită pusă peste view-ul care are deja LIMIT 10
        De ce e nevoie     fără ea, pare că limita din afară o înlocuiește pe cea dinăuntru
    🟡 Producție
        Cum se scrie       SELECT * FROM v_dept_emp_latest_date LIMIT 5
    🔴 Staff
        Unde               manualul lasă nedefinit care LIMIT se aplică; nu este un plan pe care să te bazezi
    Pași
        1. Ține LIMIT 5 pe view-ul care are LIMIT 10.
        2. Nu îl trimite în tur.
        3. Nu îl folosi ca număr garantat de grupuri.
    */
    // Începător: metoda păstrează citirea la care nu știi dacă vin cinci grupuri sau zece.
    // Producție: public static String outerLimitOnLimitedView(). Dacă dispare, anti-patternul rămâne doar în card.
    // Expert: Aceeași regulă nu se aplică la ORDER BY: ORDER BY-ul exterior îl înlocuiește pe cel din view. Aici ambele sunt LIMIT.
    public static String outerLimitOnLimitedView()
    // Începător: înăuntru stau două limite pe același rezultat.
    // Producție: fără acoladă, SELECT-ul cu LIMIT 5 nu are metodă.
    // Expert: acolada nu alege algoritmul.
    {
        // Începător: cine cere cinci rânduri poate să nu primească cinci, pentru că view-ul are deja o limită.
        // Producție: SELECT * FROM v_dept_emp_latest_date LIMIT 5. Dacă linia dispare, nu mai ai propoziția cu efect nedefinit.
        // Expert: Nu este un refuz de updatable și nu este CHECK OPTION. Definirea fără LIMIT, iar limita doar în afară, este calea stabilă. Șirul nu a fost trimis.
        return "SELECT * FROM v_dept_emp_latest_date LIMIT 5";
        // Începător: metoda a păstrat citirea nesigură.
        // Producție: acolada închide outerLimitOnLimitedView. Nu s-a numărat niciun grup.
        // Expert: Nu s-a deschis un plan. derived_merge nu decide el care LIMIT câștigă.
    }

    /*
    🟢 Începător
        Ce este            citirea semnului pe care serverul l-a pus când a creat view-ul agregat
        De ce e nevoie     fără ea, updatable pare o bănuială de la primul INSERT
    🟡 Producție
        Cum se scrie       SELECT IS_UPDATABLE FROM information_schema.VIEWS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'v_dept_emp_latest_date'
    🔴 Staff
        Unde               steagul se fixează la CREATE VIEW; pentru agregat este NO, înainte de orice scriere
    Pași
        1. Citește steagul view-ului agregat.
        2. Nu îl trimite în tur.
        3. Nu îl confunda cu privilegiul CREATE VIEW.
    */
    // Începător: metoda păstrează întrebarea prin care vezi dacă view-ul are voie să schimbe tabela.
    // Producție: public static String viewUpdatableFlag(). Dacă dispare, IS_UPDATABLE rămâne doar un nume.
    // Expert: Steagul nu este un index și nu spune dacă fereastra v_dept_emp_open este insertable. Insertable cere, în plus, toate coloanele fără default.
    public static String viewUpdatableFlag()
    // Începător: înăuntru se citește semnul, nu se scrie un rând.
    // Producție: fără acoladă, SELECT-ul pe VIEWS nu iese din metodă.
    // Expert: acolada nu schimbă SQL SECURITY.
    {
        // Începător: răspunsul spune dacă grupurile pot fi scrise înapoi, iar pentru acest view nu pot.
        // Producție: SELECT IS_UPDATABLE FROM information_schema.VIEWS pentru v_dept_emp_latest_date în baza curentă. Dacă linia dispare, nu mai ai dovada de dinaintea INSERT-ului.
        // Expert: La v_dept_emp_open același câmp poate fi YES, iar scrierea unei coloane expresie tot este oprită. Șirul nu a fost citit în tur.
        return "SELECT IS_UPDATABLE FROM information_schema.VIEWS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'v_dept_emp_latest_date'";
        // Începător: metoda a păstrat citirea semnului.
        // Producție: acolada închide viewUpdatableFlag. Nu a venit niciun YES sau NO de pe server.
        // Expert: Refuzul root@localhost nu este valoarea steagului.
    }
    // Începător: clasa se oprește după propozițiile view-ului.
    // Producție: fără închidere, fișierul nu compilează și fast nu poate citi CREATE VIEW.
    // Expert: închiderea nu execută DROP VIEW și nu șterge comentariul vechi despre CRUD.
}
