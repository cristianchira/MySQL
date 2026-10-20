package _11_STORED_ROUTINES.STORED_PROCEDURES.INPUT_PARAMETER;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8692218#questions


/*                                   Stored Procedures with an Input Parameter


    ○ A stored routine can perform a calculation that transforms

                  * an INPUT value

                  * in an OUTPUT value

    ○ Stored Procedures

                 * can take an input value

                 * and then use it in the query, or queries, written in the body of the procedure

    ○ This value is represented by

                 * the IN parameter

    ○ After that calculation is ready,

                * a RESULT will be returned




                                              SQL


    ○ DELIMITER $$
      CREATE PROCEDURE procedure_name(in parameter)

          BEGIN
              SELECT * FROM employees
              LIMIT 1000;
          END$$

      DELIMITER ;

                                           EXAMPLE


    ○ use employees;
      drop procedure if exists emp_salary;

      delimiter //
      create procedure emp_salary(IN p_emp_no INTEGER)
      begin
          select e.first_name, e.last_name, s.salary , s.from_date, s.to_date
      from employees e
          join
             salaries s on e.emp_no = s.emp_no
      where
            e.emp_no = p_emp_no;
      end //

      delimiter ;

    call emp_salary(10001);



                                       Implement a parameterized procedure


    ○ set @p_emp_no = 0;
      call employees.emp_info('Bezalel', 'Simmel', @p_emp_no);
      select @p_emp_no;



 */



/*
TOPIC:
    Parametrul IN duce o valoare în procedură. Schimbarea lui în corp nu se vede la apelant după CALL. La emp_salary, 10001 alege rândurile și nu se întoarce ca rezultat.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Același corp citește salariile oricărui emp_no primit la CALL. După CALL emp_salary(10001), rândurile citite sunt setul. Valoarea 10001 nu este rescrisă în employees.
MECHANISM:
    Serverul execută șirul din inParameterFiltersRows. IN este implicit dacă lipsește cuvântul, după 15.1.22. Efectul pe rândurile din rezultat se vede la sfârșitul SELECT-ului din CALL, nu la CREATE.
LOOKALIKES:
    OUT p_emp_no primește NULL la intrarea în corp și se citește după CALL din @p_emp_no. INOUT pleacă cu valoarea apelantului și se întoarce schimbat. Parametrul unei funcții este tot IN, dar funcția nu se pornește cu CALL.
BOOK THEORY:
    Manualul 9.7, 15.1.22, deschis în tur: IN trece valoarea în procedură; procedura poate modifica copia, iar modificarea nu este vizibilă apelantului când procedura se întoarce. Input+Parameter.pdf, deschis în tur: o rutină poate transforma o valoare de intrare într-o valoare de ieșire; pagina citită nu spune că IN însuși se întoarce schimbat.
BOOK EXAMPLE:
    inParameterFiltersRows
REPO DEMO:
    src/main/java/_11_STORED_ROUTINES/STORED_PROCEDURES/INPUT_PARAMETER/INPUT_PARAMETER_.java
BREAK LAB:
    callTreatsInAsOut ține CALL emp_salary(@p_emp_no) după SET @p_emp_no = 10001, așteptând ca procedura să schimbe variabila. IN nu scrie înapoi. În tur șirul nu a fost trimis.
TRADE-OFFS:
    OUT când valoarea trebuie să rămână în variabila de utilizator. INOUT când aceeași variabilă intră și iese. Funcția când valoarea unică trebuie să stea în SELECT, nu într-un set.
ANTI-PATTERNS:
    Să citești @p_emp_no după CALL emp_salary(@p_emp_no) și să crezi că procedura ți-a scris rezultatul în el.
STAFF QUESTIONS:
    Întrebările 1–5 de mai jos.
NOVEL TRANSFER:
    Codul unui raft intră în procedura care listează coletele. Codul nu se schimbă în urma listării. Numărul de colete, dacă trebuie scris înapoi, este OUT, nu IN.
SPACED REVIEW:
    O zi: 21 octombrie 2026, 10001 filtrează, nu se întoarce. Șapte zile: 27 octombrie 2026, @p_emp_no nu primește salariul prin IN. Treizeci de zile: 19 noiembrie 2026, alegi OUT când valoarea trebuie citită după CALL.

1. Enunț: Ce problemă rezolvă IN p_emp_no în emp_salary?
   Capcană: Fără parametru, procedura tot poate primi 10001, pentru că CALL are paranteze.
   Cheie: inParameterFiltersRows leagă 10001 de WHERE e.emp_no = p_emp_no. Fără IN, același corp nu știe ce angajat să citească. Rândurile din employees nu primesc o coloană nouă.
   Demonstrație: inParameterFiltersRows
2. Enunț: Ce se întâmplă cu valoarea și cu rândurile când corpul face SET p_emp_no = 0 pe un parametru IN?
   Capcană: După CALL emp_salary(10001), apelantul vede 0, pentru că procedura a schimbat parametrul.
   Cheie: 15.1.22: modificarea unui IN nu este vizibilă apelantului. 10001 rămâne la apelant. Rândurile citite sunt doar setul de la SELECT. Nu se scrie salaries.
   Demonstrație: inParameterFiltersRows
3. Enunț: De ce IN nu este OUT p_emp_no din emp_info?
   Capcană: Ambele se numesc p_emp_no, deci ambele se citesc cu SELECT @p_emp_no după CALL.
   Cheie: OUT începe cu NULL în corp și se întoarce în variabila de utilizator. IN primește 10001 și nu scrie înapoi. callTreatsInAsOut amestecă cele două.
   Demonstrație: callTreatsInAsOut
4. Enunț: Cum eșuează așteptarea ca @p_emp_no să țină salariul după CALL emp_salary(@p_emp_no)?
   Capcană: Variabila de utilizator se umple singură, pentru că a fost folosită la CALL.
   Cheie: callTreatsInAsOut trece valoarea înăuntru. emp_salary nu are OUT. @p_emp_no rămâne ce era înainte de CALL, dacă corpul nu este INOUT. Salariul este în setul de rezultate, nu în variabilă.
   Demonstrație: callTreatsInAsOut
5. Enunț: Când lași filtrul în aplicație și când îl pui într-un IN?
   Capcană: Orice număr trimis serverului trebuie să fie parametru de procedură, altfel este SQL injectat.
   Cheie: IN când mai mulți clienți cheamă același corp și tu vrei EXECUTE, nu SQL dinamic pe tabelă. Aplicația când propoziția se construiește o dată și nu salvezi o rutină. OUT când numărul trebuie să revină în @variabilă, nu ca set.
   Demonstrație: inParameterFiltersRows

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE același corp, alt emp_no -> inParameterFiltersRows -> CREATE PROCEDURE emp_salary(IN p_emp_no INTEGER) ... WHERE e.emp_no = p_emp_no -> nu garantează că 10001 există: lipsa rândului nu este o eroare de parametru
    2. Eșecul cel mai mic
           -> DECIDE @p_emp_no să primească rezultatul -> callTreatsInAsOut -> CALL emp_salary(@p_emp_no) -> nu garantează că variabila s-a schimbat: IN nu scrie înapoi
    3. Granița
           -> IN este mecanismul din rutina MySQL. Numele p_emp_no din Java nu filtrează. Workbench nu transformă un literal în OUT.
    4. Alternativa
           -> OUT când valoarea se citește după CALL. INOUT când aceeași variabilă intră și iese. Funcția când valoarea stă în expresie.
    5. Anti-patternul
           -> SELECT @p_emp_no după CALL emp_salary(@p_emp_no) ca să citești salariul. Variabila încă ține intrarea, iar salariul a fost în set.
    6. Dovada
           -> inParameterFiltersRows ține IN. callTreatsInAsOut ține apelul pe variabilă. root@localhost a fost refuzat, using password: NO.
    7. Interviu
           -> IN filtrează rândurile din set. Nu se întoarce la apelant. OUT se citește din @variabilă. Literalul 10001 nu este o variabilă de ieșire.
*/

public class INPUT_PARAMETER_ {

    /*
    🟢 Începător
        Ce este            numărul de angajat care intră în procedură și alege rândurile
        De ce e nevoie     fără el, corpul nu știe pe cine să citească
    🟡 Producție
        Cum se scrie       CREATE PROCEDURE emp_salary(IN p_emp_no INTEGER) BEGIN SELECT e.first_name, e.last_name, s.salary, s.from_date, s.to_date FROM employees e JOIN salaries s ON e.emp_no = s.emp_no WHERE e.emp_no = p_emp_no; END
    🔴 Staff
        Unde               copia IN se poate schimba în corp și apelantul tot vede valoarea cu care a făcut CALL
    Pași
        1. Declară IN p_emp_no.
        2. Folosește-l în WHERE, nu într-un OUT.
        3. Nu trimite șirul în tur.
    */
    // Începător: metoda păstrează procedura în care numărul intră și nu iese schimbat.
    // Producție: public static String inParameterFiltersRows(). Dacă dispare, IN rămâne doar în comentariul de curs.
    // Expert: Lipsa cuvântului IN ar fi tot IN, după 15.1.22. OUT nu este implicit.
    public static String inParameterFiltersRows() {
        // Începător: salariile afișate sunt ale numărului primit, iar numărul rămâne al celui care a sunat.
        // Producție: CREATE PROCEDURE emp_salary(IN p_emp_no INTEGER) BEGIN SELECT e.first_name, e.last_name, s.salary, s.from_date, s.to_date FROM employees e JOIN salaries s ON e.emp_no = s.emp_no WHERE e.emp_no = p_emp_no; END. Fără șir, exemplul din comentariu nu este propoziția gata de trimis.
        // Expert: SELECT fără INTO trimite setul, legal în procedură. Nu scrie @p_emp_no. Nu este RETURNS.
        return "CREATE PROCEDURE emp_salary(IN p_emp_no INTEGER) BEGIN SELECT e.first_name, e.last_name, s.salary, s.from_date, s.to_date FROM employees e JOIN salaries s ON e.emp_no = s.emp_no WHERE e.emp_no = p_emp_no; END";
        // Începător: metoda a înapoiat intrarea, nu salariul.
        // Producție: acolada închide inParameterFiltersRows. Șirul nu a creat procedura.
        // Expert: Refuzul root@localhost lasă efectul NERULAT. Acolada nu schimbă un IN în OUT.
    }

    /*
    🟢 Începător
        Ce este            apelul care bagă o variabilă și așteaptă, greșit, să o primească înapoi
        De ce e nevoie     fără el, orice @ de la CALL pare un rezultat
    🟡 Producție
        Cum se scrie       SET @p_emp_no = 10001; CALL emp_salary(@p_emp_no)
    🔴 Staff
        Unde               @p_emp_no rămâne 10001 dacă parametrul este IN. Rezultatul este setul, nu variabila
    Pași
        1. Inițializează variabila.
        2. Treci-o la o procedură care are doar IN.
        3. Nu o citi ca salariu.
    */
    // Începător: metoda păstrează apelul care confundă intrarea cu ieșirea.
    // Producție: public static String callTreatsInAsOut(). Dacă dispare, capcana rămâne nescrisă ca propoziție.
    // Expert: Pentru OUT, emp_info scrie în variabilă și SELECT @p_emp_no vine după CALL. Aici procedura nu are OUT.
    public static String callTreatsInAsOut() {
        // Începător: numărul intră în procedură, iar variabila de după apel tot numărul îl ține, nu salariul.
        // Producție: SET @p_emp_no = 10001; CALL emp_salary(@p_emp_no). Dacă linia dispare, nu mai ai confuzia dintre IN și variabila de utilizator.
        // Expert: @p_emp_no este variabilă de sesiune, nu parametrul. Parametrul IN primește valoarea ei la CALL. SET din corp pe p_emp_no nu o rescrie.
        return "SET @p_emp_no = 10001; CALL emp_salary(@p_emp_no)";
        // Începător: metoda s-a oprit la apelul care nu întoarce variabila.
        // Producție: acolada închide callTreatsInAsOut. @p_emp_no nu a fost citit pe server.
        // Expert: Nu este CALL emp_info(..., @p_emp_no), unde OUT chiar scrie variabila.
    }
}
