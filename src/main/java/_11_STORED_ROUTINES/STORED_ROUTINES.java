package _11_STORED_ROUTINES;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8692222#questions



/*                                     STORED ROUTINES


    ▬▬▐ Routine (in a context other than computer science)
                  * a usual, fixed action,
                  * or series of actions, repeated periodically

    ▬▬▐ Stored Routine
                  * an SQL statement,
                  * or a set of SQL statements,
          that can be STORED on the database server

    ○ Whenever a user needs to run the query in question, they can
                  * call,
                  * reference,
                  * or invoke the routine

    ○ This routine can bring the desired result multiple times


                                      Stored routines


         Stored procedures                                                    Functions

             = procedures                                                 = user-defined functions
                                                                           ≠ built-in functions
                                                                           (aggregate functions,
                                                                            datetime functions)


                                        TECHNICAL DIFFERENCES


           Stored PROCEDURE                                                User-defined FUNCTION


       ○ Does not return a                                             ○ Returns a value.
            value.
            HISTORICAL: propoziția „Does not return a value” nu este regula. Manualul 9.7, 15.1.22, deschis în tur: procedura nu are clauza RETURNS, dar poate trimite un set de rezultate și valori prin OUT sau INOUT. Funcția întoarce o singură valoare într-o expresie și nu poate trimite un set de rezultate.


       ○ CALL procedure;                                               ○ SELECT function;


       ○ Including a PROCEDURE in a SELECT                             ○ You can easily include a function
              statement is impossible                                      as one of the columns inside a SELECT
                                                                              statement.

                                                                                       - set @v_emp_no=10001;
                                                                                         SELECT
                                                                                             e.emp_no,
                                                                                             e.first_name,
                                                                                             e.last_name,
                                                                                             F_EMP_AVG_SALARY(@V_emp_no) AS avg_salaru
                                                                                         FROM
                                                                                             employees e
                                                                                         WHERE
                                                                                              @v_emp_no = e.emp_no;




                                         CONCEPTUAL DIFFERENCES


       ○ Can have multiple OUT                                        ○ can return a single value only
             parameters.


       ○  Can INSERT UPDATE DELETE                                   ○ Shouldn't br use to INSERT UPDATE DELETE
                                                                        it should return some value.

       ○ If you need to obtain MORE THAN ONE VALUE as a result of a calculation,

                    * you are better off using a PROCEDURE.

       ○ If you need to just ONE VALUE to be returned,

                    * then you can use a FUNCTION


 */

/*
TOPIC:
    Rutina stocată este un set de propoziții păstrate pe server. Alegi procedura când CALL trebuie să trimită un set de rezultate, să schimbe rânduri sau să scrie mai multe valori OUT. Alegi funcția când o singură valoare trebuie să intre într-o expresie.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Același salariu citit pentru un emp_no, cerut de mai mulți clienți, nu se rescrie la fiecare apel. După CREATE, schema ține rutina. Rândurile din salaries rămân până la o propoziție din corp care scrie.
MECHANISM:
    Serverul execută șirul din procedureTheServerStores și din functionInSelectReturnsOneValue. emp_salary pornește cu CALL și poate trimite rânduri. f_emp_avg_salary se evaluează în SELECT și întoarce o valoare. Schema se schimbă la sfârșitul lui CREATE. Rândurile se schimbă la sfârșitul propoziției din corp, nu la CREATE.
LOOKALIKES:
    AVG(salary) este agregat built-in: nu există CREATE și nu se pornește cu CALL. Trigger-ul pornește de la un rând scris, nu de la CALL. @v_emp_no trăiește pe sesiune și nu este rutina. DELIMITER $$ este comanda clientului mysql, nu propoziția pe care o primește serverul.
BOOK THEORY:
    Manualul 9.7, 27.2, deschis în tur: rutina stocată este un set de propoziții păstrate pe server; clienții o invocă în loc să retrimită fiecare propoziție; pe rețea pleacă mai puțin și crește lucrul pe server. 15.1.22: CALL pornește procedura; funcția se numește într-o expresie; OUT și INOUT există doar la procedură; funcția nu poate trimite un set de rezultate, eroarea este ER_SP_NO_RETSET; funcția nu poate face COMMIT sau ROLLBACK; RETURNS este obligatoriu la funcție; lista de parametri, chiar goală, este obligatorie. 15.2.1: CALL p și CALL p() sunt echivalente fără argumente; OUT se citește din variabila de utilizator după CALL. 27.9: cu binary logging, CREATE FUNCTION fără DETERMINISTIC, NO SQL sau READS SQL DATA dă ERROR 1418. 14.19.1: AVG și COUNT sunt agregate built-in, nu rutine create. Stored+Routines.pdf, deschis în tur, are pe paginile citite titlul de încheiere și TECHNICAL DIFFERENCES; nu conține ER_SP_NO_RETSET. Stored+Procedures.pdf spune că DELIMITER $$ schimbă terminatorul clientului.
BOOK EXAMPLE:
    procedureTheServerStores
REPO DEMO:
    src/main/java/_11_STORED_ROUTINES/STORED_ROUTINES.java
BREAK LAB:
    functionThatReturnsAResultSet ține un CREATE FUNCTION al cărui corp face SELECT fără INTO. Serverul nu acceptă un set de rezultate din funcție. În tur șirul nu a fost trimis: root@localhost a fost refuzat, using password: NO.
TRADE-OFFS:
    Rămâi în aplicație când calculul nu trebuie văzut de alt client și nu îți trebuie privilegiul EXECUTE. Treci la AVG(salary) când serverul are deja agregatul. Treci la trigger când codul trebuie să pornească de la scrierea unui rând, nu de la CALL.
ANTI-PATTERNS:
    SELECT emp_salary(10001) în loc de CALL, sau SET GLOBAL log_bin_trust_function_creators = 1 ca să ocolești READS SQL DATA.
STAFF QUESTIONS:
    Întrebările 1–50 din StoredRoutines_Staff_Interview. Cheia este scrisă acolo. Nu există main care să o citească.
NOVEL TRANSFER:
    Un depozit: mutarea unui colet între rafturi schimbă rânduri, deci este procedură și CALL. Codul de bare, o singură valoare pusă într-un SELECT, este funcție. Numărul de colete de pe raft este COUNT, nu o rutină creată.
SPACED REVIEW:
    O zi: 21 octombrie 2026, CREATE nu mută salariul. Șapte zile: 27 octombrie 2026, funcția care face SELECT fără INTO nu se creează. Treizeci de zile: 19 noiembrie 2026, alegi aplicația când niciun alt client nu trebuie să cheme calculul.

1. Enunț: Ce problemă rezolvă rutina stocată față de propoziția trimisă din nou de fiecare client?
   Capcană: CREATE PROCEDURE emp_salary scrie deja salariul în salaries, deci apelul nu mai are ce citi.
   Cheie: procedureTheServerStores păstrează pe server propoziția care citește employees și salaries pentru un emp_no. Rândurile rămân. Clienții trimit CALL, nu din nou JOIN-ul. Schema primește rutina la sfârșitul lui CREATE.
   Demonstrație: procedureTheServerStores
2. Enunț: Ce se întâmplă cu rândurile și cu schema când serverul termină CREATE PROCEDURE emp_salary și încă nu a primit CALL?
   Capcană: Salariile lui 10001 sunt deja filtrate, iar indexul de pe emp_no ține lacăt până la COMMIT.
   Cheie: CREATE schimbă schema: rutina există. Nu citește rândul și nu ține lacăt pe salaries. Rândurile se văd la sfârșitul SELECT-ului din corp, iar acel SELECT pleacă abia la CALL emp_salary(10001).
   Demonstrație: procedureTheServerStores
3. Enunț: De ce emp_salary nu este f_emp_avg_salary și nu este AVG(salary)?
   Capcană: Toate trei întorc salariul, deci CALL, SELECT f_emp_avg_salary(10001) și SELECT AVG(salary) sunt aceeași propoziție.
   Cheie: procedureTheServerStores se pornește cu CALL și poate trimite mai multe rânduri. functionInSelectReturnsOneValue are RETURNS și intră într-o expresie, o singură valoare. AVG(salary) este agregatul built-in din 14.19.1: nu există CREATE și nu există EXECUTE pe o rutină a ta.
   Demonstrație: functionInSelectReturnsOneValue
4. Enunț: Cum eșuează funcția al cărei corp face SELECT fără INTO?
   Capcană: Serverul întoarce rândurile și ignoră RETURNS, pentru că SELECT-ul este corect.
   Cheie: functionThatReturnsAResultSet cere o funcție care trimite un set de rezultate. 15.1.22: asta este interzis, eroarea este ER_SP_NO_RETSET. Procedura are voie să trimită rândurile. Funcția trebuie să scrie valoarea cu RETURN sau cu SELECT INTO într-o variabilă, apoi RETURN.
   Demonstrație: functionThatReturnsAResultSet
5. Enunț: Când lași calculul în aplicație și când rămâi pe AVG, nu pe o rutină creată?
   Capcană: Orice salariu mediu trebuie să fie CREATE FUNCTION, altfel clienții văd propoziții diferite.
   Cheie: Aplicația, când niciun alt client nu apelează calculul și nu îți trebuie EXECUTE. AVG(salary) când agregatul built-in este valoarea cerută și nu vrei un obiect în schemă. Procedura, când trebuie să schimbi rânduri sau să trimiți un set. Funcția stocată, când o singură valoare trebuie să stea într-un SELECT văzut de mai mulți clienți.
   Demonstrație: selectOfAProcedure

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE mai mulți clienți trebuie să citească salariile unui emp_no fără să rescrie JOIN-ul -> procedureTheServerStores -> CREATE PROCEDURE emp_salary(IN p_emp_no INTEGER) cu SELECT în corp -> nu garantează că CALL a rulat: CREATE nu citește rândul
    2. Eșecul cel mai mic
           -> DECIDE funcția să trimită rândurile -> functionThatReturnsAResultSet -> SELECT fără INTO în corpul funcției -> nu garantează un rând ER_SP_NO_RETSET în tur: root@localhost a fost refuzat, using password: NO
    3. Granița
           -> Rutina este mecanismul MySQL. DELIMITER $$ este clientul mysql. Șirul din Java nu este CREATE-ul executat. Click-ul din Workbench nu salvează rutina.
    4. Alternativa
           -> Funcția când valoarea unică intră într-o expresie. AVG(salary) când nu vrei obiect în schemă. Trigger-ul când codul pornește de la scrierea rândului. Aplicația când calculul nu iese din proces.
    5. Anti-patternul
           -> SELECT emp_salary(10001) în loc de CALL, sau log_bin_trust_function_creators = 1 ca să ascunzi lipsa lui READS SQL DATA. Procedura nu este o expresie. Variabila globală nu face funcția deterministă.
    6. Dovada
           -> procedureTheServerStores ține CREATE PROCEDURE. functionThatReturnsAResultSet ține SELECT-ul fără INTO. Planul nu a fost citit: root@localhost a fost refuzat.
    7. Interviu
           -> CREATE păstrează rutina și nu mută salariul. CALL pornește procedura și poate trimite rânduri. Funcția intră în SELECT și întoarce o valoare. SELECT fără INTO în funcție este ER_SP_NO_RETSET.
*/

public class STORED_ROUTINES {

    /*
    🟢 Începător
        Ce este            propoziția salvată pe server, care citește salariile unui angajat și nu le mută la CREATE
        De ce e nevoie     fără ea, fiecare client retrimite JOIN-ul, iar comentariul că procedura nu întoarce nimic pare regula
    🟡 Producție
        Cum se scrie       CREATE PROCEDURE emp_salary(IN p_emp_no INTEGER) BEGIN SELECT ... FROM employees e JOIN salaries s ... END
    🔴 Staff
        Unde               schema se schimbă la sfârșitul lui CREATE; rândurile se văd la CALL. Funcția nu poate trimite acest set. DELIMITER nu este în șir
    Pași
        1. Ține CREATE PROCEDURE cu un IN și cu un SELECT care nu are INTO.
        2. Nu pune DELIMITER în șir: serverul nu primește comanda clientului.
        3. Nu trimite șirul: root@localhost a fost refuzat, using password: NO.
    */
    // Începător: metoda păstrează rutina pe care serverul o poate salva, fără să mute salariul.
    // Producție: public static String procedureTheServerStores(). Dacă dispare, fast nu mai are propoziția unică.
    // Expert: Metoda nu deschide JDBC și nu trimite DELIMITER. Nu ține lacăt pe salaries, pentru că CREATE nu execută corpul.
    public static String procedureTheServerStores() {
        // Începător: serverul poate ține minte cum se citesc numele și salariul pentru un număr de angajat.
        // Producție: CREATE PROCEDURE emp_salary(IN p_emp_no INTEGER) BEGIN SELECT e.first_name, e.last_name, s.salary FROM employees e JOIN salaries s ON e.emp_no = s.emp_no WHERE e.emp_no = p_emp_no; END. Fără șir, rămâne comentariul că procedura nu întoarce o valoare.
        // Expert: SELECT-ul fără INTO este legal în procedură și interzis în funcție. IN nu trimite înapoi o valoare schimbată în corp. Șirul nu conține DELIMITER.
        return "CREATE PROCEDURE emp_salary(IN p_emp_no INTEGER) BEGIN SELECT e.first_name, e.last_name, s.salary FROM employees e JOIN salaries s ON e.emp_no = s.emp_no WHERE e.emp_no = p_emp_no; END";
        // Începător: metoda a înapoiat rutina, nu un salariu citit.
        // Producție: acolada închide procedureTheServerStores. Șirul nu a fost trimis către 3306.
        // Expert: Refuzul root@localhost, using password: NO, lasă efectul pe server NERULAT. Acolada nu este un CALL și nu este ER_SP_NO_RETSET.
    }

    /*
    🟢 Începător
        Ce este            funcția care dă înapoi un singur salariu mediu și poate sta într-un SELECT
        De ce e nevoie     fără ea, CALL pare singurul fel în care serverul întoarce o valoare
    🟡 Producție
        Cum se scrie       CREATE FUNCTION f_emp_avg_salary(p_emp_no INTEGER) RETURNS DECIMAL(10,2) READS SQL DATA RETURN (SELECT AVG(s.salary) FROM salaries s WHERE s.emp_no = p_emp_no)
    🔴 Staff
        Unde               valoarea se vede când expresia este evaluată. Nu există OUT. Cu binary logging, fără READS SQL DATA apare 1418
    Pași
        1. Pune RETURNS și un singur RETURN.
        2. Marchează READS SQL DATA, nu și NO SQL.
        3. Nu o porni cu CALL.
    */
    // Începător: metoda păstrează funcția care dă o singură valoare, nu un tabel de salarii.
    // Producție: public static String functionInSelectReturnsOneValue(). Dacă dispare, vecinul procedurii rămâne doar în comentariu.
    // Expert: Parametrul funcției este mereu IN. RETURNS nu există la emp_salary. AVG din corp este agregatul, nu rutina.
    public static String functionInSelectReturnsOneValue() {
        // Începător: cine întreabă salariul mediu al unui angajat primește o valoare, nu rândurile din salaries.
        // Producție: CREATE FUNCTION f_emp_avg_salary(p_emp_no INTEGER) RETURNS DECIMAL(10,2) READS SQL DATA RETURN (SELECT AVG(s.salary) FROM salaries s WHERE s.emp_no = p_emp_no). Dacă linia dispare, nu mai ai funcția care intră în SELECT.
        // Expert: Un SELECT f_emp_avg_salary(10001) evaluează expresia. CALL f_emp_avg_salary(10001) nu este invocarea. log_bin_trust_function_creators = 1 ar sări peste 1418 și nu face funcția deterministă.
        return "CREATE FUNCTION f_emp_avg_salary(p_emp_no INTEGER) RETURNS DECIMAL(10,2) READS SQL DATA RETURN (SELECT AVG(s.salary) FROM salaries s WHERE s.emp_no = p_emp_no)";
        // Începător: metoda a înapoiat funcția, nu media.
        // Producție: acolada închide functionInSelectReturnsOneValue. Șirul nu a creat funcția pe server.
        // Expert: 1418 nu a fost citit în tur. Acolada nu este privilegiul SUPER și nu este EXECUTE.
    }

    /*
    🟢 Începător
        Ce este            funcția care încearcă să trimită rânduri și nu are voie
        De ce e nevoie     fără ea, SELECT din corp pare la fel de legal în funcție ca în procedură
    🟡 Producție
        Cum se scrie       CREATE FUNCTION f_emp_avg_salary(p_emp_no INTEGER) RETURNS DECIMAL(10,2) READS SQL DATA BEGIN SELECT AVG(s.salary) FROM salaries s WHERE s.emp_no = p_emp_no; END
    🔴 Staff
        Unde               eșecul este la CREATE, înainte de un rând citit de client. Vecinul legal este procedura, care are voie să trimită setul
    Pași
        1. Lasă SELECT fără INTO în corpul funcției.
        2. Nu îl trimite în tur.
        3. Nu îl folosi ca plan în EXPLAIN.
    */
    // Începător: metoda păstrează cel mai mic CREATE greșit, care nu lasă o funcție în schemă.
    // Producție: public static String functionThatReturnsAResultSet(). Dacă dispare, eșecul rămâne o poveste.
    // Expert: Eroarea nu este 1418. 1418 este lipsa caracteristicii. Aici caracteristica există, iar setul de rezultate este interzis.
    public static String functionThatReturnsAResultSet() {
        // Începător: serverul nu salvează o funcție care vrea să trimită rândurile salariului.
        // Producție: CREATE FUNCTION f_emp_avg_salary(p_emp_no INTEGER) RETURNS DECIMAL(10,2) READS SQL DATA BEGIN SELECT AVG(s.salary) FROM salaries s WHERE s.emp_no = p_emp_no; END. Dacă linia dispare, nu mai ai SELECT-ul fără INTO.
        // Expert: ER_SP_NO_RETSET se vede la definire când serverul poate ști că SELECT-ul trimite un set. Procedura emp_salary, cu același SELECT, este cealaltă alegere.
        return "CREATE FUNCTION f_emp_avg_salary(p_emp_no INTEGER) RETURNS DECIMAL(10,2) READS SQL DATA BEGIN SELECT AVG(s.salary) FROM salaries s WHERE s.emp_no = p_emp_no; END";
        // Începător: metoda s-a oprit la funcția care nu are voie să existe.
        // Producție: acolada închide functionThatReturnsAResultSet. Șirul a rămas în proces.
        // Expert: Nu compara acest eșec cu un WHERE gol. Aici serverul refuză forma rutinei, nu un rând din salaries.
    }

    /*
    🟢 Începător
        Ce este            încercarea de a pune procedura în lista unui SELECT
        De ce e nevoie     fără ea, CALL și SELECT par interschimbabile
    🟡 Producție
        Cum se scrie       SELECT emp_salary(10001)
    🔴 Staff
        Unde               procedura nu este o expresie. Funcția este cea care stă în listă. Nu se citește un rând prin acest text
    Pași
        1. Ține SELECT-ul pe numele procedurii.
        2. Nu îl trimite în tur.
        3. Nu îl numi apel.
    */
    // Începător: metoda păstrează textul care tratează procedura ca pe o coloană.
    // Producție: public static String selectOfAProcedure(). Dacă dispare, anti-patternul rămâne doar o frază.
    // Expert: CALL emp_salary(10001) este invocarea. SELECT f_emp_avg_salary(10001) este invocarea funcției. Acest șir nu este niciuna.
    public static String selectOfAProcedure() {
        // Începător: salariul nu apare, pentru că emp_salary nu este o valoare dintr-o expresie.
        // Producție: SELECT emp_salary(10001). Dacă linia dispare, nu mai ai propoziția care amestecă CALL cu lista de SELECT.
        // Expert: Un spațiu între nume și paranteză nu o transformă în funcție. IGNORE_SPACE privește funcțiile built-in, nu rutina stocată, după 15.1.22.
        return "SELECT emp_salary(10001)";
        // Începător: metoda a păstrat textul greșit, fără un rezultat.
        // Producție: acolada închide selectOfAProcedure. Șirul nu a plecat spre 3306.
        // Expert: Eșecul nu este ER_SP_NO_RETSET, care stă la CREATE FUNCTION. Aici nici nu există un CALL.
    }
}
