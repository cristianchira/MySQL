package _2_SQL_Statements._2_DML._1_SELECT._5_PRECEDENCE;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8345096#overview


/*                                      Operator Precedence


    ○ So far:
                  * AND
                                - conditions set on different columns
                  * OR
                                - OR conditions set on the same column

    ○ The logical order with which you must comply
                  * when you use both operators
                  * in the same WHERE block

    ▬▬▐ An SQL rule stating that in the execution of the query,
                  * the operator AND
                                - is applied FIRST,
                                - while the operator OR is applied SECOND
                                - AND > OR
         Regardless of the order in which you use these operators,
                 * SQL will always start by reading the conditions
                                - around the AND operator


                                        ▬▬ Query order ▬▬


    ○ SELECT *
      FROM employees
      WHERE last_name ='Dennis' AND gender= 'M' OR gender = 'F';
                  * Condition1 = last_name= 'Dennis' AND gender ='M'
                  * Condition2 =  gender= 'F'

    ○ SELECT *
       FROM employees
       WHERE last_name ='Denis' AND (gender= 'M' OR gender = 'F');
           Parentheses can help us restructure the logic of the operation
                  * to retrieve a list of people with the last name Dennis
                  * independent of their gender.
           You must place parentheses around the conditions about gender.
                  * Condition1 = last_name= 'Dennis'
                                       AND
                  * Condition2 = (gender ='M'  gender= 'F')

    ○ the logical operator PRECEDENCE
                  *  is an important phenomenon when
                                 - enlisting conditions in the WHERE clause.
 */

/*
TOPIC:
    AND se leagă înaintea lui OR: last_name = 'Denis' AND gender = 'M' OR gender = 'F' îi lasă pe Denis bărbat și, separat, pe toate femeile.
FAMILIE:
    Statements
VERSION:
    Java 27, MySQL 9.7 LTS, Connector/J 26.10.0. Propoziția nu a fost trimisă: în tur nu s-a deschis o sesiune, efectul pe server rămâne NERULAT AICI.
INTENT:
    Să știi cine rămâne când ambele stau în același WHERE. Paranteza mută femeile înăuntrul lui Denis.
MECHANISM:
    Serverul leagă AND înainte de OR, indiferent de ordinea scrisă, până pui paranteze.
LOOKALIKES:
    Paranteza (gender = 'M' OR gender = 'F') schimbă grupul. Nu este un alt operator.
BOOK THEORY:
    Manualul 9.7, 15.2.13 SELECT, deschis în tur: clauzele stau în ordinea din sintaxă; WHERE păstrează rândul pentru care expresia este adevărată și nu acceptă funcții de agregare; fără WHERE sunt selectate toate rândurile; HAVING vine după GROUP BY și înainte de ORDER BY; LIMIT limitează rândurile din rezultat. PDF-urile din folderele SELECT au fost deschise în tur; multe au pe prima pagină doar titlul. Exemplul din comentariul de curs, cu Denis AND M OR F, este propoziția. PDF-ul de precedență, dacă există în folder, a fost acoperit de deschiderea PDF-urilor locale.
BOOK EXAMPLE:
    andBindsBeforeOr
REPO DEMO:
    src/main/java/_2_SQL_Statements/_2_DML/_1_SELECT/_5_PRECEDENCE/DB_PRECEDENCE.java
BREAK LAB:
    parenthesesKeepOnlyDenis ține SELECT * FROM employees WHERE last_name = 'Denis' AND (gender = 'M' OR gender = 'F'). Paranteza schimbă cine trece. Nu este aceeași mulțime cu AND legat înainte de OR.
TRADE-OFFS:
    Paranteza când grupul de gen trebuie să stea înăuntrul numelui.
ANTI-PATTERNS:
    Să crezi că ultima propoziție din curs și prima întorc aceleași rânduri.
STAFF QUESTIONS:
    andBindsBeforeOr și parenthesesKeepOnlyDenis. Cele cinci întrebări de mai jos își execută demonstrația prin ele. Nu există bancă de 50 în acest copil: fast stă la părintele din catalog.
NOVEL TRANSFER:
    Un filtrul de alarme: zona = 'A' AND sev = 1 OR sev = 2 lasă alarmele 2 din orice zonă. Paranteza pe severitate le ține doar în zona A.
SPACED REVIEW:
    O zi: 22 octombrie 2026, spui ce rămâne în tabelă la sfârșitul propoziției. Șapte zile: 28 octombrie 2026, îl desparți de vecin pe un rând concret. Treizeci de zile: 20 noiembrie 2026, alegi cealaltă unealtă când condiția din card se schimbă.

1. Enunț: Ce problemă rezolvă SELECT * FROM employees WHERE last_name = 'Denis' AND gender = 'M' OR gender = 'F'?
   Capcană: Serverul citește WHERE mereu de la stânga la dreapta, deci OR de la final nu îi prinde pe alții.
   Cheie: andBindsBeforeOr: rămân bărbații Denis și toate femeile, inclusiv cele cu alt nume. fără să știi legarea, pare că tot filtrul este despre Denis
   Demonstrație: andBindsBeforeOr
2. Enunț: Ce rămâne în tabelă sau în sesiune după SELECT * FROM employees WHERE last_name = 'Denis' AND gender = 'M' OR gender = 'F'?
   Capcană: Cele două propoziții din curs întorc aceleași rânduri, paranteza este doar lizibilitate.
   Cheie: Efectul este: rămân bărbații Denis și toate femeile, inclusiv cele cu alt nume. Se vede la sfârșitul propoziției, nu pentru că ai închis fereastra.
   Demonstrație: andBindsBeforeOr
3. Enunț: De ce SELECT * FROM employees WHERE last_name = 'Denis' AND gender = 'M' OR gender = 'F' nu este vecinul său?
   Capcană: Precedența este un index, nu o regulă de legare.
   Cheie: Paranteza (gender = 'M' OR gender = 'F') schimbă grupul. Nu este un alt operator.
   Demonstrație: andBindsBeforeOr
4. Enunț: Cum eșuează SELECT * FROM employees WHERE last_name = 'Denis' AND (gender = 'M' OR gender = 'F')?
   Capcană: Paranteza pe gen îi adaugă pe toți angajații.
   Cheie: parenthesesKeepOnlyDenis: Paranteza schimbă cine trece. Nu este aceeași mulțime cu AND legat înainte de OR.
   Demonstrație: parenthesesKeepOnlyDenis
5. Enunț: Când nu mai trimiți SELECT * FROM employees WHERE last_name = 'Denis' AND gender = 'M' OR gender = 'F'?
   Capcană: Ca să controlezi legarea, faci două interogări și le lipești în aplicație.
   Cheie: Paranteza când grupul de gen trebuie să stea înăuntrul numelui.
   Demonstrație: andBindsBeforeOr

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE vrei legarea implicită: Denis bărbat sau orice femeie -> andBindsBeforeOr -> SELECT * FROM employees WHERE last_name = 'Denis' AND gender = 'M' OR gender = 'F' -> nu garantează că scrierea de la stânga la dreapta bate AND-înaintea-lui-OR
    2. Eșecul cel mai mic
           -> DECIDE trimiți textul care nu este propoziția -> parenthesesKeepOnlyDenis -> SELECT * FROM employees WHERE last_name = 'Denis' AND (gender = 'M' OR gender = 'F') -> Paranteza schimbă cine trece. Nu este aceeași mulțime cu AND legat înainte de OR.
    3. Granița
           -> Precedența este regula serverului pe AND și OR. Nu este un click de grupare în client.
    4. Alternativa
           -> Paranteza (gender = 'M' OR gender = 'F') schimbă grupul. Nu este un alt operator.
    5. Anti-patternul
           -> Să crezi că ultima propoziție din curs și prima întorc aceleași rânduri.
    6. Dovada
           -> andBindsBeforeOr ține propoziția. parenthesesKeepOnlyDenis ține eșecul. În tur serverul nu a executat-o: NERULAT AICI.
    7. Interviu
           -> AND se leagă înaintea lui OR. Fără paranteză, femeile din exemplul de curs intră toate. Cu paranteză pe gen, rămâne doar numele Denis.
*/

public class DB_PRECEDENCE {
    /*
    🟢 Începător
        Ce este            rămân bărbații Denis și toate femeile, inclusiv cele cu alt nume
        De ce e nevoie     fără să știi legarea, pare că tot filtrul este despre Denis
    🟡 Producție
        Cum se scrie       SELECT * FROM employees WHERE last_name = 'Denis' AND gender = 'M' OR gender = 'F'
    🔴 Staff
        Unde               efectul se vede la sfârșitul propoziției. Vecinul este aceeași propoziție cu paranteză pe gen, care îi lasă pe Denis indiferent de gen și nu lasă alte femei
    Pași
        1. Ține propoziția într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu aceeași propoziție cu paranteză pe gen, care îi lasă pe Denis indiferent de gen și nu lasă alte femei.
    */
    // Începător: andBindsBeforeOr păstrează propoziția pentru rămân bărbații Denis și toate femeile, inclusiv cele cu alt nume
    // Producție: public static String andBindsBeforeOr(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Paranteza nu este opțională când vrei alt grup decât legarea implicită.
    public static String andBindsBeforeOr() {
        // Începător: rămân bărbații Denis și toate femeile, inclusiv cele cu alt nume
        // Producție: SELECT * FROM employees WHERE last_name = 'Denis' AND gender = 'M' OR gender = 'F'. Dacă linia dispare, propoziția nu mai este gata de trimis.
        // Expert: Paranteza nu este opțională când vrei alt grup decât legarea implicită. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT * FROM employees WHERE last_name = 'Denis' AND gender = 'M' OR gender = 'F'";
        // Începător: metoda a înapoiat propoziția, nu un rând scris de server.
        // Producție: acolada închide andBindsBeforeOr. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }
    /*
    🟢 Începător
        Ce este            rămân doar oamenii cu numele Denis; femeile cu alt nume cad
        De ce e nevoie     fără a doua propoziție, paranteza pare doar stil
    🟡 Producție
        Cum se scrie       SELECT * FROM employees WHERE last_name = 'Denis' AND (gender = 'M' OR gender = 'F')
    🔴 Staff
        Unde               eșecul este înainte de un rând schimbat. Vecinul corect este propoziția care rămân doar oamenii cu numele Denis; femeile cu alt nume cad
    Pași
        1. Ține textul greșit într-un șir gata de trimis.
        2. Nu îl trimite în tur: sesiunea nu a fost deschisă.
        3. Nu îl amesteca cu aceeași propoziție cu paranteză pe gen, care îi lasă pe Denis indiferent de gen și nu lasă alte femei.
    */
    // Începător: parenthesesKeepOnlyDenis păstrează textul greșit pentru rămân doar oamenii cu numele Denis; femeile cu alt nume cad
    // Producție: public static String parenthesesKeepOnlyDenis(). Dacă dispare, rămâne comentariul de curs, pe care serverul nu îl execută.
    // Expert: Metoda nu deschide JDBC și nu apasă un click în Workbench. Paranteza schimbă cine trece. Nu este aceeași mulțime cu AND legat înainte de OR.
    public static String parenthesesKeepOnlyDenis() {
        // Începător: rămân doar oamenii cu numele Denis; femeile cu alt nume cad
        // Producție: SELECT * FROM employees WHERE last_name = 'Denis' AND (gender = 'M' OR gender = 'F'). Dacă linia dispare, textul greșit nu mai este gata de trimis.
        // Expert: Paranteza schimbă cine trece. Nu este aceeași mulțime cu AND legat înainte de OR. Linia nu garantează un rând văzut de client, pentru că șirul nu a plecat.
        return "SELECT * FROM employees WHERE last_name = 'Denis' AND (gender = 'M' OR gender = 'F')";
        // Începător: metoda a înapoiat textul greșit, nu un rând scris de server.
        // Producție: acolada închide parenthesesKeepOnlyDenis. Șirul a rămas în proces.
        // Expert: Închiderea nu este COMMIT și nu este ROLLBACK. Efectul pe server rămâne NERULAT AICI.
    }

}
