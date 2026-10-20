package _11_STORED_ROUTINES.FUNCTION_AND_OPERATORS.USER_DEFINED_FUNCTION;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8692228#questions


/*                                    User-Defined Functions in MySQL


    ○  DELIMITER $$

            CREATE FUNCTION function_name(parameter data_type) RETURNS data_type
            DECLARE variable_name data_type
            HISTORICAL: DECLARE înainte de BEGIN nu este regula. Manualul 9.7, 15.1.22, deschis în tur: corpul compus stă între BEGIN și END, iar declarația stă în acel corp. Exemplul de mai jos din același fișier pune DECLARE după BEGIN.
            BEGIN
            SELECT …
            RETURN variable_name
            END$$
            DELIMITER ;

    ○ PARAMETER

                  * NO OUT parameters to define

                                - between the parentheses after the object’s name

                  * all parameters are IN,

                                - and since this is well known,

                                - you need not explicitly indicate it with the word, ‘IN’


    ○ RETURN

                * although there are no OUT parameters,

                * there is a ‘return value’

                * it is obtained after running

                * the query contained in the body of the function

                * it can be of ANY data type


                                               We cannot call a function!


    ○  we can SELECT it, indicating an input value within parentheses

    ○ SELECT function_name(input_value);


                                                  AVOID ERROR code:1418


    ○ https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/16168789#questions

    ○ SET @@global.log_bin_trust_function_creators := 1;



                                                  EXAMPLE


     ○ DROP FUNCTION IF EXISTS f_emp_avg_salary;
      DELIMITER //
      CREATE FUNCTION f_emp_avg_salary(p_emp_no integer ) RETURNS decimal(10,2)

      BEGIN
          DECLARE v_avg_salary decimal(10,2);

     SELECT
        AVG(s.salary)
     INTO v_avg_salary FROM

    salaries s
        JOIN
    employees e ON e.emp_no = s.emp_no

    WHERE
    e.emp_no = p_emp_no;

   RETURN v_avg_salary;

   END //

  DELIMITER ;

  SELECT f_emp_avg_salary(10001);


                                                   EXERCISE  -- not works


    ○ Task:

                  * Create a function called ‘emp_info’ that takes for parameters

                               - the first and last name of an employee,

                               - and returns the salary from the newest contract of that employee.

                   Hint: In the BEGIN-END block of this program, you need to declare and use two variables

                               – v_max_from_date that will be of the DATE type,

                               - and v_salary, that will be of the DECIMAL (10,2) type.

                  Finally, select this function.


    ○ Solution:

                * CREATE FUNCTION emp_info(p_first_name varchar(255), p_last_name varchar(255)) RETURNS decimal(10,2)

                 DETERMINISTIC NO SQL READS SQL DATA
                 HISTORICAL: NO SQL și READS SQL DATA nu stau împreună. Manualul 9.7, 15.1.22: caracteristica de acces la date este una singură. SELECT-ul din corp cere READS SQL DATA. NO SQL ar spune că nu există propoziții SQL. Cu binary logging, 27.9: lipsa lui DETERMINISTIC, NO SQL sau READS SQL DATA dă ERROR 1418.

               BEGIN
                   DECLARE v_max_from_date date;
                   DECLARE v_salary decimal(10,2);

                   SELECT

                       MAX(from_date)

                  INTO v_max_from_date FROM

                   employees e

                         JOIN  salaries s ON e.emp_no = s.emp_no

                 WHERE
                     e.first_name = p_first_name
                    AND e.last_name = p_last_name;



SELECT

    s.salary

INTO v_salary FROM

    employees e

        JOIN

    salaries s ON e.emp_no = s.emp_no

WHERE

    e.first_name = p_first_name

        AND e.last_name = p_last_name

        AND s.from_date = v_max_from_date;



                RETURN v_salary;



END$$



DELIMITER ;



SELECT EMP_INFO('Aruna', 'Journel');



*/

/*
TOPIC:
    Funcția stocată întoarce o valoare într-o expresie. Nu are OUT. Nu se pornește cu CALL. Cu binary logging, fără DETERMINISTIC, NO SQL sau READS SQL DATA, CREATE dă ERROR 1418.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Media salariului unui emp_no stă în schemă și intră într-un SELECT. CREATE nu calculează media. SELECT f_emp_avg_salary(10001) o cere.
MECHANISM:
    Serverul execută șirul din storedFunctionReturnsOneValue. 15.1.22: RETURNS este obligatoriu, parametrii sunt IN, corpul nu poate trimite un set. 27.9: serverul nu verifică dacă DETERMINISTIC este sincer. Efectul pe schemă se vede la CREATE. Valoarea se vede când expresia este evaluată.
LOOKALIKES:
    AVG(salary) nu are CREATE și nu primește emp_no. CALL emp_salary trimite un set. log_bin_trust_function_creators = 1 sare peste 1418 și nu face funcția deterministă.
BOOK THEORY:
    Manualul 9.7, 15.1.22 și 27.9, deschise în tur: funcția se invocă într-o expresie; nu poate face COMMIT; SELECT fără INTO este ER_SP_NO_RETSET; cu binary logging, lipsa lui DETERMINISTIC, NO SQL sau READS SQL DATA dă ERROR 1418. User-Defined+Functions.pdf, deschis în tur, arată CREATE FUNCTION, DECLARE, BEGIN, RETURN. DECLARE din scheletul de sus stă înainte de BEGIN și este marcat HISTORICAL.
BOOK EXAMPLE:
    storedFunctionReturnsOneValue
REPO DEMO:
    src/main/java/_11_STORED_ROUTINES/FUNCTION_AND_OPERATORS/USER_DEFINED_FUNCTION/USER_DEFINED_FUNCTION_.java
BREAK LAB:
    functionMissingCharacteristic ține CREATE FUNCTION fără DETERMINISTIC, NO SQL sau READS SQL DATA. 27.9: ERROR 1418 când binary logging este pornit. În tur șirul nu a fost trimis.
TRADE-OFFS:
    AVG(salary) cu WHERE când nu vrei un obiect în schemă. Procedura când trebuie un set sau un OUT.
ANTI-PATTERNS:
    SET GLOBAL log_bin_trust_function_creators = 1 ca să creezi funcția fără caracteristica citită, și CALL f_emp_avg_salary(10001) în loc de SELECT.
STAFF QUESTIONS:
    Întrebările 1–5 de mai jos.
NOVEL TRANSFER:
    Codul unui raft, calculat la fel de toți clienții, este o funcție într-un SELECT. Mutarea coletului este procedură. Numărul de colete este COUNT, fără CREATE.
SPACED REVIEW:
    O zi: 21 octombrie 2026, SELECT, nu CALL. Șapte zile: 27 octombrie 2026, 1418 fără caracteristică. Treizeci de zile: 19 noiembrie 2026, NO SQL și READS SQL DATA nu stau împreună.

1. Enunț: Ce problemă rezolvă f_emp_avg_salary față de AVG scris de fiecare client?
   Capcană: CREATE calculează deja media lui 10001 și o salvează în salaries.
   Cheie: storedFunctionReturnsOneValue păstrează expresia în schemă. Media apare când cineva evaluează SELECT f_emp_avg_salary(10001). Rândurile nu se schimbă la CREATE.
   Demonstrație: storedFunctionReturnsOneValue
2. Enunț: Ce se întâmplă cu parametrii și cu tranzacția în corp?
   Capcană: Poți declara OUT, iar funcția poate face COMMIT ca procedura.
   Cheie: 15.1.22: parametrii funcției sunt IN. RETURNS este obligatoriu. Funcția nu poate face COMMIT sau ROLLBACK și nu poate trimite un set. Valoarea iese prin RETURN, la evaluarea expresiei.
   Demonstrație: storedFunctionReturnsOneValue
3. Enunț: De ce f_emp_avg_salary nu este emp_salary și nu este AVG(salary)?
   Capcană: Toate trei se pornesc cu CALL.
   Cheie: Procedura se pornește cu CALL și poate trimite rânduri. AVG nu are obiect în schemă. Funcția se cheamă în SELECT. callTheFunction ține CALL-ul, care nu este invocarea.
   Demonstrație: callTheFunction
4. Enunț: Cum eșuează CREATE FUNCTION fără READS SQL DATA, DETERMINISTIC sau NO SQL?
   Capcană: Serverul o salvează și doar o avertizează, pentru că SELECT-ul din corp este evident o citire.
   Cheie: functionMissingCharacteristic nu declară caracteristica. 27.9: cu binary logging, ERROR 1418. log_bin_trust_function_creators = 1 sare verificarea și nu dovedește că funcția este deterministă. NO SQL împreună cu READS SQL DATA nu este o singură caracteristică.
   Demonstrație: functionMissingCharacteristic
5. Enunț: Când lași AVG în SELECT și când salvezi funcția?
   Capcană: Orice medie trebuie să fie funcție stocată, altfel clienții văd planuri diferite.
   Cheie: AVG când nu vrei obiect și privilegiu EXECUTE. Funcția când mai mulți clienți trebuie să cheme același nume fără drept pe salaries. Procedura când rezultatul este un set. Aplicația când nimeni altcineva nu evaluează expresia.
   Demonstrație: callTheFunction

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE media unui emp_no, cu nume în schemă -> storedFunctionReturnsOneValue -> CREATE FUNCTION f_emp_avg_salary(p_emp_no INTEGER) RETURNS DECIMAL(10,2) READS SQL DATA RETURN (SELECT AVG(s.salary) FROM salaries s WHERE s.emp_no = p_emp_no) -> nu garantează media: CREATE nu evaluează RETURN
    2. Eșecul cel mai mic
           -> DECIDE funcția fără caracteristică -> functionMissingCharacteristic -> CREATE FUNCTION fără READS SQL DATA -> nu garantează 1418 în tur: binary logging nu a fost citit, autentificarea a căzut
    3. Granița
           -> Funcția stocată este mecanismul MySQL. DELIMITER este clientul. DECLARE înainte de BEGIN, din schelet, nu este corpul. Java nu evaluează RETURN.
    4. Alternativa
           -> AVG cu WHERE când nu vrei obiect. CALL emp_salary când vrei rândurile. Aplicația când calculul nu iese din proces.
    5. Anti-patternul
           -> CALL f_emp_avg_salary(10001), sau log_bin_trust_function_creators = 1 ca să ascunzi 1418. Invocarea este SELECT. Variabila nu face funcția deterministă.
    6. Dovada
           -> storedFunctionReturnsOneValue ține RETURNS și READS SQL DATA. functionMissingCharacteristic nu le are. root@localhost a fost refuzat, using password: NO.
    7. Interviu
           -> Funcția intră în SELECT și întoarce o valoare. Nu are OUT și nu face COMMIT. Fără caracteristică, cu binary logging, apare 1418. CALL este pentru procedură.
*/

public class USER_DEFINED_FUNCTION_ {

    /*
    🟢 Începător
        Ce este            media unui angajat, salvată sub un nume și citită dintr-un SELECT
        De ce e nevoie     fără RETURNS, serverul nu știe ce valoare iese
    🟡 Producție
        Cum se scrie       CREATE FUNCTION f_emp_avg_salary(p_emp_no INTEGER) RETURNS DECIMAL(10,2) READS SQL DATA RETURN (SELECT AVG(s.salary) FROM salaries s WHERE s.emp_no = p_emp_no)
    🔴 Staff
        Unde               schema se schimbă la CREATE. Media se vede la SELECT f_emp_avg_salary(10001). Nu există OUT
    Pași
        1. Pune RETURNS și READS SQL DATA.
        2. Întoarce o singură valoare.
        3. Nu trimite șirul în tur.
    */
    // Începător: metoda păstrează funcția care dă o medie, nu un set de salarii.
    // Producție: public static String storedFunctionReturnsOneValue(). Dacă dispare, CREATE-ul corect rămâne doar în comentariu.
    // Expert: Nu pune și NO SQL. SELECT din corp citește. 1418 se evită cu caracteristica, nu cu log_bin_trust_function_creators.
    public static String storedFunctionReturnsOneValue() {
        // Începător: serverul ține minte cum se cere media unui număr de angajat.
        // Producție: CREATE FUNCTION f_emp_avg_salary(p_emp_no INTEGER) RETURNS DECIMAL(10,2) READS SQL DATA RETURN (SELECT AVG(s.salary) FROM salaries s WHERE s.emp_no = p_emp_no). Dacă linia dispare, rămâne scheletul cu DECLARE înainte de BEGIN.
        // Expert: Parametrul este IN fără să scrii IN. SELECT fără INTO în corp ar fi ER_SP_NO_RETSET. Această linie întoarce subcererea ca valoare.
        return "CREATE FUNCTION f_emp_avg_salary(p_emp_no INTEGER) RETURNS DECIMAL(10,2) READS SQL DATA RETURN (SELECT AVG(s.salary) FROM salaries s WHERE s.emp_no = p_emp_no)";
        // Începător: metoda a înapoiat funcția, nu media.
        // Producție: acolada închide storedFunctionReturnsOneValue. Șirul nu a creat funcția.
        // Expert: Refuzul root@localhost lasă 1418 necitit. Acolada nu este SELECT f_emp_avg_salary(10001).
    }

    /*
    🟢 Începător
        Ce este            funcția fără caracteristica pe care o cere jurnalul binar
        De ce e nevoie     fără ea, pare că orice CREATE FUNCTION trece
    🟡 Producție
        Cum se scrie       CREATE FUNCTION f_emp_avg_salary(p_emp_no INTEGER) RETURNS DECIMAL(10,2) RETURN (SELECT AVG(s.salary) FROM salaries s WHERE s.emp_no = p_emp_no)
    🔴 Staff
        Unde               27.9: ERROR 1418 dacă binary logging este pornit și lipsește DETERMINISTIC, NO SQL sau READS SQL DATA
    Pași
        1. Lasă RETURNS.
        2. Nu pune READS SQL DATA.
        3. Nu trimite șirul în tur.
    */
    // Începător: metoda păstrează CREATE-ul căruia îi lipsește caracteristica.
    // Producție: public static String functionMissingCharacteristic(). Dacă dispare, 1418 rămâne doar un link.
    // Expert: Să pui DETERMINISTIC pe o funcție care citește salarii schimbătoare minte optimizerul. Serverul nu verifică sinceritatea.
    public static String functionMissingCharacteristic() {
        // Începător: serverul poate refuza funcția înainte să calculeze vreo medie.
        // Producție: CREATE FUNCTION f_emp_avg_salary(p_emp_no INTEGER) RETURNS DECIMAL(10,2) RETURN (SELECT AVG(s.salary) FROM salaries s WHERE s.emp_no = p_emp_no). Dacă linia dispare, nu mai ai forma fără READS SQL DATA.
        // Expert: Dacă binary logging este oprit, 27.9 spune că log_bin_trust_function_creators nu se aplică. Șirul tot nu declară citirea.
        return "CREATE FUNCTION f_emp_avg_salary(p_emp_no INTEGER) RETURNS DECIMAL(10,2) RETURN (SELECT AVG(s.salary) FROM salaries s WHERE s.emp_no = p_emp_no)";
        // Începător: metoda a păstrat funcția incompletă.
        // Producție: acolada închide functionMissingCharacteristic. Șirul a rămas în proces.
        // Expert: Nu este ER_SP_NO_RETSET. Aici există RETURN. Lipsește caracteristica de jurnal.
    }

    /*
    🟢 Începător
        Ce este            pornirea funcției ca și cum ar fi o procedură
        De ce e nevoie     fără ea, CALL pare bun pentru orice rutină
    🟡 Producție
        Cum se scrie       CALL f_emp_avg_salary(10001)
    🔴 Staff
        Unde               invocarea funcției este o expresie. CALL este pentru procedură, după 15.1.22
    Pași
        1. Pune CALL pe numele funcției.
        2. Nu pune SELECT.
        3. Nu trimite șirul în tur.
    */
    // Începător: metoda păstrează apelul care nu este felul în care se citește funcția.
    // Producție: public static String callTheFunction(). Dacă dispare, deosebirea de procedură rămâne o frază.
    // Expert: SELECT f_emp_avg_salary(10001) evaluează RETURN. Un spațiu înainte de paranteză nu o face procedură.
    public static String callTheFunction() {
        // Începător: media nu iese prin CALL.
        // Producție: CALL f_emp_avg_salary(10001). Dacă linia dispare, nu mai ai invocarea greșită.
        // Expert: emp_salary, procedura, chiar se pornește cu CALL și poate trimite rânduri. Funcția nu are OUT în care să lase media.
        return "CALL f_emp_avg_salary(10001)";
        // Începător: metoda a păstrat apelul greșit.
        // Producție: acolada închide callTheFunction. Șirul nu a plecat spre 3306.
        // Expert: Nu este ERROR 1418. 1418 este la CREATE, nu la felul în care pornești rutina.
    }
}
