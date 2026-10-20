package _16_SQL_Variables.LOCAL_VARIABLES;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8745688#questions

// https://dev.mysql.com/doc/refman/8.0/en/user-variables.html
// HISTORICAL: linkul de deasupra este pagina 8.0 despre @var. Manualul citit în tur pentru variabila locală este 9.7, 15.6.4.1 DECLARE, nu user-variables.


/*                            Types of MySQL Variables – Local Variables


    ○ Scope = visibility

                  * the region of a computer program where a phenomenon,

                                - such as a variable, is considered valid


    ○ Local Variable

                  * a variable that is visible

                                - only in the BEGIN – END block in which it was created.

                                - procedure or functions

    ○ DECLARE var_name tip declară variabila locală. Același cuvânt declară și un cursor sau un handler, iar variabila trebuie să stea înaintea lor.

                  * HISTORICAL: DECLARE is a keyword that can be used when creating Local Variables only. Manualul 9.7, 15.6.4.1: variable declarations must appear before cursor or handler declarations.



 */

/*
TOPIC:
    DECLARE v_avg_salary DECIMAL(10,2) creează o variabilă vizibilă doar în blocul BEGIN...END în care este declarată. La END dispare. Nu este @ și nu este @@SESSION.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied for user 'root'@'localhost' (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Media calculată în rutină nu trebuie să rămână pe conexiune după END. Niciun rând nu se scrie doar pentru că variabila există.
MECHANISM:
    Serverul creează variabila la intrarea în bloc, o inițializează cu NULL dacă lipsește DEFAULT, și o scapă la END. Un bloc interior care declară același nume acoperă numele din afară. În SELECT, numele care se potrivește cu variabila este citit ca variabilă, nu ca coloană.
LOOKALIKES:
    SET @p_emp_no = 0 rămâne pe sesiune după CALL, până la deconectare. SET SESSION sql_mode schimbă o variabilă de sistem, nu v_avg_salary. SET LOCAL sql_mode este sinonim cu SET SESSION, nu cu DECLARE.
BOOK THEORY:
    Manualul 9.7, 15.6.4.1, deschis în tur: DECLARE var_name tip DEFAULT expresie. Fără DEFAULT, valoarea este NULL. Declararea stă înaintea cursorului și a handler-ului. Domeniul este BEGIN...END. 15.6.4.2: o variabilă locală nu poate fi referită într-un prepared statement creat în rutină, pentru că prepared statement trăiește pe sesiune. Dacă numele coincide cu o coloană, MySQL citește variabila: în exemplul manualului, SELECT ... WHERE xname = xname întoarce 'bob', nu coloana. SET var = DEFAULT pe o locală este eroare de sintaxă în 9.7. Local+Variables.pdf spune că domeniul este regiunea în care variabila este validă, că v_avg_salary se vede doar în BEGIN...END și că DECLARE creează doar variabile locale. Ultima propoziție contrazice 15.6.4.1, care pune DECLARE și pe cursor și pe handler. PDF-ul nu conține propoziția CREATE PROCEDURE cu DECLARE v_avg_salary.
BOOK EXAMPLE:
    declareAvgSalaryInBlock
REPO DEMO:
    src/main/java/_16_SQL_Variables/LOCAL_VARIABLES/LOCAL_VARIABLES_.java
BREAK LAB:
    breakDeclareOutsideRoutine ține DECLARE v_avg_salary DECIMAL(10,2) în afara unui program stocat. Nu este o propoziție de sine stătătoare. În tur nu a fost trimisă.
TRADE-OFFS:
    SET @p_emp_no când propoziția de după CALL, pe aceeași conexiune, trebuie să vadă valoarea. Valoarea care nu intră în nicio propoziție rămâne în aplicație.
ANTI-PATTERNS:
    Același nume pentru variabila locală și pentru coloană. WHERE xname = xname compară variabila cu ea, iar coloana nu filtrează.
STAFF QUESTIONS:
    Întrebările 1–50 din SqlVariables_Staff_Interview.
NOVEL TRANSFER:
    O rutină de preț pe o rețetă ține media în v_avg_salary doar cât durează blocul. După END, tejgheaua nu mai vede media. Dacă următorul SELECT de pe aceeași conexiune trebuie să o vadă, nu mai este variabilă locală.
SPACED REVIEW:
    O zi: 21 octombrie 2026, END scapă variabila. Șapte zile: 27 octombrie 2026, numele comun cu coloana citește variabila. Treizeci de zile: 19 noiembrie 2026, @ dacă valoarea trebuie să treacă de CALL.

1. Enunț: Ce problemă rezolvă DECLARE v_avg_salary în rutină?
   Capcană: Variabila rămâne pe conexiune, ca @, ca să o poată citi următorul SELECT.
   Cheie: declareAvgSalaryInBlock o creează în BEGIN...END. La END dispare. Niciun rând nu se scrie doar din declarație.
   Demonstrație: declareAvgSalaryInBlock
2. Enunț: Ce se întâmplă cu valoarea și cu schema la END?
   Capcană: DECIMAL(10,2) adaugă o coloană v_avg_salary în tabelă.
   Cheie: Tipul este al variabilei, nu o coloană nouă. La END variabila nu mai există. Nu rămâne index sau lacăt. Efectul asupra numelui se vede doar în timpul blocului.
   Demonstrație: declareAvgSalaryInBlock
3. Enunț: De ce nu este SET @p_emp_no = 0?
   Capcană: Ambele sunt variabile de utilizator și mor odată cu rutina.
   Cheie: @p_emp_no rămâne pe sesiune până la deconectare și este vizibil și în afara procedurii. v_avg_salary nu. SET LOCAL sql_mode nu declară o locală: este sesiunea de sistem.
   Demonstrație: declareAvgSalaryInBlock
4. Enunț: Cum eșuează DECLARE v_avg_salary DECIMAL(10,2) trimis singur?
   Capcană: Serverul creează o variabilă de sesiune cu acest nume.
   Cheie: breakDeclareOutsideRoutine nu este într-un program stocat. Serverul nu începe un bloc. Nu apare nici @, nici coloană. În tur nu a fost trimis.
   Demonstrație: breakDeclareOutsideRoutine
5. Enunț: Când lași media în aplicație sau în @?
   Capcană: Orice medie trebuie declarată pe server, altfel rutina nu are memorie.
   Cheie: Dacă nicio propoziție din rutină nu citește media, ea rămâne în aplicație. Dacă SELECT-ul de după CALL trebuie să o vadă, este @, nu DECLARE. DECLARE rămâne când valoarea nu are voie să iasă din bloc.
   Demonstrație: declareAvgSalaryInBlock

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE valoarea moare la END -> declareAvgSalaryInBlock -> DECLARE v_avg_salary DECIMAL(10,2) în BEGIN...END -> nu garantează că un SELECT de după CALL o mai vede
    2. Eșecul cel mai mic
           -> DECIDE trimiți DECLARE singur -> breakDeclareOutsideRoutine -> DECLARE v_avg_salary DECIMAL(10,2) -> nu garantează un cod de eroare citit în tur
    3. Granița
           -> DECLARE în programul stocat este mecanismul. Linkul 8.0 către user-variables este pagina lui @. Cursorul și handler-ul folosesc tot DECLARE, dar nu sunt această variabilă.
    4. Alternativa
           -> SET @p_emp_no când valoarea trebuie să treacă de END pe aceeași conexiune. Aplicația când serverul nu o citește.
    5. Anti-patternul
           -> Variabila locală cu numele coloanei. WHERE-ul compară variabila cu ea, iar rândurile nu se filtrează după coloană.
    6. Dovada
           -> declareAvgSalaryInBlock și breakDeclareOutsideRoutine. root@localhost a fost refuzat.
    7. Interviu
           -> DECLARE trăiește în bloc și dispare la END. @ rămâne pe sesiune. Același nume cu coloana citește variabila, nu coloana.
*/

public class LOCAL_VARIABLES_ {

    /*
    🟢 Începător
        Ce este            variabila care există doar cât rulează blocul procedurii
        De ce e nevoie     fără procedură, DECLARE pare o propoziție pe care o poți trimite oriunde
    🟡 Producție
        Cum se scrie       CREATE PROCEDURE pay_batch() BEGIN DECLARE v_avg_salary DECIMAL(10,2); SET v_avg_salary = 0; END
    🔴 Staff
        Unde               blocul BEGIN...END. La END numele dispare. Nu este o coloană și nu este @p_emp_no
    Pași
        1. Ține programul care conține DECLARE.
        2. Nu îl trimite în tur.
        3. Nu pune DECLARE după un cursor.
    */
    // Începător: metoda păstrează rutina în care media nu iese din bloc.
    // Producție: public static String declareAvgSalaryInBlock(). Dacă dispare, rămâne doar definiția de curs, fără propoziție.
    // Expert: Nu creează procedura pe 3306. Nu este parametrul IN din SQL_VARIABLES. Prepared statement din rutină nu poate referi v_avg_salary.
    public static String declareAvgSalaryInBlock() {
        // Începător: în procedură apare o medie goală, NULL, vizibilă doar până la END. Tabela nu primește coloana v_avg_salary.
        // Producție: CREATE PROCEDURE pay_batch() BEGIN DECLARE v_avg_salary DECIMAL(10,2); SET v_avg_salary = 0; END. Fără DEFAULT, declarația pornește de la NULL, iar SET scrie 0 în variabila locală. Dacă linia dispare, nu mai ai DECLARE în cod.
        // Expert: Un bloc interior cu același nume acoperă acesta. Dacă o coloană se numește v_avg_salary, SELECT-ul din bloc citește variabila. SET LOCAL sql_mode nu este această linie. În tur nu a fost trimisă.
        return "CREATE PROCEDURE pay_batch() BEGIN DECLARE v_avg_salary DECIMAL(10,2); SET v_avg_salary = 0; END";
        // Începător: metoda a înapoiat rutina, nu a lăsat media pe conexiune.
        // Producție: acolada închide declareAvgSalaryInBlock. Procedura nu a fost creată pe server.
        // Expert: Refuzul root@localhost lasă schema NERULATĂ. Acolada nu este DROP PROCEDURE.
    }

    /*
    🟢 Începător
        Ce este            DECLARE trimis fără rutină
        De ce e nevoie     fără el, pare că serverul deschide singur un bloc
    🟡 Producție
        Cum se scrie       DECLARE v_avg_salary DECIMAL(10,2)
    🔴 Staff
        Unde               eșecul este înainte de orice variabilă creată. Nu apare nici sesiune nouă, nici coloană
    Pași
        1. Ține textul în afara procedurii.
        2. Nu îl trimite.
    */
    // Începător: metoda păstrează declarația care nu are bloc.
    // Producție: public static String breakDeclareOutsideRoutine(). Dacă dispare, eșecul nu mai are propoziție.
    // Expert: Nu este ERROR 1229. Este lipsa programului stocat. @p_emp_no rămâne cealaltă cale, scrisă cu SET.
    public static String breakDeclareOutsideRoutine() {
        // Începător: serverul nu creează media și nu o lasă pe conexiune.
        // Producție: DECLARE v_avg_salary DECIMAL(10,2). Dacă linia dispare, rămâne doar procedura bună.
        // Expert: Declarația trebuie să stea înaintea cursorului, dar mai întâi trebuie să fie într-un BEGIN...END. În tur nu a fost trimisă.
        return "DECLARE v_avg_salary DECIMAL(10,2)";
        // Începător: metoda s-a oprit la textul fără bloc.
        // Producție: acolada închide breakDeclareOutsideRoutine.
        // Expert: Lipsa unei erori citite în tur nu face textul valid. Acolada nu deschide o rutină.
    }
}
