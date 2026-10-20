package _11_STORED_ROUTINES.FUNCTION_AND_OPERATORS.AGGREGATE_FUNCTION.COALESCE;

// Martin G. IF NULL() and COALESCE()
//https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8400644#questions

/*                                            Coalesce()


    ○ select
          dept_no, ifnull(dept_name,'Department name not provided') as dept_name
      from

          departments_dup;


    ○ Coalesce(expression_1, expression_2 , expression_n)

                  * can have multiple parameters

                                - think at Coalesce like a IFNULL  with more than 2 parameters


                  * Coalesce will always return a single value

                                - of the ones we have within parentheses

                                - and this value will be the first non-null value of the list

                                - reading the values from the left to right

                  * COALESCE and IFNULL does not make any change TO THE DATA SET

                                - only create an uotput instead null value

    ○ EXAMPLE


                 1. with one parameter

                                SELECT  dept_no,  dept_name,

                                COALESCE('Department manager') as fake_column

                                 FROM  departments_dup;

                  2. with 2 parameters works like IFNULL

                                 SELECT   dept_no,

                                 COALESCE(dept_name, 'Department name not provided') as dept_name

                                 FROM  departments_dup;

                  2. with multiple parameters

                                 SELECT   dept_no, dept_name,

                                 COALESCE(dept_manager,dept_name, 'N/A') as dept_name

                                 FROM  departments_dup;

                             If dept_manager is null will take dept_name and where dept name is null will take third parameter 'N/A'.



    ○ Here we will study something a bit more sophisticated.

                   * IFNULL()

                   * and COALESCE()

           are among the advanced SQL functions in the toolkit of SQL professionals.

    ○ They are used when

                  * null values are dispersed in your data table

                  * and you would like to substitute the null values with another value.

    ○ So, let’s adjust the “Departments” duplicate in a way that suits the purposes of the next video,

                   * in which we will work with

                                 - IF NULL()

                                 - and COALESCE().


                                                    IFNULL()


    ○ IFNULL(expression_1, expression_2 )

                  * return the first of two indicated values

                                - if the data value found in the table is not null

                  * and returns the second value if there is the null value

    ○ IFNULL

                  * works with precisely 2 parameters


    ○ EXAMPLE:

                  * SELECT  dept_no,

                    IFNULL(dept_name,'Department name not provided') as dept_name

                    FROM  departments_dup;


 */


/*
TOPIC:
    COALESCE întoarce primul argument nenul din listă și nu schimbă rândul din departments_dup. IFNULL primește exact două argumente și face același lucru pe două valori. Cu un singur argument, COALESCE nu înlocuiește nimic.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    În rezultat, departamentul fără nume arată un text. În tabelă, dept_name rămâne NULL. Agregatul nu intră: nu se grupează rânduri.
MECHANISM:
    Serverul execută șirul din coalesceReplacesNullInTheResult. 14.4.2: COALESCE dă primul nenul, sau NULL dacă toate sunt NULL. 14.5: IFNULL(expr1, expr2) dă expr1 dacă nu este NULL, altfel expr2. Efectul se vede la sfârșitul SELECT-ului, nu într-un UPDATE.
LOOKALIKES:
    IFNULL nu primește trei argumente. ISNULL(expr) răspunde 1 sau 0, nu valoarea de înlocuire. AVG sare peste NULL; COALESCE nu agregă. Un UPDATE ar scrie textul în tabelă.
BOOK THEORY:
    Manualul 9.7, 14.4.2, deschis în tur: COALESCE întoarce primul argument nenul. 14.5, deschis în tur: IFNULL întoarce primul argument dacă nu este NULL, altfel pe al doilea; tipul rezultatului urmează pe cel mai general dintre cele două. IFNULL()+and+COALESCE().pdf: IFNULL(expression_1, expression_2). COALESCE()+-+Preamble.pdf: înlocuiesc NULL în rezultat. Comentariul din clasă spune că nu schimbă setul de date.
BOOK EXAMPLE:
    coalesceReplacesNullInTheResult
REPO DEMO:
    src/main/java/_11_STORED_ROUTINES/FUNCTION_AND_OPERATORS/AGGREGATE_FUNCTION/COALESCE/Coalesce.java
BREAK LAB:
    updateWritesTheSubstitute ține UPDATE departments_dup SET dept_name = COALESCE(dept_name, 'Department name not provided'). Asta schimbă rândul. În tur șirul nu a fost trimis.
TRADE-OFFS:
    UPDATE când textul trebuie să rămână pentru toți clienții. IFNULL când ai exact două expresii și vrei același înlocuitor.
ANTI-PATTERNS:
    UPDATE ca să „afișezi” numele lipsă. NULL dispare din tabelă, nu doar din rezultat.
STAFF QUESTIONS:
    Întrebările 1–5 de mai jos.
NOVEL TRANSFER:
    Un colet fără etichetă arată „fără nume” în lista de pe ecran. Raftul tot nu are eticheta scrisă. UPDATE ar scrie eticheta pe colet.
SPACED REVIEW:
    O zi: 21 octombrie 2026, rezultatul se schimbă, rândul nu. Șapte zile: 27 octombrie 2026, IFNULL are două argumente. Treizeci de zile: 19 noiembrie 2026, un singur argument la COALESCE nu înlocuiește NULL.

1. Enunț: Ce problemă rezolvă COALESCE(dept_name, 'Department name not provided')?
   Capcană: Șterge NULL din departments_dup și pune textul în coloană.
   Cheie: coalesceReplacesNullInTheResult schimbă valoarea din rezultat. dept_name rămâne NULL în tabelă. Nu este un agregat și nu grupează rândurile.
   Demonstrație: coalesceReplacesNullInTheResult
2. Enunț: Ce se întâmplă cu rândul când dept_name este NULL și când nu este?
   Capcană: Ambele rânduri primesc textul de înlocuire, ca să fie consecvent.
   Cheie: Dacă dept_name nu este NULL, COALESCE îl lasă. Dacă este NULL, rezultatul este textul, iar rândul stocat rămâne NULL. 14.4.2: dacă toate argumentele sunt NULL, rezultatul este NULL.
   Demonstrație: coalesceReplacesNullInTheResult
3. Enunț: De ce COALESCE nu este IFNULL și nu este ISNULL?
   Capcană: Cele trei răspund 1 sau 0, pentru că sunt teste de NULL.
   Cheie: IFNULL are exact două argumente și întoarce o valoare, nu un test. ISNULL(expr) întoarce 1 sau 0. COALESCE poate avea mai multe argumente și ia primul nenul. Niciunul nu este AVG.
   Demonstrație: coalesceReplacesNullInTheResult
4. Enunț: Cum eșuează UPDATE care pune COALESCE înapoi în coloană, când voiai doar afișarea?
   Capcană: Este același lucru cu SELECT, pentru că funcția este aceeași.
   Cheie: updateWritesTheSubstitute scrie textul în dept_name. NULL nu mai este în tabelă. Un client ulterior nu mai poate deosebi lipsa de textul pus de tine. SELECT nu făcea asta.
   Demonstrație: updateWritesTheSubstitute
5. Enunț: Când scrii înlocuitorul în tabelă și când îl lași în SELECT?
   Capcană: Întotdeauna UPDATE, altfel următorul SELECT vede din nou NULL.
   Cheie: SELECT când lipsa trebuie să rămână lipsă și doar ecranul arată textul. UPDATE când regula spune că NULL nu are voie să rămână. IFNULL când ai exact două expresii. Aplicația când textul nu trebuie să vină de la server.
   Demonstrație: updateWritesTheSubstitute

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE text în rezultat, NULL rămâne stocat -> coalesceReplacesNullInTheResult -> SELECT dept_no, COALESCE(dept_name, 'Department name not provided') AS dept_name FROM departments_dup -> nu garantează că un al treilea argument a fost citit: se oprește la primul nenul
    2. Eșecul cel mai mic
           -> DECIDE să „repari” tabela prin afișare -> updateWritesTheSubstitute -> UPDATE departments_dup SET dept_name = COALESCE(dept_name, 'Department name not provided') -> nu garantează că lipsa mai poate fi văzută
    3. Granița
           -> COALESCE este funcția MySQL din expresie. Nu este agregat, deși folderul stă sub agregate. Java nu înlocuiește celula.
    4. Alternativa
           -> IFNULL cu exact două argumente. UPDATE când stocarea trebuie să se schimbe. ISNULL când întrebarea este testul, nu valoarea.
    5. Anti-patternul
           -> UPDATE cu COALESCE ca să afișezi numele. NULL dispare pentru toți clienții.
    6. Dovada
           -> coalesceReplacesNullInTheResult ține SELECT. updateWritesTheSubstitute ține UPDATE. root@localhost a fost refuzat, using password: NO.
    7. Interviu
           -> COALESCE ia primul nenul și nu scrie rândul. IFNULL are două argumente. Un UPDATE cu aceeași funcție schimbă tabela. Nu este un agregat.
*/

public class Coalesce {

    /*
    🟢 Începător
        Ce este            un nume de departament pus în rezultat doar când în tabelă lipsește
        De ce e nevoie     fără el, ecranul arată golul, sau cineva scrie în tabelă ca să umple golul
    🟡 Producție
        Cum se scrie       SELECT dept_no, COALESCE(dept_name, 'Department name not provided') AS dept_name FROM departments_dup
    🔴 Staff
        Unde               rândul stocat rămâne. Primul nenul câștigă. Toate NULL dau NULL
    Pași
        1. Pune înlocuitorul în SELECT.
        2. Nu pune UPDATE.
        3. Nu trimite șirul în tur.
    */
    // Începător: metoda păstrează afișarea care nu umple celula.
    // Producție: public static String coalesceReplacesNullInTheResult(). Dacă dispare, COALESCE rămâne doar în comentariu.
    // Expert: IFNULL(dept_name, 'Department name not provided') este aceeași înlocuire pe două argumente. Un al treilea argument cere COALESCE.
    public static String coalesceReplacesNullInTheResult() {
        // Începător: departamentul fără nume arată un text, iar în tabelă golul rămâne.
        // Producție: SELECT dept_no, COALESCE(dept_name, 'Department name not provided') AS dept_name FROM departments_dup. Dacă linia dispare, nu mai ai înlocuirea doar în rezultat.
        // Expert: COALESCE(dept_name) cu un argument întoarce coloana, inclusiv NULL. ISNULL ar da 1 sau 0, nu textul. AVG nu înlocuiește.
        return "SELECT dept_no, COALESCE(dept_name, 'Department name not provided') AS dept_name FROM departments_dup";
        // Începător: metoda a înapoiat afișarea, nu un UPDATE.
        // Producție: acolada închide coalesceReplacesNullInTheResult. Șirul nu a plecat spre 3306.
        // Expert: Refuzul root@localhost lasă rândul NERULAT. Acolada nu schimbă dept_name.
    }

    /*
    🟢 Începător
        Ce este            scrierea textului în celula care era goală
        De ce e nevoie     fără ea, pare că COALESCE din SELECT a și reparat tabela
    🟡 Producție
        Cum se scrie       UPDATE departments_dup SET dept_name = COALESCE(dept_name, 'Department name not provided')
    🔴 Staff
        Unde               rândul se schimbă. NULL nu mai poate fi deosebit de textul pus de propoziție
    Pași
        1. Pune COALESCE în SET.
        2. Nu îl numi afișare.
        3. Nu trimite șirul în tur.
    */
    // Începător: metoda păstrează scrierea care umple golul de-a binelea.
    // Producție: public static String updateWritesTheSubstitute(). Dacă dispare, anti-patternul rămâne o frază.
    // Expert: Un ROLLBACK poate aduce NULL înapoi doar dacă UPDATE-ul este încă în tranzacție. SELECT-ul de mai sus nu avea ce da înapoi.
    public static String updateWritesTheSubstitute() {
        // Începător: numele lipsă este înlocuit în tabelă, nu doar pe ecran.
        // Producție: UPDATE departments_dup SET dept_name = COALESCE(dept_name, 'Department name not provided'). Dacă linia dispare, nu mai ai scrierea.
        // Expert: IFNULL în SET ar scrie la fel, tot pe două argumente. Niciuna nu este agregat. Lacătul pe rând ține pe durata UPDATE-ului, nu după un SELECT.
        return "UPDATE departments_dup SET dept_name = COALESCE(dept_name, 'Department name not provided')";
        // Începător: metoda a păstrat scrierea.
        // Producție: acolada închide updateWritesTheSubstitute. Șirul a rămas în proces.
        // Expert: Nu este ERROR 1418. Acela este la CREATE FUNCTION, nu la un UPDATE.
    }
}
