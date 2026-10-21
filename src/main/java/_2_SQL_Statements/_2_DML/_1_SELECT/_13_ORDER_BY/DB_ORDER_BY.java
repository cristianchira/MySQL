package _2_SQL_Statements._2_DML._1_SELECT._13_ORDER_BY;

//Martin G. ORDER BY
// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8345178#overview


/*                                       ORDER BY


    ▬▬▐ order specified attributes(one or more column) in
                  * ascending (asc)
                                - default is asc order
                  * descending (desc)

    ○ Order by one column
                * SELECT * FROM employees
                  ORDER BY first_name DESC;

   ○ Order by multiple columns
               * SELECT * FROM employees
                 ORDER BY first_name DESC, last_name DESC;

 */

/*
TOPIC:
    ORDER BY first_name DESC ordonează rezultatul; nu schimbă ordinea rândurilor stocate în employees.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să vezi prenumele în ordine descrescătoare în rezultat. Tabela rămâne.
MECHANISM:
    Serverul sortează rezultatul după ORDER BY, după HAVING, cum este ordinea din 15.2.13. Fără ORDER BY, ordinea rândurilor din rezultat nu este garantată.
LOOKALIKES:
    Un index pe first_name poate fi folosit la plan, dar nu este clauza care promite ordinea. GROUP BY nu sortează rezultatul ca ORDER BY.
BOOK THEORY:
    Manualul 9.7, 15.2.13 SELECT, deschis în tur: clauzele stau în ordinea din sintaxă; WHERE păstrează rândul pentru care expresia este adevărată și nu acceptă funcții de agregare; fără WHERE sunt selectate toate rândurile; HAVING vine după GROUP BY și înainte de ORDER BY; LIMIT limitează rândurile din rezultat.  Comentariul de curs: ASC este implicit, DESC se scrie. Manualul: ORDER BY stă după HAVING.
BOOK EXAMPLE:
    orderByFirstNameDesc
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_1_SELECT/_13_ORDER_BY/DB_ORDER_BY.java
BREAK LAB:
    indexIsNotAnOrderClause ține CREATE INDEX idx_first_name ON employees (first_name). CREATE INDEX schimbă schema. Ordinea din rezultat o scrie ORDER BY.
TRADE-OFFS:
    CREATE INDEX când problema este planul de căutare, apoi păstrezi ORDER BY dacă ordinea trebuie să fie a rezultatului.
ANTI-PATTERNS:
    Să creezi un index și să spui că SELECT fără ORDER BY vine sortat.
STAFF QUESTIONS:
    orderByFirstNameDesc și indexIsNotAnOrderClause. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    O listă de loturi pentru etichetare trebuie să iasă alfabetic. ORDER BY lot_no DESC este pe rezultat. Indexul pe lot_no nu înlocuiește clauza.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SELECT * FROM employees ORDER BY first_name DESC?
   Capcană: ORDER BY rescrie rândurile în tabelă în ordinea prenumelui.
   Cheie: orderByFirstNameDesc: rezultatul vine cu prenumele în ordine descrescătoare, iar employees rămâne nestocat în altă ordine. fără ORDER BY, nu poți spune care prenume vine primul
   Demonstrație: orderByFirstNameDesc
2. Enunț: Ce rămâne în tabelă sau în sesiune după SELECT * FROM employees ORDER BY first_name DESC?
   Capcană: Un index pe first_name garantează ordinea fără ORDER BY.
   Cheie: Efectul este: rezultatul vine cu prenumele în ordine descrescătoare, iar employees rămâne nestocat în altă ordine. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: orderByFirstNameDesc
3. Enunț: De ce SELECT * FROM employees ORDER BY first_name DESC nu este vecinul său?
   Capcană: GROUP BY first_name este sortarea.
   Cheie: Un index pe first_name poate fi folosit la plan, dar nu este clauza care promite ordinea. GROUP BY nu sortează rezultatul ca ORDER BY.
   Demonstrație: orderByFirstNameDesc
4. Enunț: Cum eșuează CREATE INDEX idx_first_name ON employees (first_name)?
   Capcană: DESC este implicit.
   Cheie: indexIsNotAnOrderClause: CREATE INDEX schimbă schema. Ordinea din rezultat o scrie ORDER BY.
   Demonstrație: indexIsNotAnOrderClause
5. Enunț: Când nu mai trimiți SELECT * FROM employees ORDER BY first_name DESC?
   Capcană: Ca să sortezi, este de ajuns indexul.
   Cheie: CREATE INDEX când problema este planul de căutare, apoi păstrezi ORDER BY dacă ordinea trebuie să fie a rezultatului.
   Demonstrație: orderByFirstNameDesc

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei ordinea în rezultat, nu un index -> orderByFirstNameDesc -> SELECT * FROM employees ORDER BY first_name DESC -> nu garantează aceeași ordine la următoarea rulare dacă lipsește ORDER BY
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> indexIsNotAnOrderClause -> CREATE INDEX idx_first_name ON employees (first_name) -> CREATE INDEX schimbă schema. Ordinea din rezultat o scrie ORDER BY.
    3. Granița
           -> ORDER BY este clauza. Sortarea din grid-ul clientului nu schimbă propoziția.
    4. Alternativa
           -> Un index pe first_name poate fi folosit la plan, dar nu este clauza care promite ordinea. GROUP BY nu sortează rezultatul ca ORDER BY.
    5. Anti-patternul
           -> Să creezi un index și să spui că SELECT fără ORDER BY vine sortat.
    6. Dovada
           -> orderByFirstNameDesc ține propoziția. indexIsNotAnOrderClause ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> ORDER BY ordonează rezultatul. Fără el, ordinea nu este garantată. Indexul nu este clauza. ASC este implicit.
*/

public class DB_ORDER_BY {
    /*
    🟢 Începător
        Ce este            rezultatul vine cu prenumele în ordine descrescătoare, iar employees rămâne nestocat în altă ordine
        De ce e nevoie     fără ORDER BY, nu poți spune care prenume vine primul
    🟡 Producție
        Cum se scrie       SELECT * FROM employees ORDER BY first_name DESC
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este indexul pe first_name, care poate grăbi căutarea și nu este o promisiune de ordine în rezultat
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu indexul pe first_name, care poate grăbi căutarea și nu este o promisiune de ordine în rezultat.
    */
    // Începător: orderByFirstNameDesc păstrează propoziția pentru rezultatul vine cu prenumele în ordine descrescătoare, iar employees rămâne nestocat în altă ordine
    // Producție: public static String orderByFirstNameDesc(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. ORDER BY nu înlocuiește WHERE. Sortează ce a rămas, inclusiv toate rândurile dacă nu ai filtru.
    public static String orderByFirstNameDesc() {
        // Începător: rezultatul vine cu prenumele în ordine descrescătoare, iar employees rămâne nestocat în altă ordine
        // Producție: SELECT * FROM employees ORDER BY first_name DESC. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: ORDER BY nu înlocuiește WHERE. Sortează ce a rămas, inclusiv toate rândurile dacă nu ai filtru. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT * FROM employees ORDER BY first_name DESC";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide orderByFirstNameDesc. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            apare un index și rezultatul unui SELECT fără ORDER BY tot nu are ordine promisă
        De ce e nevoie     fără textul greșit, indexul pare că sortează citirea
    🟡 Producție
        Cum se scrie       CREATE INDEX idx_first_name ON employees (first_name)
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care apare un index și rezultatul unui SELECT fără ORDER BY tot nu are ordine promisă
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu indexul pe first_name, care poate grăbi căutarea și nu este o promisiune de ordine în rezultat.
    */
    // Începător: indexIsNotAnOrderClause păstrează textul greșit pentru apare un index și rezultatul unui SELECT fără ORDER BY tot nu are ordine promisă
    // Producție: public static String indexIsNotAnOrderClause(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. CREATE INDEX schimbă schema. Ordinea din rezultat o scrie ORDER BY.
    public static String indexIsNotAnOrderClause() {
        // Începător: apare un index și rezultatul unui SELECT fără ORDER BY tot nu are ordine promisă
        // Producție: CREATE INDEX idx_first_name ON employees (first_name). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: CREATE INDEX schimbă schema. Ordinea din rezultat o scrie ORDER BY. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "CREATE INDEX idx_first_name ON employees (first_name)";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide indexIsNotAnOrderClause. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
