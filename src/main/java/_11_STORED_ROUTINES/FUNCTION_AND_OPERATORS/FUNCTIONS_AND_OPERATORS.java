package _11_STORED_ROUTINES.FUNCTION_AND_OPERATORS;

// Functions and Operators
// https://dev.mysql.com/doc/refman/8.0/en/functions.html


/*                                      Functions and Operators


    1. Built-In Function and Operator Reference

                * https://dev.mysql.com/doc/refman/8.0/en/functions.html

    2. Loadable Function Reference

                * https://dev.mysql.com/doc/refman/8.0/en/udf-loading.html

    3. Type Conversion in Expression Evaluation

                * https://dev.mysql.com/doc/refman/8.0/en/type-conversion.html

    4. Operators

                * https://dev.mysql.com/doc/refman/8.0/en/operators.html

    5. Flow Control Functions

                * https://dev.mysql.com/doc/refman/8.0/en/control-flow-functions.html

    6. Numeric Functions and Operators

               * https://dev.mysql.com/doc/refman/8.0/en/numeric-functions.html

   7. Date and Time Functions

               * https://dev.mysql.com/doc/refman/8.0/en/date-and-time-functions.html

   8. String Functions and Operators

              * https://dev.mysql.com/doc/refman/8.0/en/string-functions.html

   9. What Calendar Is Used By MySQL?

              * https://dev.mysql.com/doc/refman/8.0/en/date-and-time-functions.html#function_date-add

    10. Full-Text Search Functions

              * https://dev.mysql.com/doc/refman/8.0/en/fulltext-search.html

    11. Cast Functions and Operators

              * https://dev.mysql.com/doc/refman/8.0/en/cast-functions.html

    12. XML Functions

              * https://dev.mysql.com/doc/refman/8.0/en/xml-functions.html

    13. Bit Functions and Operators

              * https://dev.mysql.com/doc/refman/8.0/en/bit-functions.html

    14. Encryption and Compression Functions

              * https://dev.mysql.com/doc/refman/8.0/en/encryption-functions.html

    15. Locking Functions

              * https://dev.mysql.com/doc/refman/8.0/en/miscellaneous-functions.html#function_get-lock

    16. Information Functions

              * https://dev.mysql.com/doc/refman/8.0/en/information-functions.html

    17. Spatial Analysis Functions

              * https://dev.mysql.com/doc/refman/8.0/en/spatial-analysis-functions.html

    18. JSON Functions

              * https://dev.mysql.com/doc/refman/8.0/en/json-functions.html

    19. Functions Used with Global Transaction Identifiers (GTIDs)

              * https://dev.mysql.com/doc/refman/8.0/en/gtid-functions.html

    20. Aggregate (GROUP BY) Functions

               * https://dev.mysql.com/doc/refman/8.0/en/group-by-functions.html

    21. Window Functions

              * https://dev.mysql.com/doc/refman/8.0/en/window-functions.html

    22. Performance Schema Functions

              * https://dev.mysql.com/doc/refman/8.0/en/performance-schema-functions.html

    23. Internal Functions

              * https://dev.mysql.com/doc/refman/8.0/en/internal-functions.html

    24. Miscellaneous Functions

              * https://dev.mysql.com/doc/refman/8.0/en/miscellaneous-functions.html

    25. Precision Math

              * https://dev.mysql.com/doc/refman/8.0/en/arithmetic-functions.html#function_acos




 */

/*
TOPIC:
    O funcție sau un operator din expresie este calculul pe care serverul îl face fără să salvezi o rutină. Alegi agregatul când multe rânduri devin o valoare, operatorul când compari sau calculezi două valori, built-in când serverul are deja funcția, și CREATE FUNCTION doar când valoarea trebuie să stea în schemă.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Salariul mediu și o comparație trebuie calculate fără un obiect nou în schemă. După SELECT, salaries rămâne. Nu apare o rutină de șters.
MECHANISM:
    Serverul execută șirul din expressionTheServerEvaluates. AVG(salary) grupează toate rândurile, pentru că nu există GROUP BY, după 14.19.1. 1 = 0 este operatorul din 14.4.2 și dă 0. Efectul este rândul de rezultat, la sfârșitul propoziției. Nu se ține un lacăt după ce rezultatul a plecat.
LOOKALIKES:
    CREATE FUNCTION f_emp_avg_salary salvează un obiect și se cheamă într-o expresie, dar cere CREATE ROUTINE. CALL emp_salary pornește o procedură și poate trimite un set. AVG nu se pornește cu CALL.
BOOK THEORY:
    Manualul 9.7, 14.19.1, deschis în tur: agregatele lucrează pe un set și, dacă nu există GROUP BY, grupul este toate rândurile; ignoră NULL dacă nu s-a spus altfel. 14.4.2: comparația dă 1, 0 sau NULL. 15.1.22: CREATE FUNCTION este rutina stocată, alt obiect decât AVG. Nu există PDF în acest folder. Introduction.pdf, deschis la agregate, spune că agregatul se aplică pe mai multe rânduri ale unei coloane și întoarce o valoare.
BOOK EXAMPLE:
    expressionTheServerEvaluates
REPO DEMO:
    src/main/java/_11_STORED_ROUTINES/FUNCTION_AND_OPERATORS/FUNCTIONS_AND_OPERATORS.java
BREAK LAB:
    createFunctionNamedCount ține CREATE FUNCTION COUNT() RETURNS INT RETURN 1. Numele este o funcție built-in. 15.1.22: apare eroare de sintaxă dacă nu lași spațiu înainte de paranteză, și tot nu trebuie să acoperi COUNT. În tur șirul nu a fost trimis.
TRADE-OFFS:
    CREATE FUNCTION când mai mulți clienți trebuie să cheme aceeași expresie sub un nume și cu EXECUTE. Aplicația când valoarea nu trebuie să iasă din proces.
ANTI-PATTERNS:
    CREATE FUNCTION cu numele unui built-in, ca să „înlocuiești” AVG sau COUNT.
STAFF QUESTIONS:
    Întrebările 1–5 de mai jos.
NOVEL TRANSFER:
    Numărul de colete de pe un raft este COUNT, nu o procedură. Comparația „raftul este gol” este un operator. O etichetă salvată pentru toți clienții este funcția stocată.
SPACED REVIEW:
    O zi: 21 octombrie 2026, AVG nu este CREATE. Șapte zile: 27 octombrie 2026, numele COUNT nu se redeclară. Treizeci de zile: 19 noiembrie 2026, funcția stocată doar când numele trebuie să rămână în schemă.

1. Enunț: Ce problemă rezolvă o expresie cu AVG și un operator, față de o rutină salvată?
   Capcană: Fără CREATE FUNCTION, serverul nu știe AVG și nu poate compara două numere.
   Cheie: expressionTheServerEvaluates cere serverului un calcul pe care îl are deja. Nu apare obiect în schemă. Rândurile din salaries nu se schimbă. Rezultatul este un rând, la sfârșitul propoziției.
   Demonstrație: expressionTheServerEvaluates
2. Enunț: Ce se întâmplă cu rândurile când AVG(salary) nu are GROUP BY?
   Capcană: AVG întoarce câte o medie pe fiecare rând, iar 1 = 0 filtrează salaries.
   Cheie: 14.19.1: fără GROUP BY, agregatul grupează toate rândurile și dă o valoare. Operatorul 1 = 0 nu citește tabela. Nu se scrie salaries și nu rămâne un lacăt după rezultat.
   Demonstrație: expressionTheServerEvaluates
3. Enunț: De ce AVG(salary) nu este f_emp_avg_salary și nu este CALL emp_salary?
   Capcană: Toate trei se pornesc cu CALL, pentru că sunt funcții.
   Cheie: AVG este built-in, fără CREATE și fără EXECUTE pe un obiect al tău. f_emp_avg_salary este funcția stocată, într-o expresie. emp_salary este procedura, cu CALL, și poate trimite mai multe rânduri.
   Demonstrație: expressionTheServerEvaluates
4. Enunț: Cum eșuează CREATE FUNCTION COUNT() RETURNS INT RETURN 1?
   Capcană: Îți înlocuiești COUNT-ul built-in cu unul care întoarce mereu 1, iar agregatele vechi rămân.
   Cheie: createFunctionNamedCount folosește numele unei funcții built-in. 15.1.22: eroarea de sintaxă apare dacă nu pui spațiu înainte de paranteză. Chiar cu spațiu, nu acesta este felul în care numeri rândurile. COUNT(salary) rămâne agregatul.
   Demonstrație: createFunctionNamedCount
5. Enunț: Când salvezi CREATE FUNCTION și când rămâi la expresia built-in?
   Capcană: Orice calcul repetat trebuie să fie rutină, altfel optimizerul îl recalculează greșit.
   Cheie: Expresia built-in când AVG, ROUND sau = sunt deja valoarea cerută. CREATE FUNCTION când numele și privilegiul EXECUTE trebuie să ascundă tabelele. Aplicația când niciun alt client nu evaluează expresia.
   Demonstrație: createFunctionNamedCount

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE media și o comparație, fără obiect nou -> expressionTheServerEvaluates -> SELECT AVG(salary) AS avg_salary, 1 = 0 AS compared FROM salaries -> nu garantează o medie pe angajat: fără GROUP BY grupul este toată tabela
    2. Eșecul cel mai mic
           -> DECIDE să redenumești COUNT -> createFunctionNamedCount -> CREATE FUNCTION COUNT() RETURNS INT RETURN 1 -> nu garantează că agregatul COUNT a fost înlocuit
    3. Granița
           -> Expresia este mecanismul MySQL. Linkul către manualul 8.0 din comentariu nu este propoziția. Java nu evaluează AVG.
    4. Alternativa
           -> CREATE FUNCTION când numele trebuie să rămână în schemă. CALL când trebuie un set sau mai multe OUT. Aplicația când valoarea nu iese din proces.
    5. Anti-patternul
           -> CREATE FUNCTION COUNT() ca să schimbi agregatul. Numele se lovește de built-in, iar numărarea rândurilor nu devine 1.
    6. Dovada
           -> expressionTheServerEvaluates ține AVG și =. createFunctionNamedCount ține numele interzis. root@localhost a fost refuzat, using password: NO.
    7. Interviu
           -> AVG fără GROUP BY este o valoare pe toată tabela. = dă 1, 0 sau NULL. Niciuna nu este CALL. CREATE FUNCTION cu numele COUNT nu înlocuiește agregatul.
*/

public class FUNCTIONS_AND_OPERATORS {

    /*
    🟢 Începător
        Ce este            media salariilor și o comparație, calculate fără o rutină salvată
        De ce e nevoie     fără ele, pare că orice calcul cere CREATE FUNCTION
    🟡 Producție
        Cum se scrie       SELECT AVG(salary) AS avg_salary, 1 = 0 AS compared FROM salaries
    🔴 Staff
        Unde               fără GROUP BY, AVG vede toate rândurile. 1 = 0 nu citește o coloană. Schema nu primește o rutină
    Pași
        1. Pune agregatul și operatorul în același SELECT.
        2. Nu pune CALL.
        3. Nu trimite șirul în tur.
    */
    // Începător: metoda păstrează calculul pe care serverul îl face fără să salveze o rutină.
    // Producție: public static String expressionTheServerEvaluates(). Dacă dispare, alegerea dintre expresie și CREATE rămâne o listă de linkuri.
    // Expert: AVG nu este f_emp_avg_salary. Operatorul nu este un index și nu ține lacăt după rezultat.
    public static String expressionTheServerEvaluates() {
        // Începător: din multe salarii rămâne o medie, iar comparația 1 cu 0 dă un nu, fără să schimbe tabela.
        // Producție: SELECT AVG(salary) AS avg_salary, 1 = 0 AS compared FROM salaries. Dacă linia dispare, nu mai ai expresia gata de trimis.
        // Expert: 14.19.1: grupul este toată tabela. 14.4.2: 1 = 0 dă 0. NULL = NULL nu ar da 0, ar da NULL. Șirul nu este CALL.
        return "SELECT AVG(salary) AS avg_salary, 1 = 0 AS compared FROM salaries";
        // Începător: metoda a înapoiat expresia, nu media.
        // Producție: acolada închide expressionTheServerEvaluates. Șirul nu a plecat spre 3306.
        // Expert: Refuzul root@localhost lasă planul NERULAT. Acolada nu creează f_emp_avg_salary.
    }

    /*
    🟢 Începător
        Ce este            încercarea de a salva o funcție cu numele lui COUNT
        De ce e nevoie     fără ea, pare că poți înlocui agregatul cu o rutină omonimă
    🟡 Producție
        Cum se scrie       CREATE FUNCTION COUNT() RETURNS INT RETURN 1
    🔴 Staff
        Unde               15.1.22: numele unui built-in dă eroare de sintaxă fără spațiu înainte de paranteză. Nu schimbă COUNT(salary)
    Pași
        1. Folosește numele COUNT.
        2. Nu trimite șirul.
        3. Nu îl numi agregat.
    */
    // Începător: metoda păstrează CREATE-ul care vrea să fure numele agregatului.
    // Producție: public static String createFunctionNamedCount(). Dacă dispare, ciocnirea de nume rămâne o frază.
    // Expert: IGNORE_SPACE nu salvează această rutină ca înlocuitor de COUNT. Spațiul dintre nume și paranteză este doar o excepție de sintaxă, nu un alt agregat.
    public static String createFunctionNamedCount() {
        // Începător: serverul nu trebuie să creadă că numărarea rândurilor a devenit mereu unu.
        // Producție: CREATE FUNCTION COUNT() RETURNS INT RETURN 1. Dacă linia dispare, nu mai ai numele built-in pus pe o rutină.
        // Expert: COUNT(*) numără rândurile, inclusiv cele cu NULL. Această funcție, dacă ar trece de nume, tot nu ar fi acel agregat și nu ar primi un set.
        return "CREATE FUNCTION COUNT() RETURNS INT RETURN 1";
        // Începător: metoda a păstrat numele greșit.
        // Producție: acolada închide createFunctionNamedCount. Șirul a rămas în proces.
        // Expert: Nu este ERROR 1418. 1418 este lipsa lui DETERMINISTIC sau READS SQL DATA la o funcție cu nume propriu.
    }
}
