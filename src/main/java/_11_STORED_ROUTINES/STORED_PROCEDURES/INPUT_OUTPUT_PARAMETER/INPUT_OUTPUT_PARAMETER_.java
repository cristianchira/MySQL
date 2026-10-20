package _11_STORED_ROUTINES.STORED_PROCEDURES.INPUT_OUTPUT_PARAMETER;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8692226#questions  6: 11



/*                                    INPUT OUTPUT PARAMETER


    ○  You should also be aware that

                    * IN-OUT parameters exist as well.

    ○ They are used whenever you are working with stored procedures and trying TO OVERRIDE

                   * the content of a data point that has been used as an INPUT

                   * with the OUTPUT value obtained after running the calculation.


    ○ This means you are having the same variable

                    * as an IN

                    * and an OUT

            parameter.



 */



/*
TOPIC:
    Parametrul INOUT intră cu valoarea apelantului și iese cu valoarea lăsată de procedură în aceeași variabilă. Fără inițializare, intrarea este NULL, iar NULL + 1 rămâne NULL.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    O valoare de salariu ținută în @p_salary intră în procedură și iese mărită cu 1 în aceeași variabilă. Rândurile din salaries nu se schimbă: SET atinge parametrul, nu tabela.
MECHANISM:
    Serverul execută șirul din inoutReturnsTheSameVariable. 15.1.22: INOUT este inițializat de apelant, poate fi modificat, iar schimbarea se vede când CALL se întoarce. 15.2.1: exemplul p face SET incr_param = incr_param + 1 după ce @increment a fost 10 și devine 11.
LOOKALIKES:
    IN nu scrie înapoi. OUT începe cu NULL și ignoră valoarea cu care ai venit. O coloană salary schimbată cu UPDATE este un rând, nu parametrul.
BOOK THEORY:
    Manualul 9.7, 15.1.22 și 15.2.1, deschise în tur: INOUT se inițializează înainte de CALL; procedura îl poate modifica; apelantul vede valoarea când procedura se întoarce. Exemplul din manual: SET @increment = 10; CALL p(@version, @increment); SELECT arată 11. Nu există PDF în acest folder.
BOOK EXAMPLE:
    inoutReturnsTheSameVariable
REPO DEMO:
    src/main/java/_11_STORED_ROUTINES/STORED_PROCEDURES/INPUT_OUTPUT_PARAMETER/INPUT_OUTPUT_PARAMETER_.java
BREAK LAB:
    inoutStartsNull ține CALL emp_raise_marker(@p_salary) fără SET înainte. Intrarea este NULL. În tur șirul nu a fost trimis.
TRADE-OFFS:
    OUT când valoarea de intrare nu contează. UPDATE pe salaries când trebuie schimbat rândul, nu o variabilă de sesiune.
ANTI-PATTERNS:
    CALL emp_raise_marker(@p_salary) fără inițializare, apoi să crezi că salariul din tabelă a crescut.
STAFF QUESTIONS:
    Întrebările 1–5 de mai jos.
NOVEL TRANSFER:
    Un contor de colete ținut în @bin_count intră și iese mărit. Raftul din tabelă nu se mută. Fără valoarea inițială, contorul rămâne NULL.
SPACED REVIEW:
    O zi: 21 octombrie 2026, aceeași variabilă intră și iese. Șapte zile: 27 octombrie 2026, fără SET intrarea este NULL. Treizeci de zile: 19 noiembrie 2026, UPDATE este rândul, INOUT este variabila.

1. Enunț: Ce problemă rezolvă INOUT p_salary față de două variabile, una IN și una OUT?
   Capcană: Serverul nu are INOUT; rescrii tu valoarea în aplicație după CALL.
   Cheie: inoutReturnsTheSameVariable primește @p_salary și îl lasă mărit în același loc. Apelantul nu copiază ieșirea într-o a doua variabilă. Tabela salaries nu este în propoziție.
   Demonstrație: inoutReturnsTheSameVariable
2. Enunț: Ce se întâmplă cu @p_salary și cu rândurile când SET @p_salary = 10 este urmat de CALL?
   Capcană: Rândul din salaries cu salariul 10 devine 11, pentru că parametrul se numește p_salary.
   Cheie: Se schimbă variabila de sesiune: 10 intră, SET din corp face 11, iar 11 se vede când CALL se întoarce. Niciun rând nu este actualizat. Nu se ține lacăt pe salaries, pentru că tabelă nu este numită.
   Demonstrație: inoutReturnsTheSameVariable
3. Enunț: De ce INOUT nu este OUT și nu este IN?
   Capcană: OUT păstrează și el 10, iar IN îl întoarce mărit.
   Cheie: OUT ar începe cu NULL și ar pierde 10. IN ar primi 10 și apelantul ar vedea tot 10 după CALL. INOUT este singurul care duce 10 înăuntru și aduce 11 înapoi.
   Demonstrație: inoutReturnsTheSameVariable
4. Enunț: Cum eșuează CALL fără SET @p_salary înainte?
   Capcană: Parametrul pleacă de la 0, deci rezultatul este 1.
   Cheie: inoutStartsNull nu inițializează variabila. 15.2.1 cere inițializarea înainte de CALL. NULL + 1 este NULL. Nu apare un salariu nou în tabelă.
   Demonstrație: inoutStartsNull
5. Enunț: Când schimbi rândul cu UPDATE și când rămâi pe INOUT?
   Capcană: Orice creștere de salariu trebuie să fie INOUT, altfel nu este pe server.
   Cheie: INOUT când valoarea trăiește în variabila sesiunii și mai multe propoziții din aceeași sesiune o folosesc. UPDATE când rândul din salaries trebuie să rămână mărit și pentru alt client. Aplicația când nimeni altcineva nu citește variabila.
   Demonstrație: inoutStartsNull

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE aceeași variabilă intră și iese -> inoutReturnsTheSameVariable -> SET @p_salary = 10; CALL emp_raise_marker(@p_salary) -> nu garantează un rând schimbat în salaries
    2. Eșecul cel mai mic
           -> DECIDE apelul fără valoarea de intrare -> inoutStartsNull -> CALL emp_raise_marker(@p_salary) -> nu garantează 1: NULL + 1 rămâne NULL
    3. Granița
           -> INOUT este parametrul procedurii. @p_salary este variabila de sesiune. Numele p_salary nu este coloana salary.
    4. Alternativa
           -> OUT când intrarea nu contează. IN când nu vrei să se întoarcă. UPDATE când trebuie schimbat rândul.
    5. Anti-patternul
           -> CALL fără inițializare, citit ca o creștere de salariu în tabelă. Variabila rămâne NULL, iar salaries rămâne cum era.
    6. Dovada
           -> inoutReturnsTheSameVariable ține SET și CALL. inoutStartsNull ține CALL-ul gol. root@localhost a fost refuzat, using password: NO.
    7. Interviu
           -> INOUT duce valoarea și o aduce înapoi. 10 devine 11 în @p_salary. Fără SET, intrarea este NULL. Tabela nu se mută.
*/

public class INPUT_OUTPUT_PARAMETER_ {

    /*
    🟢 Începător
        Ce este            aceeași variabilă, care intră cu un număr și iese cu numărul mărit
        De ce e nevoie     fără ea, ieșirea și intrarea par două locuri
    🟡 Producție
        Cum se scrie       CREATE PROCEDURE emp_raise_marker(INOUT p_salary INT) BEGIN SET p_salary = p_salary + 1; END
    🔴 Staff
        Unde               10 devine 11 în @p_salary când CALL se întoarce. salaries nu este numit
    Pași
        1. Creează procedura cu INOUT.
        2. Inițializează @p_salary înainte de CALL.
        3. Nu trimite șirul în tur.
    */
    // Începător: metoda păstrează rutina care primește și înapoiază aceeași valoare.
    // Producție: public static String inoutReturnsTheSameVariable(). Dacă dispare, INOUT rămâne doar o descriere.
    // Expert: SET pe parametru nu este UPDATE pe salaries. OUT nu ar fi păstrat 10.
    public static String inoutReturnsTheSameVariable() {
        // Începător: zece intră și unsprezece se citește în aceeași variabilă, iar tabela rămâne.
        // Producție: CREATE PROCEDURE emp_raise_marker(INOUT p_salary INT) BEGIN SET p_salary = p_salary + 1; END; SET @p_salary = 10; CALL emp_raise_marker(@p_salary). Dacă linia dispare, nu mai ai perechea din 15.2.1, mutată pe salariul din curs.
        // Expert: Exemplul p din manual scrie și un OUT cu SELECT INTO. Aici nu există set de rezultate. NULL + 1 nu apare, pentru că SET @p_salary = 10 stă înainte.
        return "CREATE PROCEDURE emp_raise_marker(INOUT p_salary INT) BEGIN SET p_salary = p_salary + 1; END; SET @p_salary = 10; CALL emp_raise_marker(@p_salary)";
        // Începător: metoda a înapoiat intrarea și ieșirea, nu un rând mărit.
        // Producție: acolada închide inoutReturnsTheSameVariable. @p_salary nu a devenit 11 pe server.
        // Expert: Refuzul root@localhost lasă efectul NERULAT. Acolada nu este un COMMIT pe salaries.
    }

    /*
    🟢 Începător
        Ce este            apelul fără numărul de plecare
        De ce e nevoie     fără el, pare că INOUT pleacă de la zero
    🟡 Producție
        Cum se scrie       CALL emp_raise_marker(@p_salary)
    🔴 Staff
        Unde               variabila neinițializată este NULL la intrare. NULL + 1 nu este 1
    Pași
        1. Nu pune SET înainte.
        2. Nu trimite șirul.
        3. Nu citi rezultatul ca un salariu din tabelă.
    */
    // Începător: metoda păstrează apelul care uită valoarea de intrare.
    // Producție: public static String inoutStartsNull(). Dacă dispare, eșecul rămâne o frază.
    // Expert: OUT începe tot cu NULL, dar INOUT fără inițializare pierde tocmai valoarea pe care trebuia să o ducă.
    public static String inoutStartsNull() {
        // Începător: procedura nu are de unde să adauge unu, pentru că nu a primit un număr.
        // Producție: CALL emp_raise_marker(@p_salary). Dacă linia dispare, nu mai ai apelul fără SET @p_salary = 10.
        // Expert: 15.2.1 inițializează @increment înainte de CALL. Această linie sare pasul. Nu scrie salaries.
        return "CALL emp_raise_marker(@p_salary)";
        // Începător: metoda a păstrat apelul incomplet.
        // Producție: acolada închide inoutStartsNull. Șirul nu a plecat spre 3306.
        // Expert: Nu este eroarea de la literal pe OUT. Aici variabila există, dar nu are valoare.
    }
}
