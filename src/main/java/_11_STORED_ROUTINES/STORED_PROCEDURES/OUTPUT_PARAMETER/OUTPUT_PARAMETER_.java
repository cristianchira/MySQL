package _11_STORED_ROUTINES.STORED_PROCEDURES.OUTPUT_PARAMETER;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8692220#questions


/*                                       Stored Procedures with an Output Parameter


    ○ every time you create a procedure containing both an IN and an OUT parameter,

                  * remember that you must use the SELECT-INTO structure
                  HISTORICAL: „must use the SELECT-INTO structure” nu este regula. Manualul 9.7, 15.2.1, deschis în tur: OUT poate fi scris și cu SET. Exemplul p face SET incr_param = incr_param + 1. SELECT INTO este una dintre scrieri, nu singura.

                  * in the query of this object’s body!

    ○ out parameter

                 * it will represent the variable containing

                 * the output value of the operation

                               - executed by the query of the stored procedure


                                                               Sql


    ○ DELIMITER $$
      CREATE PROCEDURE procedure_name(in parameter, out parameter)
      BEGIN
      SELECT * FROM employees
      LIMIT 1000;
      END$$
      DELIMITER ;


                                                        EXERCISE


    ○ Create a procedure called ‘emp_info’ that uses as parameters

                  * the first and the last name of an individual,

                  * and returns their employee number.

    ○ DELIMITER $$

    CREATE PROCEDURE emp_info(in p_first_name varchar(255), in p_last_name varchar(255), out p_emp_no integer)

BEGIN

                SELECT

                                e.emp_no

                INTO p_emp_no FROM

                                employees e

                WHERE

                                e.first_name = p_first_name

                                                AND e.last_name = p_last_name;

END$$

DELIMITER ;



 */

/*
TOPIC:
    Parametrul OUT primește NULL la intrarea în procedură și ține valoarea pe care apelantul o citește după CALL, din variabila de utilizator. emp_info scrie emp_no acolo, nu îl primește de la apelant.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    După nume, numărul angajatului rămâne în @p_emp_no. Rândurile din employees nu se schimbă. Fără OUT, numărul ar fi doar un set, nu o valoare în variabila sesiunii.
MECHANISM:
    Serverul execută șirul din outParameterReceivesEmpNo. 15.1.22: OUT începe cu NULL în corp. 15.2.1: la CALL treci @p_emp_no, apoi citești variabila. Scrierea poate fi SELECT INTO sau SET. Efectul se vede când CALL se întoarce, nu la CREATE.
LOOKALIKES:
    IN p_emp_no din emp_salary filtrează și nu scrie înapoi. INOUT pleacă cu valoarea apelantului, nu cu NULL. RETURNS de la funcție pune valoarea în expresie, nu în @p_emp_no.
BOOK THEORY:
    Manualul 9.7, 15.1.22 și 15.2.1, deschise în tur: OUT trece o valoare de la procedură la apelant; valoarea inițială în corp este NULL; la CALL se trece o variabilă de utilizator. Exemplul p folosește SELECT VERSION() INTO ver_param și SET incr_param = incr_param + 1. Output+Parameter.pdf, deschis în tur, arată un SELECT * FROM employees LIMIT 1000 care nu scrie parametrul OUT.
BOOK EXAMPLE:
    outParameterReceivesEmpNo
REPO DEMO:
    src/main/java/_11_STORED_ROUTINES/STORED_PROCEDURES/OUTPUT_PARAMETER/OUTPUT_PARAMETER_.java
BREAK LAB:
    callOutWithLiteral ține CALL emp_info('Bezalel', 'Simmel', 10001). 10001 nu este o variabilă în care OUT să scrie. În tur șirul nu a fost trimis.
TRADE-OFFS:
    Un set de rezultate când îți trebuie mai multe coloane, nu un singur emp_no. Funcția când emp_no trebuie să stea într-un SELECT, nu în @p_emp_no.
ANTI-PATTERNS:
    SELECT * în corp, ca în PDF, și apoi SELECT @p_emp_no: setul a plecat, variabila OUT a rămas NULL dacă nimeni nu a scris-o.
STAFF QUESTIONS:
    Întrebările 1–5 de mai jos.
NOVEL TRANSFER:
    Procedura care caută un colet după nume scrie codul în @bin_code. Codul nu era cunoscut la CALL. Să trimiți codul ca literal în poziția OUT nu îl aduce înapoi.
SPACED REVIEW:
    O zi: 21 octombrie 2026, OUT începe cu NULL. Șapte zile: 27 octombrie 2026, literalul nu primește emp_no. Treizeci de zile: 19 noiembrie 2026, SET scrie OUT la fel de legal ca SELECT INTO.

1. Enunț: Ce problemă rezolvă OUT p_emp_no în emp_info?
   Capcană: Numele este de ajuns: serverul întoarce emp_no în același loc în care ai scris numele, fără parametru.
   Cheie: outParameterReceivesEmpNo scrie e.emp_no în p_emp_no. Apelantul îl citește din @p_emp_no după CALL. Fără OUT, SELECT ar trimite un set, nu ar umple variabila sesiunii.
   Demonstrație: outParameterReceivesEmpNo
2. Enunț: Ce se întâmplă cu p_emp_no și cu rândul la intrarea în corp, înainte de SELECT INTO?
   Capcană: p_emp_no păstrează 10001 dacă l-ai pus în CALL, pentru că OUT înseamnă și intrare.
   Cheie: 15.1.22: valoarea inițială a unui OUT este NULL. Rândul din employees nu se schimbă. @p_emp_no se vede schimbat când CALL se întoarce, nu la CREATE.
   Demonstrație: outParameterReceivesEmpNo
3. Enunț: De ce OUT nu este IN din emp_salary și nu este RETURNS?
   Capcană: Toate trei întorc emp_no, deci SELECT @p_emp_no și SELECT f_emp_avg_salary(...) citesc același loc.
   Cheie: IN nu scrie înapoi. RETURNS pune valoarea în expresie, fără @. OUT cere variabilă la CALL și se citește după ce procedura se întoarce.
   Demonstrație: readUserVariableAfterCall
4. Enunț: Cum eșuează CALL emp_info('Bezalel', 'Simmel', 10001)?
   Capcană: Literalul este valoarea de ieșire, deci clientul primește 10001 schimbat în emp_no-ul real.
   Cheie: callOutWithLiteral nu dă o variabilă în care OUT să scrie. 15.2.1 cere o variabilă de utilizator ca să poți citi valoarea după ce procedura se întoarce.
   Demonstrație: callOutWithLiteral
5. Enunț: Când scrii OUT cu SET și când lași valoarea într-un set sau într-o funcție?
   Capcană: Orice OUT trebuie scris doar cu SELECT INTO, altfel parametrul rămâne NULL.
   Cheie: SET este legal, ca în exemplul p din 15.2.1. SELECT INTO este ce folosește emp_info. Setul de rezultate când îți trebuie coloane, nu o variabilă. Funcția când valoarea intră într-un SELECT.
   Demonstrație: readUserVariableAfterCall

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE emp_no după nume să rămână în sesiune -> outParameterReceivesEmpNo -> SELECT e.emp_no INTO p_emp_no -> nu garantează un singur rând: două persoane cu același nume fac SELECT INTO să eșueze
    2. Eșecul cel mai mic
           -> DECIDE literalul să primească ieșirea -> callOutWithLiteral -> CALL emp_info('Bezalel', 'Simmel', 10001) -> nu garantează că 10001 s-a transformat în emp_no
    3. Granița
           -> OUT este parametrul procedurii. @p_emp_no este variabila de sesiune care îl poartă. Java nu este locul în care se scrie emp_no.
    4. Alternativa
           -> SET când nu citești o tabelă. IN când valoarea doar filtrează. Funcția când rezultatul stă în expresie.
    5. Anti-patternul
           -> SELECT * FROM employees LIMIT 1000 în corp, apoi SELECT @p_emp_no. Setul pleacă, iar OUT rămâne NULL dacă nu l-ai scris.
    6. Dovada
           -> outParameterReceivesEmpNo ține SELECT INTO. readUserVariableAfterCall ține CALL și SELECT @p_emp_no. root@localhost a fost refuzat, using password: NO.
    7. Interviu
           -> OUT începe cu NULL. CALL primește @p_emp_no. După apel, SELECT @p_emp_no arată numărul. Literalul nu ține ieșirea. SET scrie OUT fără SELECT INTO.
*/

public class OUTPUT_PARAMETER_ {

    /*
    🟢 Începător
        Ce este            numărul angajatului, scris de procedură și citit de cel care a sunat
        De ce e nevoie     fără el, numele nu lasă emp_no într-o variabilă
    🟡 Producție
        Cum se scrie       CREATE PROCEDURE emp_info(IN p_first_name VARCHAR(255), IN p_last_name VARCHAR(255), OUT p_emp_no INTEGER) BEGIN SELECT e.emp_no INTO p_emp_no FROM employees e WHERE e.first_name = p_first_name AND e.last_name = p_last_name; END
    🔴 Staff
        Unde               p_emp_no intră NULL. @p_emp_no se umple când CALL se întoarce. SELECT INTO cu mai multe rânduri eșuează
    Pași
        1. Pune OUT la urmă, IN la nume.
        2. Scrie cu SELECT INTO.
        3. Nu trimite șirul în tur.
    */
    // Începător: metoda păstrează procedura care scrie numărul în afară.
    // Producție: public static String outParameterReceivesEmpNo(). Dacă dispare, OUT rămâne doar în comentariu.
    // Expert: SELECT INTO nu este singura scriere. SET este legal. Mai multe rânduri cu același nume nu lasă „un” emp_no.
    public static String outParameterReceivesEmpNo() {
        // Începător: procedura caută omul după nume și pune numărul unde poate fi citit după apel.
        // Producție: CREATE PROCEDURE emp_info(IN p_first_name VARCHAR(255), IN p_last_name VARCHAR(255), OUT p_emp_no INTEGER) BEGIN SELECT e.emp_no INTO p_emp_no FROM employees e WHERE e.first_name = p_first_name AND e.last_name = p_last_name; END. Fără șir, PDF-ul cu SELECT * LIMIT 1000 pare că umple OUT.
        // Expert: OUT nu păstrează un literal primit la CALL. IN pe nume filtrează. Funcția cu RETURNS nu umple acest parametru.
        return "CREATE PROCEDURE emp_info(IN p_first_name VARCHAR(255), IN p_last_name VARCHAR(255), OUT p_emp_no INTEGER) BEGIN SELECT e.emp_no INTO p_emp_no FROM employees e WHERE e.first_name = p_first_name AND e.last_name = p_last_name; END";
        // Începător: metoda a înapoiat procedura, nu numărul.
        // Producție: acolada închide outParameterReceivesEmpNo. Șirul nu a creat emp_info.
        // Expert: Refuzul root@localhost lasă @p_emp_no necitit. Acolada nu este SELECT INTO executat.
    }

    /*
    🟢 Începător
        Ce este            citirea numărului după ce procedura s-a întors
        De ce e nevoie     fără ea, pare că OUT se vede în CREATE
    🟡 Producție
        Cum se scrie       CALL emp_info('Bezalel', 'Simmel', @p_emp_no); SELECT @p_emp_no
    🔴 Staff
        Unde               variabila de utilizator se citește după CALL. Nu este rezultatul unui SELECT din funcție
    Pași
        1. Treci @p_emp_no la OUT.
        2. Citește variabila într-o propoziție de după.
        3. Nu trimite șirul în tur.
    */
    // Începător: metoda păstrează momentul în care numărul poate fi văzut.
    // Producție: public static String readUserVariableAfterCall(). Dacă dispare, momentul rămâne nedescris ca propoziție.
    // Expert: @p_emp_no trăiește pe sesiune. Nu este parametrul local și nu dispare la sfârșitul procedurii.
    public static String readUserVariableAfterCall() {
        // Începător: după apel, sesiunea ține numărul găsit pentru acel nume.
        // Producție: CALL emp_info('Bezalel', 'Simmel', @p_emp_no); SELECT @p_emp_no. Dacă linia dispare, nu mai ai citirea de după CALL.
        // Expert: Dacă numele lipsește, SELECT INTO nu scrie un rând și OUT poate rămâne NULL. Asta nu este eroarea literalului.
        return "CALL emp_info('Bezalel', 'Simmel', @p_emp_no); SELECT @p_emp_no";
        // Începător: metoda s-a oprit la citirea variabilei.
        // Producție: acolada închide readUserVariableAfterCall. Variabila nu a fost citită pe server.
        // Expert: Nu este SELECT f_emp_avg_salary(10001). Acolo valoarea este expresia, nu @p_emp_no.
    }

    /*
    🟢 Începător
        Ce este            apelul care pune un număr în locul în care procedura ar trebui să scrie
        De ce e nevoie     fără el, orice valoare din CALL pare o variabilă de ieșire
    🟡 Producție
        Cum se scrie       CALL emp_info('Bezalel', 'Simmel', 10001)
    🔴 Staff
        Unde               OUT nu are unde scrie. 15.2.1 cere o variabilă ca să citești valoarea după întoarcere
    Pași
        1. Pune literalul pe poziția OUT.
        2. Nu îl trimite în tur.
        3. Nu îl citi cu SELECT @.
    */
    // Începător: metoda păstrează apelul care nu lasă numărul nicăieri.
    // Producție: public static String callOutWithLiteral(). Dacă dispare, eșecul rămâne o frază.
    // Expert: 10001 pe poziție de IN ar filtra. Pe poziție de OUT nu este o variabilă de utilizator.
    public static String callOutWithLiteral() {
        // Începător: numărul căutat nu rămâne la cel care a sunat, pentru că nu i-a dat o variabilă.
        // Producție: CALL emp_info('Bezalel', 'Simmel', 10001). Dacă linia dispare, nu mai ai literalul pus pe OUT.
        // Expert: Nu este SET @p_emp_no = 10001 înainte de un INOUT. Aici poziția este OUT și valoarea nu are nume de variabilă.
        return "CALL emp_info('Bezalel', 'Simmel', 10001)";
        // Începător: metoda a păstrat apelul greșit.
        // Producție: acolada închide callOutWithLiteral. Șirul nu a plecat spre 3306.
        // Expert: Eșecul nu este ER_SP_NO_RETSET. Acela este un set trimis din funcție.
    }
}
